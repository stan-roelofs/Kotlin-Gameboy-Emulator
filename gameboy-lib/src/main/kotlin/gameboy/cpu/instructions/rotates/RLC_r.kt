package gameboy.cpu.instructions.rotates

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.memory.Mmu

class RLC_r(registers: Registers, mmu: Mmu, private val register: Int) : RLC(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = rlc(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = rlc(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = rlc(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = rlc(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = rlc(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = rlc(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = rlc(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}