package nl.stanroelofs.gameboy.cpu.instructions.rotates

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu

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
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}