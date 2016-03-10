func main(args: [String]) {
  // Defined types
  let double: Double = 64.64
  let float: Double = 32.32
  let long: Int64 = 64
  let int: Int32 = 32
  let short: Int16 = 16
  let byte: Int8 = 8

  // Auto types
  let double2 = 64.64
  let float2 = 32.32
  let long2 = 64
  let int2 = 32


  if double != double2 {
    print("double error")
  }
  if float != float2 {
    print("float error")
  }
  if long != long2 {
    print("long error")
  }
  if int != int2 {
    print("int error")
  }

  let sum = double + float + long + int + short + byte + double2 + float2 + long2 + int2

  if abs(sum - 409.9199993896484) > 0.01 {
    print("sum error")
  }

  if max(100, long) != 100 {
    print("max error")
  }

  if min(32, float) != 32 {
    print("min error")
  }

  print("finished")
}

main([])
