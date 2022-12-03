package nl.stanroelofs.gameboy.cpu

import nl.stanroelofs.gameboy.cpu.instructions.Instruction

interface InstructionsPool {
    fun getInstruction(opcode : Int): Instruction
}