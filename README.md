# Kotlin-Gameboy-Emulator

Gameboy emulator written in Kotlin using TornadoFX for rendering. This project started as a way for me to understand how emulators function. 
It is written in Kotlin because it seemed like an interesting language that I wanted to try out.

## Tests status
See [blargg_tests.md](blargg_tests.md) and [mooneye_tests.md](mooneye_tests.md)

## Features
- Runs on Windows, Linux and Mac.
- Video RAM debugger which can be used to view tiles that are currently in the video RAM.
- OAM debugger which can be used to view the sprites and their properties that are currently in the OAM.

## Status
Some games are playable but there is an issue where sprites are missing in some games, I suspect because of read/write timing issues. 
Audio is currently not implemented. Currently the focus is on passing the mooneye and blargg test roms to make the emulator more accurate.
ROM Only and MBC1 cartridges are supported.

![alt text](https://media.giphy.com/media/51WvJVuSGZAu9jbbLM/giphy.gif)

## Notes
