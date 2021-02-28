package gameboy.cpu.instructions.alu

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

class DEC_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.BC.ordinal -> registers.setBC(registers.getBC() - 1)
                    RegisterID.DE.ordinal -> registers.setDE(registers.getDE() - 1)
                    RegisterID.HL.ordinal -> registers.setHL(registers.getHL() - 1)
                    RegisterID.SP.ordinal -> registers.decSP()
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}