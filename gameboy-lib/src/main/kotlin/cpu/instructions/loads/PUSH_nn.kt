package cpu.instructions.loads

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.getFirstByte
import utils.getSecondByte

class PUSH_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 16

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                value = when (register) {
                    RegisterID.AF.ordinal -> {
                        registers.getAF()
                    }
                    RegisterID.BC.ordinal -> {
                        registers.getBC()
                    }
                    RegisterID.DE.ordinal -> {
                        registers.getDE()
                    }
                    RegisterID.HL.ordinal -> {
                        registers.getHL()
                    }
                    else -> throw Exception("Invalid register: $register")
                }
                pushToStack(value.getSecondByte())
            }
            12 -> {
                pushToStack(value.getFirstByte())
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}