package cpu.instructions.jumps

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class JP_cc_nn(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = getWordImmediate()

        when {
            flag == registers.ZFlag && state && registers.getZFlag() -> {
                registers.PC = value
                return 16
            }
            flag == registers.ZFlag && !state && !registers.getZFlag() -> {
                registers.PC = value
                return 16
            }
            flag == registers.CFlag && state && registers.getCFlag() -> {
                registers.PC = value
                return 16
            }
            flag == registers.CFlag && !state && !registers.getCFlag() -> {
                registers.PC = value
                return 16
            }
        }

        return 12
    }
}