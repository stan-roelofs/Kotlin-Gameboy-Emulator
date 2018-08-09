package cpu.instructions.miscellaneous

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class HALT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
       // val IF = mmu.readByte(0xFF0F)
      //  val IE = mmu.readByte(0xFFFF)

        //if (IF and IE != 0) {
        //    registers.haltBug = true
          //  return 4
       // }

        registers.halt = true
        return 4
    }
}