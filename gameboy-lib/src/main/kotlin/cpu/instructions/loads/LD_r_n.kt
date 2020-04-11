package cpu.instructions.loads

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LD_r_n(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 8

    override fun tick() {
        when (currentCycle) {
            0 -> {
                value = getImmediate()
            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> registers.A = value
                    RegisterID.B.ordinal -> registers.B = value
                    RegisterID.C.ordinal -> registers.C = value
                    RegisterID.D.ordinal -> registers.D = value
                    RegisterID.E.ordinal -> registers.E = value
                    RegisterID.H.ordinal -> registers.H = value
                    RegisterID.L.ordinal -> registers.L = value
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}