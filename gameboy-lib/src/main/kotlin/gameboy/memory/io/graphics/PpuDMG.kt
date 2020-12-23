package gameboy.memory.io.graphics

import gameboy.memory.Mmu
import gameboy.memory.io.graphics.mode.PixelTransfer
import gameboy.utils.toHexString

class PpuDMG(mmu: Mmu) : Ppu(mmu) {

    override val vram = Vram(1)

    private val renderer = PixelRendererDMG(lcd, lcdc, bgp, obp0, obp1)
    private val fetcher = FetcherDMG(lcdc, wx, wy, scy, scx, ly, mmu.oam, vram)
    override val pixelTransfer = PixelTransfer(renderer, fetcher, lcdc, ly, wy, wx, scx)

    init {
        reset()
    }

    override fun reset() {
        super.reset()

        lcdc.value = 0x91
        ly.value = 0x00
        lyc.value = 0x00
        stat.value = 0
        scy.value = 0x00
        scx.value = 0x00
        wy.value = 0x00
        wx.value = 0x00
        bgp.paletteByte = 0xFC
        obp0.paletteByte = 0xFF
        obp1.paletteByte = 0xFF

        vram.reset()
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            lcdc.address -> lcdc.value
            lcdc.address -> lcdc.value
            ly.address -> {
                if (lcdc.getLcdEnable()) {
                    ly.value
                } else {
                    0 // If LCD is off this register is fixed at 0
                }
            }
            lyc.address -> lyc.value
            stat.address -> {
                if (lcdc.getLcdEnable()) {
                    stat.value or 0b10000000 or getMode().mode // Bit 7 is always 1
                } else {
                    (stat.value or 0b10000000) and 0b11111000 // Bits 0-2 return 0 when LCD is off
                }
            }
            scy.address -> scy.value
            scx.address -> scx.value
            wy.address -> wy.value
            wx.address -> wx.value
            Mmu.BGP -> bgp.paletteByte
            Mmu.OBP0 -> obp0.paletteByte
            Mmu.OBP1 -> obp1.paletteByte
            in 0x8000 until 0xA000 -> vram.readByte(0, address)
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            lcdc.address -> {
                //val lcdBefore = lcdc.getLcdEnable()

                lcdc.value = newVal

                //if (lcdBefore && !lcdc.getLcdEnable()) {
                    //ticksInLine = 0
                    //setMode(Mode.HBLANK.mode)
                   // ly.value = 0
               // }
            }
            ly.address -> ly.value = newVal
            lyc.address -> lyc.value = newVal
            stat.address -> stat.value = stat.value or (newVal and 0b11111000) // Last three bits are read-only
            scy.address -> scy.value = newVal
            scx.address -> scx.value = newVal
            wx.address -> wx.value = newVal
            wy.address -> wy.value = newVal
            Mmu.BGP -> this.bgp.paletteByte = newVal
            Mmu.OBP0 -> this.obp0.paletteByte = newVal
            Mmu.OBP1 -> this.obp1.paletteByte = newVal

            in 0x8000 until 0xA000 -> vram.writeByte(0, address, newVal)
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }
}