public class Lock {
  func lock() {
    print("  locked")
  }
  func unlock() {
    print("  unlocked")
  }
  init() {
  }
}

func lock<T>(lock: Lock, body: () -> T) -> T {
  lock.lock()
  let x = body()
  lock.unlock()
  return x
}

func main(args: [String]) {
  let lockObj = Lock()

  // Simple lock
  print("before lock")
  lock(lockObj) {
    print("    currently locked")
  }
  print("after lock")
}

main([])
