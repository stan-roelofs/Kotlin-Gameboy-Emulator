package cpu.instructions.loads

import Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction

class LD_r_HL(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        when(register) {
            RegisterID.A.ordinal -> registers.A = value
            RegisterID.B.ordinal -> registers.B = value
            RegisterID.C.ordinal -> registers.C = value
            RegisterID.D.ordinal -> registers.D = value
            RegisterID.E.ordinal -> registers.E = value
            RegisterID.H.ordinal -> registers.H = value
            RegisterID.L.ordinal -> registers.L = value
            RegisterID.SP.ordinal -> registers.SP = value
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}