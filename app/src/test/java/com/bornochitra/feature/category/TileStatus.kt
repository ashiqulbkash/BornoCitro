package com.bornochitra.feature.category

import com.bornochitra.core.model.LearningState

/** The part of a [CategoryTileItem] a category screen's own test checks: which exercise, and its status. */
data class TileStatus(
    val id: String,
    val title: String,
    val learningState: LearningState = LearningState.NOT_STARTED,
    val attemptCount: Int = 0,
)

fun CategoryTileItem.status() = TileStatus(id = id, title = title, learningState = learningState, attemptCount = attemptCount)

fun CategoryGridState.statuses() = items.map { it.status() }
