package cpu.instructions.loads

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

class PUSH_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = when (register) {
            RegisterID.AF.ordinal -> {
                registers.getAF()
            }
            RegisterID.BC.ordinal -> {
                registers.getBC()
            }
            RegisterID.DE.ordinal -> {
                registers.getDE()
            }
            RegisterID.HL.ordinal -> {
                registers.getHL()
            }
            else -> throw Exception("Invalid register: $register")
        }

        pushWordToStack(value)

        return 16
    }
}