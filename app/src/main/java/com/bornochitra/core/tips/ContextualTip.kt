package com.bornochitra.core.tips

/**
 * A short hint shown at a moment it helps (plan.md section 43). Every tip is positive and
 * age-appropriate, and only one is ever shown at a time so the child is not overwhelmed.
 *
 * The wording has no reference to letters, so the same tips serve drawings too.
 */
enum class ContextualTip(val message: String) {
    /** Before the first finished attempt at an exercise. */
    FIRST_ATTEMPT("Try tracing slowly."),

    /** A stroke was lifted before it was traced well enough. */
    MISSED_STROKE("Try again — follow the dots closely"),

    /** The same stroke has been missed several times in a row. */
    REPEATED_MISSES("Keep your finger close to the dots — you can do it!"),

    /** Several recent attempts at this exercise fell short of the bar. */
    REPEATED_LOW_SCORES("Try going a little slower — steady fingers stay on the dots."),

    /** An advanced exercise was finished. */
    DIFFICULT_COMPLETED("That was a tricky one — well done for finishing it!"),
}
