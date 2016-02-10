extension Array {
  var size: Int { return self.count }
  mutating func add(element: Element) {
    self.append(element)
  }
}

extension String {
  var length: Int { return characters.count }
}

enum Exception: ErrorType {
  case NumberFormat
}

class Integer {
  static func parseInt(str: String) throws -> Int {
    if let intVal = Int(str) {
      return intVal
    }
    throw Exception.NumberFormat
  }
}

class System {
  static func currentTimeMillis() -> Int64 {
    return (Int64) (NSDate().timeIntervalSince1970 * 100.0)
  }
}

func / (lhs: Double, rhs: Int) -> Double {
  return Double(lhs) / Double(rhs)
}

func / (lhs: Int, rhs: Double) -> Double {
  return Double(lhs) / Double(rhs)
}

protocol Addable {}
extension Int: Addable {}
extension Double: Addable {}