---
title: gameboy.cpu -
---
//[gameboy-lib](../index.md)/[gameboy.cpu](index.md)



# Package gameboy.cpu  


## Types  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.cpu/Cpu///PointingToDeclaration/"></a>[Cpu](-cpu/index.md)| <a name="gameboy.cpu/Cpu///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [Cpu](-cpu/index.md)(**mmu**: [Mmu](../gameboy.memory/-mmu/index.md), **registers**: [Registers](-registers/index.md))  <br>More info  <br>Represents the Gameboy CPUOn initialization [reset](-cpu/reset.md) is called.  <br><br><br>
| <a name="gameboy.cpu/InstructionsPool///PointingToDeclaration/"></a>[InstructionsPool](-instructions-pool/index.md)| <a name="gameboy.cpu/InstructionsPool///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>interface [InstructionsPool](-instructions-pool/index.md)  <br><br><br>
| <a name="gameboy.cpu/InstructionsPoolImpl///PointingToDeclaration/"></a>[InstructionsPoolImpl](-instructions-pool-impl/index.md)| <a name="gameboy.cpu/InstructionsPoolImpl///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [InstructionsPoolImpl](-instructions-pool-impl/index.md)(**registers**: [Registers](-registers/index.md), **mmu**: [Mmu](../gameboy.memory/-mmu/index.md)) : [InstructionsPool](-instructions-pool/index.md)  <br><br><br>
| <a name="gameboy.cpu/RegisterID///PointingToDeclaration/"></a>[RegisterID](-register-i-d/index.md)| <a name="gameboy.cpu/RegisterID///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>enum [RegisterID](-register-i-d/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)<[RegisterID](-register-i-d/index.md)>   <br><br><br>
| <a name="gameboy.cpu/Registers///PointingToDeclaration/"></a>[Registers](-registers/index.md)| <a name="gameboy.cpu/Registers///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract class [Registers](-registers/index.md)  <br>More info  <br>Represents the registers of the gameboy CPU8-bit registers: A, F, B, C, D, E, H, L 16-bit registers: SP, PCOn construction [reset](-registers/reset.md) is called to set the registers to the initial values.  <br><br><br>
| <a name="gameboy.cpu/RegistersCGB///PointingToDeclaration/"></a>[RegistersCGB](-registers-c-g-b/index.md)| <a name="gameboy.cpu/RegistersCGB///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [RegistersCGB](-registers-c-g-b/index.md) : [Registers](-registers/index.md)  <br><br><br>
| <a name="gameboy.cpu/RegistersDMG///PointingToDeclaration/"></a>[RegistersDMG](-registers-d-m-g/index.md)| <a name="gameboy.cpu/RegistersDMG///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [RegistersDMG](-registers-d-m-g/index.md) : [Registers](-registers/index.md)  <br><br><br>

