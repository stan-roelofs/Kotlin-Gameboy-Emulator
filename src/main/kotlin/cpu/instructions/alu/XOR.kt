package cpu.instructions.alu

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class XOR(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun xor(value: Int) {
        registers.A = registers.A xor value

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setCFlag(false)
        registers.setHFlag(false)
    }
}