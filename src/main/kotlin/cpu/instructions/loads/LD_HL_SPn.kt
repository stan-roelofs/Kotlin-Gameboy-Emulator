package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_HL_SPn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate().toByte().toInt()
        registers.setHL(registers.SP + value)

        registers.setZFlag(false)
        registers.setNFlag(false)

        val cFlag = ((registers.SP and 0xFF) + (value and 0xFF)) > 0xFF
        registers.setCFlag(cFlag)

        val hFlag = (registers.SP and 0xF) + (value and 0xF) > 0xF
        registers.setHFlag(hFlag)

        return 12
    }
}