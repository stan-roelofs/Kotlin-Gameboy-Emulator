package cpu.instructions.shifts

import cpu.RegisterID
import cpu.Registers
import memory.Mmu
import utils.Log

class SRA_r(registers: Registers, mmu: Mmu, private val register: Int) : SRA(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A = sra(value)
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B = sra(value)
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C = sra(value)
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D = sra(value)
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E = sra(value)
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H = sra(value)
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L = sra(value)
                    }
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}