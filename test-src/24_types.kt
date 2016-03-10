fun main(args: Array<String>) {
  // Defined types
  val double: Double = 64.64
  val float: Float = 32.32f
  val long: Long = 64
  val int: Int = 32
  val short: Short = 16
  val byte: Byte = 8

  // Auto types
  val double2 = 64.64
  val float2 = 32.32f
  val long2 = 64L
  val int2 = 32


  if (double != double2) {
    println("double error")
  }
  if (float != float2) {
    println("float error")
  }
  if (long != long2) {
    println("long error")
  }
  if (int != int2) {
    println("int error")
  }

  val sum = double + float + long + int + short + byte + double2 + float2 + long2 + int2

  if (Math.abs(sum - 409.9199993896484) > 0.01) {
    println("sum error")
  }

  if (Math.max(100L, long) != 100L) {
    println("max error")
  }

  if (Math.min(32f, float) != 32f) {
    println("min error")
  }

  print("finished")
}
