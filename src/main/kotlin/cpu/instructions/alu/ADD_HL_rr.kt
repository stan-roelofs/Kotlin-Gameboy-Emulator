package cpu.instructions.alu

import memory.Mmu
import cpu.RegisterID
import cpu.Registers

class ADD_HL_rr(registers: Registers, mmu: Mmu, private val register: Int) : ADD(registers, mmu) {

    override fun execute(): Int {

        val value = when(register) {
            RegisterID.BC.ordinal -> registers.getBC()
            RegisterID.DE.ordinal -> registers.getDE()
            RegisterID.HL.ordinal -> registers.getHL()
            RegisterID.SP.ordinal -> registers.SP
            else -> throw Exception("Invalid register: " + register)
        }

        super.add16HL(value)

        return 8
    }
}