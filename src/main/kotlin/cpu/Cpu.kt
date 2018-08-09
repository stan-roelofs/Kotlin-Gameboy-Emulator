package cpu

import Gpu
import Mmu
import clearBit
import cpu.instructions.Instruction
import cpu.instructions.alu.*
import cpu.instructions.bit.*
import cpu.instructions.calls.CALL_cc_nn
import cpu.instructions.calls.CALL_nn
import cpu.instructions.jumps.*
import cpu.instructions.loads.*
import cpu.instructions.miscellaneous.*
import cpu.instructions.restarts.RST_n
import cpu.instructions.returns.RET
import cpu.instructions.returns.RETI
import cpu.instructions.returns.RET_cc
import cpu.instructions.rotates.*
import cpu.instructions.shifts.*
import getBit
import getFirstByte
import getSecondByte
import java.util.logging.Logger
import Timer

class Cpu {
    companion object {
        val logger = Logger.getLogger("Cpu")!!
    }

    val registers = Registers()
    val gpu = Gpu(registers)
    private val mmu = Mmu.instance
    private val timer = Timer(mmu)

    var lastInstruction: Int = 0

    var eiExecuted = false
    var diExecuted = false

    fun reset() {
        val a = if (mmu.cartridge!!.isGbc) 0x11 else 0x01
        registers.A = a
        registers.F = 0xB0
        registers.setBC(0x0013)
        registers.setDE(0x00D8)
        registers.setHL(0x014D)
        registers.SP = 0xFFFE
        registers.PC = 0x100
        registers.IME = true
        registers.halt = false
        registers.stop = false
        registers.haltBug = false
        registers.clock = 0
        gpu.reset()
    }

    fun step() {
        val c = registers.clock

        // Interrupt handling
        var IF = mmu.readByte(0xFF0F)
        val IE = mmu.readByte(0xFFFF)

        var address = 0
        when {
            IF.getBit(0) && IE.getBit(0) -> {
                IF = clearBit(IF, 0)
                address = 0x40
            }
            IF.getBit(1) && IE.getBit(1) -> {
                IF = clearBit(IF, 1)
                address = 0x48
            }
            IF.getBit(2) && IE.getBit(2) -> {
                IF = clearBit(IF, 2)
                address = 0x50
            }
            IF.getBit(3) && IE.getBit(3) -> {
                IF = clearBit(IF, 3)
                address = 0x58
            }
            IF.getBit(4) && IE.getBit(4) -> {
                IF = clearBit(IF, 4)
                address = 0x60
            }
        }

        // Interrupt Service Routine
        if (address != 0) {
            if (registers.halt) {
                registers.halt = false
                registers.clock += 4
            }

            if (registers.IME) {
                mmu.writeByte(0xFF0F, IF)
                registers.IME = false

                // Execute two nops
                registers.clock += 8

                // Push current PC onto stack
                registers.decSP()
                mmu.writeByte(registers.SP, registers.PC.getSecondByte())
                registers.decSP()
                mmu.writeByte(registers.SP, registers.PC.getFirstByte())
                registers.clock += 8

                // Set PC to address of handler
                registers.PC = address
                registers.clock += 4
            }
        }

        if (!registers.halt) {
            val opcode = mmu.readByte(registers.PC)

            if (!registers.haltBug) {
                registers.incPC()
            } else {
                registers.haltBug = false
            }

            val instruction = getInstruction(opcode)

            lastInstruction = opcode
            val cycles = instruction.execute()
            registers.clock += cycles

            timer.tick(cycles)
            gpu.step()

            // EI or DI executed, enable/disable interrupts not now but after next instruction
            if (opcode == 0xF3 || opcode == 0xFB) {
                return
            }

            if (eiExecuted) {
                registers.IME = true
                eiExecuted = false
            }
            if (diExecuted) {
                diExecuted = false
                registers.IME = false
            }
        } else {
            registers.clock += 1
            timer.tick(1)
            //gpu.step()
        }
    }

    private fun updateTimer(clock: Long) {
        /*
        val div = mmu.readByte(0xFF04)
        mmu.writeByte()

        if (mmu.readByte(0xFF07).getBit(2)) {
            val timer = when(mmu.readByte(0xFF07) and 0b11) {
                0b00 -> 1024
                0b01 -> 16
                0b10 -> 64
                0b11 -> 256
                else -> throw Exception()
            }

            val tima = mmu.readByte(0xFF05)

        }*/
    }

    private fun getInstruction(opcode: Int): Instruction {
        return when(opcode) {
            // 8 bit loads
            // LD r, n
            0x3E -> LD_r_n(registers, mmu, RegisterID.A.ordinal)
            0x06 -> LD_r_n(registers, mmu, RegisterID.B.ordinal)
            0x0E -> LD_r_n(registers, mmu, RegisterID.C.ordinal)
            0x16 -> LD_r_n(registers, mmu, RegisterID.D.ordinal)
            0x1E -> LD_r_n(registers, mmu, RegisterID.E.ordinal)
            0x26 -> LD_r_n(registers, mmu, RegisterID.H.ordinal)
            0x2E -> LD_r_n(registers, mmu, RegisterID.L.ordinal)

            // LD r1, r2
            0x7F -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.A.ordinal)
            0x78 -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.B.ordinal)
            0x79 -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.C.ordinal)
            0x7A -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.D.ordinal)
            0x7B -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.E.ordinal)
            0x7C -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.H.ordinal)
            0x7D -> LD_r1_r2(registers, mmu, RegisterID.A.ordinal, RegisterID.L.ordinal)

            0x47 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.A.ordinal)
            0x40 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.B.ordinal)
            0x41 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.C.ordinal)
            0x42 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.D.ordinal)
            0x43 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.E.ordinal)
            0x44 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.H.ordinal)
            0x45 -> LD_r1_r2(registers, mmu, RegisterID.B.ordinal, RegisterID.L.ordinal)

            0x4F -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.A.ordinal)
            0x48 -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.B.ordinal)
            0x49 -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.C.ordinal)
            0x4A -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.D.ordinal)
            0x4B -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.E.ordinal)
            0x4C -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.H.ordinal)
            0x4D -> LD_r1_r2(registers, mmu, RegisterID.C.ordinal, RegisterID.L.ordinal)

            0x57 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.A.ordinal)
            0x50 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.B.ordinal)
            0x51 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.C.ordinal)
            0x52 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.D.ordinal)
            0x53 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.E.ordinal)
            0x54 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.H.ordinal)
            0x55 -> LD_r1_r2(registers, mmu, RegisterID.D.ordinal, RegisterID.L.ordinal)

            0x5F -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.A.ordinal)
            0x58 -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.B.ordinal)
            0x59 -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.C.ordinal)
            0x5A -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.D.ordinal)
            0x5B -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.E.ordinal)
            0x5C -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.H.ordinal)
            0x5D -> LD_r1_r2(registers, mmu, RegisterID.E.ordinal, RegisterID.L.ordinal)

            0x67 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.A.ordinal)
            0x60 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.B.ordinal)
            0x61 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.C.ordinal)
            0x62 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.D.ordinal)
            0x63 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.E.ordinal)
            0x64 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.H.ordinal)
            0x65 -> LD_r1_r2(registers, mmu, RegisterID.H.ordinal, RegisterID.L.ordinal)

            0x6F -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.A.ordinal)
            0x68 -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.B.ordinal)
            0x69 -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.C.ordinal)
            0x6A -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.D.ordinal)
            0x6B -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.E.ordinal)
            0x6C -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.H.ordinal)
            0x6D -> LD_r1_r2(registers, mmu, RegisterID.L.ordinal, RegisterID.L.ordinal)

            // LD r, (HL)
            0x7E -> LD_r_HL(registers, mmu, RegisterID.A.ordinal)
            0x46 -> LD_r_HL(registers, mmu, RegisterID.B.ordinal)
            0x4E -> LD_r_HL(registers, mmu, RegisterID.C.ordinal)
            0x56 -> LD_r_HL(registers, mmu, RegisterID.D.ordinal)
            0x5E -> LD_r_HL(registers, mmu, RegisterID.E.ordinal)
            0x66 -> LD_r_HL(registers, mmu, RegisterID.H.ordinal)
            0x6E -> LD_r_HL(registers, mmu, RegisterID.L.ordinal)

            // LD (HL), r
            0x77 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.A.ordinal)
            0x70 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.B.ordinal)
            0x71 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.C.ordinal)
            0x72 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.D.ordinal)
            0x73 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.E.ordinal)
            0x74 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.H.ordinal)
            0x75 -> LD_rr_r(registers, mmu, RegisterID.HL.ordinal, RegisterID.L.ordinal)

            // LD (HL), n
            0x36 -> LD_HL_n(registers, mmu)

            // LD A, (rr)
            0x0A -> LD_A_rr(registers, mmu, RegisterID.BC.ordinal)
            0x1A -> LD_A_rr(registers, mmu, RegisterID.DE.ordinal)

            // LD A, (nn)
            0xFA -> LD_A_nn(registers, mmu)

            // LD (rr), A
            0x02 -> LD_rr_r(registers, mmu, RegisterID.BC.ordinal, RegisterID.A.ordinal)
            0x12 -> LD_rr_r(registers, mmu, RegisterID.DE.ordinal, RegisterID.A.ordinal)
            0xEA -> LD_nn_A(registers, mmu)

            // LD A, (C)
            0xF2 -> LD_A_C(registers, mmu)

            // LD (C), A
            0xE2 -> LD_C_A(registers, mmu)

            // LDD A, (HL)
            0x3A -> LDD_A_HL(registers, mmu)

            // LDD (HL), A
            0x32 -> LDD_HL_A(registers, mmu)

            // LDI A, (HL)
            0x2A -> LDI_A_HL(registers, mmu)

            // LDI (HL), A
            0x22 -> LDI_HL_A(registers, mmu)

            // LDH (n), A
            0xE0 -> LDH_n_A(registers, mmu)

            // LDH A, (n)
            0xF0 -> LDH_A_n(registers, mmu)

            // 16 bit loads
            // LD rr nn
            0x01 -> LD_rr_nn(registers, mmu, RegisterID.BC.ordinal)
            0x11 -> LD_rr_nn(registers, mmu, RegisterID.DE.ordinal)
            0x21 -> LD_rr_nn(registers, mmu, RegisterID.HL.ordinal)
            0x31 -> LD_rr_nn(registers, mmu, RegisterID.SP.ordinal)

            // LD SP, HL
            0xF9 -> LD_SP_HL(registers, mmu)

            // LD HL, SP+n
            0xF8 -> LD_HL_SPn(registers, mmu)

            // LD (nn), SP
            0x08 -> LD_nn_SP(registers, mmu)

            // PUSH nn
            0xF5 -> PUSH_nn(registers, mmu, RegisterID.AF.ordinal)
            0xC5 -> PUSH_nn(registers, mmu, RegisterID.BC.ordinal)
            0xD5 -> PUSH_nn(registers, mmu, RegisterID.DE.ordinal)
            0xE5 -> PUSH_nn(registers, mmu, RegisterID.HL.ordinal)

            // POP nn
            0xF1 -> POP_nn(registers, mmu, RegisterID.AF.ordinal)
            0xC1 -> POP_nn(registers, mmu, RegisterID.BC.ordinal)
            0xD1 -> POP_nn(registers, mmu, RegisterID.DE.ordinal)
            0xE1 -> POP_nn(registers, mmu, RegisterID.HL.ordinal)

            // 8 bit ALU
            // ADD A, r
            0x87 -> ADD_A_r(registers, mmu, RegisterID.A.ordinal)
            0x80 -> ADD_A_r(registers, mmu, RegisterID.B.ordinal)
            0x81 -> ADD_A_r(registers, mmu, RegisterID.C.ordinal)
            0x82 -> ADD_A_r(registers, mmu, RegisterID.D.ordinal)
            0x83 -> ADD_A_r(registers, mmu, RegisterID.E.ordinal)
            0x84 -> ADD_A_r(registers, mmu, RegisterID.H.ordinal)
            0x85 -> ADD_A_r(registers, mmu, RegisterID.L.ordinal)

            // ADD A, HL
            0x86 -> ADD_A_HL(registers, mmu)

            // ADD A, n
            0xC6 -> ADD_A_n(registers, mmu)

            // ADC A, r
            0x8F -> ADC_A_r(registers, mmu, RegisterID.A.ordinal)
            0x88 -> ADC_A_r(registers, mmu, RegisterID.B.ordinal)
            0x89 -> ADC_A_r(registers, mmu, RegisterID.C.ordinal)
            0x8A -> ADC_A_r(registers, mmu, RegisterID.D.ordinal)
            0x8B -> ADC_A_r(registers, mmu, RegisterID.E.ordinal)
            0x8C -> ADC_A_r(registers, mmu, RegisterID.H.ordinal)
            0x8D -> ADC_A_r(registers, mmu, RegisterID.L.ordinal)

            // ADC A, HL
            0x8E -> ADC_A_HL(registers, mmu)

            // ADC A, n
            0xCE -> ADC_A_n(registers, mmu)

            // SUB A, r
            0x97 -> SUB_A_r(registers, mmu, RegisterID.A.ordinal)
            0x90 -> SUB_A_r(registers, mmu, RegisterID.B.ordinal)
            0x91 -> SUB_A_r(registers, mmu, RegisterID.C.ordinal)
            0x92 -> SUB_A_r(registers, mmu, RegisterID.D.ordinal)
            0x93 -> SUB_A_r(registers, mmu, RegisterID.E.ordinal)
            0x94 -> SUB_A_r(registers, mmu, RegisterID.H.ordinal)
            0x95 -> SUB_A_r(registers, mmu, RegisterID.L.ordinal)

            // SUB A, HL
            0x96 -> SUB_A_HL(registers, mmu)

            // SUB A, n
            0xD6 -> SUB_A_n(registers, mmu)

            // SUB A, r
            0x9F -> SBC_A_r(registers, mmu, RegisterID.A.ordinal)
            0x98 -> SBC_A_r(registers, mmu, RegisterID.B.ordinal)
            0x99 -> SBC_A_r(registers, mmu, RegisterID.C.ordinal)
            0x9A -> SBC_A_r(registers, mmu, RegisterID.D.ordinal)
            0x9B -> SBC_A_r(registers, mmu, RegisterID.E.ordinal)
            0x9C -> SBC_A_r(registers, mmu, RegisterID.H.ordinal)
            0x9D -> SBC_A_r(registers, mmu, RegisterID.L.ordinal)

            // SUB A, HL
            0x9E -> SBC_A_HL(registers, mmu)

            // SUB A, n
            0xDE -> SBC_A_n(registers, mmu)

            // AND A, r
            0xA7 -> AND_A_r(registers, mmu, RegisterID.A.ordinal)
            0xA0 -> AND_A_r(registers, mmu, RegisterID.B.ordinal)
            0xA1 -> AND_A_r(registers, mmu, RegisterID.C.ordinal)
            0xA2 -> AND_A_r(registers, mmu, RegisterID.D.ordinal)
            0xA3 -> AND_A_r(registers, mmu, RegisterID.E.ordinal)
            0xA4 -> AND_A_r(registers, mmu, RegisterID.H.ordinal)
            0xA5 -> AND_A_r(registers, mmu, RegisterID.L.ordinal)

            // AND A, HL
            0xA6 -> AND_A_HL(registers, mmu)

            // AND A, n
            0xE6 -> AND_A_n(registers, mmu)

            // OR A, r
            0xB7 -> OR_A_r(registers, mmu, RegisterID.A.ordinal)
            0xB0 -> OR_A_r(registers, mmu, RegisterID.B.ordinal)
            0xB1 -> OR_A_r(registers, mmu, RegisterID.C.ordinal)
            0xB2 -> OR_A_r(registers, mmu, RegisterID.D.ordinal)
            0xB3 -> OR_A_r(registers, mmu, RegisterID.E.ordinal)
            0xB4 -> OR_A_r(registers, mmu, RegisterID.H.ordinal)
            0xB5 -> OR_A_r(registers, mmu, RegisterID.L.ordinal)

            // OR A, HL
            0xB6 -> OR_A_HL(registers, mmu)

            // OR A, n
            0xF6 -> OR_A_n(registers, mmu)

            // XOR A, r
            0xAF -> XOR_A_r(registers, mmu, RegisterID.A.ordinal)
            0xA8 -> XOR_A_r(registers, mmu, RegisterID.B.ordinal)
            0xA9 -> XOR_A_r(registers, mmu, RegisterID.C.ordinal)
            0xAA -> XOR_A_r(registers, mmu, RegisterID.D.ordinal)
            0xAB -> XOR_A_r(registers, mmu, RegisterID.E.ordinal)
            0xAC -> XOR_A_r(registers, mmu, RegisterID.H.ordinal)
            0xAD -> XOR_A_r(registers, mmu, RegisterID.L.ordinal)

            // XOR A, HL
            0xAE -> XOR_A_HL(registers, mmu)

            // XOR A, n
            0xEE -> XOR_A_n(registers, mmu)

            // CP A, r
            0xBF -> CP_A_r(registers, mmu, RegisterID.A.ordinal)
            0xB8 -> CP_A_r(registers, mmu, RegisterID.B.ordinal)
            0xB9 -> CP_A_r(registers, mmu, RegisterID.C.ordinal)
            0xBA -> CP_A_r(registers, mmu, RegisterID.D.ordinal)
            0xBB -> CP_A_r(registers, mmu, RegisterID.E.ordinal)
            0xBC -> CP_A_r(registers, mmu, RegisterID.H.ordinal)
            0xBD -> CP_A_r(registers, mmu, RegisterID.L.ordinal)

            // CP A, HL
            0xBE -> CP_A_HL(registers, mmu)

            // CP A, n
            0xFE -> CP_A_n(registers, mmu)

            // INC r
            0x3C -> INC_r(registers, mmu, RegisterID.A.ordinal)
            0x04 -> INC_r(registers, mmu, RegisterID.B.ordinal)
            0x0C -> INC_r(registers, mmu, RegisterID.C.ordinal)
            0x14 -> INC_r(registers, mmu, RegisterID.D.ordinal)
            0x1C -> INC_r(registers, mmu, RegisterID.E.ordinal)
            0x24 -> INC_r(registers, mmu, RegisterID.H.ordinal)
            0x2C -> INC_r(registers, mmu, RegisterID.L.ordinal)

            // INC HL
            0x34 -> INC_HL(registers, mmu)

            // DEC r
            0x3D -> DEC_r(registers, mmu, RegisterID.A.ordinal)
            0x05 -> DEC_r(registers, mmu, RegisterID.B.ordinal)
            0x0D -> DEC_r(registers, mmu, RegisterID.C.ordinal)
            0x15 -> DEC_r(registers, mmu, RegisterID.D.ordinal)
            0x1D -> DEC_r(registers, mmu, RegisterID.E.ordinal)
            0x25 -> DEC_r(registers, mmu, RegisterID.H.ordinal)
            0x2D -> DEC_r(registers, mmu, RegisterID.L.ordinal)

            // DEC HL
            0x35 -> DEC_HL(registers, mmu)

            // 16 bit arithmetic
            // ADD HL, rr
            0x09 -> ADD_HL_rr(registers, mmu, RegisterID.BC.ordinal)
            0x19 -> ADD_HL_rr(registers, mmu, RegisterID.DE.ordinal)
            0x29 -> ADD_HL_rr(registers, mmu, RegisterID.HL.ordinal)
            0x39 -> ADD_HL_rr(registers, mmu, RegisterID.SP.ordinal)

            // ADD SP, n
            0xE8 -> ADD_SP_n(registers, mmu)

            // INC rr
            0x03 -> INC_rr(registers, mmu, RegisterID.BC.ordinal)
            0x13 -> INC_rr(registers, mmu, RegisterID.DE.ordinal)
            0x23 -> INC_rr(registers, mmu, RegisterID.HL.ordinal)
            0x33 -> INC_rr(registers, mmu, RegisterID.SP.ordinal)

            // DEC rr
            0x0B -> DEC_rr(registers, mmu, RegisterID.BC.ordinal)
            0x1B -> DEC_rr(registers, mmu, RegisterID.DE.ordinal)
            0x2B -> DEC_rr(registers, mmu, RegisterID.HL.ordinal)
            0x3B -> DEC_rr(registers, mmu, RegisterID.SP.ordinal)

            // Miscellaneous
            // SWAP_r n

            // DAA
            0x27 -> DAA(registers, mmu)

            // CPL
            0x2F -> CPL(registers, mmu)

            // CCF
            0x3F -> CCF(registers, mmu)

            // SCF
            0x37 -> SCF(registers, mmu)

            // NOP
            0x00 -> NOP(registers, mmu)

            // HALT
            0x76 -> HALT(registers, mmu)

            // STOP
            0x10 -> STOP(registers, mmu)

            // DI
            0xF3 -> {
                diExecuted = true
                return DI(registers, mmu)
            }

            // EI
            0xFB -> {
                eiExecuted = true
                return EI(registers, mmu)
            }

            // Jumps
            // JP nn
            0xC3 -> JP_nn(registers, mmu)

            // JP cc, nn
            0xC2 -> JP_cc_nn(registers, mmu, registers.ZFlag, false)
            0xCA -> JP_cc_nn(registers, mmu, registers.ZFlag, true)
            0xD2 -> JP_cc_nn(registers, mmu, registers.CFlag, false)
            0xDA -> JP_cc_nn(registers, mmu, registers.CFlag, true)

            // JP HL
            0xE9 -> JP_HL(registers, mmu)

            // JR n
            0x18 -> JR_n(registers, mmu)

            // JR cc, n
            0x20 -> JR_cc_n(registers, mmu, registers.ZFlag, false)
            0x28 -> JR_cc_n(registers, mmu, registers.ZFlag, true)
            0x30 -> JR_cc_n(registers, mmu, registers.CFlag, false)
            0x38 -> JR_cc_n(registers, mmu, registers.CFlag, true)

            // Calls
            // CALL nn
            0xCD -> CALL_nn(registers, mmu)

            // CALL cc, nn
            0xC4 -> CALL_cc_nn(registers, mmu, registers.ZFlag, false)
            0xCC -> CALL_cc_nn(registers, mmu, registers.ZFlag, true)
            0xD4 -> CALL_cc_nn(registers, mmu, registers.CFlag, false)
            0xDC -> CALL_cc_nn(registers, mmu, registers.CFlag, true)

            // Restarts
            // RST n
            0xC7 -> RST_n(registers, mmu, 0x00)
            0xCF -> RST_n(registers, mmu, 0x08)
            0xD7 -> RST_n(registers, mmu, 0x10)
            0xDF -> RST_n(registers, mmu, 0x08)
            0xE7 -> RST_n(registers, mmu, 0x20)
            0xEF -> RST_n(registers, mmu, 0x28)
            0xF7 -> RST_n(registers, mmu, 0x30)
            0xFF -> RST_n(registers, mmu, 0x38)

            // Returns
            // RET
            0xC9 -> RET(registers, mmu)

            // RET cc
            0xC0 -> RET_cc(registers, mmu, registers.ZFlag, false)
            0xC8 -> RET_cc(registers, mmu, registers.ZFlag, true)
            0xD0 -> RET_cc(registers, mmu, registers.CFlag, false)
            0xD8 -> RET_cc(registers, mmu, registers.CFlag, true)

            // RETI
            0xD9 -> {
                registers.IME = true
                RETI(registers, mmu)
            }

            // Rotates and shifts
            // RLCA
            0x07 -> RLCA(registers, mmu)

            // RLA
            0x17 -> RLA(registers, mmu)

            // RRCA
            0x0F -> RRCA(registers, mmu)

            // RRA
            0x1F -> RRA(registers, mmu)

            0xCB -> getExtendedInstruction()

            else -> throw Exception("Instruction not implemented: " + Integer.toHexString(opcode))
        }
    }

    private fun getExtendedInstruction(): Instruction {
        val opcode = mmu.readByte(registers.PC)
        registers.incPC()
        return when(opcode) {
            // Miscellaneous
            // SWAP r
            0x37 -> SWAP_r(registers, mmu, RegisterID.A.ordinal)
            0x30 -> SWAP_r(registers, mmu, RegisterID.B.ordinal)
            0x31 -> SWAP_r(registers, mmu, RegisterID.C.ordinal)
            0x32 -> SWAP_r(registers, mmu, RegisterID.D.ordinal)
            0x33 -> SWAP_r(registers, mmu, RegisterID.E.ordinal)
            0x34 -> SWAP_r(registers, mmu, RegisterID.H.ordinal)
            0x35 -> SWAP_r(registers, mmu, RegisterID.L.ordinal)

            // SWAP (HL)
            0x36 -> SWAP_HL(registers, mmu)

            // RLC r
            0x07 -> RLC_r(registers, mmu, RegisterID.A.ordinal)
            0x00 -> RLC_r(registers, mmu, RegisterID.B.ordinal)
            0x01 -> RLC_r(registers, mmu, RegisterID.C.ordinal)
            0x02 -> RLC_r(registers, mmu, RegisterID.D.ordinal)
            0x03 -> RLC_r(registers, mmu, RegisterID.E.ordinal)
            0x04 -> RLC_r(registers, mmu, RegisterID.H.ordinal)
            0x05 -> RLC_r(registers, mmu, RegisterID.L.ordinal)

            // RLC (HL)
            0x06 -> RLC_HL(registers, mmu)

            // RL r
            0x17 -> RL_r(registers, mmu, RegisterID.A.ordinal)
            0x10 -> RL_r(registers, mmu, RegisterID.B.ordinal)
            0x11 -> RL_r(registers, mmu, RegisterID.C.ordinal)
            0x12 -> RL_r(registers, mmu, RegisterID.D.ordinal)
            0x13 -> RL_r(registers, mmu, RegisterID.E.ordinal)
            0x14 -> RL_r(registers, mmu, RegisterID.H.ordinal)
            0x15 -> RL_r(registers, mmu, RegisterID.L.ordinal)

            // RL (HL)
            0x16 -> RL_HL(registers, mmu)

            // RRC r
            0x0F -> RRC_r(registers, mmu, RegisterID.A.ordinal)
            0x08 -> RRC_r(registers, mmu, RegisterID.B.ordinal)
            0x09 -> RRC_r(registers, mmu, RegisterID.C.ordinal)
            0x0A -> RRC_r(registers, mmu, RegisterID.D.ordinal)
            0x0B -> RRC_r(registers, mmu, RegisterID.E.ordinal)
            0x0C -> RRC_r(registers, mmu, RegisterID.H.ordinal)
            0x0D -> RRC_r(registers, mmu, RegisterID.L.ordinal)

            // RRC (HL)
            0x0E -> RRC_HL(registers, mmu)

            // RR r
            0x1F -> RR_r(registers, mmu, RegisterID.A.ordinal)
            0x18 -> RR_r(registers, mmu, RegisterID.B.ordinal)
            0x19 -> RR_r(registers, mmu, RegisterID.C.ordinal)
            0x1A -> RR_r(registers, mmu, RegisterID.D.ordinal)
            0x1B -> RR_r(registers, mmu, RegisterID.E.ordinal)
            0x1C -> RR_r(registers, mmu, RegisterID.H.ordinal)
            0x1D -> RR_r(registers, mmu, RegisterID.L.ordinal)

            // RR (HL)
            0x1E -> RR_HL(registers, mmu)

            // SLA r
            0x27 -> SLA_r(registers, mmu, RegisterID.A.ordinal)
            0x20 -> SLA_r(registers, mmu, RegisterID.B.ordinal)
            0x21 -> SLA_r(registers, mmu, RegisterID.C.ordinal)
            0x22 -> SLA_r(registers, mmu, RegisterID.D.ordinal)
            0x23 -> SLA_r(registers, mmu, RegisterID.E.ordinal)
            0x24 -> SLA_r(registers, mmu, RegisterID.H.ordinal)
            0x25 -> SLA_r(registers, mmu, RegisterID.L.ordinal)

            // SLA (HL)
            0x26 -> SLA_HL(registers, mmu)

            // SRA r
            0x2F -> SRA_r(registers, mmu, RegisterID.A.ordinal)
            0x28 -> SRA_r(registers, mmu, RegisterID.B.ordinal)
            0x29 -> SRA_r(registers, mmu, RegisterID.C.ordinal)
            0x2A -> SRA_r(registers, mmu, RegisterID.D.ordinal)
            0x2B -> SRA_r(registers, mmu, RegisterID.E.ordinal)
            0x2C -> SRA_r(registers, mmu, RegisterID.H.ordinal)
            0x2D -> SRA_r(registers, mmu, RegisterID.L.ordinal)

            // SRA (HL)
            0x2E -> SRA_HL(registers, mmu)

            // SRL r
            0x3F -> SRL_r(registers, mmu, RegisterID.A.ordinal)
            0x38 -> SRL_r(registers, mmu, RegisterID.B.ordinal)
            0x39 -> SRL_r(registers, mmu, RegisterID.C.ordinal)
            0x3A -> SRL_r(registers, mmu, RegisterID.D.ordinal)
            0x3B -> SRL_r(registers, mmu, RegisterID.E.ordinal)
            0x3C -> SRL_r(registers, mmu, RegisterID.H.ordinal)
            0x3D -> SRL_r(registers, mmu, RegisterID.L.ordinal)

            // SRL (HL)
            0x3E -> SRL_HL(registers, mmu)

            // BIT
            // BIT 0, r
            0x40 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 0)
            0x41 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 0)
            0x42 -> BIT_r(registers, mmu, RegisterID.D.ordinal, 0)
            0x43 -> BIT_r(registers, mmu, RegisterID.E.ordinal, 0)
            0x44 -> BIT_r(registers, mmu, RegisterID.H.ordinal, 0)
            0x45 -> BIT_r(registers, mmu, RegisterID.L.ordinal, 0)
            0x47 -> BIT_r(registers, mmu, RegisterID.A.ordinal, 0)

            // BIT 0, (HL)
            0x46 -> BIT_HL(registers, mmu, 0)

            // BIT 1, r
            0x48 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 1)
            0x49 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 1)
            0x4A -> BIT_r(registers, mmu, RegisterID.D.ordinal, 1)
            0x4B -> BIT_r(registers, mmu, RegisterID.E.ordinal, 1)
            0x4C -> BIT_r(registers, mmu, RegisterID.H.ordinal, 1)
            0x4D -> BIT_r(registers, mmu, RegisterID.L.ordinal, 1)
            0x4F -> BIT_r(registers, mmu, RegisterID.A.ordinal, 1)

            // BIT 1, (HL)
            0x4E -> BIT_HL(registers, mmu, 1)

            // BIT 2, r
            0x50 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 2)
            0x51 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 2)
            0x52 -> BIT_r(registers, mmu, RegisterID.D.ordinal, 2)
            0x53 -> BIT_r(registers, mmu, RegisterID.E.ordinal, 2)
            0x54 -> BIT_r(registers, mmu, RegisterID.H.ordinal, 2)
            0x55 -> BIT_r(registers, mmu, RegisterID.L.ordinal, 2)
            0x57 -> BIT_r(registers, mmu, RegisterID.A.ordinal, 2)

            // BIT 2, (HL)
            0x56 -> BIT_HL(registers, mmu, 2)

            // BIT 3, r
            0x58 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 3)
            0x59 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 3)
            0x5A -> BIT_r(registers, mmu, RegisterID.D.ordinal, 3)
            0x5B -> BIT_r(registers, mmu, RegisterID.E.ordinal, 3)
            0x5C -> BIT_r(registers, mmu, RegisterID.H.ordinal, 3)
            0x5D -> BIT_r(registers, mmu, RegisterID.L.ordinal, 3)
            0x5F -> BIT_r(registers, mmu, RegisterID.A.ordinal, 3)

            // BIT 3, (HL)
            0x5E -> BIT_HL(registers, mmu, 3)

            // BIT 4, r
            0x60 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 4)
            0x61 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 4)
            0x62 -> BIT_r(registers, mmu, RegisterID.D.ordinal, 4)
            0x63 -> BIT_r(registers, mmu, RegisterID.E.ordinal, 4)
            0x64 -> BIT_r(registers, mmu, RegisterID.H.ordinal, 4)
            0x65 -> BIT_r(registers, mmu, RegisterID.L.ordinal, 4)
            0x67 -> BIT_r(registers, mmu, RegisterID.A.ordinal, 4)

            // BIT 4, (HL)
            0x66 -> BIT_HL(registers, mmu, 4)

            // BIT 5, r
            0x68 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 5)
            0x69 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 5)
            0x6A -> BIT_r(registers, mmu, RegisterID.D.ordinal, 5)
            0x6B -> BIT_r(registers, mmu, RegisterID.E.ordinal, 5)
            0x6C -> BIT_r(registers, mmu, RegisterID.H.ordinal, 5)
            0x6D -> BIT_r(registers, mmu, RegisterID.L.ordinal, 5)
            0x6F -> BIT_r(registers, mmu, RegisterID.A.ordinal, 5)

            // BIT 5, (HL)
            0x6E -> BIT_HL(registers, mmu, 5)

            // BIT 6, r
            0x70 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 6)
            0x71 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 6)
            0x72 -> BIT_r(registers, mmu, RegisterID.D.ordinal, 6)
            0x73 -> BIT_r(registers, mmu, RegisterID.E.ordinal, 6)
            0x74 -> BIT_r(registers, mmu, RegisterID.H.ordinal, 6)
            0x75 -> BIT_r(registers, mmu, RegisterID.L.ordinal, 6)
            0x77 -> BIT_r(registers, mmu, RegisterID.A.ordinal, 6)

            // BIT 6, (HL)
            0x76 -> BIT_HL(registers, mmu, 6)

            // BIT 7, r
            0x78 -> BIT_r(registers, mmu, RegisterID.B.ordinal, 7)
            0x79 -> BIT_r(registers, mmu, RegisterID.C.ordinal, 7)
            0x7A -> BIT_r(registers, mmu, RegisterID.D.ordinal, 7)
            0x7B -> BIT_r(registers, mmu, RegisterID.E.ordinal, 7)
            0x7C -> BIT_r(registers, mmu, RegisterID.H.ordinal, 7)
            0x7D -> BIT_r(registers, mmu, RegisterID.L.ordinal, 7)
            0x7F -> BIT_r(registers, mmu, RegisterID.A.ordinal, 7)

            // BIT 7, (HL)
            0x7E -> BIT_HL(registers, mmu, 7)

            // SET
            // SET 0, r
            0xC0 -> SET_r(registers, mmu, RegisterID.B.ordinal, 0)
            0xC1 -> SET_r(registers, mmu, RegisterID.C.ordinal, 0)
            0xC2 -> SET_r(registers, mmu, RegisterID.D.ordinal, 0)
            0xC3 -> SET_r(registers, mmu, RegisterID.E.ordinal, 0)
            0xC4 -> SET_r(registers, mmu, RegisterID.H.ordinal, 0)
            0xC5 -> SET_r(registers, mmu, RegisterID.L.ordinal, 0)
            0xC7 -> SET_r(registers, mmu, RegisterID.A.ordinal, 0)

            // SET 0, (HL)
            0xC6 -> SET_HL(registers, mmu, 0)

            // SET 1, r
            0xC8 -> SET_r(registers, mmu, RegisterID.B.ordinal, 1)
            0xC9 -> SET_r(registers, mmu, RegisterID.C.ordinal, 1)
            0xCA -> SET_r(registers, mmu, RegisterID.D.ordinal, 1)
            0xCB -> SET_r(registers, mmu, RegisterID.E.ordinal, 1)
            0xCC -> SET_r(registers, mmu, RegisterID.H.ordinal, 1)
            0xCD -> SET_r(registers, mmu, RegisterID.L.ordinal, 1)
            0xCF -> SET_r(registers, mmu, RegisterID.A.ordinal, 1)

            // SET 1, (HL)
            0xCE -> SET_HL(registers, mmu, 1)

            // SET 2, r
            0xD0 -> SET_r(registers, mmu, RegisterID.B.ordinal, 2)
            0xD1 -> SET_r(registers, mmu, RegisterID.C.ordinal, 2)
            0xD2 -> SET_r(registers, mmu, RegisterID.D.ordinal, 2)
            0xD3 -> SET_r(registers, mmu, RegisterID.E.ordinal, 2)
            0xD4 -> SET_r(registers, mmu, RegisterID.H.ordinal, 2)
            0xD5 -> SET_r(registers, mmu, RegisterID.L.ordinal, 2)
            0xD7 -> SET_r(registers, mmu, RegisterID.A.ordinal, 2)

            // SET 2, (HL)
            0xD6 -> SET_HL(registers, mmu, 2)

            // SET 3, r
            0xD8 -> SET_r(registers, mmu, RegisterID.B.ordinal, 3)
            0xD9 -> SET_r(registers, mmu, RegisterID.C.ordinal, 3)
            0xDA -> SET_r(registers, mmu, RegisterID.D.ordinal, 3)
            0xDB -> SET_r(registers, mmu, RegisterID.E.ordinal, 3)
            0xDC -> SET_r(registers, mmu, RegisterID.H.ordinal, 3)
            0xDD -> SET_r(registers, mmu, RegisterID.L.ordinal, 3)
            0xDF -> SET_r(registers, mmu, RegisterID.A.ordinal, 3)

            // SET 3, (HL)
            0xDE -> SET_HL(registers, mmu, 3)

            // SET 4, r
            0xE0 -> SET_r(registers, mmu, RegisterID.B.ordinal, 4)
            0xE1 -> SET_r(registers, mmu, RegisterID.C.ordinal, 4)
            0xE2 -> SET_r(registers, mmu, RegisterID.D.ordinal, 4)
            0xE3 -> SET_r(registers, mmu, RegisterID.E.ordinal, 4)
            0xE4 -> SET_r(registers, mmu, RegisterID.H.ordinal, 4)
            0xE5 -> SET_r(registers, mmu, RegisterID.L.ordinal, 4)
            0xE7 -> SET_r(registers, mmu, RegisterID.A.ordinal, 4)

            // SET 4, (HL)
            0xE6 -> SET_HL(registers, mmu, 4)

            // SET 5, r
            0xE8 -> SET_r(registers, mmu, RegisterID.B.ordinal, 5)
            0xE9 -> SET_r(registers, mmu, RegisterID.C.ordinal, 5)
            0xEA -> SET_r(registers, mmu, RegisterID.D.ordinal, 5)
            0xEB -> SET_r(registers, mmu, RegisterID.E.ordinal, 5)
            0xEC -> SET_r(registers, mmu, RegisterID.H.ordinal, 5)
            0xED -> SET_r(registers, mmu, RegisterID.L.ordinal, 5)
            0xEF -> SET_r(registers, mmu, RegisterID.A.ordinal, 5)

            // SET 5, (HL)
            0xEE -> SET_HL(registers, mmu, 5)

            // SET 6, r
            0xF0 -> SET_r(registers, mmu, RegisterID.B.ordinal, 6)
            0xF1 -> SET_r(registers, mmu, RegisterID.C.ordinal, 6)
            0xF2 -> SET_r(registers, mmu, RegisterID.D.ordinal, 6)
            0xF3 -> SET_r(registers, mmu, RegisterID.E.ordinal, 6)
            0xF4 -> SET_r(registers, mmu, RegisterID.H.ordinal, 6)
            0xF5 -> SET_r(registers, mmu, RegisterID.L.ordinal, 6)
            0xF7 -> SET_r(registers, mmu, RegisterID.A.ordinal, 6)

            // SET 6, (HL)
            0xF6 -> SET_HL(registers, mmu, 6)

            // SET 7, r
            0xF8 -> SET_r(registers, mmu, RegisterID.B.ordinal, 7)
            0xF9 -> SET_r(registers, mmu, RegisterID.C.ordinal, 7)
            0xFA -> SET_r(registers, mmu, RegisterID.D.ordinal, 7)
            0xFB -> SET_r(registers, mmu, RegisterID.E.ordinal, 7)
            0xFC -> SET_r(registers, mmu, RegisterID.H.ordinal, 7)
            0xFD -> SET_r(registers, mmu, RegisterID.L.ordinal, 7)
            0xFF -> SET_r(registers, mmu, RegisterID.A.ordinal, 7)

            // SET 7, (HL)
            0xFE -> SET_HL(registers, mmu, 7)

            // RES
            // RES 0, r
            0x80 -> RES_r(registers, mmu, RegisterID.B.ordinal, 0)
            0x81 -> RES_r(registers, mmu, RegisterID.C.ordinal, 0)
            0x82 -> RES_r(registers, mmu, RegisterID.D.ordinal, 0)
            0x83 -> RES_r(registers, mmu, RegisterID.E.ordinal, 0)
            0x84 -> RES_r(registers, mmu, RegisterID.H.ordinal, 0)
            0x85 -> RES_r(registers, mmu, RegisterID.L.ordinal, 0)
            0x87 -> RES_r(registers, mmu, RegisterID.A.ordinal, 0)

            // RES 0, (HL)
            0x86 -> RES_HL(registers, mmu, 0)

            // RES 1, r
            0x88 -> RES_r(registers, mmu, RegisterID.B.ordinal, 1)
            0x89 -> RES_r(registers, mmu, RegisterID.C.ordinal, 1)
            0x8A -> RES_r(registers, mmu, RegisterID.D.ordinal, 1)
            0x8B -> RES_r(registers, mmu, RegisterID.E.ordinal, 1)
            0x8C -> RES_r(registers, mmu, RegisterID.H.ordinal, 1)
            0x8D -> RES_r(registers, mmu, RegisterID.L.ordinal, 1)
            0x8F -> RES_r(registers, mmu, RegisterID.A.ordinal, 1)

            // RES 1, (HL)
            0x8E -> RES_HL(registers, mmu, 1)

            // RES 2, r
            0x90 -> RES_r(registers, mmu, RegisterID.B.ordinal, 2)
            0x91 -> RES_r(registers, mmu, RegisterID.C.ordinal, 2)
            0x92 -> RES_r(registers, mmu, RegisterID.D.ordinal, 2)
            0x93 -> RES_r(registers, mmu, RegisterID.E.ordinal, 2)
            0x94 -> RES_r(registers, mmu, RegisterID.H.ordinal, 2)
            0x95 -> RES_r(registers, mmu, RegisterID.L.ordinal, 2)
            0x97 -> RES_r(registers, mmu, RegisterID.A.ordinal, 2)

            // RES 2, (HL)
            0x96 -> RES_HL(registers, mmu, 2)

            // RES 3, r
            0x98 -> RES_r(registers, mmu, RegisterID.B.ordinal, 3)
            0x99 -> RES_r(registers, mmu, RegisterID.C.ordinal, 3)
            0x9A -> RES_r(registers, mmu, RegisterID.D.ordinal, 3)
            0x9B -> RES_r(registers, mmu, RegisterID.E.ordinal, 3)
            0x9C -> RES_r(registers, mmu, RegisterID.H.ordinal, 3)
            0x9D -> RES_r(registers, mmu, RegisterID.L.ordinal, 3)
            0x9F -> RES_r(registers, mmu, RegisterID.A.ordinal, 3)

            // RES 3, (HL)
            0x9E -> RES_HL(registers, mmu, 3)

            // RES 4, r
            0xA0 -> RES_r(registers, mmu, RegisterID.B.ordinal, 4)
            0xA1 -> RES_r(registers, mmu, RegisterID.C.ordinal, 4)
            0xA2 -> RES_r(registers, mmu, RegisterID.D.ordinal, 4)
            0xA3 -> RES_r(registers, mmu, RegisterID.E.ordinal, 4)
            0xA4 -> RES_r(registers, mmu, RegisterID.H.ordinal, 4)
            0xA5 -> RES_r(registers, mmu, RegisterID.L.ordinal, 4)
            0xA7 -> RES_r(registers, mmu, RegisterID.A.ordinal, 4)

            // RES 4, (HL)
            0xA6 -> RES_HL(registers, mmu, 4)

            // RES 5, r
            0xA8 -> RES_r(registers, mmu, RegisterID.B.ordinal, 5)
            0xA9 -> RES_r(registers, mmu, RegisterID.C.ordinal, 5)
            0xAA -> RES_r(registers, mmu, RegisterID.D.ordinal, 5)
            0xAB -> RES_r(registers, mmu, RegisterID.E.ordinal, 5)
            0xAC -> RES_r(registers, mmu, RegisterID.H.ordinal, 5)
            0xAD -> RES_r(registers, mmu, RegisterID.L.ordinal, 5)
            0xAF -> RES_r(registers, mmu, RegisterID.A.ordinal, 5)

            // RES 5, (HL)
            0xAE -> RES_HL(registers, mmu, 5)

            // RES 6, r
            0xB0 -> RES_r(registers, mmu, RegisterID.B.ordinal, 6)
            0xB1 -> RES_r(registers, mmu, RegisterID.C.ordinal, 6)
            0xB2 -> RES_r(registers, mmu, RegisterID.D.ordinal, 6)
            0xB3 -> RES_r(registers, mmu, RegisterID.E.ordinal, 6)
            0xB4 -> RES_r(registers, mmu, RegisterID.H.ordinal, 6)
            0xB5 -> RES_r(registers, mmu, RegisterID.L.ordinal, 6)
            0xB7 -> RES_r(registers, mmu, RegisterID.A.ordinal, 6)

            // RES 6, (HL)
            0xB6 -> RES_HL(registers, mmu, 6)

            // RES 7, r
            0xB8 -> RES_r(registers, mmu, RegisterID.B.ordinal, 7)
            0xB9 -> RES_r(registers, mmu, RegisterID.C.ordinal, 7)
            0xBA -> RES_r(registers, mmu, RegisterID.D.ordinal, 7)
            0xBB -> RES_r(registers, mmu, RegisterID.E.ordinal, 7)
            0xBC -> RES_r(registers, mmu, RegisterID.H.ordinal, 7)
            0xBD -> RES_r(registers, mmu, RegisterID.L.ordinal, 7)
            0xBF -> RES_r(registers, mmu, RegisterID.A.ordinal, 7)

            // RES 7, (HL)
            0xBE -> RES_HL(registers, mmu, 7)
            else -> throw Exception("Instruction not implemented: " + Integer.toHexString(opcode))
        }
    }
}

