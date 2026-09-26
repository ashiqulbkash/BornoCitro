package com.bornochitra.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.SessionScores
import com.bornochitra.feature.banglanumbers.BanglaNumbersScreen
import com.bornochitra.feature.consonants.ConsonantsScreen
import com.bornochitra.feature.drawing.DrawingScreen
import com.bornochitra.feature.english.EnglishLettersScreen
import com.bornochitra.feature.fillblanks.FillBlanksCategoryScreen
import com.bornochitra.feature.fillblanks.FillBlanksGateEvent
import com.bornochitra.feature.fillblanks.FillBlanksGateViewModel
import com.bornochitra.feature.fillblanks.FillBlanksScreen
import com.bornochitra.feature.fillblanks.HandwritingModelDialog
import com.bornochitra.feature.grownups.GrownUpsScreen
import com.bornochitra.feature.home.HomeScreen
import com.bornochitra.feature.hub.BanglaHubScreen
import com.bornochitra.feature.hub.EnglishHubScreen
import com.bornochitra.feature.language.LanguageEvent
import com.bornochitra.feature.language.LanguageSheet
import com.bornochitra.feature.language.LanguageViewModel
import com.bornochitra.feature.learn.LearnScreen
import com.bornochitra.feature.math.MathScreen
import com.bornochitra.feature.onboarding.OnboardingScreen
import com.bornochitra.feature.practice.PracticeScreen
import com.bornochitra.feature.progress.ProgressScreen
import com.bornochitra.feature.result.ResultScreen
import com.bornochitra.feature.vowels.VowelsScreen

/**
 * Opens [tab] with only Home behind it, so tabs never stack copies: Back from Progress or Grown-ups returns to Home,
 * and Back from Home leaves the app. Each tab keeps its state while another is open.
 */
private fun NavHostController.navigateToTab(tab: BcTab) {
    if (tab == BcTab.LEARN) {
        popBackStack(BcDestination.Home.route, inclusive = false)
        return
    }
    navigate(tab.destination.route) {
        popUpTo(BcDestination.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun BcNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    // Activity-scoped, like the language itself: the Home chip, its sheet and the Grown-ups switch share it.
    val languageViewModel: LanguageViewModel = hiltViewModel()
    val languageState by languageViewModel.uiState.collectAsStateWithLifecycle()
    val fillBlanksGateViewModel: FillBlanksGateViewModel = hiltViewModel()
    val fillBlanksGateState by fillBlanksGateViewModel.uiState.collectAsStateWithLifecycle()

    // Runs again after every recreate, which is when a language chosen elsewhere reaches the app.
    LaunchedEffect(Unit) { languageViewModel.onEvent(LanguageEvent.Shown) }

    fillBlanksGateState.modelDialog?.let { dialog ->
        HandwritingModelDialog(dialog = dialog, onEvent = fillBlanksGateViewModel::onEvent)
    }
    LaunchedEffect(fillBlanksGateState.openFillBlanks) {
        val language = fillBlanksGateState.openFillBlanks ?: return@LaunchedEffect
        fillBlanksGateViewModel.onEvent(FillBlanksGateEvent.FillBlanksOpened)
        navController.navigate(BcDestination.FillBlanks.createRoute(language))
    }

    val tabBar: @Composable (BcTab) -> Unit = { selected ->
        BcTabBar(selected = selected, onTabClick = navController::navigateToTab)
    }
    val onLanguageSelected: (AppLanguage) -> Unit = { languageViewModel.onEvent(LanguageEvent.LanguageSelected(it)) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(BcDestination.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(BcDestination.Home.route) {
                        popUpTo(BcDestination.Onboarding.route) { inclusive = true }
                    }
                },
            )
        }

        composable(BcDestination.Home.route) {
            var showLanguageSheet by rememberSaveable { mutableStateOf(false) }
            HomeScreen(
                onBanglaClick = { navController.navigate(BcDestination.BanglaHub.route) },
                onEnglishClick = { navController.navigate(BcDestination.EnglishHub.route) },
                onDrawingClick = { navController.navigate(BcDestination.Drawing.route) },
                onContinueClick = { exerciseId ->
                    navController.navigate(BcDestination.Practice.createRoute(exerciseId))
                },
                languageName = stringResource(languageState.language.nameRes()),
                onLanguageClick = {
                    languageViewModel.onEvent(LanguageEvent.Shown)
                    showLanguageSheet = true
                },
                navigationBar = { tabBar(BcTab.LEARN) },
            )
            if (showLanguageSheet) {
                LanguageSheet(
                    language = languageState.language,
                    // The sheet closes as the choice is made: the screen changing language is the answer, and below
                    // Android 13 the change is applied in place, which the sheet's own window would not pick up.
                    onLanguageSelected = { language ->
                        showLanguageSheet = false
                        onLanguageSelected(language)
                    },
                    onDismissRequest = { showLanguageSheet = false },
                )
            }
        }

        composable(BcDestination.BanglaHub.route) {
            BanglaHubScreen(
                onBackClick = { navController.popBackStack() },
                onVowelsClick = { navController.navigate(BcDestination.Vowels.route) },
                onConsonantsClick = { navController.navigate(BcDestination.Consonants.route) },
                onBanglaNumbersClick = { navController.navigate(BcDestination.BanglaNumbers.route) },
                onMathClick = { navController.navigate(BcDestination.Math.route) },
                onFillBlanksClick = {
                    fillBlanksGateViewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.BANGLA))
                },
                onLearnClick = { navController.navigate(BcDestination.Learn.createRoute(AppLanguage.BANGLA)) },
            )
        }

        composable(BcDestination.EnglishHub.route) {
            EnglishHubScreen(
                onBackClick = { navController.popBackStack() },
                onEnglishSmallClick = {
                    navController.navigate(BcDestination.EnglishLetters.createRoute(ExerciseType.ENGLISH_SMALL))
                },
                onEnglishCapitalClick = {
                    navController.navigate(BcDestination.EnglishLetters.createRoute(ExerciseType.ENGLISH_CAPITAL))
                },
                onMathClick = { navController.navigate(BcDestination.Math.route) },
                onFillBlanksClick = {
                    fillBlanksGateViewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
                },
                onLearnClick = { navController.navigate(BcDestination.Learn.createRoute(AppLanguage.ENGLISH)) },
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

        composable(
            route = BcDestination.Learn.route,
            arguments = listOf(navArgument(BcDestination.Learn.ARG_LANGUAGE) { type = NavType.StringType }),
        ) {
            LearnScreen(
                onBackClick = { navController.popBackStack() },
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
            route = BcDestination.FillBlanks.route,
            arguments = listOf(navArgument(BcDestination.FillBlanks.ARG_LANGUAGE) { type = NavType.StringType }),
        ) { backStackEntry ->
            val language = AppLanguage.valueOf(
                checkNotNull(backStackEntry.arguments?.getString(BcDestination.FillBlanks.ARG_LANGUAGE)),
            )
            FillBlanksCategoryScreen(
                language = language,
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
            FillBlanksScreen(
                onBackClick = { navController.popBackStack() },
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
                onProgressClick = { navController.navigateToTab(BcTab.PROGRESS) },
            )
        }

        composable(BcDestination.Progress.route) {
            ProgressScreen(navigationBar = { tabBar(BcTab.PROGRESS) })
        }

        composable(BcDestination.GrownUps.route) {
            GrownUpsScreen(
                language = languageState.language,
                onLanguageSelected = onLanguageSelected,
                navigationBar = { tabBar(BcTab.GROWN_UPS) },
            )
        }
    }
}

/** Each language is named in its own language, whichever one the app is in. */
private fun AppLanguage.nameRes(): Int = when (this) {
    AppLanguage.BANGLA -> R.string.language_bangla
    AppLanguage.ENGLISH -> R.string.language_english
}
