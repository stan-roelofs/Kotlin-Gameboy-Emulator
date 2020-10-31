---
title: gameboy.memory.cartridge -
---
//[gameboy-lib](../index.md)/[gameboy.memory.cartridge](index.md)



# Package gameboy.memory.cartridge  


## Types  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.memory.cartridge/Cartridge///PointingToDeclaration/"></a>[Cartridge](-cartridge/index.md)| <a name="gameboy.memory.cartridge/Cartridge///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [Cartridge](-cartridge/index.md)(**file**: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) : [Memory](../gameboy.memory/-memory/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/CartridgeType///PointingToDeclaration/"></a>[CartridgeType](-cartridge-type/index.md)| <a name="gameboy.memory.cartridge/CartridgeType///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>interface [CartridgeType](-cartridge-type/index.md) : [Memory](../gameboy.memory/-memory/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/MBC///PointingToDeclaration/"></a>[MBC](-m-b-c/index.md)| <a name="gameboy.memory.cartridge/MBC///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>interface [MBC](-m-b-c/index.md) : [CartridgeType](-cartridge-type/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/MBC1///PointingToDeclaration/"></a>[MBC1](-m-b-c1/index.md)| <a name="gameboy.memory.cartridge/MBC1///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [MBC1](-m-b-c1/index.md)(**romBanks**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **ramSize**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **hasBattery**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Memory](../gameboy.memory/-memory/index.md), [MBC](-m-b-c/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/MBC2///PointingToDeclaration/"></a>[MBC2](-m-b-c2/index.md)| <a name="gameboy.memory.cartridge/MBC2///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [MBC2](-m-b-c2/index.md)(**romBanks**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **hasBattery**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Memory](../gameboy.memory/-memory/index.md), [MBC](-m-b-c/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/MBC3///PointingToDeclaration/"></a>[MBC3](-m-b-c3/index.md)| <a name="gameboy.memory.cartridge/MBC3///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [MBC3](-m-b-c3/index.md)(**romBanks**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **ramSize**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **hasBattery**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **hasTimer**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Memory](../gameboy.memory/-memory/index.md), [MBC](-m-b-c/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/MBC5///PointingToDeclaration/"></a>[MBC5](-m-b-c5/index.md)| <a name="gameboy.memory.cartridge/MBC5///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [MBC5](-m-b-c5/index.md)(**romBanks**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **ramSize**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **hasBattery**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Memory](../gameboy.memory/-memory/index.md), [MBC](-m-b-c/index.md)  <br><br><br>
| <a name="gameboy.memory.cartridge/ROMONLY///PointingToDeclaration/"></a>[ROMONLY](-r-o-m-o-n-l-y/index.md)| <a name="gameboy.memory.cartridge/ROMONLY///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>class [ROMONLY](-r-o-m-o-n-l-y/index.md) : [Memory](../gameboy.memory/-memory/index.md), [CartridgeType](-cartridge-type/index.md)  <br><br><br>

