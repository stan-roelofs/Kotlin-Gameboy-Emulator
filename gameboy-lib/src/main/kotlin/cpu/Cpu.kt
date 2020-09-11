package gameboy.cpu

import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.clearBit
import gameboy.utils.getBit
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte

/**
 * Represents the Gameboy CPU
 *
 * On initialization [reset] is called.
 */
class Cpu(private val mmu : Mmu) {
    /** Cpu registers */
    val registers = Registers()

    private var instructionsPool: InstructionsPool = InstructionsPoolImpl(registers, mmu)
    private var currentInstruction: Instruction? = null

    init {
        reset()
    }

    /**
     * Resets the cpu to the initial state
     */
    fun reset() {
        registers.reset()
        currentInstruction = null
    }

    internal fun step() {
        if (currentInstruction != null && currentInstruction!!.isExecuting()) {
            currentInstruction!!.tick()
            increaseClock(4)
        } else {
            // Handle interrupts
            processInterrupts()

            // Read next instruction
            if (!registers.halt) {
                val opcode = mmu.readByte(registers.PC)

                if (!registers.haltBug) {
                    registers.incPC()
                } else {
                    registers.haltBug = false
                }

                currentInstruction = instructionsPool.getInstruction(opcode)
                currentInstruction!!.tick()
                increaseClock(4)

                // If EI was executed, return such that interrupts are only enabled after the next instruction
                if (opcode == 0xFB) {
                    return
                }

                if (registers.eiExecuted) {
                    registers.IME = true
                    registers.eiExecuted = false
                }
            } else {
                increaseClock(4)
            }
        }
    }

    private fun processInterrupts() {
        // Interrupt handling
        var IF = mmu.readByte(Mmu.IF)
        val IE = mmu.readByte(Mmu.IE)

        var interruptTriggered = false
        for (i in 0..4) {
            if (IE.getBit(i) && IF.getBit(i)) {
                interruptTriggered = true
            }
        }

        // Interrupt Service Routine
        if (interruptTriggered) {
            if (registers.halt) {
                registers.halt = false
                increaseClock(4)
            }

            if (registers.IME) {
                registers.IME = false

                // Execute two nops
                increaseClock(4)
                increaseClock(4)

                // Push high byte of current PC onto stack
                registers.decSP()
                mmu.writeByte(registers.SP, registers.PC.getSecondByte())
                increaseClock(4)

                // It is possible the SP was at the IE registers, which might mean the interrupt was cancelled by writing the PC to the stack
                val newIE = mmu.readByte(Mmu.IE)

                var interruptBit = -1
                for (i in 0..4) {
                    if (newIE.getBit(i) && IF.getBit(i)) {
                        interruptBit = i
                        break
                    }
                }

                if (interruptBit >= 0) {
                    registers.decSP()
                    mmu.writeByte(registers.SP, registers.PC.getFirstByte())
                    increaseClock(4)

                    // Clear interrupt flag
                    IF = clearBit(IF, interruptBit)
                    mmu.writeByte(Mmu.IF, IF)

                    // Calculate interrupt handler address
                    val address = 0x40 + (interruptBit * 8)

                    // Set PC to address of handler
                    registers.PC = address
                } else {
                    // Cancellation apparently sets pc to 0x0000 according to mooneye gb tests
                    registers.PC = 0x0000
                }

                increaseClock(4)
            }
        }
    }

    private fun increaseClock(steps: Int) {
        if (steps < 0) {
            throw IllegalArgumentException("Cannot increase clock by negative value")
        }
        mmu.tick(steps)
    }
}

