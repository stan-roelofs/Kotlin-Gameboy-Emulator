package gameboy.memory.io.graphics

import gameboy.memory.Mmu
import gameboy.utils.getBit
import gameboy.utils.toHexString

class PpuCGB(private val mmu: Mmu) : Ppu(mmu) {

    // Video RAM memory
    val vram = Array(2) {IntArray(0x2000)}

    private var BCPS = 0
    private var OCPS = 0
    var bgPalettes = IntArray(0x40)
    var objPalettes = IntArray(0x40)

    private var currentBank = 0

    init {
        reset()
    }

    override fun reset() {
        super.reset()

        LCDC = 0x91
        LY = 0x00
        LYC = 0x00
        STAT = 0
        SCY = 0x00
        SCX = 0x00
        WY = 0x00
        WX = 0x00
        BGP = 0xFC
        OBP0 = 0xFF
        OBP1 = 0xFF
        BCPS = 0
        OCPS = 0

        bgPalettes.fill(0)
        objPalettes.fill(0)

        for (bank in vram) {
            bank.fill(0)
        }
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.LCDC -> this.LCDC
            Mmu.LY -> {
                if (lcdEnabled()) {
                    this.LY
                } else {
                    0 // If LCD is off this register is fixed at 0
                }
            }
            Mmu.LYC -> this.LYC
            Mmu.STAT -> {
                if (lcdEnabled()) {
                this.STAT or 0b10000000 or getMode().mode // Bit 7 is always 1
                } else {
                  (this.STAT or 0b10000000) and 0b11111000 // Bits 0-2 return 0 when LCD is off
                }
            }
            Mmu.SCY -> this.SCY
            Mmu.SCX -> this.SCX
            Mmu.WY -> this.WY
            Mmu.WX -> this.WX
            Mmu.BGP -> this.BGP
            Mmu.OBP0 -> this.OBP0
            Mmu.OBP1 -> this.OBP1
            Mmu.VBK -> this.currentBank or 0b11111110
            Mmu.BCPS -> this.BCPS or 0b01000000
            Mmu.BCPD -> this.bgPalettes[BCPS and 0b00111111]
            Mmu.OCPS -> this.OCPS or 0b01000000
            Mmu.OCPD -> this.objPalettes[OCPS and 0b00111111]
            in 0x8000 until 0xA000 -> vram[currentBank][address - 0x8000]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.LCDC -> {
                val lcdBefore = lcdEnabled()

                this.LCDC = newVal

                if (lcdBefore && !lcdEnabled()) {
                    ticksInLine = 0
                    //setMode(Mode.HBLANK.mode)
                    this.LY = 0
                }
            }
            Mmu.LY -> this.LY = 0
            Mmu.LYC -> this.LYC = newVal
            Mmu.STAT -> this.STAT = this.STAT or (newVal and 0b11111000) // Last three bits are read-only
            Mmu.SCY -> this.SCY = newVal
            Mmu.SCX -> this.SCX = newVal
            Mmu.WY -> this.WY = newVal
            Mmu.WX -> this.WX = newVal
            Mmu.BGP -> this.BGP = newVal
            Mmu.OBP0 -> this.OBP0 = newVal
            Mmu.OBP1 -> this.OBP1 = newVal
            Mmu.VBK -> this.currentBank = newVal and 0b00000001
            Mmu.BCPS -> this.BCPS = newVal and 0b10111111
            Mmu.BCPD -> {
                this.bgPalettes[BCPS and 0b00111111] = newVal
                if (this.BCPS.getBit(7)) {
                    this.BCPS = (this.BCPS + 1) and 0b10111111
                }
            }
            Mmu.OCPS -> this.OCPS = newVal and 0b10111111
            Mmu.OCPD -> {
                this.objPalettes[OCPS and 0b00111111] = newVal
                if (this.OCPS.getBit(7)) {
                    this.OCPS = (this.OCPS + 1) and 0b10111111
                }
            }
            in 0x8000 until 0xA000 -> vram[currentBank][address - 0x8000] = value and 0xFF
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }
}