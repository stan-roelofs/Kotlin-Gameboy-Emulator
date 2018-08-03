package cpu.instructions.loads

import Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction

class LD_rr_r(registers: Registers, mmu: Mmu, private val register1: Int, private val register2: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = when(register2) {
            RegisterID.A.ordinal -> registers.A
            RegisterID.B.ordinal -> registers.B
            RegisterID.C.ordinal -> registers.C
            RegisterID.D.ordinal -> registers.D
            RegisterID.E.ordinal -> registers.E
            RegisterID.H.ordinal -> registers.H
            RegisterID.L.ordinal -> registers.L
            else -> throw Exception("Invalid register: " + register2)
        }

        when(register1) {
            RegisterID.BC.ordinal -> mmu.writeByte(registers.getBC(), value)
            RegisterID.DE.ordinal -> mmu.writeByte(registers.getDE(), value)
            RegisterID.HL.ordinal -> mmu.writeByte(registers.getHL(), value)
            else -> throw Exception("Invalid register: " + register1)
        }

        return 8
    }
}