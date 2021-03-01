package nl.stanroelofs.gameboy

import nl.stanroelofs.gameboy.cpu.*
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.memory.MmuCGB
import nl.stanroelofs.gameboy.memory.MmuDMG
import nl.stanroelofs.gameboy.memory.cartridge.Cartridge

/**
 * Main Gameboy class <BR>
 *
 * Implements the Runnable interface such that it can be ran in a thread
 */
abstract class GameBoy {

    companion object {
        /** The number of ticks per second the CPU is supposed to execute */
        const val TICKS_PER_SEC = 4194304

        /** The number of horizontal pixels of the Gameboy's screen */
        const val SCREEN_WIDTH = 160

        /** The number of vertical pixels of the Gameboy's screen */
        const val SCREEN_HEIGHT = 144

        /** The width of the screen buffer */
        const val SCREENBUFFER_WIDTH = 256

        /** The height of the screen buffer */
        const val SCREENBUFFER_HEIGHT = 256
    }

    /** The Gameboy's MMU instance */
    abstract val mmu: Mmu

    /** The Gameboy's CPU instance */
    abstract val cpu: Cpu

    /** Indicates whether the gameboy is a GBC */
    abstract val isGbc: Boolean

    /** Resets all registers and memory addresses */
    fun reset() {
        mmu.reset()
        cpu.reset()
    }

    /** Performs a single cpu step */
    open fun step() {
        cpu.step()
        mmu.tick(1)
    }
}

class GameBoyCGB(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuCGB(cartridge)
    override val cpu = CpuCGB(mmu, RegistersCGB())
    override val isGbc = true

    init {
        reset()
    }

    override fun step() {
        if (!mmu.io.hdma.inProgress())
            cpu.step()

        mmu.tick(if (cpu.doubleSpeed) 2 else 1)
    }
}

class GameBoyDMG(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuDMG(cartridge)
    override val cpu = CpuDMG(mmu, RegistersDMG())
    override val isGbc = false

    init {
        reset()
    }
}