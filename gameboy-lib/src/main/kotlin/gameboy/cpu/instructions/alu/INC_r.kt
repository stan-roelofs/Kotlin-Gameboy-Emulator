package gameboy.cpu.instructions.alu

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.memory.Mmu

class INC_r(registers: Registers, mmu: Mmu, private val register: Int) : INC(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {

        when(currentCycle) {
            0 -> {
                val zFlag: Boolean
                when(register) {
                    RegisterID.A.ordinal -> {
                        value = registers.A
                        registers.A += 1
                        zFlag = registers.A == 0
                    }
                    RegisterID.B.ordinal -> {
                        value = registers.B
                        registers.B += 1
                        zFlag = registers.B == 0
                    }
                    RegisterID.C.ordinal -> {
                        value = registers.C
                        registers.C += 1
                        zFlag = registers.C == 0
                    }
                    RegisterID.D.ordinal -> {
                        value = registers.D
                        registers.D += 1
                        zFlag = registers.D == 0
                    }
                    RegisterID.E.ordinal -> {
                        value = registers.E
                        registers.E += 1
                        zFlag = registers.E == 0
                    }
                    RegisterID.H.ordinal -> {
                        value = registers.H
                        registers.H += 1
                        zFlag = registers.H == 0
                    }
                    RegisterID.L.ordinal -> {
                        value = registers.L
                        registers.L += 1
                        zFlag = registers.L == 0
                    }
                    else -> throw Exception("Invalid register: $register")
                }

                registers.setZFlag(zFlag)
                super.inc(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}