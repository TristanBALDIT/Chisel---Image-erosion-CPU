import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(16.W))
    val ALU_op = Output(UInt(4.W))
    val REG_aSel = Output(UInt(4.W))
    val REG_bSEl = Output(UInt(4.W))
    val REG_writeEnable = Output(Bool())
  })

  //Implement this module here

}