package cpu.instructions.alu

import cpu.RegisterID
import cpu.Registers
import memory.Mmu
import utils.Log

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
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}