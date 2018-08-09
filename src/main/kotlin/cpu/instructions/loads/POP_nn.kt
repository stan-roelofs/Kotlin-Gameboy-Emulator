package cpu.instructions.loads

import memory.Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction

class POP_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = popWordFromStack()

        when (register) {
            RegisterID.AF.ordinal -> registers.setAF(value)
            RegisterID.BC.ordinal -> registers.setBC(value)
            RegisterID.DE.ordinal -> registers.setDE(value)
            RegisterID.HL.ordinal -> registers.setHL(value)
            else -> throw Exception("Invalid register: " + register)
        }

        return 12
    }
}