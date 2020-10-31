---
title: Instruction -
---
//[gameboy-lib](../../index.md)/[gameboy.cpu.instructions](../index.md)/[Instruction](index.md)



# Instruction  
 [jvm] abstract class [Instruction](index.md)(**registers**: [Registers](../../gameboy.cpu/-registers/index.md), **mmu**: [Mmu](../../gameboy.memory/-mmu/index.md))   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open operator fun [equals](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [hashCode](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="gameboy.cpu.instructions/Instruction/isExecuting/#/PointingToDeclaration/"></a>[isExecuting](is-executing.md)| <a name="gameboy.cpu.instructions/Instruction/isExecuting/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [isExecuting](is-executing.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="gameboy.cpu.instructions/Instruction/reset/#/PointingToDeclaration/"></a>[reset](reset.md)| <a name="gameboy.cpu.instructions/Instruction/reset/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [reset](reset.md)()  <br><br><br>
| <a name="gameboy.cpu.instructions/Instruction/tick/#/PointingToDeclaration/"></a>[tick](tick.md)| <a name="gameboy.cpu.instructions/Instruction/tick/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>abstract fun [tick](tick.md)()  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>open fun [toString](../../gameboy.utils/-log/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F456262920)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="gameboy.cpu.instructions/Instruction/mmu/#/PointingToDeclaration/"></a>[mmu](mmu.md)| <a name="gameboy.cpu.instructions/Instruction/mmu/#/PointingToDeclaration/"></a> [jvm] val [mmu](mmu.md): [Mmu](../../gameboy.memory/-mmu/index.md)   <br>
| <a name="gameboy.cpu.instructions/Instruction/registers/#/PointingToDeclaration/"></a>[registers](registers.md)| <a name="gameboy.cpu.instructions/Instruction/registers/#/PointingToDeclaration/"></a> [jvm] val [registers](registers.md): [Registers](../../gameboy.cpu/-registers/index.md)   <br>
| <a name="gameboy.cpu.instructions/Instruction/totalCycles/#/PointingToDeclaration/"></a>[totalCycles](total-cycles.md)| <a name="gameboy.cpu.instructions/Instruction/totalCycles/#/PointingToDeclaration/"></a> [jvm] abstract val [totalCycles](total-cycles.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)   <br>


## Inheritors  
  
|  Name| 
|---|
| <a name="gameboy.cpu.instructions.alu/ADC///PointingToDeclaration/"></a>[ADC](../../gameboy.cpu.instructions.alu/-a-d-c/index.md)
| <a name="gameboy.cpu.instructions.alu/ADD///PointingToDeclaration/"></a>[ADD](../../gameboy.cpu.instructions.alu/-a-d-d/index.md)
| <a name="gameboy.cpu.instructions.alu/AND///PointingToDeclaration/"></a>[AND](../../gameboy.cpu.instructions.alu/-a-n-d/index.md)
| <a name="gameboy.cpu.instructions.alu/CP///PointingToDeclaration/"></a>[CP](../../gameboy.cpu.instructions.alu/-c-p/index.md)
| <a name="gameboy.cpu.instructions.alu/DEC///PointingToDeclaration/"></a>[DEC](../../gameboy.cpu.instructions.alu/-d-e-c/index.md)
| <a name="gameboy.cpu.instructions.alu/DEC_rr///PointingToDeclaration/"></a>[DEC_rr](../../gameboy.cpu.instructions.alu/-d-e-c_rr/index.md)
| <a name="gameboy.cpu.instructions.alu/INC///PointingToDeclaration/"></a>[INC](../../gameboy.cpu.instructions.alu/-i-n-c/index.md)
| <a name="gameboy.cpu.instructions.alu/INC_rr///PointingToDeclaration/"></a>[INC_rr](../../gameboy.cpu.instructions.alu/-i-n-c_rr/index.md)
| <a name="gameboy.cpu.instructions.alu/OR///PointingToDeclaration/"></a>[OR](../../gameboy.cpu.instructions.alu/-o-r/index.md)
| <a name="gameboy.cpu.instructions.alu/SBC///PointingToDeclaration/"></a>[SBC](../../gameboy.cpu.instructions.alu/-s-b-c/index.md)
| <a name="gameboy.cpu.instructions.alu/SUB///PointingToDeclaration/"></a>[SUB](../../gameboy.cpu.instructions.alu/-s-u-b/index.md)
| <a name="gameboy.cpu.instructions.alu/XOR///PointingToDeclaration/"></a>[XOR](../../gameboy.cpu.instructions.alu/-x-o-r/index.md)
| <a name="gameboy.cpu.instructions.bit/BIT///PointingToDeclaration/"></a>[BIT](../../gameboy.cpu.instructions.bit/-b-i-t/index.md)
| <a name="gameboy.cpu.instructions.bit/RES_HL///PointingToDeclaration/"></a>[RES_HL](../../gameboy.cpu.instructions.bit/-r-e-s_-h-l/index.md)
| <a name="gameboy.cpu.instructions.bit/RES_r///PointingToDeclaration/"></a>[RES_r](../../gameboy.cpu.instructions.bit/-r-e-s_r/index.md)
| <a name="gameboy.cpu.instructions.bit/SET_HL///PointingToDeclaration/"></a>[SET_HL](../../gameboy.cpu.instructions.bit/-s-e-t_-h-l/index.md)
| <a name="gameboy.cpu.instructions.bit/SET_r///PointingToDeclaration/"></a>[SET_r](../../gameboy.cpu.instructions.bit/-s-e-t_r/index.md)
| <a name="gameboy.cpu.instructions.calls/CALL_cc_nn///PointingToDeclaration/"></a>[CALL_cc_nn](../../gameboy.cpu.instructions.calls/-c-a-l-l_cc_nn/index.md)
| <a name="gameboy.cpu.instructions.calls/CALL_nn///PointingToDeclaration/"></a>[CALL_nn](../../gameboy.cpu.instructions.calls/-c-a-l-l_nn/index.md)
| <a name="gameboy.cpu.instructions.jumps/JP_HL///PointingToDeclaration/"></a>[JP_HL](../../gameboy.cpu.instructions.jumps/-j-p_-h-l/index.md)
| <a name="gameboy.cpu.instructions.jumps/JP_cc_nn///PointingToDeclaration/"></a>[JP_cc_nn](../../gameboy.cpu.instructions.jumps/-j-p_cc_nn/index.md)
| <a name="gameboy.cpu.instructions.jumps/JP_nn///PointingToDeclaration/"></a>[JP_nn](../../gameboy.cpu.instructions.jumps/-j-p_nn/index.md)
| <a name="gameboy.cpu.instructions.jumps/JR_cc_n///PointingToDeclaration/"></a>[JR_cc_n](../../gameboy.cpu.instructions.jumps/-j-r_cc_n/index.md)
| <a name="gameboy.cpu.instructions.jumps/JR_n///PointingToDeclaration/"></a>[JR_n](../../gameboy.cpu.instructions.jumps/-j-r_n/index.md)
| <a name="gameboy.cpu.instructions.loads/LDD_A_HL///PointingToDeclaration/"></a>[LDD_A_HL](../../gameboy.cpu.instructions.loads/-l-d-d_-a_-h-l/index.md)
| <a name="gameboy.cpu.instructions.loads/LDD_HL_A///PointingToDeclaration/"></a>[LDD_HL_A](../../gameboy.cpu.instructions.loads/-l-d-d_-h-l_-a/index.md)
| <a name="gameboy.cpu.instructions.loads/LDH_A_C///PointingToDeclaration/"></a>[LDH_A_C](../../gameboy.cpu.instructions.loads/-l-d-h_-a_-c/index.md)
| <a name="gameboy.cpu.instructions.loads/LDH_A_n///PointingToDeclaration/"></a>[LDH_A_n](../../gameboy.cpu.instructions.loads/-l-d-h_-a_n/index.md)
| <a name="gameboy.cpu.instructions.loads/LDH_C_A///PointingToDeclaration/"></a>[LDH_C_A](../../gameboy.cpu.instructions.loads/-l-d-h_-c_-a/index.md)
| <a name="gameboy.cpu.instructions.loads/LDH_n_A///PointingToDeclaration/"></a>[LDH_n_A](../../gameboy.cpu.instructions.loads/-l-d-h_n_-a/index.md)
| <a name="gameboy.cpu.instructions.loads/LDI_A_HL///PointingToDeclaration/"></a>[LDI_A_HL](../../gameboy.cpu.instructions.loads/-l-d-i_-a_-h-l/index.md)
| <a name="gameboy.cpu.instructions.loads/LDI_HL_A///PointingToDeclaration/"></a>[LDI_HL_A](../../gameboy.cpu.instructions.loads/-l-d-i_-h-l_-a/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_A_nn///PointingToDeclaration/"></a>[LD_A_nn](../../gameboy.cpu.instructions.loads/-l-d_-a_nn/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_A_rr///PointingToDeclaration/"></a>[LD_A_rr](../../gameboy.cpu.instructions.loads/-l-d_-a_rr/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_HL_SPn///PointingToDeclaration/"></a>[LD_HL_SPn](../../gameboy.cpu.instructions.loads/-l-d_-h-l_-s-pn/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_HL_n///PointingToDeclaration/"></a>[LD_HL_n](../../gameboy.cpu.instructions.loads/-l-d_-h-l_n/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_SP_HL///PointingToDeclaration/"></a>[LD_SP_HL](../../gameboy.cpu.instructions.loads/-l-d_-s-p_-h-l/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_nn_A///PointingToDeclaration/"></a>[LD_nn_A](../../gameboy.cpu.instructions.loads/-l-d_nn_-a/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_nn_SP///PointingToDeclaration/"></a>[LD_nn_SP](../../gameboy.cpu.instructions.loads/-l-d_nn_-s-p/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_r1_r2///PointingToDeclaration/"></a>[LD_r1_r2](../../gameboy.cpu.instructions.loads/-l-d_r1_r2/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_r_HL///PointingToDeclaration/"></a>[LD_r_HL](../../gameboy.cpu.instructions.loads/-l-d_r_-h-l/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_r_n///PointingToDeclaration/"></a>[LD_r_n](../../gameboy.cpu.instructions.loads/-l-d_r_n/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_rr_nn///PointingToDeclaration/"></a>[LD_rr_nn](../../gameboy.cpu.instructions.loads/-l-d_rr_nn/index.md)
| <a name="gameboy.cpu.instructions.loads/LD_rr_r///PointingToDeclaration/"></a>[LD_rr_r](../../gameboy.cpu.instructions.loads/-l-d_rr_r/index.md)
| <a name="gameboy.cpu.instructions.loads/POP_nn///PointingToDeclaration/"></a>[POP_nn](../../gameboy.cpu.instructions.loads/-p-o-p_nn/index.md)
| <a name="gameboy.cpu.instructions.loads/PUSH_nn///PointingToDeclaration/"></a>[PUSH_nn](../../gameboy.cpu.instructions.loads/-p-u-s-h_nn/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/CCF///PointingToDeclaration/"></a>[CCF](../../gameboy.cpu.instructions.miscellaneous/-c-c-f/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/CPL///PointingToDeclaration/"></a>[CPL](../../gameboy.cpu.instructions.miscellaneous/-c-p-l/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/DAA///PointingToDeclaration/"></a>[DAA](../../gameboy.cpu.instructions.miscellaneous/-d-a-a/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/DI///PointingToDeclaration/"></a>[DI](../../gameboy.cpu.instructions.miscellaneous/-d-i/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/EI///PointingToDeclaration/"></a>[EI](../../gameboy.cpu.instructions.miscellaneous/-e-i/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/HALT///PointingToDeclaration/"></a>[HALT](../../gameboy.cpu.instructions.miscellaneous/-h-a-l-t/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/NOP///PointingToDeclaration/"></a>[NOP](../../gameboy.cpu.instructions.miscellaneous/-n-o-p/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/SCF///PointingToDeclaration/"></a>[SCF](../../gameboy.cpu.instructions.miscellaneous/-s-c-f/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/STOP///PointingToDeclaration/"></a>[STOP](../../gameboy.cpu.instructions.miscellaneous/-s-t-o-p/index.md)
| <a name="gameboy.cpu.instructions.miscellaneous/SWAP///PointingToDeclaration/"></a>[SWAP](../../gameboy.cpu.instructions.miscellaneous/-s-w-a-p/index.md)
| <a name="gameboy.cpu.instructions.restarts/RST_n///PointingToDeclaration/"></a>[RST_n](../../gameboy.cpu.instructions.restarts/-r-s-t_n/index.md)
| <a name="gameboy.cpu.instructions.returns/RET///PointingToDeclaration/"></a>[RET](../../gameboy.cpu.instructions.returns/-r-e-t/index.md)
| <a name="gameboy.cpu.instructions.returns/RETI///PointingToDeclaration/"></a>[RETI](../../gameboy.cpu.instructions.returns/-r-e-t-i/index.md)
| <a name="gameboy.cpu.instructions.returns/RET_cc///PointingToDeclaration/"></a>[RET_cc](../../gameboy.cpu.instructions.returns/-r-e-t_cc/index.md)
| <a name="gameboy.cpu.instructions.rotates/RL///PointingToDeclaration/"></a>[RL](../../gameboy.cpu.instructions.rotates/-r-l/index.md)
| <a name="gameboy.cpu.instructions.rotates/RLC///PointingToDeclaration/"></a>[RLC](../../gameboy.cpu.instructions.rotates/-r-l-c/index.md)
| <a name="gameboy.cpu.instructions.rotates/RR///PointingToDeclaration/"></a>[RR](../../gameboy.cpu.instructions.rotates/-r-r/index.md)
| <a name="gameboy.cpu.instructions.rotates/RRC///PointingToDeclaration/"></a>[RRC](../../gameboy.cpu.instructions.rotates/-r-r-c/index.md)
| <a name="gameboy.cpu.instructions.shifts/SLA///PointingToDeclaration/"></a>[SLA](../../gameboy.cpu.instructions.shifts/-s-l-a/index.md)
| <a name="gameboy.cpu.instructions.shifts/SRA///PointingToDeclaration/"></a>[SRA](../../gameboy.cpu.instructions.shifts/-s-r-a/index.md)
| <a name="gameboy.cpu.instructions.shifts/SRL///PointingToDeclaration/"></a>[SRL](../../gameboy.cpu.instructions.shifts/-s-r-l/index.md)

