package gameboy.cpu.instructions.loads

import gameboy.cpu.RegisterID
import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.setSecondByte

class LD_rr_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
            }
            8 -> {
                value = setSecondByte(value, getImmediate())
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