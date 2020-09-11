package gameboy

import gameboy.cpu.Cpu
import gameboy.memory.Mmu
import gameboy.memory.cartridge.Cartridge

/**
 * Main Gameboy class
 *
 * Implements the Runnable interface such that it can be ran in a thread
 */
class GameBoy(cartridge: Cartridge) : Runnable {

    companion object {
        /** The number of ticks per second the CPU is supposed to execute */
        const val TICKS_PER_SEC = 4194304 / 4
    }

    /** The Gameboy's MMU instance */
    val mmu = Mmu(cartridge)

    /** The Gameboy's CPU instance */
    val cpu = Cpu(mmu)

    /** Indicates whether the gameboy is currently running or not */
    var running = false
        private set

    /** Indicates whether the gameboy is paused or not */
    var paused = false

    init {
        reset()
    }

    /** Resets all registers and memory addresses */
    fun reset() {
        mmu.reset()
        cpu.reset()
    }

    /** Performs a single cpu step */
    fun step() {
        cpu.step()
    }

    /** Toggle pause on / off */
    fun togglePause() {
        paused = !paused
    }

    /** Stop running */
    fun stop() {
        running = false
    }

    override fun run() {
        running = true
        while (running) {
            if (!paused) {
                step()
            }
        }
    }
}