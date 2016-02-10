# Kotlift #

Kotlift is the first source-to-source language transpiler from Kotlin to Swift.

It is not intended to support the full Kotlin or Swift language, but most of the generated Swift code will be valid. Neither functions from the Kotlin stdlib are support, nor framework interfacing code from Android or iOS/Cocoa.

Kotlift uses lots of regular expressions and and a simple structure tree.

To use Kotlift for your own project, a file called replacementFile.json can be edited to customize replacements (like `.toString() `to `.mySwiftyToStringFunction()`).

Supported versions: Kotlin 1.0.0-rc-1036, Swift 2.1

### Supported features ###

The following language features are currently transpiled, but some edge cases might produce invalid Swift code.

* Variables, arrays and lists
* Functions with parameters
* For and while loops
* String interpolation
* Main function calling (in swift playground)
* Custom rewrites (comment the preceding line with `// SWIFT: this.will(be.replaced)` to replace the following line)
* Basic null safety
* Basic try-catch
* Casting
* Ranges and iteration
* When / switch case
* Classes with none or one constructor
* Inheritance
* Data classes (constructor and description will be generated)
* Generics
* Extension functions
* Companion objects / static class properties

### Unsupported features ###

The following features are currently unsupported, but are ordered by likelihood to be implemented soon:

* Computed properties
* Visibility modifiers
* Force optional unwrapping
* Lambdas / closures
* Maps / dictionaries and sets
* Enums
* (Data) classes with opening & closing brackets in same line or without brackets at all
* Line-wrapped function and class definitions
* Unnamed constructor parameter
* ...

### Dependencies ###

* Kotlin 1.0.0-rc-1036
* Jackson jackson-module-kotlin:2.7.1

### Contribution guidelines ###

For every transpiled language feature there is a source Kotlin test file and a destination Swift test file. Calling Kotlift with a fourth argument not only transpiles all test files, but also compares them for any differences.

If you add any new features, please add a Kotlin and Swift test file. Pull requests are welcome.

Please contact valentin.slawicek@moshbit.com for any inquiries.