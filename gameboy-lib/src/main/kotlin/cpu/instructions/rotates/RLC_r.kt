package cpu.instructions.rotates

import cpu.RegisterID
import cpu.Registers
import memory.Mmu
import utils.Log

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
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}