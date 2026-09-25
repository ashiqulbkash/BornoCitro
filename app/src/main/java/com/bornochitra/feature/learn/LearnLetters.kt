package com.bornochitra.feature.learn

/**
 * A letter and the word the child learns it with. The words are content in the letter's own language,
 * spoken in that language whatever the app's language is, so they live here and not in the strings.
 */
data class LearnLetter(val letter: String, val word: String)

/** What the explanation button says: "A for apple". */
val LearnLetter.explanation: String
    get() = "$letter for $word"

/** The English letters A to Z with their words (plan.md Step 12). */
val englishLearnLetters: List<LearnLetter> = listOf(
    LearnLetter("A", "apple"),
    LearnLetter("B", "ball"),
    LearnLetter("C", "cat"),
    LearnLetter("D", "dog"),
    LearnLetter("E", "egg"),
    LearnLetter("F", "fish"),
    LearnLetter("G", "goat"),
    LearnLetter("H", "hat"),
    LearnLetter("I", "ice cream"),
    LearnLetter("J", "jug"),
    LearnLetter("K", "kite"),
    LearnLetter("L", "lion"),
    LearnLetter("M", "mango"),
    LearnLetter("N", "nest"),
    LearnLetter("O", "orange"),
    LearnLetter("P", "parrot"),
    LearnLetter("Q", "queen"),
    LearnLetter("R", "rabbit"),
    LearnLetter("S", "sun"),
    LearnLetter("T", "tiger"),
    LearnLetter("U", "umbrella"),
    LearnLetter("V", "van"),
    LearnLetter("W", "watch"),
    LearnLetter("X", "xylophone"),
    LearnLetter("Y", "yak"),
    LearnLetter("Z", "zebra"),
)
