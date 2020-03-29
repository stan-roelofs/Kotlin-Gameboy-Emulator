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
}