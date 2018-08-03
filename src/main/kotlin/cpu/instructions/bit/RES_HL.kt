package cpu.instructions.bit

import Mmu
import clearBit
import cpu.Registers
import cpu.instructions.Instruction

class RES_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {
        var value = mmu.readByte(registers.getHL())
        value = clearBit(value, index)
        mmu.writeByte(registers.getHL(), value)

        return 16
    }
}