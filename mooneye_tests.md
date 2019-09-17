# Mooneye tests

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
- [ ] intr_timing
- [ ] jp_cc_timing
- [ ] ld_hl_sp_e_timing
- [x] if_ie_registers
- [ ] call_timing
- [ ] boot_div_dmgABC
- [ ] push_timing
- [ ] pop_timing
- [x] halt_ime0_nointr_timing
- [x] oam_dma_restart
- [x] oam_dma_timing
- [x] oam_dma_start
- [ ] call_timing2
- [ ] halt_ime1_timing2_GS
- [ ] jp_timing
- [ ] ei_sequence
- [ ] ret_cc_timing
- [x] boot_hwio_dmgABC
- [ ] call_cc_timing
- [ ] add_sp_e_timing

### Bits
- [x] mem_oam
- [x] reg_f
- [x] unused_hwio

### Interrupts
- [x] ie_push - R4: unwanted cancel

### Oam_dma
- [x] basic
- [x] reg_read
- [x] sources-dmgABCmgbS

### PPU
- [ ] hblank_ly_scx_timing-GS
- [ ] intr_1_2_timing-GS
- [ ] intr_2_0_timing - Fails, incorrect timing between STAT mode=1 and STAT mode=2 interrupt
- [x] intr_2_mode0_timing
- [ ] intr_2_mode0_timing_sprites
- [x] intr_2_mode3_timing
- [ ] intr_2_oam_ok_timing - E register value incorrect
- [ ] lcdon_timing-dmgABCmgbS - Cycle: 00 Expected: 00 Actual: 90
- [ ] lcdon_write_timing-GS - Cycle: 12 Expected: 00 Actual: 81
- [x] stat_irq_blocking
- [ ] stat_lyc_onoff - Somehow tries to execute instruction 0xED which corresponds to nothing
- [x] vblank_stat_intr-GS

### Timer
- [x] div_write
- [x] rapid_toggle
- [x] tim00
- [x] tim00_div_trigger
- [x] tim01
- [x] tim01_div_trigger
- [x] tim10
- [x] tim10_div_trigger
- [x] tim11
- [x] tim11_div_trigger
- [ ] tima_reload
- [ ] time_write_reloading
- [ ] tma_write_reloading

### Instructions
- [x] daa

### Serial
- [ ] boot_sclk_align-dmgABCmgb.gb

## Emulator only
### MBC1
- [ ] multicart_rom_8Mb
- [x] rom_512Kb
- [x] rom_1Mb
- [x] rom_2Mb
- [x] rom_4Mb
- [x] rom_8Mb
- [x] rom_16Mb
- [x] ram_256Kb
- [x] ram_64Kb
- [x] bits_ram_en
