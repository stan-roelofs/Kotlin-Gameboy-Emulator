package cpu.instructions.loads

import memory.Mmu
import cpu.RegisterID
import cpu.Registers
import cpu.instructions.Instruction
import setSecondByte

class PUSH_nn(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    override fun execute(): Int {

        var value: Int

        when (register) {
            RegisterID.AF.ordinal -> {
                value = registers.F
                value = setSecondByte(value, registers.A)
            }
            RegisterID.BC.ordinal -> {
                value = registers.C
                value = setSecondByte(value, registers.B)
            }
            RegisterID.DE.ordinal -> {
                value = registers.E
                value = setSecondByte(value, registers.D)
            }
            RegisterID.HL.ordinal -> {
                value = registers.L
                value = setSecondByte(value, registers.H)
            }
            else -> throw Exception("Invalid register: " + register)
        }
        pushWordToStack(value)

        return 16
    }
}