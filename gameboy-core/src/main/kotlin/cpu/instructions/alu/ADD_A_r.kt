package cpu.instructions.alu

import cpu.RegisterID
import cpu.Registers
import memory.Mmu
import utils.Log

class ADD_A_r(registers: Registers, mmu: Mmu, private val register: Int) : ADD(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = when(register) {
                    RegisterID.A.ordinal -> registers.A
                    RegisterID.B.ordinal -> registers.B
                    RegisterID.C.ordinal -> registers.C
                    RegisterID.D.ordinal -> registers.D
                    RegisterID.E.ordinal -> registers.E
                    RegisterID.H.ordinal -> registers.H
                    RegisterID.L.ordinal -> registers.L
                    else -> throw Exception("Invalid register: $register")
                }
                super.add8(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}