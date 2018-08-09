package cpu.instructions.rotates

import memory.Mmu
import cpu.RegisterID
import cpu.Registers

class RRC_r(registers: Registers, mmu: Mmu, private val register: Int) : RRC(registers, mmu) {

    override fun execute(): Int {
        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = rrc(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = rrc(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = rrc(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = rrc(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = rrc(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = rrc(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = rrc(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}