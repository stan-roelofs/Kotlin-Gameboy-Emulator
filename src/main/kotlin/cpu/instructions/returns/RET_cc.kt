package cpu.instructions.returns

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class RET_cc(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {
    override fun execute(): Int {
        when {
            flag == registers.ZFlag && state && registers.getZFlag() -> {
                val address = popWordFromStack()
                registers.PC = address
                return 20
            }
            flag == registers.ZFlag && !state && !registers.getZFlag() -> {
                val address = popWordFromStack()
                registers.PC = address
                return 20
            }
            flag == registers.CFlag && state && registers.getCFlag() -> {
                val address = popWordFromStack()
                registers.PC = address
                return 20
            }
            flag == registers.CFlag && !state && !registers.getCFlag() -> {
                val address = popWordFromStack()
                registers.PC = address
                return 20
            }
        }

        return 8
    }
}