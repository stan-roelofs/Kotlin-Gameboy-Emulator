package cpu.instructions.shifts

import memory.Mmu
import cpu.RegisterID
import cpu.Registers

class SRL_r(registers: Registers, mmu: Mmu, private val register: Int) : SRL(registers, mmu) {
    override fun execute(): Int {

        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = srl(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = srl(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = srl(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = srl(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = srl(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = srl(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = srl(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}