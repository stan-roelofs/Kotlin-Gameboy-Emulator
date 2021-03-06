package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu

class ADD_HL_rr(registers: Registers, mmu: Mmu, private val register: Int) : ADD(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = when(register) {
                    RegisterID.BC.ordinal -> registers.getBC()
                    RegisterID.DE.ordinal -> registers.getDE()
                    RegisterID.HL.ordinal -> registers.getHL()
                    RegisterID.SP.ordinal -> registers.SP
                    else -> throw Exception("Invalid register: $register")
                }
                super.add16HL(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}