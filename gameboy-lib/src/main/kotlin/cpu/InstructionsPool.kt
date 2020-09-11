package gameboy.cpu

import gameboy.cpu.instructions.Instruction

interface InstructionsPool {
    fun getInstruction(opcode : Int): Instruction
}