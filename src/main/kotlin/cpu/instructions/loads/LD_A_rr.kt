package cpu.instructions.loads

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

class LD_A_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val address = when(register) {
            RegisterID.BC.ordinal -> registers.getBC()
            RegisterID.DE.ordinal -> registers.getDE()
            RegisterID.HL.ordinal -> registers.getHL()
            else -> throw Exception("Invalid register: $register")
        }

        registers.A = mmu.readByte(address)

        return 8
    }
}