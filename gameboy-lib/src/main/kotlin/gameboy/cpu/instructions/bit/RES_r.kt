package gameboy.cpu.instructions.bit

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.clearBit

class RES_r(registers: Registers, mmu: Mmu, private val register: Int, private val index: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> registers.A = clearBit(registers.A, index)
                    RegisterID.B.ordinal -> registers.B = clearBit(registers.B, index)
                    RegisterID.C.ordinal -> registers.C = clearBit(registers.C, index)
                    RegisterID.D.ordinal -> registers.D = clearBit(registers.D, index)
                    RegisterID.E.ordinal -> registers.E = clearBit(registers.E, index)
                    RegisterID.H.ordinal -> registers.H = clearBit(registers.H, index)
                    RegisterID.L.ordinal -> registers.L = clearBit(registers.L, index)
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}