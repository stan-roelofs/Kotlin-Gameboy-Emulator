package cpu.instructions.miscellaneous

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class SWAP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun swap(value: Int): Int {
        return ((value shr 4) or (value shl 4))
    }
}