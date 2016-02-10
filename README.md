# README #

Kotlift is a language transpiler from Kotlin to Swift.

It is not intended to support the full Kotlin or Swift language, but most of the generated Swift code will be valid. Neither functions from the Kotlin stdlib are support, nor framework interfacing code from Android or iOS/Cocoa.

To use Kotlift for your own project, a file called replacementFile.json can be edited to support custom replacements (like ".toString()" to ".mySwiftyToStringFunction()").

### Contribution guidelines ###

For every transpiled language feature there is a source Kotlin test file and a destination Swift test file. Calling Kotlift with a fourth argument not only tranpiles all test files, but also compares them for any differences. 

If you add any new features, please add a Kotlin and Swift test file. Pull requests are welcome.

### Who do I talk to? ###

Please contact valentin.slawicek@moshbit.com for any inquiries.