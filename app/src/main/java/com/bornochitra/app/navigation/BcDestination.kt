package com.bornochitra.app.navigation

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.SessionScores

/** Navigation routes for every planned screen. See plan.md section 13. */
sealed interface BcDestination {

    val route: String

    data object Welcome : BcDestination {
        override val route = "welcome"
    }

    data object Home : BcDestination {
        override val route = "home"
    }

    /** The Bangla hub: vowels, consonants, Bangla numbers and math. */
    data object BanglaHub : BcDestination {
        override val route = "bangla"
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

    data object BanglaNumbers : BcDestination {
        override val route = "bangla-numbers"
    }

    data object Drawing : BcDestination {
        override val route = "drawing"
    }

    /** The fill-in-the-blanks category picker. */
    data object FillBlanks : BcDestination {
        override val route = "fill-blanks"
    }

    /** A fill-in-the-blanks sequence from one category at one difficulty. */
    data object FillBlanksSequence : BcDestination {
        const val ARG_TYPE = "type"
        const val ARG_DIFFICULTY = "difficulty"
        override val route = "fill-blanks/{$ARG_TYPE}/{$ARG_DIFFICULTY}"

        fun createRoute(type: ExerciseType, difficulty: Difficulty) = "fill-blanks/${type.name}/${difficulty.name}"
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

    /** The drawer's About page; the same content Welcome shows on first launch. */
    data object About : BcDestination {
        override val route = "about"
    }
}
