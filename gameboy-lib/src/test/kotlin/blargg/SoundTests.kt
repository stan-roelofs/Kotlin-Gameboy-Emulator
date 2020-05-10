package blargg

import org.junit.Test

class SoundTests : BlarggTest() {

    override val path = "dmg_sound/rom_singles"
    override val method = ValidateMethod.MEMORY

    @Test
    fun testRegisters() {
        runBlarggTest("01-registers.gb")
    }

    @Test
    fun testLengthCounter() {
        runBlarggTest("02-len ctr.gb")
    }

    @Test
    fun testTrigger() {
        runBlarggTest("03-trigger.gb")
    }

    @Test
    fun testSweep() {
        runBlarggTest("04-sweep.gb")
    }



    @Test
    fun testRegsAfterPower() {
        runBlarggTest("11-regs after power.gb")
    }
}