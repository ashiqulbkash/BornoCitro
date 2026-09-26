# BornoChitra (বর্ণচিত্র) — Design Spec for Compose + Material 3

Source of truth: the "বর্ণচিত্র — BornoChitra redesign" design canvas. This spec only describes it; nothing is added or removed.
Units: dp for size/space, sp for text. Screen reference: 360 × 800 dp, portrait. Status bar / gesture bar are system (not drawn).
`(cw)` = new Bengali text, check wording before shipping.

---

## 1. Color

### 1.1 Material 3 `ColorScheme`

| M3 role | Light | Dark | Used for |
|---|---|---|---|
| primary | `#6236D9` | `#CDBDFF` | filled buttons, Continue card, progress fill, active states |
| onPrimary | `#FFFFFF` | `#32118A` | text/icons on primary |
| primaryContainer | `#EAE1FF` | `#4B27B5` | Bangla subject card, row lead tiles, tonal button, nav indicator, "practising" tile |
| onPrimaryContainer | `#22005D` | `#EDE5FF` | text on primaryContainer |
| secondary | `#A34A00` | `#FFB77C` | Drawing glyph tile, low-score headline/score, drawing shape strokes |
| onSecondary | `#FFE0C2` | `#6E3A00` | glyph on secondary |
| secondaryContainer | `#FFE0C2` | `#6E3A00` | Drawing subject card, Tip banner, filled sequence cell, dialog icon (reset) |
| onSecondaryContainer | `#3D1D00` | `#FFDCC0` | text on secondaryContainer |
| tertiary *(success)* | `#17754E` | `#7DDBAC` | Mastered tile outline, check badge, great score % |
| onTertiary | `#CFF3E0` | `#00513A` | check icon on badge |
| tertiaryContainer | `#CFF3E0` | `#00513A` | Mastered tile fill, Success banner, "learned" chip |
| onTertiaryContainer | `#002114` | `#C8F5DE` | text on tertiaryContainer |
| error | `#BA1A1A` | `#FFB4AB` | system errors only (never for a child's wrong answer) |
| onError | `#FFFFFF` | `#690005` | — |
| errorContainer | `#FFDAD6` | `#93000A` | Error banner, "failed" dialog icon |
| onErrorContainer | `#410002` | `#FFDAD6` | — |
| background / surface | `#FDF8FF` | `#15121B` | screen background |
| onBackground / onSurface | `#1C1A22` | `#E9E1F0` | main text |
| onSurfaceVariant | `#4A4458` | `#CBC3D6` | secondary text, inactive nav, captions |
| surfaceContainerLowest | `#FFFFFF` | `#1E1A25` | cards, rows, tiles, canvas, sequence cells (dark: intentionally lighter than surface) |
| surfaceContainerLow | `#F7F1FC` | `#221E2A` | nav bar, flat cards, sheets, dialogs; cards in dark |
| surfaceContainer | `#F0E9F8` | `#2B2634` | segmented track, chips, tonal icon button |
| surfaceContainerHigh | `#E4DCEF` | `#39323F` | progress track, disabled button |
| outline | `#7A7289` | `#958DA1` | radio ring, dashed blank cell, chevrons, sheet handle |
| outlineVariant | `#D4CCDF` | `#4A4456` | 2dp borders of tiles/choices/canvas/outline buttons, dividers, empty stars (dark empty star `#6A6378`) |
| scrim | `#14101E` @ 48% | `#000000` @ 62% | behind sheets and dialogs |

### 1.2 Extended colors (`BcColors` via CompositionLocal)

| Token | Light | Dark | Used for |
|---|---|---|---|
| accent (pencil orange) | `#F58A2E` | `#FF9F4A` | "continue here" ring + flag, Continue play button, filled-answer cell border, mastery segments. Text on accent: `#2B1400` |
| star | `#F2B100` | `#FFC940` | filled stars |
| guide | `#C3B9D2` | `#5E5770` | tracing guide dots and line |
| english | `#0B63A5` | `#9ECAFF` | English glyph tile, English progress fill |
| englishContainer | `#D4EAFF` | `#00497D` | English subject card, row leads, Info banner |
| onEnglishContainer | `#001D35` | `#D4EAFF` | text on englishContainer (also glyph color on `english`) |
| wordingMark | `#E0730B` | `#E0730B` | design-only dotted underline, NOT shipped |

Subject identity: **Bangla = primary**, **English = english**, **Drawing = secondary**. Math (সংখ্যা ও চিহ্ন) uses Bangla colors in the Bangla hub and English colors in the English hub (same destination, same progress).

Card elevation: light = shadow `0 1dp 2dp` + `0 4dp 14dp`, color `#32146E` @ 8% (≈ tonalElevation 1 / shadowElevation 2). Dark = no shadow, background surfaceContainerLow + 1dp outlineVariant border.

---

## 2. Typography

Fonts (bundle as `res/font`, Google Fonts OFL):
- **Baloo Da 2** (500/600/700/800) — display, titles, buttons, letters. Bengali + Latin.
- **Hind Siliguri** (400/500/600) — body, captions, labels in text.
- **Andika** (400/700) — English letters only (tiles, tracing, English glyphs, "English" label).
Fallback: Noto Sans Bengali → system sans.

| M3 role | Font | Size / line height | Weight | Used for |
|---|---|---|---|---|
| displaySmall | Baloo Da 2 | 40 / 50 | 800 | স্বাগতম! (onboarding), দারুণ!, splash wordmark |
| headlineMedium | Baloo Da 2 | 28 / 38 | 700 | Home "স্বাগতম!", "বর্ণচিত্র কী?" |
| titleLarge | Baloo Da 2 | 22 / 30 | 700 | top-bar titles, subject names, sheet & dialog titles, Continue title |
| titleMedium | Baloo Da 2 | 18 / 24 | 700 | row names, game-card names, % labels, choice labels |
| bodyLarge | Hind Siliguri | 18 / 29 | 500 (600 for instructions) | instructions, onboarding subtitle |
| bodyMedium | Hind Siliguri | 16 / 25 | 500 | body copy, feature cards, dialog body |
| bodySmall | Hind Siliguri | 14 / 20 | 500 (600 in chips/meta) | captions, row meta, chips |
| labelLarge | Baloo Da 2 | 17 / 20 | 700 | buttons (15sp in small buttons) |
| labelMedium | Baloo Da 2 | 15 / 20 | 700 | section labels, language chip |
| labelSmall | Hind Siliguri | 13 / 16 | 600 (700 active) | nav labels, tile status line |

Custom styles (`BcType`): `letterTile` Baloo 40/46 700 · `letterPractice` Baloo 56 800 · `letterResult` Baloo 72/80 800 · `scoreXL` Baloo 56/62 800 · `scoreL` Baloo 48/53 800 · `percentHeader` Baloo 30/33 800 · `flag` Baloo 11 700.
Rules: minimum text 13sp (tile status 11sp only on Progress detail 4-column grid). Use sp; layouts must survive 1.3× font scale. Bengali needs the tall line heights above.

---

## 3. Spacing & shape

Spacing scale (dp): `4 xxs · 8 xs · 12 s · 16 m · 20 screen · 24 l · 32 xl · 40 xxl`.
- Screen side margin **20**. Gap between list items **12** (Progress list 10). Between sections **20–24**. Card inner padding **16**.
- Minimum gap between tappable neighbours **8**, preferred 12.

Corner radius (dp):
| Token | dp | Used for |
|---|---|---|
| xs | 7–10 | mini cells inside cards |
| sm | 12–14 | small glyph tiles (36–48dp), chips-square |
| md | 16 | sequence cells, row lead (52dp), banners (18) |
| lg | 20 | letter tiles, rows, choice cards, glyph tile (72dp), language/choice leads |
| xl | 24 | cards, subject cards, header cards, practice letter tile |
| xxl | 28 | Continue card, canvas, sheets (top corners), dialogs |
| full | 50% | buttons, chips, segmented, nav indicator, progress bars, icon buttons |

Touch targets: child action buttons 56 tall; rows ≥ 76 (Progress rows 62); tiles ≥ 96; icon buttons 48; nothing tappable < 48 (chips in top bar are 40 tall inside a 48 touch area).

Icons: 24dp outline, stroke 2dp, round caps/joins (Material Symbols Rounded, outlined, weight 400 is a good match). 22dp inside buttons, 20dp in chips/language chip, 16dp in small chips.

---

## 4. Components

### Buttons (all: height 56, radius full, horizontal padding 24, labelLarge, icon 22 + gap 8)
| Name | Container | Content | States |
|---|---|---|---|
| BcPrimaryButton | primary | onPrimary | pressed: +12% onPrimary overlay; disabled: surfaceContainerHigh / outline (prefer hiding over disabling) |
| BcTonalButton *(new)* | primaryContainer | onPrimaryContainer | same overlay rules |
| BcOutlineButton (BcSecondaryButton) | transparent, 2dp outlineVariant border | primary | pressed: primary 12% overlay |
| BcTextButton | transparent, padding 16 | primary | pressed overlay |
| Small variant | height 44, padding 16, 15sp | — | — |
Rule: one primary button per screen = the screen's main action.

### Icon button — 48×48, radius full, icon 24 onSurface. Tonal variant: surfaceContainer bg. Back icon = arrow-left.

### Language chip *(new, Home top bar)* — height 40, padding start 10 / end 14, radius full, surfaceContainer, globe 20 + gap 6 + current language name (labelMedium). Tap → Language sheet.

### Top app bar (BcTopAppBar)
- **Inner**: height 64, padding horizontal 8; [back IconButton 48] + title titleLarge (left-aligned, start padding 4) + optional trailing Chip (margin end 8).
- **Tab screens** (Progress, Grown-ups): no back, title start padding 16.
- **Home**: start padding 16, gap 10: logo circle 36 (primary, "অ" Baloo 800 20sp onPrimary) + "বর্ণচিত্র" titleLarge in primary + Language chip.
- **Result**: title centered, no back.

### Navigation bar *(new, replaces drawer)* — M3 `NavigationBar`, height 80, container surfaceContainerLow, padding 12 top/bottom, 8 sides. 3 items, labels always shown: indicator pill 64×32 primaryContainer, icon 24; label labelSmall, 4dp below. Selected: icon onPrimaryContainer, label onSurface 700. Unselected: onSurfaceVariant.
Items: ✏️-pencil **শিখি** (cw) → Home · star **অগ্রগতি** → Progress · two-people **বড়দের জন্য** (cw) → Grown-ups.

### Subject card (BcSubjectCard, new) — Home only
Full width, min height 100, radius 24, padding 14 (start) / 16, gap 16, row. Container per subject (primaryContainer / englishContainer / secondaryContainer).
- Glyph tile 72×72 radius 20 (primary+onPrimary "অ" / english+englishContainer "Aa" Andika / secondary+onSecondary triangle-and-circle icon 44), text 38sp Baloo 800.
- Column gap 6: name titleLarge · caption bodySmall 85% alpha · progress row (bar on-container track + % bodySmall 700, gap 10).
States: pressed overlay; always shows progress (0% shows empty bar).

### Continue card *(redesigned "শেখা চালিয়ে যাও")* — Home
Radius 28, primary container, padding 16, column gap 14. Hidden when there is no continueExerciseId (as today).
- Row gap 14: letter tile 76×76 radius 20, bg onPrimary, text primary, letterPractice at 44sp, 3dp accent ring · column: "শেখা চালিয়ে যাও" titleLarge + "স্বরবর্ণ · ★★★" bodySmall 90% · play button 52 circle accent with play icon 26 `#2B1400`.
- Mastery row: caption "আর ২ বার লিখলে “আ” শেখা হয়ে যাবে" (cw, bodySmall 600) + 3 segments (flex, height 8, radius 4, gap 6): completed = accent, remaining = onPrimary @ 28%. Segments = completions toward MasteryRule (3).

### Activity row (BcActivityRow, new)
Min height 76, radius 20, padding 12, gap 14, surfaceContainerLowest + card elevation.
Lead 52×52 radius 16 (primaryContainer or englishContainer; glyph 26sp Baloo 800) · column gap 6: name titleMedium + meta row (bar flex + % bodySmall 600 onSurfaceVariant, gap 10) · chevron 24 outline.
Compact variant (Progress list): min height 62, padding 8/10, lead 44 radius 14 (22sp), name 16sp, gap 4.

### Game card *(new)* — hubs
2 per row (grid gap 12), min height 144, radius 24, padding 16, column gap 14, card surface. Top: mini picture (Fill: 3 cells 38 tall radius 10, 18sp, middle = current "?"; Listen: glyph tile 44 radius 14 + speaker 30 in subject color). Bottom-aligned name titleMedium.

### Letter tile (BcExerciseTile)
Square (width = (content − 2×12)/3 ≈ 98), radius 20, 2dp inner border, column centered gap 2: character letterTile · status line labelSmall (min height 16).
| State | Fill | Border | Status line |
|---|---|---|---|
| Not started | surfaceContainerLowest | 2dp outlineVariant | empty |
| Practising | primaryContainer | 2dp primary | attempt count "২ বার" |
| Completed | surfaceContainerLow | 2dp outlineVariant | stars 13sp (best score) |
| Mastered | tertiaryContainer | 2dp tertiary | stars + check badge 26 circle (tertiary, check 16 stroke 3, 3dp background ring) at top-end −6/−6 |
| Continue here (overlay on its state) | — | 3dp accent + 4dp accent @ 22% halo | flag "এখান থেকে" (cw) pill at top-center −10: accent bg, `#2B1400`, flag style, padding 4/8 |
Pressed: overlay. Tile opens Practice.

### Stars (BcStars, new)
3 stars, gap 1. Filled = star color, empty = outlineVariant (dark `#6A6378`). Sizes: 13sp tiles/chips, 14 cards, 18 grown-ups, 30 legend. Result stars: 64 / 88 / 64 dp, middle raised 14dp, sides rotated −10° / +10°; empty result stars = outline only, 1.6 stroke outlineVariant. Star count uses the existing best-score star rule.

### Progress bar (BcLabeledProgress)
Height 10 (lists) or 14 (headers), radius full, track surfaceContainerHigh (on colored containers: black 10% / white 16% dark), fill subject color. Any value > 0 renders at least 10dp wide. 0% = empty track. No end-dot.

### Header progress card — category & detail screens
surfaceContainerLow, radius 24, padding 14/16, gap 10: [bar 14 flex + % titleMedium] + chip row (gap 8, wrap): "১টি শিখেছ" (cw, tertiaryContainer + check 16) · "★ ২টি শেষ" (cw) · "১টি চলছে" (cw).

### Chip — height 32, padding 12, radius full, surfaceContainer, bodySmall 600 onSurfaceVariant, icon 16 gap 6.

### Segmented switch (BcLanguageSwitch)
Track surfaceContainer, radius full, padding 4, gap 4. Segments flex, height 48, radius full, labelLarge-ish 17sp 700. Selected: primary / onPrimary, animated thumb (keep current animation). Unselected: onSurfaceVariant. "English" in Andika.

### Choice card (BcChoiceButton)
Full width, padding 14/16, radius 20, gap 14, 2dp outlineVariant border, surfaceContainerLowest. Radio 24 (2dp outline ring). Selected: 3dp primary border, primaryContainer fill, radio = 7dp primary ring. Variants: row (language, 72–88 tall, lead tile 44–56 radius 14–16) and column (difficulty: radio+label row, mini pattern, caption; gap 10).

### Tracing canvas (ExerciseTracingCanvas)
Square, width = screen − 40 (320 at 360dp), radius 28, surfaceContainerLowest, 2dp outlineVariant border, clip.
Guide: line 3dp guide @ 60% + dots 13dp diameter every 19dp in guide color. Dots already traced → primary. Finger ink: primary, 9dp, round caps. Fill-the-blanks canvas: two dashed guide lines (2dp, dash 6 gap 8) at ~29% and ~78% height; no dots until Hint.

### Sequence cell
Flex in a row (gap 8), height 64, radius 16, titleLarge-ish 28sp Baloo 700.
| State | Style |
|---|---|
| Given | surfaceContainerLowest, 2dp outlineVariant |
| Current blank | primaryContainer, 3dp primary, "?" in primary |
| Later blank | transparent, 2dp dashed outline, empty |
| Filled by child | secondaryContainer, 2dp accent, onSecondaryContainer |

### Banner (BcFeedbackBanner / BcTip)
Radius 18, padding 14/16, gap 12, icon 24 top-aligned, 15sp/22 Hind 500.
Tip = secondaryContainer (bulb) · Success = tertiaryContainer (check) · Info = englishContainer (info) · Error = errorContainer (alert; system errors only).

### Bottom sheet — `ModalBottomSheet`: container surfaceContainerLow, top radius 28, padding 10 top / 20 sides / 28 bottom, gap 16, handle 36×4 outline @ 60%, scrim.
Sheet header: icon circle 48 (tonal) + title titleLarge (+ optional caption), gap 12.

### Dialog — centered, insets 24 left/right, radius 28, padding 24, gap 16, surfaceContainerLow. Icon circle 56 (icon 28) centered · title titleLarge + body bodyMedium onSurfaceVariant, centered, gap 6–8 · buttons: stacked full-width (primary + text) or right-aligned row (text + primary).

### Page indicator — dots 8×8 radius 4 outlineVariant, active 24×8 primary, gap 8.

### Empty / loading / error states (unchanged behavior)
Use banner Error or BcEmptyState centered in content: icon circle 56, titleMedium, bodyMedium, optional primary button. Loading: M3 CircularProgressIndicator, primary.

---

## 5. Screens

Common: background = surface; content padding 20 horizontal unless noted. Tab screens have the Navigation bar; all other screens do not.

### 1 · Splash (BrandSplash) — existing
Centered column, gap 28, padding 24: logo 176 (purple circle, white "অ", dotted line + orange pencil — current icon) · wordmark "বর্ণচিত্র" displaySmall primary · tagline "লেখো, শেখো, বেড়ে ওঠো" titleLarge onSurfaceVariant. Fades out as today.

### 2a · Onboarding — Language — **NEW, replaces Welcome (part 1/2)**
Padding 56 top / 20 / 24 bottom, gap 24.
1. Centered column gap 12 (+16 top): logo 104 · "স্বাগতম!" displaySmall · "অ্যাপটা কোন ভাষায় দেখবে?" (cw) + "Which language should the app use?" bodyLarge onSurfaceVariant.
2. Two row choice cards (gap 12, min height 88): lead 56 "অ" primary / "A" englishContainer · "বাংলা" / "English" titleLarge · radio. Selecting switches language immediately (same setting as the switch).
3. Caption centered: "পরে “বড়দের জন্য” থেকে বদলানো যাবে।" (cw).
4. Bottom (gap 20): page indicator (1 of 2) · Primary full-width "এগিয়ে চলো" → page 2 (swipe also works; HorizontalPager).

### 2b · Onboarding — About — **NEW, replaces Welcome (part 2/2)**
Padding 40 top / 20 / 24, gap 16.
1. "বর্ণচিত্র কী?" headlineMedium + intro bodyMedium (existing AboutContent text), gap 6.
2. 4 feature cards (existing text), gap 10: card padding 14, gap 14, lead 52 radius 16 (অa primaryContainer · ১+২ englishContainer · house icon secondaryContainer · star icon tertiaryContainer) + bodyMedium.
3. Bottom: indicator (2 of 2) · Primary "শুরু করো →" (cw). Does exactly what Welcome's Continue did: mark seen, open Home, remove onboarding from back stack.

### 3 · Home — existing, redesigned (drawer removed)
1. Home top bar (64): logo + "বর্ণচিত্র" + Language chip.
2. Content: padding 8 top / 20 / 16, gap 16.
   - Greeting: "স্বাগতম!" headlineMedium + "আজ কী শিখবে?" (cw) bodyMedium onSurfaceVariant.
   - Continue card (only when continueExerciseId exists).
   - 3 Subject cards, gap 12: বাংলা ("বর্ণ, সংখ্যা, খেলা" cw) → Bangla hub · ইংরেজি ("ছোট ও বড় হাতের অক্ষর" cw) → English hub · আঁকা ("রেখা, গোল, বাড়ি" cw) → Drawing grid.
3. Navigation bar (শিখি selected).
Data added to HomeUiState (display only): continue item's letter, category, best stars, completions; progress % per subject.

### 4 · Language sheet — **NEW, replaces the language switch in the drawer**
Opened by the Language chip on Home. ModalBottomSheet over Home:
header (globe circle 48 primaryContainer/primary + "অ্যাপের ভাষা" (cw) titleLarge + "App language" caption) · 2 row choice cards (min height 72, lead 44) · caption centered "বেছে নিলেই বদলে যাবে। অগ্রগতি মুছবে না।" (cw). Choice applies at once and persists (same setting/API as today). Dismiss: drag, scrim, Back.

### 5 · Grown-ups tab (বড়দের জন্য) — **NEW tab, replaces the drawer and the About screen**
1. Tab top bar "বড়দের জন্য" (cw).
2. Scrollable column, padding 4 top / 20 / 20, gap 22:
   - Section "অ্যাপের ভাষা · App language" (cw, labelMedium) + Segmented switch (BcLanguageSwitch).
   - Section "অগ্রগতি কীভাবে গোনা হয়" (cw): card padding 16, gap 12, three rows (leading 54 wide, gap 12, bodySmall): stars "প্রতিটা অক্ষরের তারা আসে সবচেয়ে ভালো লেখা থেকে।" (cw) · check badge "৩ বার লিখলে আর অন্তত একবার ৮০% পেলে অক্ষরটা শেখা হয়ে যায়।" (cw) · phone icon "সব অগ্রগতি এই ফোনেই থাকে। কোনো অ্যাকাউন্ট লাগে না।" (cw).
   - Section "পরিচিতি": "বর্ণচিত্র কী?" titleLarge + intro bodyMedium + flat card (padding 6/16) with the 4 feature lines, numbered ১–৪ in primary, 1dp outlineVariant dividers, rows padding 10 vertical, bodySmall.
3. Navigation bar (বড়দের জন্য selected).

### 6 · Bangla hub — existing, redesigned
1. Top bar: back → Home, "বাংলা".
2. Content padding 4 / 20 / 20, gap 12:
   - Section label "লিখে শেখো" (cw).
   - 4 Activity rows: অ স্বরবর্ণ → Vowels · ক ব্যঞ্জনবর্ণ → Consonants · ১ বাংলা সংখ্যা → Bangla numbers · + সংখ্যা ও চিহ্ন (30sp) → Math.
   - Section label "খেলে শেখো" (cw), 12 top padding.
   - Game cards 2-up: শূন্যস্থান পূরণ → (model dialog if needed) → Fill picker (Bangla) · শুনে শিখি → Listen (Bangla).

### 7 · English hub — existing, redesigned
Same as Bangla hub, English colors, leads in Andika: a ছোট হাতের অক্ষর · A বড় হাতের অক্ষর · 1+ সংখ্যা ও চিহ্ন (22sp). Under the rows: caption "“সংখ্যা ও চিহ্ন” বাংলাতেও আছে — অগ্রগতি একটাই।" (cw). Game cards: Fill (cells a ? c, Andika) · Listen (A tile english + speaker).

### 8 · Category grid (Vowels, Consonants, Bangla numbers, Math, English small/capital) — existing, redesigned
1. Top bar: back, category name.
2. Content padding 4 / 20 / 20, gap 20: Header progress card · LazyVerticalGrid 3 columns, spacing 14 vertical / 12 horizontal, Letter tiles with states. Tile → Practice. Empty state unchanged.
English grids use Andika for characters.

### 9 · Drawing grid — existing, redesigned
Top bar "আঁকা" · header card (bar 14 in secondary + %) · grid **2 columns**, gap 14, tiles height 150 (not square), column gap 8: shape 64×64 (stroke 5 secondary: রেখা, গোল, চারকোনা, তিনকোনা, বাড়ি — all cw) + name titleMedium. Same tile states as letters.

### 10 · Practice — existing, redesigned
1. Top bar: back → grid, "অনুশীলন", trailing chip "স্বরবর্ণ · ২/১১" (position in category).
2. Content padding 4 / 20 / 24, gap 16:
   - Header row gap 16: model letter tile 84×84 radius 24 primaryContainer, letterPractice 56 · instruction "ফোঁটার ওপর দিয়ে আঙুল টেনে লেখো" bodyLarge 600.
   - Tracing canvas 320×320.
   - Tip banner (TipSelector text; sample "আগে ওপরের লম্বা দাগটা টানো।" cw).
   - Outline button "আবার শুরু" with reset icon, min width 200, centered, pushed to bottom.
Landscape: canvas left, header + tip + button right (as today). Loading/error unchanged. Finishing the trace → Result automatically (unchanged).

### 11 · Restart confirm dialog — **NEW, added to Practice and Fill-the-blanks Reset**
Shown when "আবার শুরু" is tapped AND the canvas has ink; with no ink, reset directly. Dialog: icon 56 secondaryContainer + reset · "আবার শুরু করবে?" (cw) · "যা লিখেছ, সব মুছে যাবে।" (cw) · stacked: Primary "হ্যাঁ, মুছে দাও" (cw) → clears ink · Text "না, লিখতে থাকি" (cw) → dismiss.

### 12a · Result — great / medium — existing, redesigned
System Back still blocked. No back arrow.
1. Top bar centered "ফলাফল".
2. Decorative confetti (6 small shapes, accent/primary/english/success/star, top 280dp, static or one-shot animation).
3. Content padding 0 / 20 / 24, gap 16:
   - Stars row (64/88/64, 8 top), stars = this try's score via the star rule.
   - Banner text centered: level title displaySmall primary ("দারুণ!" perfect; medium level uses its existing string) + subtitle bodyLarge ("একদম ঠিক!").
   - Score card (padding 16/20, gap 16): letter letterResult 72 · right column: score scoreL 48 in tertiary + caption "এবারের স্কোর" (cw). Keep count-up; bounce on perfect.
   - "তুমি ১ বার চেষ্টা করেছ। তোমার গড় অগ্রগতি ৯৩%।" bodyMedium centered.
   - Bottom buttons, gap 10: Primary "আবার অনুশীলন করো" (reset icon) · Tonal "পরেরটি: ই →" · Text "অগ্রগতি দেখো" → Progress.
   - Tip after attempt (when present): Tip banner above the buttons.

### 12b · Result — low score — existing, redesigned
Same layout, no confetti: 1 filled + 2 empty stars · headline "ভালো চেষ্টা!" (cw) in secondary · "আরেকবার লিখলে আরও তারা পাবে।" (cw) · score in secondary · tries/average line · Tip banner "আস্তে আস্তে প্রতিটা ফোঁটা ছুঁয়ে যাও।" (cw) · Primary "আবার অনুশীলন করো" · Text "অগ্রগতি দেখো". **Next hidden** (low score or last item, as today).

### 13 · Fill the blanks — category picker — existing, redesigned
1. Top bar: back → hub, "শূন্যস্থান পূরণ".
2. Content padding 4 / 20 / 20, gap 12:
   - Section "কতটা কঠিন" · 2 column choice cards (grid gap 12, padding 14): radio + সহজ / কঠিন · mini pattern (5 cells 26 tall radius 7, 13sp; Easy "অ _ ই _ উ", Hard "অ _ _ ঈ উ") · caption "ফাঁকা ঘর দূরে দূরে" / "পাশাপাশি ফাঁকা ঘরও থাকে" (cw).
   - Section "একটা বিভাগ বেছে নাও" (12 top) · Activity rows per language (Bangla: স্বরবর্ণ, ব্যঞ্জনবর্ণ, বাংলা সংখ্যা, সংখ্যা ও চিহ্ন · English: ছোট হাতের অক্ষর, বড় হাতের অক্ষর, সংখ্যা ও চিহ্ন) → Sequence.

### 14 · Handwriting model dialog — existing, redesigned
Dialog over the hub. "Offer to download" state: icon 56 primaryContainer + download · "একবার নামিয়ে নাও" (cw) · "শূন্যস্থান পূরণ খেলতে হাতের লেখা চেনার ফাইল লাগবে। একবার নামালে পরে ইন্টারনেট ছাড়াই চলবে।" (cw) · Info banner (14sp, padding 10/14, wifi 20) "ইন্টারনেট লাগবে। বড়দের জিজ্ঞেস করো।" (cw) · row, right-aligned: Text "পরে" (cw) · Primary "নামাও" + download icon (cw).
Other states, same shell (texts cw): Checking = spinner icon primaryContainer · Downloading = LinearProgressIndicator + Cancel · No internet / Waiting = wifi-off icon englishContainer, opens by itself when ready · Failed = reset icon errorContainer + Primary "আবার চেষ্টা করো".

### 15 · Fill the blanks — sequence — existing, redesigned
1. Top bar: back → picker, "শূন্যস্থান পূরণ", chip "সহজ"/"কঠিন".
2. Content padding 4 / 20 / 24, gap 14:
   - Sequence row (5–8 cells, gap 8, height 64; cells flex so 8 fit).
   - "বাদ পড়াটি ঘরে লেখো" bodyLarge 600 centered.
   - Canvas 320×320 (no guide until Hint).
   - Wrong-letter Tip banner (only when shown): "প্রায় হয়েছে! “আবার শুরু” চেপে আবার লেখো।" (cw).
   - Bottom: 2 equal Outline buttons, gap 12: "সাহায্য" (bulb) → Hint sheet · "আবার শুরু" (reset) → Restart dialog if ink.
Recognition after 0.8 s pause unchanged. Correct → cell becomes Filled, next blank becomes Current.

### 16 · Hint sheet — **NEW, replaces the instant Help (সাহায্য)**
ModalBottomSheet over Sequence: header (bulb circle 48 secondaryContainer + "সাহায্য লাগবে?" (cw)) · row gap 14: preview canvas 112×112 radius 20 with dotted guide in primary @ 55% + "ফোঁটা দেখালে লেখা সহজ হবে। তবে এই ঘরে সবচেয়ে বেশি ৭০% পাবে।" (cw) · Primary "ফোঁটা দেখাও" (cw) → today's hint behavior (show guide, cap 70) · Text "না, নিজে লিখি" (cw) → dismiss.

### 17 · Fill the blanks — sequence done — existing summary, redesigned
Top bar as 15 · sequence row with all blanks Filled · centered column (40 top, gap 12): stars 60/80/60 from sequence score · score scoreXL 56 primary · "তুমি ২টি ফাঁকা ঘর পূরণ করেছ। তোমার স্কোর ৭৩%।" titleMedium centered (2 lines) · Primary full-width "পরের সারি →" at bottom.

### 18 · Listen and learn — existing, redesigned
1. Top bar: back → hub, "শুনে শিখি".
2. Content padding 4 / 20 / 20, gap 10:
   - No-voice Info banner (only when TTS voice missing): "এই ফোনে বাংলা কথা বলার ভয়েস নেই। ফোনের সেটিংস থেকে ইনস্টল করতে বড়দের বলো।" (cw).
   - Section labels per group (স্বরবর্ণ, ব্যঞ্জনবর্ণ; English: A–Z).
   - LazyColumn rows, gap 10, each row gap 10: **Letter button** 72×64 radius 20 primary, letter 30sp Baloo 800 onPrimary, small speaker 14 at bottom-end 8 → speaks letter name · **Example button** flex, 64 tall, radius 20, surfaceContainerLowest, 2dp outlineVariant, padding 16, gap 12: speaker 24 primary + "অ তে **অজগর**" (18sp Hind 600, word bold primary) → speaks example.
English: english colors, Andika letters.

### 19 · Progress tab — existing, redesigned
1. Tab top bar "অগ্রগতি".
2. Content padding 0 / 20 / 12, gap 10:
   - Overall card: primaryContainer, radius 24, padding 14/16, gap 10: "সব মিলিয়ে" titleMedium + percentHeader right · bar 14 (on-container track) · chips on surfaceContainerLowest: "১টি শিখেছ" (cw, tertiary check) · "★ ৫টি শেষ" (cw).
   - 7 compact Activity rows: স্বরবর্ণ, ব্যঞ্জনবর্ণ, বাংলা সংখ্যা, সংখ্যা ও চিহ্ন, ছোট হাতের অক্ষর, বড় হাতের অক্ষর, আঁকা (subject colors) → Category detail.
3. Navigation bar (অগ্রগতি selected). Loading/empty/error unchanged.

### 20 · Progress — category detail — existing, redesigned
1. Top bar: back → Progress tab, category name.
2. Content padding 4 / 20 / 20, gap 16:
   - Header progress card.
   - Grid **4 columns**, gap 12 vertical / 10 horizontal, tiles height 96 radius 18 (same state styles as Letter tile): character 32sp · stars 13 · state 11sp ("শিখে ফেলেছ" cw / "শেষ হয়েছে" / "২ বার" / empty). Not tappable (as today).
   - Info banner (14sp) at bottom: "৩ বার লিখলে আর অন্তত একবার ৮০% পেলে অক্ষরটা শেখা হয়ে যায়।" (cw).

### Dark mode
Every screen uses the same layout with the dark scheme. Reference artboards: Home, Practice, Result, Progress detail.

---

## 6. Navigation

```
App start ─► System splash ─► BrandSplash
   first launch? ─ yes ─► Onboarding 2a ─► 2b ──শুরু করো──► Home   (onboarding removed from back stack)
                └ no ──────────────────────────────────► Home

Bottom bar (tab screens only):  শিখি = Home │ অগ্রগতি = Progress │ বড়দের জন্য = Grown-ups
   Tabs: single top, restore state, never stack copies (replaces navigateAboveHome).
   Back from Progress / Grown-ups → Home tab; Back from Home exits.

Home ─┬─ Language chip ─► Language sheet (NEW)
      ├─ Continue card ─► Practice
      ├─ বাংলা  ─► Bangla hub  ─┬─ 4 rows ─► Category grid ─► Practice
      │                         ├─ শূন্যস্থান পূরণ ─► [Model dialog if needed] ─► Picker ─► Sequence ─┬─ সাহায্য ─► Hint sheet (NEW)
      │                         │                                                             ├─ আবার শুরু ─► Restart dialog (NEW, if ink)
      │                         │                                                             └─ done ─► Summary ─► পরের সারি ─► Sequence
      │                         └─ শুনে শিখি ─► Listen
      ├─ ইংরেজি ─► English hub ─ (same pattern, English categories)
      └─ আঁকা  ─► Drawing grid ─► Practice

Practice ─┬─ আবার শুরু ─► Restart dialog (NEW, if ink)
          └─ trace finished (auto) ─► Result ─┬─ আবার অনুশীলন করো ─► Practice (same item, replaces attempt)
                                              ├─ পরেরটি ─► Practice (next item, replaces attempt; hidden on low/last)
                                              └─ অগ্রগতি দেখো ─► Progress tab
Progress tab ─► Category detail ─► Back
```
Unchanged rules: Result blocks system Back; Practice→Result→Practice pops the finished Practice; Math is one destination from both hubs with shared progress; route arguments unchanged. The navigation bar is hidden on every non-tab screen.

## 7. What is new and what it replaces

| NEW | Replaces / adds to | Behavior change |
|---|---|---|
| Bottom navigation bar | ModalNavigationDrawer + menu icon | none: same 3 destinations, one tap away |
| Onboarding 2a + 2b | Welcome (single page) | none: same language switch, About content, Continue logic |
| Language sheet | Language switch in drawer | none: same setting |
| Grown-ups tab | Drawer + About screen | none: About content + language switch in one place |
| Restart confirm dialog | Instant Reset on Practice / Fill sequence | asks only when ink exists |
| Hint sheet | Instant Help on Fill sequence | one extra confirm; same hint + 70 cap |
| Continue card, subject/row progress, tile stars, result stars | Plain pills / plain result | display only, from existing progress data |
