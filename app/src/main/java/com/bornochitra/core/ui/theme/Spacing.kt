package com.bornochitra.core.ui.theme

import androidx.compose.ui.unit.dp

/** The spacing scale of design/DESIGN_SPEC.md 3. */
object BcSpacing {
    val xxs = 4.dp
    val xs = 8.dp

    // In-between gaps the design uses inside cards and rows.
    val tight = 6.dp
    val snug = 10.dp
    val s = 12.dp
    val roomy = 14.dp
    val m = 16.dp

    /** Screen side margin. */
    val screen = 20.dp

    /** Between the Grown-ups tab's sections. */
    val section = 22.dp
    val l = 24.dp
    val xl = 32.dp
    val xxl = 40.dp
}

/** Component sizes from design/DESIGN_SPEC.md 3–4. */
object BcDimens {
    // Child action buttons are taller than the 48dp Material minimum — small children have less precise motor control.
    val buttonHeight = 56.dp
    val buttonHeightSmall = 44.dp
    val buttonPadding = 24.dp
    val buttonPaddingSmall = 16.dp
    val buttonIcon = 22.dp
    val iconButton = 48.dp
    val icon = 24.dp
    val iconChip = 20.dp
    val iconSmall = 16.dp
    val iconCorner = 14.dp

    val topBarHeight = 64.dp
    val topBarLogo = 36.dp
    val chipTopBarHeight = 40.dp
    val chipHeight = 32.dp

    val navBarHeight = 80.dp
    val navIndicatorWidth = 64.dp
    val navIndicatorHeight = 32.dp

    val subjectCardMinHeight = 100.dp
    val subjectGlyphTile = 72.dp
    val rowMinHeight = 76.dp
    val rowMinHeightCompact = 62.dp
    val rowLead = 52.dp
    val rowLeadCompact = 44.dp
    val gameCardMinHeight = 144.dp
    val gameCell = 38.dp
    val gameGlyphTile = 44.dp
    val gameSpeaker = 30.dp

    val tileMinSize = 96.dp
    val tileStatusMinHeight = 16.dp
    val grownUpsLead = 54.dp
    val drawingTileHeight = 150.dp
    val drawingShape = 64.dp
    val drawingShapeStroke = 5.dp

    /** Space kept around a drawing's shape inside its box, as the design's shape icons have. */
    val drawingShapeInset = 8.dp

    /** The shapes icon on Home's Drawing subject card. */
    val subjectIcon = 44.dp
    val detailTileMinHeight = 96.dp
    val badge = 26.dp
    val badgeRing = 3.dp
    val flagOffset = 10.dp
    val continueRing = 3.dp
    val continueHalo = 4.dp
    val tileBorder = 2.dp
    val tileCorner = 20.dp
    val detailTileCorner = 18.dp
    val selectedBorder = 3.dp

    val continueTile = 76.dp
    val continuePlay = 52.dp
    val continuePlayIcon = 26.dp
    val masterySegmentHeight = 8.dp
    val masterySegmentCorner = 4.dp

    val progressBar = 10.dp
    val progressBarHeader = 14.dp
    val progressMinFill = 10.dp

    val choiceRadio = 24.dp
    val choiceRadioSelectedRing = 7.dp
    val choiceLead = 56.dp
    val choiceLeadSmall = 44.dp
    val choiceMinHeight = 88.dp
    val choiceMinHeightSmall = 72.dp

    val practiceTile = 84.dp
    val sequenceCellHeight = 64.dp
    val miniCellHeight = 26.dp
    val hintPreview = 112.dp
    val learnLetterWidth = 72.dp
    val learnRowHeight = 64.dp

    val sheetHandleWidth = 36.dp
    val sheetHandleHeight = 4.dp
    val sheetIcon = 48.dp
    val dialogIcon = 56.dp
    val dialogIconGlyph = 28.dp
    val emptyStateIcon = 56.dp

    val pageDot = 8.dp
    val pageDotActive = 24.dp

    val logoOnboarding = 104.dp
    val logoSplash = 176.dp

    val cardShadow = 4.dp
    val divider = 1.dp
    val outline = 1.dp
}
