package com.bornochitra.core.tips

/**
 * A short hint shown at a moment it helps (plan.md section 43). Every tip is positive and
 * age-appropriate, and only one is ever shown at a time so the child is not overwhelmed.
 *
 * The wording lives in string resources (`tip_*`) and has no reference to letters, so the same tips
 * serve drawings too.
 */
enum class ContextualTip {
    /** Before the first finished attempt at an exercise. */
    FIRST_ATTEMPT,

    /** The finger was lifted with part of the shape still untraced. */
    UNFINISHED_TRACE,

    /** Several recent attempts at this exercise fell short of the bar. */
    REPEATED_LOW_SCORES,

    /** An advanced exercise was finished. */
    DIFFICULT_COMPLETED,
}
