package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DottedPathSamplerTest {

    @Test
    fun `straight line places dots evenly including start and exact end`() {
        val points = listOf(Point(0f, 0f), Point(30f, 0f))

        val dots = DottedPathSampler.sample(points, spacing = 10f)

        assertEquals(listOf(Point(0f, 0f), Point(10f, 0f), Point(20f, 0f), Point(30f, 0f)), dots)
    }

    @Test
    fun `straight line does not force a dot onto an uneven remainder`() {
        val points = listOf(Point(0f, 0f), Point(25f, 0f))

        val dots = DottedPathSampler.sample(points, spacing = 10f)

        assertEquals(listOf(Point(0f, 0f), Point(10f, 0f), Point(20f, 0f)), dots)
    }

    @Test
    fun `multi-segment curve carries leftover distance across segment boundaries`() {
        val points = listOf(Point(0f, 0f), Point(10f, 0f), Point(10f, 10f))

        val dots = DottedPathSampler.sample(points, spacing = 5f)

        assertEquals(
            listOf(Point(0f, 0f), Point(5f, 0f), Point(10f, 0f), Point(10f, 5f), Point(10f, 10f)),
            dots,
        )
    }

    @Test
    fun `single point path is returned unchanged`() {
        val points = listOf(Point(5f, 5f))

        assertEquals(points, DottedPathSampler.sample(points, spacing = 10f))
    }

    @Test
    fun `empty path is returned unchanged`() {
        assertEquals(emptyList<Point>(), DottedPathSampler.sample(emptyList(), spacing = 10f))
    }

    @Test
    fun `non-positive spacing is rejected`() {
        val points = listOf(Point(0f, 0f), Point(10f, 0f))

        assertThrows(IllegalArgumentException::class.java) {
            DottedPathSampler.sample(points, spacing = 0f)
        }
    }
}
