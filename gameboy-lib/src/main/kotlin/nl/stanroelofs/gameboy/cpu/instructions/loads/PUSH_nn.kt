package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.firstByte
import nl.stanroelofs.gameboy.utils.secondByte

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
                        registers.AF
                    }
                    RegisterID.BC.ordinal -> {
                        registers.BC
                    }
                    RegisterID.DE.ordinal -> {
                        registers.DE
                    }
                    RegisterID.HL.ordinal -> {
                        registers.HL
                    }
                    else -> throw Exception("Invalid register: $register")
                }
                pushToStack(value.secondByte)
            }
            12 -> {
                pushToStack(value.firstByte)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}