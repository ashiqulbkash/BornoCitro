package com.bornochitra.app.navigation

import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.SessionScores

/** Navigation routes for every planned screen. See plan.md section 13. */
sealed interface BcDestination {

    val route: String

    data object Tips : BcDestination {
        override val route = "tips"
    }

    data object Home : BcDestination {
        override val route = "home"
    }

    data object Vowels : BcDestination {
        override val route = "vowels"
    }

    data object Consonants : BcDestination {
        override val route = "consonants"
    }

    /** The English small or capital letters, one screen for both. */
    data object EnglishLetters : BcDestination {
        const val ARG_TYPE = "type"
        override val route = "english/{$ARG_TYPE}"

        fun createRoute(type: ExerciseType) = "english/${type.name}"
    }

    data object Math : BcDestination {
        override val route = "math"
    }

    data object Drawing : BcDestination {
        override val route = "drawing"
    }

    data object Practice : BcDestination {
        const val ARG_EXERCISE_ID = "exerciseId"
        override val route = "practice/{$ARG_EXERCISE_ID}?${SessionScores.ARG}={${SessionScores.ARG}}"

        fun createRoute(exerciseId: String, sessionScores: List<Float> = emptyList()) =
            "practice/$exerciseId?${SessionScores.ARG}=${SessionScores.encode(sessionScores)}"
    }

    data object Result : BcDestination {
        const val ARG_SESSION_ID = "sessionId"
        override val route = "result/{$ARG_SESSION_ID}?${SessionScores.ARG}={${SessionScores.ARG}}"

        fun createRoute(sessionId: String, sessionScores: List<Float>) =
            "result/$sessionId?${SessionScores.ARG}=${SessionScores.encode(sessionScores)}"
    }

    data object Progress : BcDestination {
        override val route = "progress"
    }
}
