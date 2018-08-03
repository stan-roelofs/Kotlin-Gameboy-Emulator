package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_HL_SPn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        var value = getImmediate().toByte().toInt()

        value += registers.SP

        registers.setHL(value)

        registers.setZFlag(false)
        registers.setNFlag(false)

        val cFlag = value > 0xFFFF
        registers.setCFlag(cFlag)

        val hFlag = value > 0xF
        registers.setHFlag(hFlag)

        return 12
    }
}