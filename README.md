# kGB (Kotlin-Gameboy-Emulator) [![Build Status](https://travis-ci.org/stan-roelofs/Kotlin-Gameboy-Emulator.svg?branch=master)](https://travis-ci.org/stan-roelofs/Kotlin-Gameboy-Emulator)
A Gameboy emulator written in Kotlin.

## Can I use this emulator to play?
Yes you can, but you shouldn't. There are a lot more capable emulators available. 
This is a work in progress personal project with the aim for me to learn about emulation.

## Downloads
See [releases](https://github.com/stan-roelofs/Kotlin-Gameboy-Emulator/releases)

## Building
Make sure you have JDK8 installed.
After cloning the repository, simply use the gradle wrapper.

To build:
`./gradlew build`

To run:
`./gradlew gameboy-libgdx-desktop:run`

## Tests status
See [blargg_tests.md](blargg_tests.md) and [mooneye_tests.md](mooneye_tests.md).

## Features
- Runs on Windows, Linux and Mac.
- Video RAM debugger which can be used to view tiles that are currently in the video RAM.
- OAM debugger which can be used to view the sprites and their properties that are currently in the OAM.

## Notes
This emulator is for the original Game Boy (DMG) and the versions A,B,C of the CPU.

## Preview
![alt text](https://media.giphy.com/media/Sr8w0qUxx9K9LhviS1/giphy.gif)

## Getting started
gameboy-lib contains the gameboy emulator library.
gameboy-libgdx-core contains common rendering code.
gameboy-libgdx-desktop and gameboy-libgdx-android contain code specific to different platforms. 
