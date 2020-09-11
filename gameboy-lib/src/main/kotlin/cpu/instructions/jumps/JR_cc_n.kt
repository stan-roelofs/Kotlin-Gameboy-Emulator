package gameboy.cpu.instructions.jumps

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log

class JR_cc_n(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    private var value = 0
    private var conditionHolds = true
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        value = 0
        conditionHolds = true
    }

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()

                if (!(flag == registers.ZFlag && state && registers.getZFlag()) &&
                        !(flag == registers.ZFlag && !state && !registers.getZFlag()) &&
                        !(flag == registers.CFlag && state && registers.getCFlag()) &&
                        !(flag == registers.CFlag && !state && !registers.getCFlag())) {
                    conditionHolds = false
                }
            }
            8 -> {
                if (conditionHolds) {
                    registers.PC += value
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }

    override fun isExecuting(): Boolean {
        if ((!conditionHolds && currentCycle == 8) || (conditionHolds && currentCycle == totalCycles)) {
            return false
        }
        return true
    }
}