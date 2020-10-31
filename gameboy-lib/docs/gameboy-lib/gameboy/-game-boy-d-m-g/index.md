---
title: GameBoyDMG -
---
//[gameboy-lib](../../index.md)/[gameboy](../index.md)/[GameBoyDMG](index.md)



# GameBoyDMG  
 [jvm] class [GameBoyDMG](index.md)(**cartridge**: [Cartridge](../../gameboy.memory.cartridge/-cartridge/index.md)) : [GameBoy](../-game-boy/index.md)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy/GameBoy/reset/#/PointingToDeclaration/"></a>[reset](../-game-boy/reset.md)| <a name="gameboy/GameBoy/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [reset](../-game-boy/reset.md)()  <br>More info  <br>Resets all registers and memory addresses  <br><br><br>
| <a name="gameboy/GameBoy/run/#/PointingToDeclaration/"></a>[run](../-game-boy/run.md)| <a name="gameboy/GameBoy/run/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open override fun [run](../-game-boy/run.md)()  <br><br><br>
| <a name="gameboy/GameBoy/step/#/PointingToDeclaration/"></a>[step](../-game-boy/step.md)| <a name="gameboy/GameBoy/step/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [step](../-game-boy/step.md)()  <br>More info  <br>Performs a single cpu step  <br><br><br>
| <a name="gameboy/GameBoy/stop/#/PointingToDeclaration/"></a>[stop](../-game-boy/stop.md)| <a name="gameboy/GameBoy/stop/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [stop](../-game-boy/stop.md)()  <br>More info  <br>Stop running  <br><br><br>
| <a name="gameboy/GameBoy/togglePause/#/PointingToDeclaration/"></a>[togglePause](../-game-boy/toggle-pause.md)| <a name="gameboy/GameBoy/togglePause/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [togglePause](../-game-boy/toggle-pause.md)()  <br>More info  <br>Toggle pause on / off  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy/GameBoyDMG/cpu/#/PointingToDeclaration/"></a>[cpu](cpu.md)| <a name="gameboy/GameBoyDMG/cpu/#/PointingToDeclaration/"></a> [jvm] open override val [cpu](cpu.md): [Cpu](../../gameboy.cpu/-cpu/index.md)The Gameboy's CPU instance   <br>
| <a name="gameboy/GameBoyDMG/mmu/#/PointingToDeclaration/"></a>[mmu](mmu.md)| <a name="gameboy/GameBoyDMG/mmu/#/PointingToDeclaration/"></a> [jvm] open override val [mmu](mmu.md): [MmuDMG](../../gameboy.memory/-mmu-d-m-g/index.md)The Gameboy's MMU instance   <br>
| <a name="gameboy/GameBoyDMG/paused/#/PointingToDeclaration/"></a>[paused](paused.md)| <a name="gameboy/GameBoyDMG/paused/#/PointingToDeclaration/"></a> [jvm] var [paused](paused.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseIndicates whether the gameboy is paused or not   <br>
| <a name="gameboy/GameBoyDMG/running/#/PointingToDeclaration/"></a>[running](running.md)| <a name="gameboy/GameBoyDMG/running/#/PointingToDeclaration/"></a> [jvm] var [running](running.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = falseIndicates whether the gameboy is currently running or not   <br>

