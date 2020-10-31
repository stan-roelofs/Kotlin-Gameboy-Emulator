---
title: Registers -
---
//[gameboy-lib](../../index.md)/[gameboy.cpu](../index.md)/[Registers](index.md)



# Registers  
 [jvm] abstract class [Registers](index.md)

Represents the registers of the gameboy CPU



8-bit registers: A, F, B, C, D, E, H, L 16-bit registers: SP, PC



On construction [reset](reset.md) is called to set the registers to the initial values.

   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.cpu/Registers/Registers/#/PointingToDeclaration/"></a>[Registers](-registers.md)| <a name="gameboy.cpu/Registers/Registers/#/PointingToDeclaration/"></a> [jvm] fun [Registers](-registers.md)()   <br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="gameboy.cpu/Registers/getAF/#/PointingToDeclaration/"></a>[getAF](get-a-f.md)| <a name="gameboy.cpu/Registers/getAF/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getAF](get-a-f.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Returns the combined register AF (16 bit)  <br><br><br>
| <a name="gameboy.cpu/Registers/getBC/#/PointingToDeclaration/"></a>[getBC](get-b-c.md)| <a name="gameboy.cpu/Registers/getBC/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getBC](get-b-c.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Returns the combined register BC (16 bit)  <br><br><br>
| <a name="gameboy.cpu/Registers/getCFlag/#/PointingToDeclaration/"></a>[getCFlag](get-c-flag.md)| <a name="gameboy.cpu/Registers/getCFlag/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getCFlag](get-c-flag.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Returns the state of the C flag the flags registers  <br><br><br>
| <a name="gameboy.cpu/Registers/getDE/#/PointingToDeclaration/"></a>[getDE](get-d-e.md)| <a name="gameboy.cpu/Registers/getDE/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getDE](get-d-e.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Returns the combined register DE (16 bit)  <br><br><br>
| <a name="gameboy.cpu/Registers/getHFlag/#/PointingToDeclaration/"></a>[getHFlag](get-h-flag.md)| <a name="gameboy.cpu/Registers/getHFlag/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getHFlag](get-h-flag.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Returns the state of the H flag the flags registers  <br><br><br>
| <a name="gameboy.cpu/Registers/getHL/#/PointingToDeclaration/"></a>[getHL](get-h-l.md)| <a name="gameboy.cpu/Registers/getHL/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getHL](get-h-l.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br>More info  <br>Returns the combined register HL (16 bit)  <br><br><br>
| <a name="gameboy.cpu/Registers/getNFlag/#/PointingToDeclaration/"></a>[getNFlag](get-n-flag.md)| <a name="gameboy.cpu/Registers/getNFlag/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getNFlag](get-n-flag.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Returns the state of the N flag the flags registers  <br><br><br>
| <a name="gameboy.cpu/Registers/getZFlag/#/PointingToDeclaration/"></a>[getZFlag](get-z-flag.md)| <a name="gameboy.cpu/Registers/getZFlag/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getZFlag](get-z-flag.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Returns the state of the Z flag the flags registers  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy.cpu/Registers/reset/#/PointingToDeclaration/"></a>[reset](reset.md)| <a name="gameboy.cpu/Registers/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [reset](reset.md)()  <br>More info  <br>Sets the registers to the values they should be after running the boot rom  <br><br><br>
| <a name="gameboy.cpu/Registers/toString/#/PointingToDeclaration/"></a>[toString](to-string.md)| <a name="gameboy.cpu/Registers/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.cpu/Registers/A/#/PointingToDeclaration/"></a>[A](-a.md)| <a name="gameboy.cpu/Registers/A/#/PointingToDeclaration/"></a> [jvm] var [A](-a.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register A (8-bit)   <br>
| <a name="gameboy.cpu/Registers/B/#/PointingToDeclaration/"></a>[B](-b.md)| <a name="gameboy.cpu/Registers/B/#/PointingToDeclaration/"></a> [jvm] var [B](-b.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register B (8-bit)   <br>
| <a name="gameboy.cpu/Registers/C/#/PointingToDeclaration/"></a>[C](-c.md)| <a name="gameboy.cpu/Registers/C/#/PointingToDeclaration/"></a> [jvm] var [C](-c.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register C (8-bit)   <br>
| <a name="gameboy.cpu/Registers/D/#/PointingToDeclaration/"></a>[D](-d.md)| <a name="gameboy.cpu/Registers/D/#/PointingToDeclaration/"></a> [jvm] var [D](-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register D (8-bit)   <br>
| <a name="gameboy.cpu/Registers/E/#/PointingToDeclaration/"></a>[E](-e.md)| <a name="gameboy.cpu/Registers/E/#/PointingToDeclaration/"></a> [jvm] var [E](-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register E (8-bit)   <br>
| <a name="gameboy.cpu/Registers/eiExecuted/#/PointingToDeclaration/"></a>[eiExecuted](ei-executed.md)| <a name="gameboy.cpu/Registers/eiExecuted/#/PointingToDeclaration/"></a> [jvm] var [eiExecuted](ei-executed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false   <br>
| <a name="gameboy.cpu/Registers/F/#/PointingToDeclaration/"></a>[F](-f.md)| <a name="gameboy.cpu/Registers/F/#/PointingToDeclaration/"></a> [jvm] var [F](-f.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register F (8-bit)   <br>
| <a name="gameboy.cpu/Registers/H/#/PointingToDeclaration/"></a>[H](-h.md)| <a name="gameboy.cpu/Registers/H/#/PointingToDeclaration/"></a> [jvm] var [H](-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register H (8-bit)   <br>
| <a name="gameboy.cpu/Registers/halt/#/PointingToDeclaration/"></a>[halt](halt.md)| <a name="gameboy.cpu/Registers/halt/#/PointingToDeclaration/"></a> [jvm] var [halt](halt.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseHalt flag, enabled when the cpu is in the halt state   <br>
| <a name="gameboy.cpu/Registers/haltBug/#/PointingToDeclaration/"></a>[haltBug](halt-bug.md)| <a name="gameboy.cpu/Registers/haltBug/#/PointingToDeclaration/"></a> [jvm] var [haltBug](halt-bug.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false   <br>
| <a name="gameboy.cpu/Registers/IME/#/PointingToDeclaration/"></a>[IME](-i-m-e.md)| <a name="gameboy.cpu/Registers/IME/#/PointingToDeclaration/"></a> [jvm] var [IME](-i-m-e.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseIME (Interrupt Master Enable) flag.   <br>
| <a name="gameboy.cpu/Registers/L/#/PointingToDeclaration/"></a>[L](-l.md)| <a name="gameboy.cpu/Registers/L/#/PointingToDeclaration/"></a> [jvm] var [L](-l.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Register L (8-bit)   <br>
| <a name="gameboy.cpu/Registers/PC/#/PointingToDeclaration/"></a>[PC](-p-c.md)| <a name="gameboy.cpu/Registers/PC/#/PointingToDeclaration/"></a> [jvm] var [PC](-p-c.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Program counter (16-bit)   <br>
| <a name="gameboy.cpu/Registers/SP/#/PointingToDeclaration/"></a>[SP](-s-p.md)| <a name="gameboy.cpu/Registers/SP/#/PointingToDeclaration/"></a> [jvm] var [SP](-s-p.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0Stack pointer (16-bit)   <br>
| <a name="gameboy.cpu/Registers/stop/#/PointingToDeclaration/"></a>[stop](stop.md)| <a name="gameboy.cpu/Registers/stop/#/PointingToDeclaration/"></a> [jvm] var [stop](stop.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseStop flag, enabled when the cpu is in the STOP state   <br>


## Inheritors  
  
|  Name| 
|---|
| <a name="gameboy.cpu/RegistersCGB///PointingToDeclaration/"></a>[RegistersCGB](../-registers-c-g-b/index.md)
| <a name="gameboy.cpu/RegistersDMG///PointingToDeclaration/"></a>[RegistersDMG](../-registers-d-m-g/index.md)

