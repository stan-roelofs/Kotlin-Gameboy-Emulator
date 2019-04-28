package cpu.instructions.alu

import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class INC_rr(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    private var value = 0

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = when(register) {
                    RegisterID.BC.ordinal -> registers.getBC() + 1
                    RegisterID.DE.ordinal -> registers.getDE() + 1
                    RegisterID.HL.ordinal -> registers.getHL() + 1
                    RegisterID.SP.ordinal -> registers.SP
                    else -> throw Exception("Invalid register: $register")
                }
            }
            4 -> {
                when(register) {
                    RegisterID.BC.ordinal -> registers.setBC(value)
                    RegisterID.DE.ordinal -> registers.setDE(value)
                    RegisterID.HL.ordinal -> registers.setHL(value)
                    RegisterID.SP.ordinal -> registers.SP = value
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}