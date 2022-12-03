package blargg

import org.junit.Ignore
import org.junit.Test

class CgbSoundTests : BlarggTestMemory() {

    override val path = "cgb_sound/rom_singles"

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
    fun testSweepDetails() {
        runBlarggTest("05-sweep details.gb")
    }

    @Test
    fun testOverflowOnTrigger() {
        runBlarggTest("06-overflow on trigger.gb")
    }

    @Test
    fun testWaveTriggerWhileOn() {
        runBlarggTest("10-wave trigger while on.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun testRegsAfterPower() {
        runBlarggTest("11-regs after power.gb")
    }
}