package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit

class BIT_r(registers: Registers, mmu: Mmu, private val register: Int, private val index: Int) : BIT(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                state = when(register) {
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
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}