package cpu.instructions.bit

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction
import setBit

class SET_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {
        var value = mmu.readByte(registers.getHL())
        value = setBit(value, index)
        mmu.writeByte(registers.getHL(), value)

        return 16
    }
}