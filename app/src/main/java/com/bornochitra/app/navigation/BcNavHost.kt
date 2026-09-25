package com.bornochitra.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.SessionScores
import com.bornochitra.feature.banglanumbers.BanglaNumbersScreen
import com.bornochitra.feature.consonants.ConsonantsScreen
import com.bornochitra.feature.drawing.DrawingScreen
import com.bornochitra.feature.english.EnglishLettersScreen
import com.bornochitra.feature.fillblanks.FillBlanksCategoryScreen
import com.bornochitra.feature.fillblanks.FillBlanksScreen
import com.bornochitra.feature.home.HomeScreen
import com.bornochitra.feature.math.MathScreen
import com.bornochitra.feature.practice.PracticeScreen
import com.bornochitra.feature.progress.ProgressScreen
import com.bornochitra.feature.result.ResultScreen
import com.bornochitra.feature.vowels.VowelsScreen
import com.bornochitra.feature.welcome.WelcomeScreen

@Composable
fun BcNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BcDestination.Welcome.route,
        modifier = modifier,
    ) {
        composable(BcDestination.Welcome.route) {
            WelcomeScreen(
                onContinueClick = {
                    navController.navigate(BcDestination.Home.route) {
                        popUpTo(BcDestination.Welcome.route) { inclusive = true }
                    }
                },
            )
        }

        composable(BcDestination.Home.route) {
            HomeScreen(
                onVowelsClick = { navController.navigate(BcDestination.Vowels.route) },
                onConsonantsClick = { navController.navigate(BcDestination.Consonants.route) },
                onEnglishSmallClick = {
                    navController.navigate(BcDestination.EnglishLetters.createRoute(ExerciseType.ENGLISH_SMALL))
                },
                onEnglishCapitalClick = {
                    navController.navigate(BcDestination.EnglishLetters.createRoute(ExerciseType.ENGLISH_CAPITAL))
                },
                onMathClick = { navController.navigate(BcDestination.Math.route) },
                onBanglaNumbersClick = { navController.navigate(BcDestination.BanglaNumbers.route) },
                onDrawingClick = { navController.navigate(BcDestination.Drawing.route) },
                onFillBlanksClick = { navController.navigate(BcDestination.FillBlanks.route) },
                onProgressClick = { navController.navigate(BcDestination.Progress.route) },
                onContinueClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.Vowels.route) {
            VowelsScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.Consonants.route) {
            ConsonantsScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(
            route = BcDestination.EnglishLetters.route,
            arguments = listOf(navArgument(BcDestination.EnglishLetters.ARG_TYPE) { type = NavType.StringType }),
        ) {
            EnglishLettersScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.Math.route) {
            MathScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.BanglaNumbers.route) {
            BanglaNumbersScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.Drawing.route) {
            DrawingScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(BcDestination.FillBlanks.route) {
            FillBlanksCategoryScreen(
                onBackClick = { navController.popBackStack() },
                onCategoryClick = { type, difficulty ->
                    navController.navigate(BcDestination.FillBlanksSequence.createRoute(type, difficulty))
                },
            )
        }

        composable(
            route = BcDestination.FillBlanksSequence.route,
            arguments = listOf(
                navArgument(BcDestination.FillBlanksSequence.ARG_TYPE) { type = NavType.StringType },
                navArgument(BcDestination.FillBlanksSequence.ARG_DIFFICULTY) { type = NavType.StringType },
            ),
        ) {
            FillBlanksScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = BcDestination.Practice.route,
            arguments = listOf(
                navArgument(BcDestination.Practice.ARG_EXERCISE_ID) { type = NavType.StringType },
                navArgument(SessionScores.ARG) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString(BcDestination.Practice.ARG_EXERCISE_ID).orEmpty()
            PracticeScreen(
                exerciseId = exerciseId,
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { sessionId, sessionScores ->
                    navController.navigate(BcDestination.Result.createRoute(sessionId, sessionScores))
                },
            )
        }

        composable(
            route = BcDestination.Result.route,
            arguments = listOf(
                navArgument(BcDestination.Result.ARG_SESSION_ID) { type = NavType.StringType },
                navArgument(SessionScores.ARG) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            ResultScreen(
                // Practising again or moving on replaces the finished attempt, so Back from the new
                // attempt returns to the exercise list instead of an already-scored result.
                onPracticeClick = { exerciseId, sessionScores ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId, sessionScores)) {
                        popUpTo(BcDestination.Practice.route) { inclusive = true }
                    }
                },
                onProgressClick = {
                    navController.navigate(BcDestination.Progress.route) {
                        popUpTo(BcDestination.Home.route)
                    }
                },
            )
        }

        composable(BcDestination.Progress.route) {
            ProgressScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
