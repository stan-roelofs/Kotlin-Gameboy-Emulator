package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getFirstByte
import nl.stanroelofs.gameboy.utils.getSecondByte

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