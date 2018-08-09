# Kotlin-Gameboy-Emulator

Gameboy emulator written in Kotlin using TornadoFX for rendering. 

## Tests status
### Blargg's tests
- [x] 01 - Special
- [x] 02 - Interrupts
- [x] 03 - op sp, hl
- [x] 04 - op r, imm
- [x] 05 - op rp
- [x] 06 - ld r,r
- [x] 07 - jr,jp,call,ret,rst
- [x] 08 - misc instrs
- [x] 09 - op r,r
- [x] 10 - bit ops
- [x] 11 - op a,(hl)

## Roadmap
- [x] Pass all CPU tests
- [ ] Implement GPU
- [ ] Get Tetris running
- [ ] Controls
- [ ] Sound

## Status
Currently working on cleaning up some of the code, mainly related to the timer and interrupts.
Next step is to properly implement the GPU.