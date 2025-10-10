import chisel3._
import chisel3.util._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val aSel = Input(UInt(4.W))
    val bSel = Input(UInt(4.W))
    val writeData = Input(UInt(32.W))
    val writeSel = Input(UInt(4.W))
    val writeEnable = Input(Bool())
    val a = Output(UInt(32.W))
    val b = Output(UInt(32.W))
  })

  // Vector of 8 registers
  val regs = RegInit(VecInit(Seq.fill(16)(0.U(32.W))))

  when(io.writeEnable) {
    when(io.writeSel < 16.U) {
      regs(io.writeSel) := io.writeData
    }
  }

  // Read ports
  io.a := Mux(io.aSel < 16.U, regs(io.aSel), 0.U)
  io.b := Mux(io.bSel < 16.U, regs(io.bSel), 0.U)
}