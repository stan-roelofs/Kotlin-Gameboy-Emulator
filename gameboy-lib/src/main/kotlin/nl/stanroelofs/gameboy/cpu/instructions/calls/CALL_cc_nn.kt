package nl.stanroelofs.gameboy.cpu.instructions.calls

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getFirstByte
import nl.stanroelofs.gameboy.utils.getSecondByte
import nl.stanroelofs.gameboy.utils.setSecondByte

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
                address = address.setSecondByte(getImmediate())

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