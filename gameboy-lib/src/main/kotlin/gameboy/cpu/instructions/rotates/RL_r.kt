package gameboy.cpu.instructions.rotates

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.memory.Mmu
import gameboy.utils.Log

class RL_r(registers: Registers, mmu: Mmu, private val register: Int) : RL(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = rl(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = rl(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = rl(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = rl(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = rl(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = rl(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = rl(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}