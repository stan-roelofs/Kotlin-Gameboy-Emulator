package gameboy.cpu

import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.clearBit
import gameboy.utils.getBit
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte

/**
 * Represents the Gameboy CPU <BR>
 *
 * On initialization [reset] is called.
 */
class Cpu(private val mmu : Mmu, private val registers : Registers) {

    private var instructionsPool: InstructionsPool = InstructionsPoolImpl(registers, mmu)
    private var currentInstruction: Instruction? = null

    private var interruptBit = 0
    private var count = 0
    private var state = State.EXECUTE

    enum class State {
        EXECUTE,
        INTERRUPT_WAIT,
        INTERRUPT_PUSH_PC_HIGH,
        INTERRUPT_PUSH_PC_LOW,
        INTERRUPT_SET_PC
    }

    init {
        reset()
    }

    /**
     * Resets the cpu to the initial state
     */
    fun reset() {
        registers.reset()
        count = 0
        state = State.EXECUTE
        interruptBit = 0
    }

    internal fun step() {
        if (++count == 4) {
            count = 0
        } else {
            return
        }

        when (state) {
            State.EXECUTE -> {
                if (currentInstruction != null && currentInstruction!!.isExecuting())
                    currentInstruction!!.tick()
                else {
                    // Check whether an interrupt was triggered
                    if (checkInterrupts()) {
                        state = State.INTERRUPT_WAIT
                        return
                    }

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
                        state = State.EXECUTE

                        if (opcode == 0xFB)
                            return
                    }
                }

                if (registers.eiExecuted) {
                    registers.IME = true
                    registers.eiExecuted = false
                }
            }
            State.INTERRUPT_WAIT -> {
                state = State.INTERRUPT_PUSH_PC_HIGH
            }
            State.INTERRUPT_PUSH_PC_HIGH -> {
                // Push high byte of current PC onto stack
                registers.decSP()
                mmu.writeByte(registers.SP, registers.PC.getSecondByte())

                state = State.INTERRUPT_PUSH_PC_LOW
            }
            State.INTERRUPT_PUSH_PC_LOW -> {
                // It is possible the SP was at the IE registers, which might mean the interrupt was cancelled by writing the PC to the stack
                val IE = mmu.readByte(Mmu.IE)
                val IF = mmu.readByte(Mmu.IF)

                interruptBit = -1
                for (i in 0..4) {
                    if (IE.getBit(i) && IF.getBit(i)) {
                        interruptBit = i
                        break
                    }
                }

                if (interruptBit >= 0) {
                    registers.decSP()
                    mmu.writeByte(registers.SP, registers.PC.getFirstByte())
                    state = State.INTERRUPT_SET_PC
                } else {
                    registers.PC = 0x0000
                    state = State.EXECUTE
                }
            }
            State.INTERRUPT_SET_PC -> {
                var IF = mmu.readByte(Mmu.IF)
                // Clear interrupt flag
                IF = clearBit(IF, interruptBit)
                mmu.writeByte(Mmu.IF, IF)

                // Calculate interrupt handler address
                val interruptAddress = 0x40 + (interruptBit * 8)

                registers.PC = interruptAddress

                // Set PC to address of handler
                state = State.EXECUTE
            }
        }
    }

    private fun checkInterrupts() : Boolean {
        // Interrupt handling
        val IF = mmu.readByte(Mmu.IF)
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
            }

            if (registers.IME) {
                registers.IME = false
                return true
            }
        }
        return false
    }
}

