package memory

class Gpu : Memory {

    private val vram: IntArray = IntArray(0x2000)

    override fun reset() {
        vram.fill(0)
    }

    override fun readByte(address: Int): Int {
        if (address < 0x8000 || address >= 0xA000) {
            throw IllegalArgumentException("Address $address does not belong to Gpu")
        }

        return vram[address - 0x8000]
    }

    override fun writeByte(address: Int, value: Int) {
        if (address < 0x8000 || address >= 0xA000) {
            throw IllegalArgumentException("Address $address does not belong to Gpu")
        }

        vram[address - 0x8000] = value and 0xFF
    }
}