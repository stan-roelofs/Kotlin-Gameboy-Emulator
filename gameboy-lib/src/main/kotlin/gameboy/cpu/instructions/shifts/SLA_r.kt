package gameboy.cpu.instructions.shifts

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.memory.Mmu

class SLA_r(registers: Registers, mmu: Mmu, private val register: Int) : SLA(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = sla(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = sla(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = sla(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = sla(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = sla(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = sla(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = sla(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}