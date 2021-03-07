package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.setBit

class SET_r(registers: Registers, mmu: Mmu, private val register: Int, private val index: Int) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                when(register) {
                    RegisterID.A.ordinal -> registers.A = registers.A.setBit(index)
                    RegisterID.B.ordinal -> registers.B = registers.B.setBit(index)
                    RegisterID.C.ordinal -> registers.C = registers.C.setBit(index)
                    RegisterID.D.ordinal -> registers.D = registers.D.setBit(index)
                    RegisterID.E.ordinal -> registers.E = registers.E.setBit(index)
                    RegisterID.H.ordinal -> registers.H = registers.H.setBit(index)
                    RegisterID.L.ordinal -> registers.L = registers.L.setBit(index)
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}