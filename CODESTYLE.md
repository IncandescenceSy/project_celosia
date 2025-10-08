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
* All class fields must be private
* Use this when calling a method, and only use this when necessary on fields
* Favor using fields over calling methods
* Use no-arg Builder methods for booleans
  * For example, if isPierce defaults false, instead of isPierce(boolean), create pierce()
* Do not add unused methods (unless they're for modders)
* Setters before getters (only for consistency, not because it's a better order)
* Early return whenever it can make code cleaner
* Do not have >1 nested classes
* Explicitly annotate Overrides, Nullability, FunctionalInterfaces, etc
* Do not use Optional<T> unless you have a very good reason to. It's much clunkier than @Nullable and doesn't actually have improved null safety
* When you need to invert a number, multiply it by -1 instead of prefixing it with - (for readability)
* Split big logic into separate functions
* Comment when necessary, but don't over-comment self-explanatory code
* In expressions, use brackets to improve clarity even when not strictly necessary
  * For example, instead of 50 + i * 10, write 50 + (i * 10) 
* For loop iterators are named i, j, k, etc unless another name would improve clarity
* Use standard keyword order (public static type)
* Multi-line logic statements must have {}. Inlined logic statements must not have {}
  * In most cases, inlined logic statements should be avoided entirely
  * An if followed by an else must have {}, and the else must be set against the }, unless there is a comment above the else
* Only write strings directly in code if they will only ever appear in 1 place, or if getting them wrong causes a crash that cannot go unnoticed
* Don't use Gdx.Array unless you have a really good reason
  * It's super jank and unnecessary. It allegedly slightly improves performance in most areas, but you'd need some massive Lists before that starts mattering
  * In unordered mode, non-last removal is much faster than for an ArrayList. But again, this only matters with massive Lists. And even in that case, you probably want a Set. Also, implementing an unordered remove for a regular List is 2 lines
  * It causes a lot of code compatibility issues and bespoke errors that may not be noticed until much later, and even considering it in the first place instead of instantly defaulting to ArrayList requires you to spend extra time thinking about it
* General coding conventions