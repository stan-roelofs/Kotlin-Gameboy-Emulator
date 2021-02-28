package gameboy.cpu.instructions.loads

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte

class PUSH_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 16

    override fun reset() {
        super.reset()
        value = 0
    }

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
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}