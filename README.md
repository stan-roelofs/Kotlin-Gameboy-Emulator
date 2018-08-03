# Kotlin-Gameboy-Emulator

Gameboy emulator written in Kotlin using TornadoFX for rendering. 

## Currently implemented
* Cpu (except for interrupts) and most of the instructions, but should still be tested
* Mmu 
* Cartridge: reading the header and loading the rom, so far only ROM ONLY cartridges are supported

## Tests status
### Blargg's tests
- [x] 01 - Special
- [ ] 02 - Interrupts
- [x] 03 - op sp, hl
- [x] 04 - op r, imm
- [x] 05 - op rp
- [x] 06 - ld r,r
- [x] 07 - jr,jp,call,ret,rst
- [x] 08 - misc instrs
- [x] 09 - op r,r
- [ ] 10 - bit ops
- [ ] 11 - op a,(hl)

## Roadmap
- Pass all CPU tests
- Implement GPU
- Get Tetris running
- Controls
- Sound