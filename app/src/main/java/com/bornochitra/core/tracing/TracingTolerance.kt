package com.bornochitra.core.tracing

/**
 * Configurable tolerance for how far a finger may stray from the expected path — measured in the
 * same normalized 0..100 canvas units as [com.bornochitra.core.model.Point] — and still count as
 * "on track". Kept deliberately forgiving so young children are not penalized for imprecise
 * fingers, per plan.md Step 10.4.
 */
data class TracingTolerance(val maxDistance: Float = DEFAULT_MAX_DISTANCE) {

    init {
        require(maxDistance > 0f) { "maxDistance must be positive" }
    }

    fun isWithinTolerance(distance: Float): Boolean = distance <= maxDistance

    companion object {
        /** Roughly 3x the default dot radius ([DottedPathStyle]), forgiving enough for a child's finger. */
        const val DEFAULT_MAX_DISTANCE = 8f
    }
}
