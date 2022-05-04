package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LD_A_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val address = when(register) {
                    RegisterID.BC.ordinal -> registers.BC
                    RegisterID.DE.ordinal -> registers.DE
                    RegisterID.HL.ordinal -> registers.HL
                    else -> throw Exception("Invalid register: $register")
                }

                registers.A = mmu.readByte(address)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}