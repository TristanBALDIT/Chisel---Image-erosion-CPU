import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(16.W))
    val ALU_op = Output(UInt(3.W))
    val REG_aSel = Output(UInt(4.W))
    val REG_bSEl = Output(UInt(4.W))
    val REG_writeSel = Output(UInt(4.W))
    val REG_writeEnable = Output(Bool())
    val PC_stop = Output(Bool())
    val DATA_dataRead = Output(Bool())
    val DATA_dataWrite = Output(Bool())
    val AorPC = Output(Bool())
    val BorIMM = Output(Bool())
    val imm = Output(UInt(12.W))
  })

  val op = io.opcode(7, 0)
  io.imm := io.opcode(31, 20)

  io.ALU_op := 0.U
  io.REG_aSel := io.opcode(15,12)
  io.REG_bSEl := io.opcode(19,16)
  io.REG_writeSel := io.opcode(11,8)
  io.REG_writeEnable := 0.U
  io.PC_stop := 0.U
  io.DATA_dataRead := 0.U
  io.DATA_dataWrite := 0.U
  io.AorPC := op(8)
  io.BorIMM := op(7)

  switch(op){
    // ADD
    is("b0_0_000001".U){
      io.ALU_op := ALUOps.add
      io.REG_writeEnable := 1.U
    }
    // ADDI
    is("b0_1_000001".U){
      io.ALU_op := ALUOps.add
      io.REG_writeEnable := 1.U
    }
    // LW   rd = MEM[rs1 + imm]
    is("b0_1_000010".U){
      io.ALU_op := ALUOps.add
      io.REG_writeEnable := 1.U
      io.DATA_dataRead := 1.U
    }
    // SW MEM[rs1 + imm] = rs2
    is("b0_1_00011".U){
      io.DATA_dataWrite := 1.U
    }
    // BEQ (rs1 == rs2) --> PC = PC + imm
    is("b1_1_000001".U){
      io.ALU_op := ALUOps.eq
    }
  }
  // B/S - TYPE
  // LW, SW, BEQ

  // R - TYPE
  // ADD

  // I-TYPE
  // ADDI


  //    Imm(12) Rs2(4) Rs1(4) RD(4) OPCODE(8)
}