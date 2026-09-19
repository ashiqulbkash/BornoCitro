package com.bornochitra.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bornochitra.core.content.ExerciseRepositoryImpl
import com.bornochitra.core.database.repository.ProgressRepositoryImpl
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Runs [ProgressRepositoryImpl] against a real Room database instead of fake DAOs. */
class ProgressRoomTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val databaseName = "progress-room-test.db"
    private lateinit var database: AppDatabase
    private lateinit var repository: ProgressRepositoryImpl

    @Before
    fun setUp() {
        context.deleteDatabase(databaseName)
        open()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    private fun open() {
        database = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
        repository = ProgressRepositoryImpl(
            exerciseProgressDao = database.exerciseProgressDao(),
            practiceSessionDao = database.practiceSessionDao(),
            exerciseRepository = ExerciseRepositoryImpl(),
            masteryRule = MasteryRule(),
        )
    }

    private fun result(
        exerciseId: String = "vowel-o",
        score: Float = 90f,
        completed: Boolean = true,
        at: Long = 1_000L,
    ) = PracticeResult(
        exerciseId = exerciseId,
        score = score,
        scoreLevel = if (score >= 90f) ScoreLevel.PERFECT else ScoreLevel.MEDIUM,
        completed = completed,
        durationMs = 4_000L,
        completedAtMs = at,
    )

    private fun progressOf(exerciseId: String) =
        runBlocking { repository.observeExerciseProgress(listOf(exerciseId)).first()[exerciseId] }

    @Test
    fun savedPracticeResult_canBeReadBackBySessionId() = runBlocking {
        val sessionId = repository.savePracticeResult(result(score = 76f))

        val saved = repository.getPracticeResult(sessionId)

        assertEquals(76f, saved?.score)
        assertEquals(ScoreLevel.MEDIUM, saved?.scoreLevel)
        assertEquals("vowel-o", saved?.exerciseId)
    }

    @Test
    fun attemptCount_incrementsAndBestScoreOnlyRises() = runBlocking {
        repository.savePracticeResult(result(score = 70f, at = 1L))
        repository.savePracticeResult(result(score = 95f, at = 2L))
        repository.savePracticeResult(result(score = 50f, at = 3L))

        val progress = progressOf("vowel-o")

        assertEquals(3, progress?.attemptCount)
        assertEquals(95f, progress?.bestScore)
        assertEquals(50f, progress?.lastScore)
    }

    @Test
    fun recentResults_comeBackNewestFirstForOneExercise() = runBlocking {
        repository.savePracticeResult(result(score = 60f, at = 1L))
        repository.savePracticeResult(result(exerciseId = "vowel-aa", score = 99f, at = 2L))
        repository.savePracticeResult(result(score = 80f, at = 3L))

        val recent = repository.getRecentResults("vowel-o", limit = 5)

        assertEquals(listOf(80f, 60f), recent.map { it.score })
    }

    @Test
    fun progressAggregates_againstTheWholeCatalogue() = runBlocking {
        repository.savePracticeResult(result(exerciseId = "vowel-o"))
        repository.savePracticeResult(result(exerciseId = "drawing-circle"))
        repository.savePracticeResult(result(exerciseId = "consonant-ko", completed = false))

        val progress = repository.observeProgress().first()

        assertTrue(progress.vowelProgress > 0f)
        assertTrue(progress.drawingProgress > 0f)
        assertEquals("an attempt that was not completed does not count", 0f, progress.consonantProgress)
        assertTrue(progress.overallProgress in 0f..1f)
    }

    @Test
    fun masteryIsEarnedAfterEnoughGoodCompletions_andKept() = runBlocking {
        repository.savePracticeResult(result(score = 90f, at = 1L))
        repository.savePracticeResult(result(score = 90f, at = 2L))
        assertFalse(progressOf("vowel-o")!!.isMastered)

        repository.savePracticeResult(result(score = 90f, at = 3L))
        assertTrue(progressOf("vowel-o")!!.isMastered)

        repository.savePracticeResult(result(score = 10f, completed = false, at = 4L))
        assertTrue("a weaker attempt does not take mastery back", progressOf("vowel-o")!!.isMastered)
    }

    @Test
    fun progressSurvivesClosingAndReopeningTheDatabase() = runBlocking {
        val sessionId = repository.savePracticeResult(result(score = 88f))

        database.close()
        open()

        assertEquals(88f, repository.getPracticeResult(sessionId)?.score)
        assertNotNull(progressOf("vowel-o"))
        assertNull(progressOf("vowel-aa"))
    }
}
