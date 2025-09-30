This is mostly just written as a reminder for myself, since I doubt anyone but me will ever contribute to this

It is strongly recommended to use Intellij IDEA

# Auto-enforced via check
* spotlessCheck (can be ignored, just run spotlessApply)
* [checkStyleMain](core/checkstyle.xml)
  * Tab width of 4
  * No switch case fallthrough
  * No star (*) imports
  * Constant names must be SCREAMING_SNAKE_CASE
  * No empty blocks (except default interface methods)
  * Do not use var
* Tests
  * There aren't any yet

# Manually enforced
* Standard naming convention
  * UpperCamelCase classes
  * lowerCamelCase fields and methods
  * Do not prefix interface names with I
* Global constants must be final
* Only use this when necessary
* Use no-arg Builder methods for booleans
  * For example, if isPierce defaults true, instead of isPierce(boolean), create pierce()
* Do not add unused methods (unless they're for modders)
* Setters before getters (only for consistency, not because it's a better order)
* Early return whenever it can make code cleaner
* Do not have >1 nested classes
* Split big logic into separate functions
* Comment when necessary, but don't over-comment self-explanatory code
* Logic statements must have {}
  * I'd allow bracketless if inlined, but spotless forces a newline. That can be turned off, but I couldn't figure out how
* Only write strings directly in code if they will only ever appear in 1 place, or if getting them wrong causes a crash that cannot go unnoticed
* General coding conventions