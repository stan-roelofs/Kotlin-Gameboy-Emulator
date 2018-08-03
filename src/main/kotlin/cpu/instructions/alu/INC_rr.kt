package cpu.instructions.alu

import Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction

class INC_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        when(register) {
            RegisterID.BC.ordinal -> registers.setBC(registers.getBC() + 1)
            RegisterID.DE.ordinal -> registers.setDE(registers.getDE() + 1)
            RegisterID.HL.ordinal -> registers.setHL(registers.getHL() + 1)
            RegisterID.SP.ordinal -> registers.incSP()
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}