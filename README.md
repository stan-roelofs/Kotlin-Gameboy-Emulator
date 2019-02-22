# Kotlin-Gameboy-Emulator
Gameboy emulator written in Kotlin. This project started as a way for me to 
understand emulation. It is written in Kotlin because it seemed like an interesting 
language that I wanted to try out.

## Downloads
See [releases](https://github.com/stan-roelofs/Kotlin-Gameboy-Emulator/releases)

## Tests status
See [blargg_tests.md](blargg_tests.md) and [mooneye_tests.md](mooneye_tests.md).

## Features
- Runs on Windows, Linux and Mac.
- Video RAM debugger which can be used to view tiles that are currently in the video RAM.
- OAM debugger which can be used to view the sprites and their properties that are currently in the OAM.

## Status
Some games are playable but there is an issue where sprites are missing in some games. Audio is currently not implemented. Currently the focus is on passing the mooneye and blargg test roms to make the emulator more accurate. ROM Only and MBC1 cartridges are supported.

## Notes
This emulator is for the original Game Boy (DMG) and the versions A,B,C of the CPU.

## Preview
![alt text](https://media.giphy.com/media/51WvJVuSGZAu9jbbLM/giphy.gif)
![alt text](https://media.giphy.com/media/5t3Mqb7Ivzf5Kcsl15/giphy.gif)
