package gameboy.cpu.instructions.miscellaneous

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

abstract class SWAP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun swap(value: Int): Int {
        return ((value shr 4) or (value shl 4))
    }
}