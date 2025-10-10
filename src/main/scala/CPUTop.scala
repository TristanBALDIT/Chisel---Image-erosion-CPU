import chisel3._
import chisel3.util._

class CPUTop extends Module {
  val io = IO(new Bundle {
    val done = Output(Bool ())
    val run = Input(Bool ())
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerDataMemEnable = Input(Bool ())
    val testerDataMemAddress = Input(UInt (16.W))
    val testerDataMemDataRead = Output(UInt (32.W))
    val testerDataMemWriteEnable = Input(Bool ())
    val testerDataMemDataWrite = Input(UInt (32.W))
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerProgMemEnable = Input(Bool ())
    val testerProgMemAddress = Input(UInt (16.W))
    val testerProgMemDataRead = Output(UInt (32.W))
    val testerProgMemWriteEnable = Input(Bool ())
    val testerProgMemDataWrite = Input(UInt (32.W))
  })

  //Creating components
  val programCounter = Module(new ProgramCounter())
  val dataMemory = Module(new DataMemory())
  val programMemory = Module(new ProgramMemory())
  val registerFile = Module(new RegisterFile())
  val controlUnit = Module(new ControlUnit())
  val alu = Module(new ALU())

  //Connecting the modules
  //programCounter.io.run := io.run
  programCounter.io.jump := alu.io.comparison_result
  programCounter.io.programCounterJump := programCounter.io.programCounter + controlUnit.io.imm
  //programMemory.io.address := programCounter.io.programCounter


  alu.io.op := controlUnit.io
  alu.io.operand_1 := Mux(controlUnit.io.AorPC, programCounter.io.programCounter, registerFile.io.a)
  alu.io.operand_2 := Mux(controlUnit.io.BorIMM, controlUnit.io.imm.asSInt.pad(16).asUInt, registerFile.io.b)

  dataMemory.io.address :=  alu.io.result
  dataMemory.io.writeEnable := controlUnit.io.DATA_writeEnable
  dataMemory.io.dataWrite := registerFile.io.b

  val muxResult = Mux(controlUnit.io.DATA_readEnable, dataMemory.io.dataRead, alu.io.result)

  registerFile.io.aSel := controlUnit.io.REG_aSel
  registerFile.io.bSel := controlUnit.io.REG_bSEl
  registerFile.io.writeSel := controlUnit.io.REG_writeSel
  registerFile.io.writeEnable := controlUnit.io.REG_writeEnable
  registerFile.io.writeData := muxResult

  //This signals are used by the tester for loading the program to the program memory, do not touch
  programMemory.io.testerAddress := io.testerProgMemAddress
  io.testerProgMemDataRead := programMemory.io.testerDataRead
  programMemory.io.testerDataWrite := io.testerProgMemDataWrite
  programMemory.io.testerEnable := io.testerProgMemEnable
  programMemory.io.testerWriteEnable := io.testerProgMemWriteEnable
  //This signals are used by the tester for loading and dumping the data memory content, do not touch
  dataMemory.io.testerAddress := io.testerDataMemAddress
  io.testerDataMemDataRead := dataMemory.io.testerDataRead
  dataMemory.io.testerDataWrite := io.testerDataMemDataWrite
  dataMemory.io.testerEnable := io.testerDataMemEnable
  dataMemory.io.testerWriteEnable := io.testerDataMemWriteEnable
}