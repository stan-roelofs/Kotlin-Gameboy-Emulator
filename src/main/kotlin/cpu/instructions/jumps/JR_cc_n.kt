package cpu.instructions.jumps

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class JR_cc_n(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate().toByte().toInt()

        when {
            flag == registers.ZFlag && state && registers.getZFlag() -> {
                registers.PC += value
                return 12
            }
            flag == registers.ZFlag && !state && !registers.getZFlag() -> {
                registers.PC += value
                return 12
            }
            flag == registers.CFlag && state && registers.getCFlag() -> {
                registers.PC += value
                return 12
            }
            flag == registers.CFlag && !state && !registers.getCFlag() -> {
                registers.PC += value
                return 12
            }
        }

        return 8
    }
}