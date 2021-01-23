package gameboy

import gameboy.cpu.*
import gameboy.memory.Mmu
import gameboy.memory.MmuCGB
import gameboy.memory.MmuDMG
import gameboy.memory.cartridge.Cartridge
import gameboy.memory.io.Hdma

/**
 * Main Gameboy class <BR>
 *
 * Implements the Runnable interface such that it can be ran in a thread
 */
abstract class GameBoy : Runnable {

    companion object {
        /** The number of ticks per second the CPU is supposed to execute */
        const val TICKS_PER_SEC = 4194304

        /** The number of horizontal pixels of the Gameboy's screen */
        const val SCREEN_WIDTH = 160

        /** The number of vertical pixels of the Gameboy's screen */
        const val SCREEN_HEIGHT = 144
    }

    /** The Gameboy's MMU instance */
    abstract val mmu: Mmu

    /** The Gameboy's CPU instance */
    abstract val cpu: Cpu

    /** Indicates whether the gameboy is currently running or not */
    var running = false
        private set

    /** Indicates whether the gameboy is paused or not */
    var paused = false

    /** Resets all registers and memory addresses */
    fun reset() {
        mmu.reset()
        cpu.reset()
    }

    /** Performs a single cpu step */
    open fun step() {
        cpu.step()
        mmu.tick(2)
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

class GameBoyCGB(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuCGB(cartridge)
    override val cpu = CpuCGB(mmu, RegistersCGB())

    init {
        reset()
    }

    override fun step() {
        if (mmu.io.hdma.state != Hdma.State.TRANSFER)
            cpu.step()

        mmu.tick(if (cpu.doubleSpeed) 1 else 2)
    }
}

class GameBoyDMG(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuDMG(cartridge)
    override val cpu = CpuDMG(mmu, RegistersDMG())

    init {
        reset()
    }
}