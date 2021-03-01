package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class INC_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.BC.ordinal -> registers.setBC(registers.getBC() + 1)
                    RegisterID.DE.ordinal -> registers.setDE(registers.getDE() + 1)
                    RegisterID.HL.ordinal -> registers.setHL(registers.getHL() + 1)
                    RegisterID.SP.ordinal -> registers.incSP()
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}