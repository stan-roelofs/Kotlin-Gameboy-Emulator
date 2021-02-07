# Mooneye tests
Each test rom has a corresponding unit test.
Each Mooneye GB test rom executes opcode `0x40: LD B, B` when finished. 
When a test ran successfully, the registers should contain the following values:
```
B = 3
C = 5
D = 8
E = 13
H = 21
L = 34
```
Register A should have value 0, a value not equal to 0 indicates one or more assertions failed. 

A screenshot is made after the test is finished, and can be found in the testoutput directory.
A hash of the screen buffer will be written to a text file in this directory.

## Acceptance
- [x] boot_regs_dmgABC
- [x] call_cc_timing2
- [x] ei_timing
- [x] div_timing
- [x] rapid_di_ei
- [ ] di_timing_GS
- [x] ret_timing
- [x] halt_ime0_ei
- [x] halt_ime1_timing
- [x] reti_intr_timing
- [x] rst_timing
- [x] reti_timing
- [x] intr_timing
- [x] jp_cc_timing
- [x] ld_hl_sp_e_timing
- [x] if_ie_registers
- [x] call_timing
- [x] boot_div_dmgABC
- [x] push_timing
- [x] pop_timing
- [ ] halt_ime0_nointr_timing
- [x] oam_dma_restart
- [x] oam_dma_timing
- [x] oam_dma_start
- [x] call_timing2
- [ ] halt_ime1_timing2_GS
- [x] jp_timing
- [ ] ei_sequence
- [x] ret_cc_timing
- [ ] boot_hwio_dmgABC
- [x] call_cc_timing
- [x] add_sp_e_timing

### Bits
- [x] mem_oam
- [x] reg_f
- [x] unused_hwio

### Interrupts
- [x] ie_push

### Oam_dma
- [x] basic
- [x] reg_read
- [ ] sources-dmgABCmgbS

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
- [ ] stat_irq_blocking
- [ ] stat_lyc_onoff
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
- [x] tima_reload
- [x] time_write_reloading
- [x] tma_write_reloading

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
- [x] bits_bank1
- [x] bits_bank2
- [x] bits_mode
- [x] bits_ramg

### MBC2
- [x] rom_1Mb
- [x] rom_2Mb
- [x] rom_512Kb
- [x] ram
- [x] bits_ramg
- [x] bits_romb
- [x] bits_unused

### MBC5
- [x] rom_1Mb
- [x] rom_2Mb
- [x] rom_4Mb
- [x] rom_8Mb
- [x] rom_16Mb
- [x] rom_32Mb
- [x] rom_64Mb
- [x] rom_512Kb