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

## Tests
See [blargg_tests.md](blargg_tests.md) and [mooneye_tests.md](mooneye_tests.md).

## Status
Most games run fine with some minor graphical glitches such as wrong sprite priorities. 
Accuracy of the APU and GPU is decent, although 

## Preview
![alt text](res/preview.gif)

![alt text](res/preview2.gif)

![alt text](res/preview3.gif)
