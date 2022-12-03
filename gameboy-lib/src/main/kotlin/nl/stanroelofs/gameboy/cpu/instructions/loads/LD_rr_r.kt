package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LD_rr_r(registers: Registers, mmu: Mmu, private val register1: Int, private val register2: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val value = when(register2) {
                    RegisterID.A.ordinal -> registers.A
                    RegisterID.B.ordinal -> registers.B
                    RegisterID.C.ordinal -> registers.C
                    RegisterID.D.ordinal -> registers.D
                    RegisterID.E.ordinal -> registers.E
                    RegisterID.H.ordinal -> registers.H
                    RegisterID.L.ordinal -> registers.L
                    else -> throw Exception("Invalid register: $register2")
                }

                when(register1) {
                    RegisterID.BC.ordinal -> mmu.writeByte(registers.BC, value)
                    RegisterID.DE.ordinal -> mmu.writeByte(registers.DE, value)
                    RegisterID.HL.ordinal -> mmu.writeByte(registers.HL, value)
                    else -> throw Exception("Invalid register: $register1")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}