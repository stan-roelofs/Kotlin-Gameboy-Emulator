package cpu.instructions.bit

import Mmu
import cpu.RegisterID
import cpu.Registers
import getBit

class BIT_r(registers: Registers, mmu: Mmu, private val register: Int, private val index: Int) : BIT(registers, mmu) {

    override fun execute(): Int {

        val state = when(register) {
            RegisterID.A.ordinal -> registers.A.getBit(index)
            RegisterID.B.ordinal -> registers.B.getBit(index)
            RegisterID.C.ordinal -> registers.C.getBit(index)
            RegisterID.D.ordinal -> registers.D.getBit(index)
            RegisterID.E.ordinal -> registers.E.getBit(index)
            RegisterID.H.ordinal -> registers.H.getBit(index)
            RegisterID.L.ordinal -> registers.L.getBit(index)
            else -> throw Exception("Invalid register: $register")
        }

        super.bit(state)

        return 8
    }
}