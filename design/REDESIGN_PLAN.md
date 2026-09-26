# BornoChitra UI redesign — execution plan

This file is the detailed plan for **Step 15 of `plan.md`**. `plan.md` says what the step is and when it is done; this
file says how it is carried out.

- **What the screens look like:** `design/DESIGN_SPEC.md` (source of truth) and `design/screens.pdf` (visual reference).
- **Branch:** `ui-redesign`. One commit per phase; stop for the user's OK after each phase.
- **PDF page map:** 1 design system · 2 splash · 3–4 onboarding · 5 Home · 6 language sheet · 7 Grown-ups ·
  8 Bangla hub · 9 English hub · 10 category grid · 11 drawing grid · 12 Practice · 13 restart dialog ·
  14 Result (great) · 15 Result (low score) · 16 fill picker · 17 model dialog · 18 fill sequence · 19 hint sheet ·
  20 fill done · 21 Listen and learn · 22 Progress · 23 Progress detail · 24 dark mode.
- Orange dotted underlines in the PDF only mark wording to check; they are not built.

## Decisions (agreed with the user)

### D1 · Fonts
- **Learning characters keep the font their tracing guide was derived from.** No guide, path data or scoring changes.
  - Bengali letters and numbers: Noto Sans Bengali **Bold**. The guides were derived from the phone's own system font
    (Noto Sans Bengali v2.001, `/system/fonts/NotoSansBengali-VF.ttf`), which the app already draws because it sets no
    font of its own. The Bengali learning style therefore keeps the platform font at Bold. Bundling Google Fonts'
    newer v3 file would draw ন differently from its guide.
  - English small letters: Andika Bold (`res/font/andika_bold.ttf`, already bundled).
  - English capitals, digits and math signs: Inter, pinned to wght 700 / opsz 14 (`res/font/inter.ttf`, already bundled).
- A learning character is any single letter, digit or sign shown to learn, trace or recognise: the Practice model
  letter, the Result letter, tiles in the category grids and Progress detail, the Continue-card tile, category lead
  glyphs on the hub, Progress and fill-picker rows, fill-the-blanks sequence cells and hint preview, and the Listen
  letter buttons. They all use **one shared letter style** (`letterStyle` / `BcLetterText` in `BcLetter.kt`), which
  picks the font from the character, so it can be changed in one place. Sizes, line heights and colours follow the
  spec; only the font family and weight differ.
- Everything else uses the spec's fonts: Baloo Da 2 (500/600/700/800), Hind Siliguri (400/500/600/700) and Andika Bold
  (the word "English" and the "Aa" mark). Only the weights in use are bundled; their OFL licenses are in
  `design/licenses/fonts/`. Hind Siliguri has no "→" glyph, which does not matter because arrows are icons.
- **This is an intentional difference from the PDF.** It is not "fixed" when screenshots are compared, and it is listed
  in the final list of differences.

### D2 · Icons
The 25 icons in `design/icons/drawable` are copied to `res/drawable` (`bc_ic_*`). All of them are 24×24 with a 2dp
round stroke, are tinted where they are used, and cover every icon the spec names.

### D3 · Bilingual lines
"অ্যাপটা কোন ভাষায় দেখবে?" with "Which language should the app use?", "অ্যাপের ভাষা · App language", and the
"App language" caption appear exactly as the design shows them, in both app languages (`translatable="false"` strings).

### Other agreed points
- If a child has finished an item enough times but the best score is still below the mastery score, the Continue
  card uses a second caption. Its numbers come from `MasteryRule`.
- Result wording follows the spec: the low score shows "ভালো চেষ্টা!" and "আরেকবার লিখলে আরও তারা পাবে।";
  "আবার অনুশীলন করো" is used at every level; there is no emoji.
- The splash follows the new design: a 176dp logo, the wordmark and the tagline.
- Subject % = completed items ÷ all items in the subject's categories. Math counts towards both Bangla and English.
- The star rule (best score → 1, 2 or 3 stars via `ScoreThresholds`) moves unchanged from `ProgressViewModel` to
  `core/model/StarRule.kt`.

## Phases (plan.md sub-steps 15.1–15.8)

| Phase | Scope | PDF pages | Status |
|---|---|---|---|
| 1 | Theme (light/dark), fonts, spacing, shapes, icons, shared components | 1 | Done (`b04b115`, `d462a7e`) |
| 2 | Navigation shell: bottom bar, Grown-ups tab, two-page onboarding, language sheet; drawer, About and Welcome removed | 3, 4, 6, 7 | Done |
| 3 | Home, Bangla hub, English hub, category grids, drawing grid | 5, 8–11 | Done |
| 4 | Practice, restart dialog, Result | 12–15 | Done |
| 5 | Fill picker, model dialog, sequence, hint sheet, done view, Listen and learn | 16–21 | Done |
| 6 | Progress tab, Progress detail, splash | 2, 22, 23 | Done |
| 7 | Polish: dark mode, 1.3× font scale, small phones, landscape Practice; unused code and strings; `NEW_STRINGS.md`; list of remaining differences | 24 | Done |
| 8 | Deferred items below, one at a time, each only after the user decides | — | Next |

## Verification loop (every phase)

1. Build the app and run the unit tests.
2. Run the instrumented tests (`connectedDebugAndroidTest`) on a phone (always reinstall for the testing). if phone is not connected, run the tests on the emulator.
3. Take screenshot every changed screen.
4. Compare each screenshot with its PDF page and with `DESIGN_SPEC.md`: colours, fonts, sizes, spacing, text and states.
5. **If every test passes and every screen matches the design, the phase is finished. If not, fix the design
   differences or the failing tests, and go back to step 1.** Intentional differences (D1) are not fixed; they are
   noted for the Phase 7 list.
6. `git add` new files and commit.
7. Tell the user what to test on the phone, then stop for the user's OK.

## Remaining differences from the design

Checked in Phase 7 on the Pixel_5_2 emulator at 360 × 800dp and 360 × 640dp, in light and dark, at 1× and 1.3× font
scale, in Bangla and English, and Practice and Result in landscape. Each item below is intentional. The first
group was agreed during the phases; the second is how the app handles sizes the design does not draw.

**Agreed differences**
- **Learning-character fonts (D1).** Letters, digits and signs use the font their tracing guide was derived from:
  Noto Sans Bengali Bold, Andika Bold, or Inter. The PDF uses Baloo Da 2.
- **Canvas (DEFER-1).** Guide dots, guide line and ink keep today's sizes; only their colours follow the spec. The
  guide keeps its darker colour (user correction b), and passed dots are not coloured.
- **Fill-the-blanks Help (DEFER-2).** "সাহায্য" is disabled after use rather than hidden.
- **Drawing pictures.** Tiles, the Practice model, Result and Progress show the exercise's own strokes, so the line
  is horizontal (the PDF draws it diagonal) and the house is the exercise's roof and walls (not the house icon).
- **Stars.** Filled stars keep their thin outline in light mode (user correction c).
- **Dialog scrim.** It is black at the spec's strength (48% light, 62% dark), not `#14101E`, because a window dim is
  always black.
- **Progress detail tiles.** They have no mastered check badge, as the PDF draws them; the status line says
  "শিখে ফেলেছ" instead. The status is 12sp (user correction d), not 11sp, with a 4dp inner padding.

**Sizes the design does not draw**
- **Short screens.** Every screen scrolls when its content is taller than the window, including onboarding page 1,
  whose caption falls below the fold on a phone shorter than 800dp. On Practice and in the fill sequence, the canvas
  shrinks to the space left, and the ink scales with it.
- **1.3× font scale.** Chip rows wrap to a second line. Button labels shrink to fit one line, down to 13sp; the
  fill sequence's two half-width buttons need this. The Progress detail status shrinks to fit a 4-column tile, down
  to 9sp, which with the 1.3× scale is still about 12sp on screen. On Progress detail, the Info banner stays pinned
  at the bottom as designed, so on a short phone with large text the grid above it scrolls in a smaller space.
- **Result confetti.** It is drawn over the top bar as designed and moves with the content when Result scrolls, so it
  never lands on the score card.
- **Landscape.** Practice puts the canvas on the left and the model, instruction, tip and button on the right, as it
  did before the redesign.

## Deferred items (nothing is built for these until the user decides)

### DEFER-1 · Canvas look compared with the spec
**The problem.** The spec's canvas differs from today's in four ways:

| | Spec | Today (on a 320dp canvas) |
|---|---|---|
| Guide dots | 13dp across, every 19dp | 16dp across (radius 2.5 units), every 19.2dp (6 units) |
| Guide line | 3dp, guide colour at 60% | 4.8dp (1.5 units) |
| Finger ink | 9dp, primary | 7.2dp (2.25 units), was secondary |
| Traced dots | each dot the finger has passed turns primary | not possible today: the canvas only learns when the whole exercise is finished |

The dot spacing only affects drawing, but every guide was checked on the device at today's dot size: each guide
was tuned so that every dot lies on the glyph's ink within the 2.5-unit radius. Changing the size changes what the
child sees on top of the letter. Colouring each passed dot needs the tracker's per-dot progress
(`MultiStrokeTracker`) to be sent to the canvas on every move. That is new output from the engine, not only a restyle.

**What could be done.**
- (a) Keep today's dot size, spacing and line width.
- (b) Change the colours only: guide colour for dots and line, primary for ink. **Done in Phase 1**, because it is
  pure colour.
- (c) Optionally make the ink 9dp wide. This is visual only and does not affect scoring.
- (d) Optionally expose "dots reached so far" from the tracker as a read-only list and colour those dots primary. This
  touches the engine and needs tests, but does not change scoring.

**Status:** waiting for the user's decision on (c) and (d). Phase 1 applied (a) and (b) only.

### DEFER-2 · Hide "সাহায্য" after the hint is used
See `design/plans/HINT_HELP_HIDE.md`. Until then, Phase 5 keeps today's behaviour: the button is disabled after
it is used. Before this item is built, the user gets a prompt describing it, and afterwards the result is shown.
