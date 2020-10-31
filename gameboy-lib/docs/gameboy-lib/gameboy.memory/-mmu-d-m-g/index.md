---
title: MmuDMG -
---
//[gameboy-lib](../../index.md)/[gameboy.memory](../index.md)/[MmuDMG](index.md)



# MmuDMG  
 [jvm] class [MmuDMG](index.md)(**cartridge**: [Cartridge](../../gameboy.memory.cartridge/-cartridge/index.md)) : [Mmu](../-mmu/index.md)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy.memory/MmuDMG/readByte/#kotlin.Int/PointingToDeclaration/"></a>[readByte](read-byte.md)| <a name="gameboy.memory/MmuDMG/readByte/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open override fun [readByte](read-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads an 8-bit value at address  <br><br><br>
| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[readWord](../-memory/read-word.md)| <a name="gameboy.memory/Memory/readWord/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [readWord](../-memory/read-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Reads a 16-bit value at address  <br><br><br>
| <a name="gameboy.memory/Mmu/requestInterrupt/#kotlin.Int/PointingToDeclaration/"></a>[requestInterrupt](../-mmu/request-interrupt.md)| <a name="gameboy.memory/Mmu/requestInterrupt/#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [requestInterrupt](../-mmu/request-interrupt.md)(pos: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Sets the bit at pos to true in the Interrupt Flags register  <br><br><br>
| <a name="gameboy.memory/Mmu/reset/#/PointingToDeclaration/"></a>[reset](../-mmu/reset.md)| <a name="gameboy.memory/Mmu/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>override fun [reset](../-mmu/reset.md)()  <br>More info  <br>Resets each memory address to their default value  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| <a name="gameboy.memory/MmuDMG/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeByte](write-byte.md)| <a name="gameboy.memory/MmuDMG/writeByte/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open override fun [writeByte](write-byte.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>
| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[writeWord](../-memory/write-word.md)| <a name="gameboy.memory/Memory/writeWord/#kotlin.Int#kotlin.Int/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [writeWord](../-memory/write-word.md)(address: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br>More info  <br>Writes an 8-bit value to address  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.memory/MmuDMG/cartridge/#/PointingToDeclaration/"></a>[cartridge](cartridge.md)| <a name="gameboy.memory/MmuDMG/cartridge/#/PointingToDeclaration/"></a> [jvm] val [cartridge](cartridge.md): [Cartridge](../../gameboy.memory.cartridge/-cartridge/index.md)   <br>
| <a name="gameboy.memory/MmuDMG/io/#/PointingToDeclaration/"></a>[io](io.md)| <a name="gameboy.memory/MmuDMG/io/#/PointingToDeclaration/"></a> [jvm] open override val [io](io.md): [IODMG](../../gameboy.memory.io/-i-o-d-m-g/index.md)   <br>

