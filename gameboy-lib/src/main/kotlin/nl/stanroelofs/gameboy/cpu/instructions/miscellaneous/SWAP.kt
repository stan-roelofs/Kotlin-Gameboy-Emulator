package nl.stanroelofs.gameboy.cpu.instructions.miscellaneous

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

abstract class SWAP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun swap(value: Int): Int {
        return ((value shr 4) or (value shl 4))
    }
}