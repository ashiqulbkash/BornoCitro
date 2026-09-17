package com.bornochitra.core.content

import com.bornochitra.core.model.ExerciseType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseRepositoryImplTest {

    private val repository = ExerciseRepositoryImpl()

    @Test
    fun `observeExercises returns only exercises of the requested type, ordered by order`() = runTest {
        val vowels = repository.observeExercises(ExerciseType.VOWEL).first()

        assertTrue(vowels.isNotEmpty())
        assertEquals(vowels, vowels.sortedBy { it.order })
        assertTrue(vowels.all { it.type == ExerciseType.VOWEL })
    }

    @Test
    fun `getExercise returns the matching exercise by id`() = runTest {
        val exercise = repository.getExercise("vowel-o")

        assertEquals("vowel-o", exercise?.id)
        assertEquals(ExerciseType.VOWEL, exercise?.type)
    }

    @Test
    fun `getExercise returns null for an unknown id`() = runTest {
        val exercise = repository.getExercise("does-not-exist")

        assertNull(exercise)
    }
}
