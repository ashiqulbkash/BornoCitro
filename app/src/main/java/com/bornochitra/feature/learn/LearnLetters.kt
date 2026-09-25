package com.bornochitra.feature.learn

import com.bornochitra.core.locale.AppLanguage

/**
 * A letter and the word the child learns it with. The words are content in the letter's own language,
 * spoken in that language whatever the app's language is, so they live here and not in the strings.
 * [spoken] is what the letter button says, when saying [letter] alone does not work.
 */
data class LearnLetter(val letter: String, val word: String, val spoken: String = letter)

/** The letters taught in [language], with their words. */
fun learnLetters(language: AppLanguage): List<LearnLetter> = when (language) {
    AppLanguage.BANGLA -> banglaLearnLetters
    AppLanguage.ENGLISH -> englishLearnLetters
}

/** What the explanation button says in [language]: "A for apple", "অ তে অজগর". */
fun LearnLetter.explanation(language: AppLanguage): String = when (language) {
    AppLanguage.BANGLA -> "$letter তে $word"
    AppLanguage.ENGLISH -> "$letter for $word"
}

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

/**
 * The Bangla vowels and consonants with their words (plan.md Step 12), in the order of the tracing
 * lists. A letter no word starts with, such as ঙ or ং, is taught with a word it ends in, as in a
 * primer. ড় ঢ় য় are written as the letter plus nukta (U+09BC), the same way their exercise titles are.
 *
 * Some letters are spoken by their primer names (স্বরে অ, হ্রস্ব ই, দীর্ঘ ঈ, খণ্ড ত, দন্ত্য ন …).
 * Google's Bangla voice renders a lone অ, আ, ও or ৎ as near silence and ই, এ, উ very quietly, and says
 * ই/ঈ, উ/ঊ, জ/য, ণ/ন, শ/ষ/স, র/ড়/ঢ় and ঞ/য় identically. The names come out at full volume and tell
 * them apart. The other letters, র and ঞ among them once their look-alikes are named, are clear alone;
 * ঃ and ঁ are already read by their names.
 */
val banglaLearnLetters: List<LearnLetter> = listOf(
    LearnLetter("অ", "অজগর", spoken = "স্বরে অ"),
    LearnLetter("আ", "আম", spoken = "স্বরে আ"),
    LearnLetter("ই", "ইঁদুর", spoken = "হ্রস্ব ই"),
    LearnLetter("ঈ", "ঈগল", spoken = "দীর্ঘ ঈ"),
    LearnLetter("উ", "উট", spoken = "হ্রস্ব উ"),
    LearnLetter("ঊ", "ঊষা", spoken = "দীর্ঘ ঊ"),
    LearnLetter("ঋ", "ঋষি"),
    LearnLetter("এ", "একতারা", spoken = "স্বরে এ"),
    LearnLetter("ঐ", "ঐরাবত"),
    LearnLetter("ও", "ওল", spoken = "স্বরে ও"),
    LearnLetter("ঔ", "ঔষধ"),
    LearnLetter("ক", "কলা"),
    LearnLetter("খ", "খরগোশ"),
    LearnLetter("গ", "গরু"),
    LearnLetter("ঘ", "ঘড়ি"),
    LearnLetter("ঙ", "ব্যাঙ"),
    LearnLetter("চ", "চশমা"),
    LearnLetter("ছ", "ছাতা"),
    LearnLetter("জ", "জাহাজ", spoken = "বর্গীয় জ"),
    LearnLetter("ঝ", "ঝিনুক"),
    LearnLetter("ঞ", "মিঞা"),
    LearnLetter("ট", "টিয়া"),
    LearnLetter("ঠ", "ঠোঁট"),
    LearnLetter("ড", "ডালিম"),
    LearnLetter("ঢ", "ঢোল"),
    LearnLetter("ণ", "হরিণ", spoken = "মূর্ধন্য ণ"),
    LearnLetter("ত", "তরমুজ"),
    LearnLetter("থ", "থালা"),
    LearnLetter("দ", "দরজা"),
    LearnLetter("ধ", "ধান"),
    LearnLetter("ন", "নৌকা", spoken = "দন্ত্য ন"),
    LearnLetter("প", "পাখি"),
    LearnLetter("ফ", "ফুল"),
    LearnLetter("ব", "বই"),
    LearnLetter("ভ", "ভালুক"),
    LearnLetter("ম", "মাছ"),
    LearnLetter("য", "যব", spoken = "অন্তঃস্থ য"),
    LearnLetter("র", "রকেট"),
    LearnLetter("ল", "লাটিম"),
    LearnLetter("শ", "শাপলা", spoken = "তালব্য শ"),
    LearnLetter("ষ", "ষাঁড়", spoken = "মূর্ধন্য ষ"),
    LearnLetter("স", "সাপ", spoken = "দন্ত্য স"),
    LearnLetter("হ", "হাঁস"),
    LearnLetter("ড়", "পাহাড়", spoken = "ড-এ শূন্য ড়"),
    LearnLetter("ঢ়", "আষাঢ়", spoken = "ঢ-এ শূন্য ঢ়"),
    LearnLetter("য়", "ময়ূর", spoken = "য-এ শূন্য য়"),
    LearnLetter("ৎ", "শরৎ", spoken = "খণ্ড ত"),
    LearnLetter("ং", "রং"),
    LearnLetter("ঃ", "দুঃখ"),
    LearnLetter("ঁ", "চাঁদ"),
)
