package gameboy.cpu.instructions.calls

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte
import gameboy.utils.setSecondByte

class CALL_cc_nn(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    private var conditionHolds = true
    private var address = 0
    override val totalCycles = 24

    override fun reset() {
        super.reset()
        conditionHolds = true
        address = 0
    }

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = getImmediate()
            }
            8 -> {
                address = setSecondByte(address, getImmediate())

                if (!(flag == registers.ZFlag && state && registers.getZFlag()) &&
                        !(flag == registers.ZFlag && !state && !registers.getZFlag()) &&
                        !(flag == registers.CFlag && state && registers.getCFlag()) &&
                        !(flag == registers.CFlag && !state && !registers.getCFlag())) {
                    conditionHolds = false
                }
            }
            12 -> {

            }
            16 -> {
                if (conditionHolds) {
                    pushToStack(registers.PC.getSecondByte())
                }
            }
            20 -> {
                if (conditionHolds) {
                    pushToStack(registers.PC.getFirstByte())
                    registers.PC = address
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }

    override fun isExecuting(): Boolean {
        if ((!conditionHolds && currentCycle == 12) || (conditionHolds && currentCycle == totalCycles)) {
            return false
        }
        return true
    }
}