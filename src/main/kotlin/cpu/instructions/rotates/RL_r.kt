package cpu.instructions.rotates

import memory.Mmu
import cpu.RegisterID
import cpu.Registers

class RL_r(registers: Registers, mmu: Mmu, private val register: Int) : RL(registers, mmu) {

    override fun execute(): Int {
        val value: Int
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = rl(value)
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = rl(value)
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = rl(value)
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = rl(value)
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = rl(value)
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = rl(value)
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = rl(value)
            }
            else -> throw Exception("Invalid register: " + register)
        }

        return 8
    }
}