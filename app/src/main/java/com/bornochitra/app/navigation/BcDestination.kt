package com.bornochitra.app.navigation

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

    data object Drawing : BcDestination {
        override val route = "drawing"
    }

    data object Practice : BcDestination {
        const val ARG_EXERCISE_ID = "exerciseId"
        override val route = "practice/{$ARG_EXERCISE_ID}"

        fun createRoute(exerciseId: String) = "practice/$exerciseId"
    }

    data object Result : BcDestination {
        const val ARG_SESSION_ID = "sessionId"
        override val route = "result/{$ARG_SESSION_ID}"

        fun createRoute(sessionId: String) = "result/$sessionId"
    }

    data object Progress : BcDestination {
        override val route = "progress"
    }
}
