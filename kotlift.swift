import Foundation

extension Array {
  var size: Int { return self.count }
  mutating func add(element: Element) {
    self.append(element)
  }
  mutating func addAll(other: Array) {
    for element in other {
      self.append(element)
    }
  }
  func get(index: Int) -> Element {
    return self[index]
  }
  mutating func clear() {
    self.removeAll()
  }
}

extension Dictionary {
  var size: Int { return self.count }
  mutating func put(key: Key, _ value: Value) {
    self[key] = value
  }
  mutating func putAll(other: Dictionary) {
    for (key, value) in other {
      self.updateValue(value, forKey: key)
    }
  }
  func isEmpty() -> Bool {
    return self.isEmpty
  }
  mutating func remove(key: Key) -> Value? {
    return self.removeValueForKey(key)
  }
  mutating func clear() {
    self.removeAll()
  }
}

extension Set {
  var size: Int { return self.count }
  mutating func add(element: Element) {
    self.insert(element)
  }
  mutating func addAll(other: Set) {
    for element in other {
      self.insert(element)
    }
  }
  func isEmpty() -> Bool {
    return self.isEmpty
  }
  mutating func clear() {
    self.removeAll()
  }
}

extension String {
  var length: Int { return characters.count }
  func substring(startIndex: Int, _ endIndex: Int) -> String {
    let start = self.startIndex.advancedBy(startIndex)
    let end = self.startIndex.advancedBy(endIndex)
    return self.substringWithRange(Range<String.Index>(start: start, end: end))
  }
  func substring(startIndex: Int) -> String {
    return substring(startIndex, self.length)
  }
  func contains(string: String) -> Bool {
    return self.rangeOfString(string) != nil
  }
  func replace(target: String, _ withString: String) -> String {
    return self.stringByReplacingOccurrencesOfString(target,
                                                     withString: withString,
                                                     options: NSStringCompareOptions.LiteralSearch,
                                                     range: nil)
  }
  func isEmpty() -> Bool {
    return isEmpty
  }
  func split(p: String) -> [String] {
    return self.componentsSeparatedByString(p)
  }
}

protocol _StringType { }
extension String: _StringType { }
extension Array where Element: _StringType {
  func joinToString(separator separator: String = "") -> String {
    var retval = ""
    var first = true
    for rawObject in self {
      let element = rawObject as! String
      if (first) {
        retval += element
      } else {
        retval += separator + element
      }
      first = false
    }
    return retval
  }
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
    return (Int64) (NSDate().timeIntervalSince1970 * 1000.0)
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


func + (a: Int, b: Double) -> Double {
  return Double(a) + b
}
func + (a: Double, b: Int) -> Double {
  return Double(b) + a
}

func + (a: Int64, b: Double) -> Double {
  return Double(a) + b
}
func + (a: Double, b: Int64) -> Double {
  return Double(b) + a
}

func + (a: Int32, b: Double) -> Double {
  return Double(a) + b
}
func + (a: Double, b: Int32) -> Double {
  return Double(b) + a
}

func + (a: Int16, b: Double) -> Double {
  return Double(a) + b
}
func + (a: Double, b: Int16) -> Double {
  return Double(b) + a
}

func + (a: Int8, b: Double) -> Double {
  return Double(a) + b
}
func + (a: Double, b: Int8) -> Double {
  return Double(b) + a
}

func == (a: Int32, b: Int) -> Bool {
  return Int(a) == b
}
func == (a: Int, b: Int32) -> Bool {
  return Int(b) == a
}

func == (a: Int32, b: Int64) -> Bool {
  return Int64(a) == b
}
func == (a: Int64, b: Int32) -> Bool {
  return Int64(b) == a
}

func == (a: Int, b: Int64) -> Bool {
  return Int64(a) == b
}
func == (a: Int64, b: Int) -> Bool {
  return Int64(b) == a
}

func != (a: Int32, b: Int) -> Bool {
  return Int(a) != b
}
func != (a: Int, b: Int32) -> Bool {
  return Int(b) != a
}

func != (a: Int32, b: Int64) -> Bool {
  return Int64(a) != b
}
func != (a: Int64, b: Int32) -> Bool {
  return Int64(b) != a
}

func != (a: Int, b: Int64) -> Bool {
  return Int64(a) != b
}
func != (a: Int64, b: Int) -> Bool {
  return Int64(b) != a
}

// Nil Coalescing Operator for use with statements. E.g. try a ?! { throw Error() }
infix operator ?! { associativity right precedence 131 }
public func ?! <T> (optional: Optional<T>, throwsStatement: () throws -> Void) rethrows -> T {
  switch optional {
  case .Some(let value):
    return value
  case .None:
    try throwsStatement()
    return optional! // This line will never be executed when thowsStatement actually throws
  }
}
