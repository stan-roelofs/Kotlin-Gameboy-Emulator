package cpu.instructions.shifts

import memory.Mmu
import cpu.RegisterID
import cpu.Registers

class SLA_r(registers: Registers, mmu: Mmu, private val register: Int) : SLA(registers, mmu) {
    override fun execute(): Int {

        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = sla(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = sla(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = sla(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = sla(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = sla(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = sla(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = sla(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}