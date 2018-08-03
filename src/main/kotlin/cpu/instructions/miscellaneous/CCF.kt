package cpu.instructions.miscellaneous

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class CCF(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {

        if(registers.getCFlag()) {
            registers.setCFlag(false)
        } else {
            registers.setCFlag(true)
        }

        registers.setNFlag(false)
        registers.setHFlag(false)

        return 4
    }
}