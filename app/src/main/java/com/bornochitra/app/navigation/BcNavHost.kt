package com.bornochitra.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bornochitra.feature.consonants.ConsonantsScreen
import com.bornochitra.feature.drawing.DrawingScreen
import com.bornochitra.feature.home.HomeScreen
import com.bornochitra.feature.practice.PracticeScreen
import com.bornochitra.feature.progress.ProgressScreen
import com.bornochitra.feature.result.ResultScreen
import com.bornochitra.feature.tips.TipsScreen
import com.bornochitra.feature.vowels.VowelsScreen

@Composable
fun BcNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BcDestination.Tips.route,
        modifier = modifier,
    ) {
        composable(BcDestination.Tips.route) {
            TipsScreen(
                onContinueClick = {
                    navController.navigate(BcDestination.Home.route) {
                        popUpTo(BcDestination.Tips.route) { inclusive = true }
                    }
                },
            )
        }

        composable(BcDestination.Home.route) {
            HomeScreen(
                onVowelsClick = { navController.navigate(BcDestination.Vowels.route) },
                onConsonantsClick = { navController.navigate(BcDestination.Consonants.route) },
                onDrawingClick = { navController.navigate(BcDestination.Drawing.route) },
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

        composable(BcDestination.Drawing.route) {
            DrawingScreen(
                onBackClick = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
            )
        }

        composable(
            route = BcDestination.Practice.route,
            arguments = listOf(navArgument(BcDestination.Practice.ARG_EXERCISE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString(BcDestination.Practice.ARG_EXERCISE_ID).orEmpty()
            PracticeScreen(
                exerciseId = exerciseId,
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { sessionId ->
                    navController.navigate(BcDestination.Result.createRoute(sessionId))
                },
            )
        }

        composable(
            route = BcDestination.Result.route,
            arguments = listOf(navArgument(BcDestination.Result.ARG_SESSION_ID) { type = NavType.StringType }),
        ) {
            ResultScreen(
                // Practising again or moving on replaces the finished attempt, so Back from the new
                // attempt returns to the exercise list instead of an already-scored result.
                onPracticeClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId)) {
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
