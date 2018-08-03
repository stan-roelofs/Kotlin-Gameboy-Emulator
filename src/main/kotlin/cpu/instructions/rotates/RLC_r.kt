package cpu.instructions.rotates

import Mmu
import cpu.RegisterID
import cpu.Registers

class RLC_r(registers: Registers, mmu: Mmu, private val register: Int) : RLC(registers, mmu) {

    override fun execute(): Int {
        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = rlc(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = rlc(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = rlc(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = rlc(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = rlc(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = rlc(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = rlc(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}