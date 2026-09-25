package com.bornochitra.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.SessionScores
import com.bornochitra.feature.banglanumbers.BanglaNumbersScreen
import com.bornochitra.feature.consonants.ConsonantsScreen
import com.bornochitra.feature.drawer.DrawerContent
import com.bornochitra.feature.drawer.DrawerEvent
import com.bornochitra.feature.drawer.DrawerViewModel
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
import kotlinx.coroutines.launch

/** Goes to Home from anywhere and leaves nothing behind it, so Back from Home leaves the app. */
private fun NavHostController.navigateToHome() {
    navigate(BcDestination.Home.route) {
        popUpTo(BcDestination.Home.route) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Screens where swiping from the edge opens the drawer. Where the child writes or draws, that
 * swipe would fight the strokes, so the menu icon is the only way in there. Welcome has no drawer.
 */
private val DRAWER_SWIPE_ROUTES = setOf(
    BcDestination.Home.route,
    BcDestination.Vowels.route,
    BcDestination.Consonants.route,
    BcDestination.EnglishLetters.route,
    BcDestination.Math.route,
    BcDestination.BanglaNumbers.route,
    BcDestination.Drawing.route,
    BcDestination.FillBlanks.route,
    BcDestination.Progress.route,
)

/** Opens Progress with only Home behind it, so opening it again from the drawer does not stack copies. */
private fun NavHostController.navigateToProgress() {
    navigate(BcDestination.Progress.route) {
        popUpTo(BcDestination.Home.route)
        launchSingleTop = true
    }
}

@Composable
fun BcNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerViewModel: DrawerViewModel = hiltViewModel()
    val drawerUiState by drawerViewModel.uiState.collectAsStateWithLifecycle()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(drawerState.targetValue) {
        if (drawerState.targetValue == DrawerValue.Open) drawerViewModel.onEvent(DrawerEvent.Opened)
    }
    // Added only while the drawer opens, so it is registered after the NavHost's and the screens' own
    // back handlers and Back closes the drawer instead of leaving the screen behind it.
    if (drawerState.targetValue == DrawerValue.Open) {
        BackHandler { scope.launch { drawerState.close() } }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                state = drawerUiState,
                onEvent = drawerViewModel::onEvent,
                onHomeClick = {
                    scope.launch { drawerState.close() }
                    navController.navigateToHome()
                },
                onProgressClick = {
                    scope.launch { drawerState.close() }
                    navController.navigateToProgress()
                },
            )
        },
        modifier = modifier,
        gesturesEnabled = currentRoute in DRAWER_SWIPE_ROUTES,
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
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
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onContinueClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.Vowels.route) {
                VowelsScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onExerciseClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.Consonants.route) {
                ConsonantsScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
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
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onExerciseClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.Math.route) {
                MathScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onExerciseClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.BanglaNumbers.route) {
                BanglaNumbersScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onExerciseClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.Drawing.route) {
                DrawingScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onExerciseClick = { exerciseId ->
                        navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                    },
                )
            }

            composable(BcDestination.FillBlanks.route) {
                FillBlanksCategoryScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
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
                FillBlanksScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
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
                    onMenuClick = { scope.launch { drawerState.open() } },
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
                    onProgressClick = { navController.navigateToProgress() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            }

            composable(BcDestination.Progress.route) {
                ProgressScreen(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            }
        }
    }
}
