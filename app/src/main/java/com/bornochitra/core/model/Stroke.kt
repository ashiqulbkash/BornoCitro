package com.bornochitra.core.model

/** One continuous pen path within an [Exercise]. See plan.md section 8. */
data class Stroke(val id: String, val points: List<Point>)
