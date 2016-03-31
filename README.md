# Kotlift #

![Kotlift: The first source-to-source language transpiler from Kotlin to Swift](banner.png)

Kotlift is the first source-to-source language transpiler from [Kotlin](https://kotlinlang.org/) to
[Swift](https://swift.org/).

Kotlift helps you to write business logic once, and reuse most of the code on iOS.
Porting an app from Android to iOS will be faster and less error-prone.
With Kotlift, Kotlin truly is the Swift of Android.

The program is not intended to support the full Kotlin or Swift language, but most of the generated Swift code will be
valid.
All framework interfacing code from Android or iOS/Cocoa is not supported.
Many functions from the Kotlin stdlib are the same in Swift, therefore less logic has to be rewritten.

Kotlift is already used in production by [Moshbit App Design & Development](http://moshbit.com) and will be updated to
new Kotlin and Swift versions.

Supported versions: Kotlin 1.0.0, Swift 2.1

### Supported features ###

The following language features are currently transpiled, but some edge cases might produce invalid Swift code.

* Variables, arrays and lists
* Basic types (String, Boolean, Double, Float, Long, Int, Short, Byte)
* Functions with parameters
* If and elvis operator
* For and while loops
* String interpolation
* Main function calling (in Swift playground)
* Custom rewrites (comment the preceding line with `// SWIFT: this.will(be.replaced)` to replace the following line)
* Null safety, safe calls, null coalescing
* Basic try-catch
* Casting
* Ranges and iteration
* When / switch case
* Classes with none or one constructor
* Inheritance
* Abstract classes
* Interfaces / protocols
* Data classes (constructor and description will be generated)
* Generics
* Extension functions
* Companion objects / static class properties
* Properties with getters and setters / backing fields / computed properties
* Visibility modifiers
* Basic lambdas / closures
* Maps / dictionaries
* Sets
* Very basic smart casts

See also the [testcases in Kotlin](/test-src) and the [generated Swift](/test-dest) files.

### Unsupported features ###

The following features are currently unsupported, but are ordered by likelihood to be implemented soon:

* Enums
* (Data) classes, functions or getters & setters with opening & closing brackets in same line or without brackets at all
* Line-wrapped function and class definitions
* Unnamed constructor parameter
* Unnamed function parameters (except the first one)
* Auto inferred function return types
* Finally / defer
* Full support for smart casts
* ...

### Usage ###

The repository contains an IntelliJ project. Usage of the precompiled jar:

`java -jar moshbit.kotlift.jar test-src dest replacementFile.json test-dest`

1. parameter: folder of Kotlin source files. (mandatory)
2. parameter: destination folder where Swift code should be written to. (mandatory)
3. parameter: replacement file, used for standard language replacements and may be customized. (mandatory)
4. parameter: Swift testcase folder. If given, all files in the destination folder are compared to the files in this
folder. (optional)

Kotlift uses various regular expressions and a simple structure tree.

For advanced Kotlift usage in your project, modify replacementFile.json to customize
replacements (such as `.toString() `to `.mySwiftyToStringFunction()`).

### Dependencies ###

* Kotlin 1.0.0

### Contribution guidelines ###

For every transpiled language feature there is a Kotlin test file in test-src and a Swift file in test-dest.
Executing Kotlift with a fourth argument not only transpiles all test files, but also checks them for any differences.

Pull requests are welcome. If you add new features, please add Kotlin and Swift test files.

Please contact valentin.slawicek@moshbit.com for any inquiries.