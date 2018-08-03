package cpu.instructions.loads

import Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction

class LD_rr_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = getWordImmediate()

        when(register) {
            RegisterID.BC.ordinal -> registers.setBC(value)
            RegisterID.DE.ordinal -> registers.setDE(value)
            RegisterID.HL.ordinal -> registers.setHL(value)
            RegisterID.SP.ordinal -> registers.SP = value
            else -> throw Exception("Invalid register: " + register)
        }

        return 12
    }
}