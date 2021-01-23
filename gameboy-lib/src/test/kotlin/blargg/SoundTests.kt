package blargg

import org.junit.Test

class SoundTests : BlarggTestMemory() {

    override val path = "dmg_sound/rom_singles"

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

    @Test
    fun testRegsAfterPower() {
        runBlarggTest("11-regs after power.gb")
    }
}