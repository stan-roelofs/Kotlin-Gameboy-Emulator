# Mooneye tests
pathToTests in MooneyeTest.kt should be the location of the root Mooneye tests folder. 

## Acceptance
- [x] boot_regs_dmgABC
- [ ] call_cc_timing2
- [x] ei_timing
- [x] div_timing
- [x] rapid_di_ei
- [ ] di_timing_GS
- [ ] ret_timing
- [x] halt_ime0_ei
- [x] halt_ime1_timing
- [x] reti_intr_timing
- [ ] rst_timing
- [ ] reti_timing
- [ ] jp_cc_timing
- [ ] ld_hl_sp_e_timing
- [x] if_ie_registers
- [ ] call_timing
- [ ] push_timing
- [ ] pop_timing
- [x] halt_ime0_nointr_timing
- [ ] oam_dma_restart
- [ ] oam_dma_timing
- [ ] oam_dma_start
- [ ] call_timing2
- [ ] halt_ime1_timing2_GS
- [ ] jp_timing
- [ ] ei_sequence
- [ ] ret_cc_timing
- [ ] call_cc_timing
- [ ] boot_hwio_dmgABC
- [ ] add_sp_e_timing


### Bits
- [x] mem_oam
- [x] reg_f
- [x] unused_hwio

### Interrupts
- [ ] ie_push - R4: unwanted cancel

### Oam_dma
- [x] basic
- [x] reg_read
- [x] sources-dmgABCmgbS

### PPU
- [ ] hblank_ly_scx_timing-GS - Crashes because somehow LY increases infinitely
- [ ] intr_1_2_timing-GS - Crashes because somehow LY increases infinitely
- [ ] intr_2_0_timing - Fails, incorrect timing between STAT mode=1 and STAT mode=2 interrupt
- [x] intr_2_mode0_timing
- [ ] intr_2_mode0_timing_sprites - Crashes, somehow one of the indices becomes negative at screen in renderSprites()
- [x] intr_2_mode3_timing
- [ ] intr_2_oam_ok_timing - E register value incorrect
- [ ] lcdon_timing-dmgABCmgbS - Cycle: 00 Expected: 00 Actual: 90
- [ ] lcdon_write_timing-GS - Cycle: 12 Expected: 00 Actual: 81
- [ ] stat_irq_blocking - Crashes because somehow LY increases infinitely
- [ ] stat_lyc_onoff - Somehow tries to execute instruction 0xED which corresponds to nothing
- [ ] vblank_stat_intr-GS - Crashes because somehow LY increases infinitely

### Timer
- [x] div_write
- [ ] rapid_toggle - Unexpected timer increases not emulated
- [x] tim00
- [ ] tim00_div_trigger
- [x] tim01
- [ ] tim01_div_trigger
- [x] tim10
- [ ] tim10_div_trigger
- [x] tim11
- [ ] tim11_div_trigger
- [ ] tima_reload
- [ ] time_write_reloading
- [ ] tma_write_reloading

## Emulator only
### MBC1
- [ ] multicart_rom_8Mb
- [x] rom_512Kb
- [x] rom_1Mb
- [x] rom_2Mb
- [x] rom_4Mb
- [ ] rom_8Mb
- [ ] rom_16Mb
- [x] ram_256Kb
- [x] ram_64Kb
- [ ] bits_ram_en
