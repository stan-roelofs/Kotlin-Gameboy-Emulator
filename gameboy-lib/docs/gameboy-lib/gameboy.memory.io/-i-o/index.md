---
title: IO -
---
//[gameboy-lib](../../index.md)/[gameboy.memory.io](../index.md)/[IO](index.md)



# IO  
 [jvm] abstract class [IO](index.md)(**mmu**: [Mmu](../../gameboy.memory/-mmu/index.md)) : [Memory](../../gameboy.memory/-memory/index.md)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy.memory/Memory/readByte/#kotlin.Int/PointingToDeclaration/"></a>[readByte](../../gameboy.memory/-memory/read-byte.md)| <a name="gameboy.memory/Memory/readByte/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [readByte](../../gameboy.memory/-memory/read-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads an 8-bit value at address  <br><br><br>
| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[readWord](../../gameboy.memory/-memory/read-word.md)| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [readWord](../../gameboy.memory/-memory/read-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads a 16-bit value at address  <br><br><br>
| <a name="gameboy.memory.io/IO/reset/#/PointingToDeclaration/"></a>[reset](reset.md)| <a name="gameboy.memory.io/IO/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>override fun [reset](reset.md)()  <br>More info  <br>Resets each memory address to their default value  <br><br><br>
| <a name="gameboy.memory.io/IO/tick/#kotlin.Int/PointingToDeclaration/"></a>[tick](tick.md)| <a name="gameboy.memory.io/IO/tick/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [tick](tick.md)(cycles: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| <a name="gameboy.memory/Memory/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeByte](../../gameboy.memory/-memory/write-byte.md)| <a name="gameboy.memory/Memory/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [writeByte](../../gameboy.memory/-memory/write-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>
| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeWord](../../gameboy.memory/-memory/write-word.md)| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [writeWord](../../gameboy.memory/-memory/write-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.memory.io/IO/dma/#/PointingToDeclaration/"></a>[dma](dma.md)| <a name="gameboy.memory.io/IO/dma/#/PointingToDeclaration/"></a> [jvm] abstract val [dma](dma.md): [Dma](../-dma/index.md)   <br>
| <a name="gameboy.memory.io/IO/joypad/#/PointingToDeclaration/"></a>[joypad](joypad.md)| <a name="gameboy.memory.io/IO/joypad/#/PointingToDeclaration/"></a> [jvm] val [joypad](joypad.md): [Joypad](../-joypad/index.md)   <br>
| <a name="gameboy.memory.io/IO/lcd/#/PointingToDeclaration/"></a>[lcd](lcd.md)| <a name="gameboy.memory.io/IO/lcd/#/PointingToDeclaration/"></a> [jvm] abstract val [lcd](lcd.md): [Lcd](../../gameboy.memory.io.graphics/-lcd/index.md)   <br>
| <a name="gameboy.memory.io/IO/serial/#/PointingToDeclaration/"></a>[serial](serial.md)| <a name="gameboy.memory.io/IO/serial/#/PointingToDeclaration/"></a> [jvm] val [serial](serial.md): [Serial](../-serial/index.md)   <br>
| <a name="gameboy.memory.io/IO/sound/#/PointingToDeclaration/"></a>[sound](sound.md)| <a name="gameboy.memory.io/IO/sound/#/PointingToDeclaration/"></a> [jvm] abstract val [sound](sound.md): [Sound](../../gameboy.memory.io.sound/-sound/index.md)   <br>


## Inheritors  
  
|  Name| 
|---|
| <a name="gameboy.memory.io/IOCGB///PointingToDeclaration/"></a>[IOCGB](../-i-o-c-g-b/index.md)
| <a name="gameboy.memory.io/IODMG///PointingToDeclaration/"></a>[IODMG](../-i-o-d-m-g/index.md)

