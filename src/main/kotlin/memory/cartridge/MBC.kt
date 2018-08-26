package memory.cartridge

interface MBC : CartridgeType {
    /** Current ROM bank number */
    var currentRomBank: Int

    /** Current RAM bank number */
    var currentRamBank: Int

    /** Enables or disables RAM */
    var ramEnabled: Boolean
}