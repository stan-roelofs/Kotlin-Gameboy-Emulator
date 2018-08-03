package cpu.instructions.miscellaneous

import Mmu
import cpu.RegisterID
import cpu.Registers

class SWAP_r(registers: Registers, mmu: Mmu, private val register: Int) : SWAP(registers, mmu) {
    override fun execute(): Int {

        val value: Int
        val zFlag: Boolean
        when(register) {
            RegisterID.A.ordinal -> {
                value = registers.A
                registers.A = swap(value)
                zFlag = registers.A == 0
            }
            RegisterID.B.ordinal -> {
                value = registers.B
                registers.B = swap(value)
                zFlag = registers.B == 0
            }
            RegisterID.C.ordinal -> {
                value = registers.C
                registers.C = swap(value)
                zFlag = registers.C == 0
            }
            RegisterID.D.ordinal -> {
                value = registers.D
                registers.D = swap(value)
                zFlag = registers.D == 0
            }
            RegisterID.E.ordinal -> {
                value = registers.E
                registers.E = swap(value)
                zFlag = registers.E == 0
            }
            RegisterID.H.ordinal -> {
                value = registers.H
                registers.H = swap(value)
                zFlag = registers.H == 0
            }
            RegisterID.L.ordinal -> {
                value = registers.L
                registers.L = swap(value)
                zFlag = registers.L == 0
            }
            else -> throw Exception("Invalid register: " + register)
        }

        registers.setZFlag(zFlag)
        registers.setNFlag(false)
        registers.setHFlag(false)
        registers.setCFlag(false)

        return 8
    }
}