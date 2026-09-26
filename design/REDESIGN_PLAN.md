# BornoChitra UI redesign — execution plan

Source of truth for **what** the screens look like: `design/DESIGN_SPEC.md` + `design/screens.pdf`.
Branch: `ui-redesign`. One commit per phase; stop for the user's OK after each phase.

## Decisions (agreed with the user)

### D1 · Fonts
- **Learning characters keep the font their tracing guide was derived from.** No guide, path data or scoring changes.
  - Bengali letters and numbers: Noto Sans Bengali **Bold**. The guides were derived from the phone's own
    system font (Noto Sans Bengali v2.001, `/system/fonts/NotoSansBengali-VF.ttf`), which the app draws today by
    setting no font. So the Bengali learning style keeps using the platform font at Bold. Bundling Google Fonts'
    v3 file would redraw ন differently from its guide.
  - English small letters: Andika Bold (`res/font/andika_bold.ttf`, bundled already).
  - English capitals, digits and math signs: Inter, pinned wght 700 / opsz 14 (`res/font/inter.ttf`, bundled already).
- A learning character is any single letter, digit or sign shown to learn, trace or recognise: the Practice model
  letter, the Result letter, tiles in category grids and Progress detail, the Continue-card tile, category lead
  glyphs on hub/Progress/Fill-picker rows, Fill-the-blanks sequence cells and hint preview, Listen letter buttons.
  It is built as **one shared letter style/component** (`BcLetterText` + `letterStyle(...)`) that picks the font
  from the character/category, so it can be changed in one place. Sizes, line heights and colours follow the spec;
  only family and weight differ.
- Everything else uses the spec fonts: Baloo Da 2 (500/600/700/800), Hind Siliguri (400/500/600), Andika Bold
  ("English", "Aa"). Only used weights are bundled, with their OFL license files in `design/licenses/fonts/`.
- **Intentional difference from the PDF** — not to be "fixed" at screenshot comparison, and listed in the final
  differences list.

### D2 · Icons
The 25 icons in `design/icons/drawable` are copied to `res/drawable` (`bc_ic_*`). All are 24×24, 2dp round
stroke, tinted at use, and cover every icon the spec names.

### D3 · Bilingual lines
"অ্যাপটা কোন ভাষায় দেখবে?" + "Which language should the app use?", "অ্যাপের ভাষা · App language", and the
"App language" caption are shown as the design shows them in both app languages
(`translatable="false"` strings).

### Other agreed points
- Continue caption when completions are done but the best score is below the mastery score: a second string
  (numbers from `MasteryRule`).
- Result wording per the spec: low = "ভালো চেষ্টা!" + "আরেকবার লিখলে আরও তারা পাবে।"; "আবার অনুশীলন করো" at
  every level; no emoji.
- Splash follows the new UI: logo 176, wordmark, tagline.
- Subject %: completed items ÷ total items of the subject's categories (math counts in both Bangla and English).
- The star rule (best score → 1/2/3 via `ScoreThresholds`) moves unchanged from `ProgressViewModel` to `core/model`.

## Phases
1. Theme (light/dark), fonts, spacing, shapes, icons, shared components (spec 1–4, PDF 1).
2. Navigation shell: bottom bar, Grown-ups tab, 2-page onboarding, language sheet; drawer, About, Welcome removed.
3. Home, Bangla hub, English hub, category grids, drawing grid.
4. Practice, restart dialog, Result.
5. Fill picker, model dialog, sequence, hint sheet, done view, Listen and learn.
6. Progress tab, Progress detail, splash.
7. Polish (dark mode, 1.3× font scale, small phones, landscape Practice), unused code/strings, `NEW_STRINGS.md`,
   list of remaining differences.
8. **Deferred items below**, one at a time, each only after the user decides.

## Deferred items (decide later; nothing built for them yet)

### DEFER-1 · Canvas look vs the spec (the user decides at implementation time)
**The problem.** The spec's canvas differs from today's canvas in three ways:

| | Spec | Today (at a 320dp canvas) |
|---|---|---|
| Guide dots | 13dp diameter every 19dp | 16dp (radius 2.5 units) every 19.2dp (6 units) |
| Guide line | 3dp at 60% guide colour | 4.8dp (1.5 units) |
| Finger ink | 9dp, primary | 7.2dp (2.25 units), secondary |
| Traced dots | each dot already passed turns primary | not available: the canvas only learns "whole exercise finished" |

The dot spacing is used only for drawing, but the guides were checked on the device at today's dot size
(every guide was tuned so each dot lies on the glyph's ink within the 2.5-unit radius). Changing size
changes what the child sees on top of the letter. Per-dot colouring needs the tracker's per-dot coverage
(`MultiStrokeTracker`) to be exposed to the canvas each move: that is new engine output, not only a restyle.

**What I would do if asked.** (a) Keep dot size/spacing and line width as today, (b) switch colours only
(guide token, ink primary) — this part is done in Phase 1 as it is pure colour, (c) optionally set ink width
to 9dp (visual only, no scoring impact), (d) optionally expose "dots reached so far" from the tracker as a
read-only list and colour them primary (touches the engine; needs tests; no scoring change).
**Status: waiting for the user's decision on (c) and (d).** Phase 1 applies (a) + (b) only.

### DEFER-2 · Hide "সাহায্য" after the hint is used
See `design/plans/HINT_HELP_HIDE.md`. Phase 5 keeps today's behaviour (button disabled after use). When this
item is implemented, a prompt is given to the user first and the result is shown after.

## Phase checklist (every phase)
Build → install on an emulator → screenshot every changed screen (light; dark in Phase 7) → compare with the
PDF page and fix → run unit tests (+ UI tests where affected, on the emulator since they wipe app data) →
`git add` new files → commit → tell the user what to test on the phone → stop.
