package cpu.instructions.rotates

import Mmu
import cpu.RegisterID
import cpu.Registers

class RR_r(registers: Registers, mmu: Mmu, private val register: Int) : RR(registers, mmu) {

    override fun execute(): Int {
        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = rr(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = rr(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = rr(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = rr(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = rr(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = rr(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = rr(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}