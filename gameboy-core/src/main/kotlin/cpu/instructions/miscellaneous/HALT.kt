package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class HALT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
       // val IF = mmu.readByte(0xFF0F)
      //  val IE = mmu.readByte(0xFFFF)

        //if (IF and IE != 0) {
        //    registers.haltBug = true
          //  return 4
       // }

        when(currentCycle) {
            0 -> {
                registers.halt = true
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}