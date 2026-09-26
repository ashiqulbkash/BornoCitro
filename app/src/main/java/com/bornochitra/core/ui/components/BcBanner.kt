package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** What a banner says (design/DESIGN_SPEC.md 4, Banner). Error is for system errors only, never a child's answer. */
enum class BcBannerKind { TIP, SUCCESS, INFO, ERROR }

/** A rounded banner with a leading icon. [compact] is the 14sp variant used in dialogs and Progress detail. */
@Composable
fun BcBanner(
    kind: BcBannerKind,
    text: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    @DrawableRes icon: Int = kind.icon,
) {
    val (container, content) = kind.colors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(container, BcShapes.banner)
            .padding(
                horizontal = if (compact) BcSpacing.roomy else BcSpacing.m,
                vertical = if (compact) BcSpacing.snug else BcSpacing.roomy,
            ),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(if (compact) BcDimens.iconChip else BcDimens.icon),
        )
        Text(text = text, style = if (compact) BcType.bannerSmall else BcType.banner, color = content, modifier = Modifier.weight(1f))
    }
}

/**
 * A quiet hint shown at a moment it helps, deliberately lighter than a verdict so it reads as a nudge
 * (plan.md section 43). The wording comes from [com.bornochitra.core.tips.TipSelector].
 */
@Composable
fun BcTip(
    tip: ContextualTip,
    modifier: Modifier = Modifier,
) {
    BcBanner(kind = BcBannerKind.TIP, text = stringResource(tip.messageRes()), modifier = modifier)
}

/** Interim: the old result/voice banner (removed when Result and Listen are redesigned, Phases 4–5). */
enum class BcFeedbackTone(val kind: BcBannerKind) {
    ENCOURAGING(BcBannerKind.INFO),
    GOOD(BcBannerKind.TIP),
    GREAT(BcBannerKind.SUCCESS),
}

/** Interim: a titled banner for screens not redesigned yet. */
@Composable
fun BcFeedbackBanner(
    tone: BcFeedbackTone,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    val (container, content) = tone.kind.colors()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(container, BcShapes.banner)
            .padding(horizontal = BcSpacing.m, vertical = BcSpacing.roomy),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.xxs),
    ) {
        Text(text = title, style = BcType.banner.copy(fontWeight = FontWeight.Bold), color = content)
        Text(text = message, style = BcType.banner, color = content)
    }
}

private val BcBannerKind.icon: Int
    get() = when (this) {
        BcBannerKind.TIP -> R.drawable.bc_ic_bulb
        BcBannerKind.SUCCESS -> R.drawable.bc_ic_check
        BcBannerKind.INFO -> R.drawable.bc_ic_info
        BcBannerKind.ERROR -> R.drawable.bc_ic_alert
    }

@Composable
private fun BcBannerKind.colors(): Pair<Color, Color> {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        BcBannerKind.TIP -> scheme.secondaryContainer to scheme.onSecondaryContainer
        BcBannerKind.SUCCESS -> scheme.tertiaryContainer to scheme.onTertiaryContainer
        BcBannerKind.INFO -> BcTheme.colors.englishContainer to BcTheme.colors.onEnglishContainer
        BcBannerKind.ERROR -> scheme.errorContainer to scheme.onErrorContainer
    }
}

@StringRes
private fun ContextualTip.messageRes(): Int = when (this) {
    ContextualTip.FIRST_ATTEMPT -> R.string.tip_first_attempt
    ContextualTip.UNFINISHED_TRACE -> R.string.tip_unfinished_trace
    ContextualTip.REPEATED_LOW_SCORES -> R.string.tip_repeated_low_scores
    ContextualTip.DIFFICULT_COMPLETED -> R.string.tip_difficult_completed
}

@Preview(showBackground = true)
@Composable
private fun BcBannerPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
            ContextualTip.entries.forEach { BcTip(tip = it) }
            BcBanner(kind = BcBannerKind.SUCCESS, text = "একদম ঠিক!")
            BcBanner(kind = BcBannerKind.INFO, text = "ইন্টারনেট লাগবে। বড়দের জিজ্ঞেস করো।", compact = true, icon = R.drawable.bc_ic_wifi)
            BcBanner(kind = BcBannerKind.ERROR, text = "তোমার অগ্রগতি খোলা গেল না।")
        }
    }
}
