package cpu.instructions.loads

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setSecondByte

class POP_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = popFromStack()
            }
            8 -> {
                value = setSecondByte(value, popFromStack())

                when (register) {
                    RegisterID.AF.ordinal -> registers.setAF(value)
                    RegisterID.BC.ordinal -> registers.setBC(value)
                    RegisterID.DE.ordinal -> registers.setDE(value)
                    RegisterID.HL.ordinal -> registers.setHL(value)
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}