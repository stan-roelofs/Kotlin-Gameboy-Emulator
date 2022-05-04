package nl.stanroelofs.gameboy.memory.cartridge

import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.utils.toHexString

class MBC3(romBanks: Int, ramSize: Int, override val hasBattery: Boolean = false, val hasTimer: Boolean = false) : Memory, MBC {

    override val ram: Array<IntArray>?
    override val rom: Array<IntArray>

    override var currentRomBank = 0
    override var currentRamBank = 0
    override var ramEnabled = false

    private val clock = IntArray(5)
    private var registerMapped = false
    private var mappedRegister = 0

    private var latchReady = false

    var mode = 0

    init {
        reset()

        if (romBanks !in 0..128) {
            throw IllegalArgumentException("Illegal number of ROM banks: $romBanks")
        }
        rom = Array(romBanks) {IntArray(0x4000)}
        ram = when (ramSize) {
            0       -> null
            0x800   -> Array(1) {IntArray(0x800)}
            0x2000  -> Array(1) {IntArray(0x2000)}
            0x8000  -> Array(4) {IntArray(0x2000)}
            0x20000 -> Array(16) {IntArray(0x2000)}
            else -> throw IllegalArgumentException("Illegal RAM size: $ramSize")
        }
    }

    override fun reset() {
        super.reset()
        currentRamBank = 0
        currentRomBank = 1
        mode = 0
        ramEnabled = false
        mappedRegister = 0
        latchReady = false
    }

    override fun loadRom(value: ByteArray) {
        for (i in value.indices) {
            val bank: Int = i / 0x4000
            val index: Int = i - (bank * 0x4000)

            rom[bank][index] = (value[i].toInt()) and 0xFF
        }
    }

    override fun readRom(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x4000 -> {
                this.rom[0][address]
            }
            in 0x4000 until 0x8000 -> {
                val romBank = currentRomBank % rom.size
                this.rom[romBank][address - 0x4000]
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }
    }

    override fun writeRom(address: Int, value: Int) {
        when(address) {
            in 0x0000 until 0x2000 -> ramEnabled = (value and 0x0F) == 0x0A

            in 0x2000 until 0x4000 -> {
                var newRomBank = value and 0b01111111

                if (newRomBank == 0) {
                    newRomBank = 1
                }

                currentRomBank = newRomBank % rom.size
            }

            in 0x4000 until 0x6000 -> {
                // If value is 0x08-0x0C the corresponding RTC register is mapped into memory at A000-BFFF

                if (value in 0x08..0x0C && hasTimer) {
                    val reg = value - 0x08 // Get register index for this.clock
                    this.mappedRegister = reg
                    this.registerMapped = true
                } else {
                    this.registerMapped = false

                    val newVal = value and 0b00000011
                    currentRamBank = newVal
                }
            }

            in 0x6000 until 0x8000 -> {
                val newVal = value and 0xFF
                if (newVal == 0) {
                    latchReady = true
                } else if (newVal == 1) {
                    if (latchReady) {
                        //TODO: latch
                    }
                }
            }
        }
    }

    override fun readRam(address: Int): Int {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (ram == null || !ramEnabled) {
            return 0xFF
        }

        return if (registerMapped && hasTimer) {
            this.clock[mappedRegister]
        } else {
            val newAddress = address - 0xA000
            this.ram[currentRamBank][newAddress]
        }
    }

    override fun writeRam(address: Int, value: Int) {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (ram == null || !ramEnabled) {
            return
        }

        val newAddress = address - 0xA000
        val newVal = value and 0xFF
        if (registerMapped && hasTimer) {
            // TODO: register write value limits
            this.clock[mappedRegister] = newVal
        } else {
            this.ram[currentRamBank][newAddress] = newVal
        }
    }

    override fun toString(): String {
        return "MBC3, ${rom.size} banks of ROM, ${ram?.size ?: 0} banks of RAM, Battery: $hasBattery, Timer: $hasTimer"
    }
}