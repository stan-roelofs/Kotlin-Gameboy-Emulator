package cpu.instructions.bit

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.clearBit

class RES_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {
        var value = mmu.readByte(registers.getHL())
        value = clearBit(value, index)
        mmu.writeByte(registers.getHL(), value)

        return 16
    }
}