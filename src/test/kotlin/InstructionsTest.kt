import cpu.Cpu
import memory.Mmu
import org.junit.Assert.assertEquals
import org.junit.Test

internal class InstructionsTest {

    @Test
    fun testPOPAF() {
        val cpu = Cpu()
        val mmu = Mmu.instance

        cpu.registers.PC = 0x8000
        mmu.writeByte(0x8000, 0x01) // LD BC, nn
        mmu.writeByte(0x8001, 0x00)
        mmu.writeByte(0x8002, 0x12)

        cpu.step()
        assertEquals(0x8003, cpu.registers.PC)
        assertEquals(0x1200, cpu.registers.getBC())

        mmu.writeByte(0x8003, 0xC5) // PUSH BC

        cpu.registers.SP = 0xFF82
        cpu.step()
        assertEquals(0x8004, cpu.registers.PC)
        assertEquals(0xFF80, cpu.registers.SP)
        assertEquals(0x12, mmu.readByte(0xFF81))
        assertEquals(0x00, mmu.readByte(0xFF80))

        mmu.writeByte(0x8004, 0xF1) // POP AF

        cpu.step()
        assertEquals(0x8005, cpu.registers.PC)
        assertEquals(0xFF82, cpu.registers.SP)
        assertEquals(0x1200, cpu.registers.getAF())

        mmu.writeByte(0x8005, 0xF5) // PUSH AF

        cpu.step()
        assertEquals(0x8006, cpu.registers.PC)
        assertEquals(0xFF80, cpu.registers.SP)
        assertEquals(0x12, mmu.readByte(0xFF81))
        assertEquals(0x00, mmu.readByte(0xFF80))

        mmu.writeByte(0x8006, 0xD1) // POP DE

        cpu.step()
        assertEquals(0x8007, cpu.registers.PC)
        assertEquals(0xFF82, cpu.registers.SP)
        assertEquals(0x1200, cpu.registers.getDE())

        mmu.writeByte(0x8007, 0x79) // LD A, C

        cpu.step()
        assertEquals(0x8008, cpu.registers.PC)
        assertEquals(0x00, cpu.registers.A)

        mmu.writeByte(0x8008, 0xE6) // AND A, n
        mmu.writeByte(0x8009, 0xF0) // n = F0

        cpu.step()
        assertEquals(0x800A, cpu.registers.PC)
        assertEquals(0x00, cpu.registers.A)
        assertEquals(true, cpu.registers.getZFlag())
        assertEquals(false, cpu.registers.getNFlag())
        assertEquals(true, cpu.registers.getHFlag())
        assertEquals(false, cpu.registers.getCFlag())

        mmu.writeByte(0x800A, 0xBB) // CP A, E

        cpu.step()
        assertEquals(0x800B, cpu.registers.PC)
        assertEquals(true, cpu.registers.getZFlag())
        assertEquals(true, cpu.registers.getNFlag())

        mmu.writeByte(0x800B, 0xC2) // JP nz, 0x9100
        mmu.writeByte(0x800C, 0x00)
        mmu.writeByte(0x800D, 0x91)

        cpu.step()
        assertEquals(0x800E, cpu.registers.PC)
    }

    @Test
    fun testIE() {
        val cpu = Cpu()
        val mmu = Mmu.instance

        cpu.registers.PC = 0x8000
        cpu.registers.A = 0x04
        mmu.writeByte(0x8000, 0xE0) // LDH (n), A
        mmu.writeByte(0x8001, 0xFF) // LDH 0xFFFF, A
        cpu.step()

        assertEquals(0x04, mmu.readByte(0xFFFF))

        mmu.writeByte(0x8002, 0xFB) // EI
        cpu.step()

        assertEquals(false, cpu.registers.IME)


        mmu.writeByte(0x8003, 0x01) // LD BC, nn
        mmu.writeByte(0x8004, 0x00) // nn = 0x00
        mmu.writeByte(0x8005, 0x00)
        cpu.step()
        assertEquals(true, cpu.registers.IME)
        assertEquals(0, cpu.registers.getBC())


        mmu.writeByte(0x8006, 0xC5) // PUSH BC
        cpu.step()
        mmu.writeByte(0x8007, 0xC1) // POP BC
        cpu.step()
        mmu.writeByte(0x8008, 0x04) // INC B
        cpu.step()

        assertEquals(1, cpu.registers.B)

        cpu.registers.A = 0x04
        mmu.writeByte(0x8009, 0xE0) // LDH (n), A
        mmu.writeByte(0x800A, 0x0F) // LDH 0xFFFF, A
        cpu.step()

        assertEquals(0x04, mmu.readByte(0xFF0F))

        mmu.writeByte(0x800B, 0x05) // DEC B
        cpu.step()

        assertEquals(0, cpu.registers.B)
        assertEquals(true, cpu.registers.getZFlag())

        mmu.writeByte(0x800C, 0xC2) // JP nz, nn
        mmu.writeByte(0x800D, 0x23) // nn = 1923
        mmu.writeByte(0x800E, 0x19)
        cpu.step()

        assertEquals(0x800F, cpu.registers.PC)

    }

    @Test
    fun testTimer() {
        val cpu = Cpu()
        val mmu = Mmu.instance

        mmu.writeByte(0xFF07, 0x05)
        mmu.writeByte(0xFF05, 0x00)
        mmu.writeByte(0xFF0F, 0x00)

        // every 16 cycles tima should increase
        // a nop takes 4 cycles

        for (i in 0..3) {
            cpu.registers.PC = 0x8000
            mmu.writeByte(0x8000, 0x00) // NOP
            cpu.step()
        }

        assertEquals(1, mmu.readByte(0xFF05))
    }

    @Test
    fun testHalt() {
        val cpu = Cpu()
        val mmu = Mmu.instance

        mmu.writeByte(0xFFFF, 0x04)
        mmu.writeByte(0xFF07, 0x05)
        mmu.writeByte(0xFF05, 0x00)
        mmu.writeByte(0xFF0F, 0x00)

        // overflow timer, should set IF of timer interrupt
        for (j in 0..255) {
            for (i in 0..3) {
                cpu.registers.PC = 0x8000
                mmu.writeByte(0x8000, 0x00) // NOP
                cpu.step()
            }
        }
        assertEquals(true, mmu.readByte(0xFF0F).getBit(2)) //IF of timer set
        assertEquals(true, mmu.readByte(0xFFFF).getBit(2)) //IE of timer set

        // should jump to 0x48 to handle timer interrupt
        cpu.step()

        assertEquals(0x50, cpu.registers.PC)

    }
}