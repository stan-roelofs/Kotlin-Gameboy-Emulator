package nl.stanroelofs.gameboy.cpu.instructions.shifts

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu

class SRL_r(registers: Registers, mmu: Mmu, private val register: Int) : SRL(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = srl(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = srl(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = srl(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = srl(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = srl(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = srl(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = srl(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}