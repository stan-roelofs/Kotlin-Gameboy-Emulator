package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LD_HL_SPn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()
            }
            8 -> {
                registers.setHL(registers.SP + value)

                registers.setZFlag(false)
                registers.setNFlag(false)

                val cFlag = ((registers.SP and 0xFF) + (value and 0xFF)) > 0xFF
                registers.setCFlag(cFlag)

                val hFlag = (registers.SP and 0xF) + (value and 0xF) > 0xF
                registers.setHFlag(hFlag)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}