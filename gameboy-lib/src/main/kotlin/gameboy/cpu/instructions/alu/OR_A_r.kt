package gameboy.cpu.instructions.alu

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.memory.Mmu

class OR_A_r(registers: Registers, mmu: Mmu, private val register: Int) : OR(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = when(register) {
                    RegisterID.A.ordinal -> registers.A
                    RegisterID.B.ordinal -> registers.B
                    RegisterID.C.ordinal -> registers.C
                    RegisterID.D.ordinal -> registers.D
                    RegisterID.E.ordinal -> registers.E
                    RegisterID.H.ordinal -> registers.H
                    RegisterID.L.ordinal -> registers.L
                    else -> throw Exception("Invalid register: $register")
                }

                super.or(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}