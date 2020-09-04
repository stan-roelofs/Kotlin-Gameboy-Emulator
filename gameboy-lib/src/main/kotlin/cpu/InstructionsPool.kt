package cpu

import cpu.instructions.Instruction

interface InstructionsPool {
    fun getInstruction(opcode : Int): Instruction
}