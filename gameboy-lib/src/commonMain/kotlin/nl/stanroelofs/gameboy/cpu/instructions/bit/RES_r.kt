package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.clearBit

class RES_r(registers: Registers, mmu: Mmu, private val register: Int, private val index: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> registers.A = registers.A.clearBit(index)
                    RegisterID.B.ordinal -> registers.B = registers.B.clearBit(index)
                    RegisterID.C.ordinal -> registers.C = registers.C.clearBit(index)
                    RegisterID.D.ordinal -> registers.D = registers.D.clearBit(index)
                    RegisterID.E.ordinal -> registers.E = registers.E.clearBit(index)
                    RegisterID.H.ordinal -> registers.H = registers.H.clearBit(index)
                    RegisterID.L.ordinal -> registers.L = registers.L.clearBit(index)
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}