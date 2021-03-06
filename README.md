# Kotlin-Gameboy-Emulator 
[![Build Status](https://www.travis-ci.com/stan-roelofs/Kotlin-Gameboy-Emulator.svg?branch=master)](https://www.travis-ci.com/stan-roelofs/Kotlin-Gameboy-Emulator)
 ![Maven Central](https://img.shields.io/maven-central/v/nl.stanroelofs/gameboy-lib)
\
A Gameboy / Gameboy color emulator written in Kotlin.

## Can I use this emulator to play?
There are a lot more capable emulators available. 
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
- Video RAM viewer which can be used to view tiles that are currently in the video RAM.
- OAM viewer which can be used to view the sprites and their properties.

## Notes
This emulator is for the original Game Boy (DMG) and the versions A,B,C of the CPU.

## Preview
![alt text](res/preview.gif)

![alt text](res/preview2.gif)

![alt text](res/preview3.gif)
