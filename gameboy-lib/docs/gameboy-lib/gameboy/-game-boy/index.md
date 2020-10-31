---
title: GameBoy -
---
//[gameboy-lib](../../index.md)/[gameboy](../index.md)/[GameBoy](index.md)



# GameBoy  
 [jvm] abstract class [GameBoy](index.md) : [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)

Main Gameboy class



Implements the Runnable interface such that it can be ran in a thread

   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy/GameBoy/GameBoy/#/PointingToDeclaration/"></a>[GameBoy](-game-boy.md)| <a name="gameboy/GameBoy/GameBoy/#/PointingToDeclaration/"></a> [jvm] fun [GameBoy](-game-boy.md)()   <br>


## Types  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy/GameBoy.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="gameboy/GameBoy.Companion///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy/GameBoy/reset/#/PointingToDeclaration/"></a>[reset](reset.md)| <a name="gameboy/GameBoy/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [reset](reset.md)()  <br>More info  <br>Resets all registers and memory addresses  <br><br><br>
| <a name="gameboy/GameBoy/run/#/PointingToDeclaration/"></a>[run](run.md)| <a name="gameboy/GameBoy/run/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open override fun [run](run.md)()  <br><br><br>
| <a name="gameboy/GameBoy/step/#/PointingToDeclaration/"></a>[step](step.md)| <a name="gameboy/GameBoy/step/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [step](step.md)()  <br>More info  <br>Performs a single cpu step  <br><br><br>
| <a name="gameboy/GameBoy/stop/#/PointingToDeclaration/"></a>[stop](stop.md)| <a name="gameboy/GameBoy/stop/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [stop](stop.md)()  <br>More info  <br>Stop running  <br><br><br>
| <a name="gameboy/GameBoy/togglePause/#/PointingToDeclaration/"></a>[togglePause](toggle-pause.md)| <a name="gameboy/GameBoy/togglePause/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [togglePause](toggle-pause.md)()  <br>More info  <br>Toggle pause on / off  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy/GameBoy/cpu/#/PointingToDeclaration/"></a>[cpu](cpu.md)| <a name="gameboy/GameBoy/cpu/#/PointingToDeclaration/"></a> [jvm] abstract val [cpu](cpu.md): [Cpu](../../gameboy.cpu/-cpu/index.md)The Gameboy's CPU instance   <br>
| <a name="gameboy/GameBoy/mmu/#/PointingToDeclaration/"></a>[mmu](mmu.md)| <a name="gameboy/GameBoy/mmu/#/PointingToDeclaration/"></a> [jvm] abstract val [mmu](mmu.md): [Mmu](../../gameboy.memory/-mmu/index.md)The Gameboy's MMU instance   <br>
| <a name="gameboy/GameBoy/paused/#/PointingToDeclaration/"></a>[paused](paused.md)| <a name="gameboy/GameBoy/paused/#/PointingToDeclaration/"></a> [jvm] var [paused](paused.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseIndicates whether the gameboy is paused or not   <br>
| <a name="gameboy/GameBoy/running/#/PointingToDeclaration/"></a>[running](running.md)| <a name="gameboy/GameBoy/running/#/PointingToDeclaration/"></a> [jvm] var [running](running.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseIndicates whether the gameboy is currently running or not   <br>


## Inheritors  
  
|  Name| 
|---|
| <a name="gameboy/GameBoyCGB///PointingToDeclaration/"></a>[GameBoyCGB](../-game-boy-c-g-b/index.md)
| <a name="gameboy/GameBoyDMG///PointingToDeclaration/"></a>[GameBoyDMG](../-game-boy-d-m-g/index.md)

