package cpu.instructions.alu

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

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
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}