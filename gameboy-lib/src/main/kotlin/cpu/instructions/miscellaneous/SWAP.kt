package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

abstract class SWAP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun swap(value: Int): Int {
        return ((value shr 4) or (value shl 4))
    }
}