package com.bornochitra.core.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Corner radii of design/DESIGN_SPEC.md 3. */
object BcShapes {
    /** Mini pattern cells inside a choice card. */
    val miniCell = RoundedCornerShape(7.dp)

    /** Mini cells inside game cards. */
    val cell = RoundedCornerShape(10.dp)

    /** Square chips. */
    val chipSquare = RoundedCornerShape(12.dp)

    /** Small glyph tiles (36–48dp). */
    val smallTile = RoundedCornerShape(14.dp)

    /** Sequence cells, 52dp row leads. */
    val md = RoundedCornerShape(16.dp)

    /** Banners and Progress detail tiles. */
    val banner = RoundedCornerShape(18.dp)

    /** Letter tiles, rows, choice cards, 72dp glyph tiles. */
    val lg = RoundedCornerShape(20.dp)

    /** Cards, subject cards, header cards, the practice letter tile. */
    val xl = RoundedCornerShape(24.dp)

    /** Continue card, canvas, sheets, dialogs. */
    val xxl = RoundedCornerShape(28.dp)

    /** Sheets: top corners only. */
    val sheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    /** Buttons, chips, segmented switch, nav indicator, progress bars, icon buttons. */
    val full = CircleShape
}

internal val BcMaterialShapes = Shapes(
    extraSmall = BcShapes.cell,
    small = BcShapes.smallTile,
    medium = BcShapes.md,
    large = BcShapes.lg,
    extraLarge = BcShapes.xxl,
)
