package cpu.instructions.calls

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class CALL_cc_nn(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {
    override fun execute(): Int {

        val address = getWordImmediate()

        when {
            flag == registers.ZFlag && state && registers.getZFlag() -> {
                pushWordToStack(registers.PC)
                registers.PC = address
                return 24
            }
            flag == registers.ZFlag && !state && !registers.getZFlag() -> {
                pushWordToStack(registers.PC)
                registers.PC = address
                return 24
            }
            flag == registers.CFlag && state && registers.getCFlag() -> {
                pushWordToStack(registers.PC)
                registers.PC = address
                return 24
            }
            flag == registers.CFlag && !state && !registers.getCFlag() -> {
                pushWordToStack(registers.PC)
                registers.PC = address
                return 24
            }
        }

        return 12
    }
}