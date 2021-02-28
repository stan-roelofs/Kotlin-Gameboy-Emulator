package gameboy.cpu.instructions.loads

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

class LD_A_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val address = when(register) {
                    RegisterID.BC.ordinal -> registers.getBC()
                    RegisterID.DE.ordinal -> registers.getDE()
                    RegisterID.HL.ordinal -> registers.getHL()
                    else -> throw Exception("Invalid register: $register")
                }

                registers.A = mmu.readByte(address)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}