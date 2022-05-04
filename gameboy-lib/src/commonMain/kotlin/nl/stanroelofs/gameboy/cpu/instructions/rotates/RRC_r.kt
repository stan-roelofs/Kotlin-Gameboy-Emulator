package nl.stanroelofs.gameboy.cpu.instructions.rotates

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu

class RRC_r(registers: Registers, mmu: Mmu, private val register: Int) : RRC(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = rrc(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = rrc(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = rrc(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = rrc(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = rrc(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = rrc(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = rrc(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}