package gameboy.cpu

import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.memory.MmuCGB
import gameboy.memory.MmuDMG
import gameboy.memory.Register
import gameboy.utils.*

/**
 * Represents the Gameboy CPU <BR>
 *
 * On initialization [reset] is called.
 */
abstract class Cpu(protected val mmu : Mmu, val registers : Registers) {

    private var instructionsPool: InstructionsPool = InstructionsPoolImpl(registers, mmu)
    private var currentInstruction: Instruction? = null
    private var interruptBit = 0
    protected var count = 0
    protected var state = State.EXECUTE
    var opcode = 0
        private set

    enum class State {
        EXECUTE,
        INTERRUPT_WAIT,
        INTERRUPT_PUSH_PC_HIGH,
        INTERRUPT_PUSH_PC_LOW,
        INTERRUPT_SET_PC,
        SPEED_SWITCH
    }

    /**
     * Resets the cpu to the initial state
     */
    open fun reset() {
        registers.reset()
        count = 0
        state = State.EXECUTE
        interruptBit = 0
        opcode = 0
    }

    internal open fun step() {
        count++
        if (count == 4) {
            count = 0
        } else {
            return
        }

        handleStep()
    }

    protected open fun handleStep() {
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

                    checkSpeedMode()

                    // Read next instruction
                    if (!registers.halt && !registers.stop) {
                        opcode = mmu.readByte(registers.PC)

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
            else -> throw IllegalStateException("Invalid state: $state")
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
            registers.halt = false

            if (registers.stop) {
                registers.stop = false
            }

            if (registers.IME) {
                registers.IME = false
                return true
            }
        }
        return false
    }

    protected abstract fun checkSpeedMode()
}

class CpuDMG(mmu: MmuDMG, registers: RegistersDMG) : Cpu(mmu, registers) {

    init {
        reset()
    }

    override fun checkSpeedMode() {}
}

class CpuCGB(mmu: MmuCGB, registers: RegistersCGB) : Cpu(mmu, registers) {

    var doubleSpeed = false
        private set
    var key1 = Register(Mmu.KEY1)
    private var speedSwitchCounter = 0

    // TODO: Based on https://gbdev.io/pandocs/ but TCAGBD mentions (128 × 1024 - 76) clocks, which one is correct?
    private val speedSwitchCycles = 8200

    init {
        mmu.key1 = key1
        reset()
    }

    override fun step() {
        count++
        val step = if (doubleSpeed) 2 else 4
        if (count >= step) {
            count = 0
        } else {
            return
        }

        handleStep()
    }

    override fun reset() {
        super.reset()

        key1.value = 0
        doubleSpeed = false
        speedSwitchCounter = 0
    }

    override fun handleStep() {
        when(state) {
            State.EXECUTE,
            State.INTERRUPT_SET_PC,
            State.INTERRUPT_PUSH_PC_HIGH,
            State.INTERRUPT_PUSH_PC_LOW,
            State.INTERRUPT_WAIT -> super.handleStep()

            State.SPEED_SWITCH -> {
                speedSwitchCounter += 4
                if (speedSwitchCounter == speedSwitchCycles) {
                    doubleSpeed = !doubleSpeed
                    key1.value = setBit(0, 7, doubleSpeed)
                    state = State.EXECUTE
                    registers.stop = false
                }
            }
        }
    }

    override fun checkSpeedMode() {
        // Bit 0 indicates a speed switch needs to be performed
        if (registers.stop && key1.getBit(0)) {
            state = State.SPEED_SWITCH
            speedSwitchCounter = 0
        }
    }
}