package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.RegisterID
import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LD_r_n(registers: Registers, mmu: Mmu, private val register: Int) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 8

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when (currentCycle) {
            0 -> {
            }
            4 -> {
                value = getImmediate()

                when(register) {
                    RegisterID.A.ordinal -> registers.A = value
                    RegisterID.B.ordinal -> registers.B = value
                    RegisterID.C.ordinal -> registers.C = value
                    RegisterID.D.ordinal -> registers.D = value
                    RegisterID.E.ordinal -> registers.E = value
                    RegisterID.H.ordinal -> registers.H = value
                    RegisterID.L.ordinal -> registers.L = value
                    else -> throw Exception("Invalid register: $register")
                }
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}