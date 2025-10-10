import chisel3._
import chisel3.util._

object ALUOps {
  val add :: sub :: eq :: and :: xor :: passA :: passB :: Nil = Enum(7)
}

class ALU extends Module {
  val io = IO(new Bundle {
    val operand_1 = Input(UInt(32.W))
    val operand_2 = Input(UInt(32.W))
    val op = Input(UInt(3.W))
    val result = Output(UInt(32.W))
    val comparison_result = Output(Bool())
  })

  io.result := 0.U
  io.comparison_result := 0.U

  switch(io.op){
    is(ALUOps.add){
      io.result := io.operand_1 + io.operand_2
    }

    is(ALUOps.eq){
      io.comparison_result := (io.operand_1 === io.operand_1)
    }

    is(ALUOps.sub){
      io.result := io.operand_1 - io.operand_2
    }

    is(ALUOps.and){
      io.result := io.operand_1 & io.operand_2
    }

    is(ALUOps.xor){
      io.result := io.operand_1 ^ io.operand_2
    }

    is(ALUOps.passA){
      io.result := io.operand_1
    }

    is(ALUOps.passB){
      io.result := io.operand_2
    }
  }
}