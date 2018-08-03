package cpu.instructions.miscellaneous

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class DAA(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        if (!registers.getNFlag()) {
            if (registers.getCFlag() || registers.A > 0x99) {
                registers.A += 0x60
                registers.setCFlag(true)
            }
            if (registers.getHFlag() || (registers.A and 0x0F) > 0x09) {
                registers.A += 0x6
            }
        } else {
            if (registers.getCFlag()) {
                registers.A -= 0x60
            }
            if (registers.getHFlag()) {
                registers.A -= 0x6
            }
        }

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setHFlag(false)

        return 4
    }
}