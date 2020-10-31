---
title: Memory -
---
//[gameboy-lib](../../index.md)/[gameboy.memory](../index.md)/[Memory](index.md)



# Memory  
 [jvm] interface [Memory](index.md)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy.memory/Memory/readByte/#kotlin.Int/PointingToDeclaration/"></a>[readByte](read-byte.md)| <a name="gameboy.memory/Memory/readByte/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [readByte](read-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads an 8-bit value at address  <br><br><br>
| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[readWord](read-word.md)| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [readWord](read-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads a 16-bit value at address  <br><br><br>
| <a name="gameboy.memory/Memory/reset/#/PointingToDeclaration/"></a>[reset](reset.md)| <a name="gameboy.memory/Memory/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [reset](reset.md)()  <br>More info  <br>Resets each memory address to their default value  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| <a name="gameboy.memory/Memory/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeByte](write-byte.md)| <a name="gameboy.memory/Memory/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [writeByte](write-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>
| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeWord](write-word.md)| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [writeWord](write-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>


## Inheritors  
  
|  Name| 
|---|
| <a name="gameboy.memory/HRam///PointingToDeclaration/"></a>[HRam](../-h-ram/index.md)
| <a name="gameboy.memory/InternalRam///PointingToDeclaration/"></a>[InternalRam](../-internal-ram/index.md)
| <a name="gameboy.memory/Mmu///PointingToDeclaration/"></a>[Mmu](../-mmu/index.md)
| <a name="gameboy.memory/Oam///PointingToDeclaration/"></a>[Oam](../-oam/index.md)
| <a name="gameboy.memory.cartridge/Cartridge///PointingToDeclaration/"></a>[Cartridge](../../gameboy.memory.cartridge/-cartridge/index.md)
| <a name="gameboy.memory.cartridge/CartridgeType///PointingToDeclaration/"></a>[CartridgeType](../../gameboy.memory.cartridge/-cartridge-type/index.md)
| <a name="gameboy.memory.cartridge/MBC1///PointingToDeclaration/"></a>[MBC1](../../gameboy.memory.cartridge/-m-b-c1/index.md)
| <a name="gameboy.memory.cartridge/MBC2///PointingToDeclaration/"></a>[MBC2](../../gameboy.memory.cartridge/-m-b-c2/index.md)
| <a name="gameboy.memory.cartridge/MBC3///PointingToDeclaration/"></a>[MBC3](../../gameboy.memory.cartridge/-m-b-c3/index.md)
| <a name="gameboy.memory.cartridge/MBC5///PointingToDeclaration/"></a>[MBC5](../../gameboy.memory.cartridge/-m-b-c5/index.md)
| <a name="gameboy.memory.cartridge/ROMONLY///PointingToDeclaration/"></a>[ROMONLY](../../gameboy.memory.cartridge/-r-o-m-o-n-l-y/index.md)
| <a name="gameboy.memory.io/Dma///PointingToDeclaration/"></a>[Dma](../../gameboy.memory.io/-dma/index.md)
| <a name="gameboy.memory.io/IO///PointingToDeclaration/"></a>[IO](../../gameboy.memory.io/-i-o/index.md)
| <a name="gameboy.memory.io/Joypad///PointingToDeclaration/"></a>[Joypad](../../gameboy.memory.io/-joypad/index.md)
| <a name="gameboy.memory.io/Serial///PointingToDeclaration/"></a>[Serial](../../gameboy.memory.io/-serial/index.md)
| <a name="gameboy.memory.io/Timer///PointingToDeclaration/"></a>[Timer](../../gameboy.memory.io/-timer/index.md)
| <a name="gameboy.memory.io.graphics/Lcd///PointingToDeclaration/"></a>[Lcd](../../gameboy.memory.io.graphics/-lcd/index.md)
| <a name="gameboy.memory.io.sound/Sound///PointingToDeclaration/"></a>[Sound](../../gameboy.memory.io.sound/-sound/index.md)
| <a name="gameboy.memory.io.sound/SoundChannel///PointingToDeclaration/"></a>[SoundChannel](../../gameboy.memory.io.sound/-sound-channel/index.md)

