# Barnacitro — Implementation Plan

> **Purpose:** This document lists the requirements and execution order for the current task. It defines **what** to build. `CLAUDE.md` defines **how**.
>
> **Agent usage:** Execute incrementally, one numbered step at a time. Do not implement the whole plan in one pass.

---

# 0. Agent Execution Instructions

General development conventions — architecture, code quality, testing, build/verification, git, and task-reporting requirements — are defined in `CLAUDE.md` and apply to every step below. This section adds only the rules specific to executing this plan.

## PLAN EXECUTION RULES

1. Read the entire `plan.md` before making changes.
2. Implement exactly **one numbered step at a time** (a lettered sub-step such as 1.3 counts as one step). Do not implement multiple steps unless explicitly instructed.
3. Do not move to the next step automatically unless explicitly instructed.
4. Do not use placeholder implementations for a real feature.
5. Keep the tracing engine independent from ViewModel, Room, Hilt, and Compose UI implementation details.
6. Before changing behavior, inspect the existing code and reuse the existing patterns (exercise catalog, category screens, Home/Progress category buttons, progress calculation).
7. Glyph stroke geometry is derived from the rendered glyph, never drawn by eye (see the glyph stroke derivation workflow in project memory). Every new letter/digit is checked on a device before its step is closed.

## STEP COMPLETION

A step is complete only when its Definition of Done is satisfied.

Do not mark a step complete merely because the application compiles.

After completing a step, **STOP and wait for the next instruction.**

---

# 1. Context

The app teaches handwriting by tracing. Today it ships 11 vowels, **5 consonants (ক খ গ ঘ ঙ)** and a set of drawings. Each `Exercise` (`core/model/Exercise.kt`) has an `ExerciseType` (`VOWEL`, `CONSONANT`, `DRAWING`); static content lives in `core/content/*Exercises.kt` and is collected in `ExerciseCatalog`. Home and Progress have one button per category. Progress per exercise is stored through `ProgressRepository` (Room: `exercise_progress`, `practice_sessions`).

This plan extends the app in five areas, in this order:

| # | Area | Outcome |
|---|------|---------|
| 1 | Remaining consonants | All 39 Bengali consonants are practicable |
| 2 | Practice sets | 5 or 10 repetitions of one character on one screen; averaged progress |
| 3 | English characters | a–z and A–Z |
| 4 | Math characters | 1–20 and basic operators |
| 5 | Fill in the blanks | Sequences with missing items the child fills in |

## The 39 consonants

| Group | Letters | Status |
|-------|---------|--------|
| ক-বর্গ | ক খ গ ঘ ঙ | **Done** |
| চ-বর্গ | চ ছ জ ঝ ঞ | Steps 1.1–1.5 |
| ট-বর্গ | ট ঠ ড ঢ ণ | Steps 1.6–1.10 |
| ত-বর্গ | ত থ দ ধ ন | Steps 1.11–1.15 |
| প-বর্গ | প ফ ব ভ ম | Steps 1.16–1.20 |
| অন্তঃস্থ / ঊষ্ম | য র ল শ ষ স হ | Steps 1.21–1.27 |
| অন্যান্য | ড় ঢ় য় ৎ ং ঃ ঁ | Steps 1.28–1.34 |

5 + 5 + 5 + 5 + 5 + 7 + 7 = **39**. Remaining to add: **34**.

---

# 2. Step 1 — Remaining Consonants

## Requirement

Add the 34 missing consonants so the Consonants screen lists all 39 in the order above. Each behaves exactly like the existing five: dotted guide, tracing, scoring, result, progress.

## Implement (sub-steps 1.1–1.34, **one consonant per step**)

- Each sub-step adds exactly **one** letter, then stops. Do not add a second letter in the same step.
- Derive the letter's strokes from the rendered Noto Sans Bengali glyph (ink bbox fit; fit by width for letters wider than tall) and add it to `ConsonantExercises.kt` with id `consonant-<romanised>` and the next `order` value.
- Author strokes in the natural hand-writing order and direction; the matra is drawn last where the letter has one. Letters without a full headline (like ঙ) follow the actual glyph.
- ড় ঢ় য় need their dot as a separate stroke; ং ঃ ঁ are mark-only shapes; ৎ is a single letter (khanda-ta), not a conjunct.
- Per letter: `check.py` (every sample on ink), overlay review, unit tests for the catalog, then a device trace at 95%+.
- The last sub-step (1.34) also confirms the Consonants screen scrolls and lays out 39 items at 720x1280 and 720x1600, and that the Home/Progress consonant bar measures against 39.

## Per-letter recipe

Worked out while implementing 1.1; follow it for every remaining letter.

### Toolchain

The derivation scripts are **not in the repository** — they live in the Claude scratchpad and are
copied forward between sessions. Copy `glyph.py`, `canvas.py`, `skel_branches.py`, `check.py`,
`overlay.py` and `noto2.ttf` (Noto Sans Bengali, the font Android draws the character with above the
canvas) from the newest scratchpad that has them into the current one before starting. A letter
wider than it is tall uses the width-fit set instead — `wide_norm.py` (branches, skeleton/ink grids
and the pen half-width), `wide_ink.py`, `wide_check.py` and `wide_overlay.py`, written for জ in 1.3
and taking the character as an argument. `tall_norm.py` is its height-fit mirror, written for ট in
1.6, so a tall letter's `<letter>_build.py` can use the same setup/branches/half_width API.
`zoom_ref.py` crops the reference glyph and the guide out of a Practice screenshot for
comparison (written for ঠ in 1.7). `calib_practice.py` and `trace_letter.py` do the device
check for any letter, except that `calib_practice.py` measures the *topmost* row of guide dots as
the matra: a letter whose headline is not its highest stroke, or whose headline is broken into two
bars, needs `jho_calib.py` instead (written for ঝ in 1.4 — it takes the densest row of dots and
measures only the run left of the break), and a letter with no headline at all needs `nio_calib.py`
(written for ঞ in 1.5 — it reads the scale and the dot radius off the whole guide's bounding box,
so it works for any letter and needs no matra).

1. `skel_branches.py <letter>` — the ordered centreline branches in the 0..100 canvas, and the mean
   pen half-width. Branches are returned longest first, so identify each one before using it.
2. Print the ink and skeleton grids (`canvas.py`) as ascii to see the shape, its junctions and where
   one stroke should end and the next begin.
3. Write `<letter>_build.py` exporting `STROKES = {name: [(x, y), ...]}`: orient each branch so the
   pen travels the way the letter is written, resample at ~3 canvas units, round to whole units.
   A closed loop's two ends sit on the same junction, so orient it by its second point, not by
   which end is nearer.
4. `check.py <letter> <letter>_build` — **every stroke must report off-ink 0**, and its centreline
   distance must stay well inside the mean pen half-width.
5. `overlay.py <letter> <letter>_build out.png` — look at the guide drawn over the glyph.
   Then, once it is on the device, `zoom_ref.py <practice-screenshot> out.png` — the reference
   glyph and the dotted guide cropped from the same screenshot and scaled to one height, side by
   side. Off-ink 0 only says the guide sits *on* the ink; this is what shows whether it has the
   letter's shape and flow. A stroke split must come from the glyph (where would the pen lift?),
   not from a skeleton junction — a thick matra knots every branch touching it, and following
   that knot leaves strokes meeting the headline at different points and loops hanging open.
6. `apply_<letter>.py` — append the Kotlin `Exercise` before the closing `)` of `consonantExercises`,
   with a KDoc recording how the letter was derived and why the strokes were split where they were.

### Constraints the catalog test already enforces

- **Normalization.** `canvas.py`/`skel_branches.py` fit the *ink* bbox to y 7..90 with x centred.
  A letter wider than it is tall must be fitted by width (x 3..97) with y centred instead
  (`wide_norm.py`; `ko_norm.py` was the ক-only original), or its matra leaves the canvas. Check the
  aspect ratio the render prints before choosing — জ is 1.40 and height-fitting it puts the headline
  past x=100. `ExerciseCatalogTest` checks every point is in 0..100 and that the guide's bbox centre
  is within 3 units of (50, 50).
- **Stroke ids** must start with the exercise id, be unique, and a `-matra` stroke must be last.
- **`order`** must stay contiguous from 1 within the type, so each letter takes the next value.

### Authoring conventions

- **Stroke length.** Keep one stroke under roughly 140 canvas units — a child cannot hold a longer
  pass. ঙ (275) and চ (185) are both split. Split only at a landmark the child can see, such as a
  corner, a neck or a foot, never at an arbitrary midpoint.
- **Matra.** A straight `StrokePoints.line` at the skeleton's headline y, its extent taken from the
  ink and inset ~2 units so the guide stops short of the end caps. Written last.
- **Straight strokes** use `StrokePoints.line`, curved or slightly bent ones the dense polyline from
  the skeleton — a line through a bend drifts off the centreline.
- **Difficulty** reflects the letter's own complexity, not a progression through the alphabet.
  `ADVANCED` also switches on the extra tip via `TipRules.DEFAULT_DIFFICULT_FROM`.

### Tests to update for every letter

`ExerciseCatalogTest` holds two hand-maintained assertions that fail until the new letter is added:
the consonant title string (`কখগঘঙচছজ` after 1.3) and the per-exercise stroke-count map. Everything
else in that file passes on its own. Baseline after 1.1: **336 unit tests, 0 failures**.

### Device check

- Navigate Home → ব্যঞ্জনবর্ণ (360, 697 at 720x1600) → the letter's cell in the 3-column grid
  (columns x = 136 / 360 / 583; rows y = 351 / 560 / …). From the 10th letter the grid needs
  scrolling before the cell is tappable.
- **Re-measure the canvas mapping from a screenshot** rather than reusing remembered coefficients —
  it moves whenever the Practice layout changes. The dotted guide is sampled at `dotSpacing = 6f`
  canvas units with the first dot on the path start, so the matra's dot centres give both scale and
  offset. It is `sx = 31.8 + 6.556x`, `sy = 503.9 + 6.556y` as of 1.1.
- Densify the synthetic trace to ~1.5 canvas units; feeding only a stroke's authored points leaves
  most of the guide untraced and the attempt silently never completes.
- Read the score back with `run-as com.bornochitra cat databases/bornochitra.db`. A faithful trace
  of a correctly derived letter scores 95–99.

## Sub-steps

- [x] 1.1 চ
- [x] 1.2 ছ
- [x] 1.3 জ
- [x] 1.4 ঝ
- [x] 1.5 ঞ
- [x] 1.6 ট
- [x] 1.7 ঠ
- [x] 1.8 ড
- [x] 1.9 ঢ
- [ ] 1.10 ণ
- [ ] 1.11 ত
- [ ] 1.12 থ
- [ ] 1.13 দ
- [ ] 1.14 ধ
- [ ] 1.15 ন
- [ ] 1.16 প
- [ ] 1.17 ফ
- [ ] 1.18 ব
- [ ] 1.19 ভ
- [ ] 1.20 ম
- [ ] 1.21 য
- [ ] 1.22 র
- [ ] 1.23 ল
- [ ] 1.24 শ
- [ ] 1.25 ষ
- [ ] 1.26 স
- [ ] 1.27 হ
- [ ] 1.28 ড়
- [ ] 1.29 ঢ়
- [ ] 1.30 য়
- [ ] 1.31 ৎ
- [ ] 1.32 ং
- [ ] 1.33 ঃ
- [ ] 1.34 ঁ

## Definition of Done (applies to every sub-step)

- [ ] The one letter is added with strokes derived from its rendered glyph
- [ ] Every guide sample lies on the glyph's ink
- [ ] It completes at 95%+ on a faithful synthetic trace on the device
- [ ] `ExerciseCatalogTest`'s consonant title string and stroke-count map include the new
      letter, and its id, `order`, canvas-bounds, centring and matra-last assertions pass
- [ ] `ConsonantsViewModelTest` still passes
- [ ] Verified by running the app

Step 1 as a whole is done when all 34 sub-steps are checked, the catalog test asserts 39 consonants, and consonant progress on Home/Progress is computed against 39.

## Notes

- **1.1 চ** — derived from the glyph's centreline: below the headline চ is one closed bowl whose left edge is the stem, so it is `stem` (matra line down to the foot), `body` (foot round the bottom, up the right, back along the top to the stem) and `matra`. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).
- **1.2 ছ** — চ plus a ২-shaped lobe and tail on the right, so it is `bowl` (the whole চ part in one movement — only ~100 canvas units here, where চ alone is 185 and has to be split), `lobe` (out of the top of the bowl, clockwise down the right and back left along the bottom bar to its blunt tip), `tail` (from the bottom of the lobe down to the right) and `matra`. The bottom bar belongs to the lobe, not the tail: the ink there is one pen width thick, so the tail cannot reach that far left. Both flat-cut ends are where the skeleton forks into two prongs, and each guide stops 2 units short of the cut's midpoint. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).
- **1.3 জ** — the first wide consonant since ক: aspect 1.40, so it is fitted by width with y centred. Below the headline it is a spiral and a hook. The spiral is ~165 canvas units in one movement — down from the headline, counter-clockwise round the inner bowl, up to a sharp point in the middle, then reversing and running clockwise round the outer bowl to a flat cut on the far left — so it is split at the point, where the pen turns back on itself and the skeleton grows a spur into the ink's wedge. `curl` runs up into the point, `sweep` restarts at the junction just below it, `hook` hangs off the headline to the right and descends, `matra` last. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).
- **1.4 ঝ** — wider than tall (aspect 1.19), so fitted by width like জ. Its headline is genuinely broken: a long bar over the body and a short one over the right post, with 14 canvas units of blank canvas between them, so it takes two strokes. Below it the body is ক's shape exactly — `wedge` leaves the middle stem, runs down-left, rounds the blunt left point (an arc at x=12, where the skeleton forks into a spur to the ink's tip) and comes back down-right to the stem's foot — so it is written as ক's knot is: `wedge`, then `stem`. `arm` then leaves the stem halfway down and runs into `post`, the right vertical, which rises above the headline as খ's and গ's stems do. `cap` and `matra` are the two headline bars, written last; only the long one carries the `-matra` name the catalog orders on. Every guide sample is on ink; traced on the RMX3624 it completed at 97% (PERFECT).

- **1.5 ঞ** — wider than tall (aspect 1.40), so fitted by width like জ and ঝ; like ঙ it has no headline bar at all, so no stroke is a matra. It is a middle stem carrying a sail on the left, two bowls on the right and a bar-and-base along the bottom. `stem` is written first, as চ's is, because everything else hangs off it. `hood` then leaves the stem's top, rises over the apex, comes down the left flank and stops in the filled ball that ends the sail — the outline's own terminal there is a disc, not a cut, and the skeleton's branch already ends at its centre, so the sail is drawn *into* it the way ক's lobe is drawn into its ball. The right half is one spiral of ~120 canvas units out of the stem's top; the leftward point between its two bowls is a tapering wedge rather than a pen terminal — the ink narrows to a rounded tip and the skeleton grows a spur into it, so the pen turns back on itself there — and it is split at that point exactly as জ is: `bowl` runs into the point, `sweep` restarts on the far side of the turn and runs round the lower bowl back to the stem. `base` is the last stroke, in the place a letter with a headline would write its matra: the left bar from its flat-cut top, down, round the corner and right along the bottom to the stem's foot. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).

- **1.6 ট** — taller than it is wide (aspect 0.64), so it is fitted by height like চ and ছ. `hook` is written first, being the topmost and leftmost stroke: down the short bar from its flat-cut top, then right across the whole letter above the headline and down onto the headline at the far right. `stem` is straight from the headline to where its foot starts to curve, and `bowl` carries that curve — out of the filled ball that heads it, down the right flank, round the bottom and back left into the stem's foot, the way চ's body closes onto its own stem. The ball is a blunt filled terminal, not a pen cut: the skeleton collapses it to a single point at its centre, so the bowl is drawn out of that centre exactly as ক's lobe is drawn into its own ball. `matra` is the full headline, last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.6% (PERFECT).

- **1.7 ঠ** — taller than it is wide (aspect 0.61), so it is fitted by height like ট. The glyph is one continuous movement broken only by the headline: down the flourish, through the matra, on into the bowl's left arm, out round the bottom-left, along the foot, up the right flank and back onto the matra. The skeleton does not say that on its own — the matra is 7 canvas units thick, so skeletonizing it knots the flourish, both halves of the headline and the bowl's two arms into a small diamond below it, stopping the flourish's branch at x=45 and reporting both of the loop's ends at the same junction point. Read literally those junctions land the flourish and the bowl on the headline at different places and leave the loop hanging open, which is how the first attempt at this letter came out; the two strokes are joined at the headline crossing instead, the only landmark in the letter a child can see, and the split also keeps each stroke short enough to hold in one pass. So `hook` runs from the rounded terminal at the top down onto the matra, `bowl` takes over at that same point and ends five units along the headline — under one dot spacing — so the loop reads as closed, and `matra` is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.8 ড** — wider than it is tall (aspect 1.23), so fitted by width like ক, জ and ঝ. Below the full headline the letter is one continuous movement: down the short middle stem, right and down through the shallow V, up to the sharp point at the top right, then back down the right flank, round the bottom and up the long left arm to its flat cut. That is ~190 canvas units, too long for one pass, so it is split at the point — where the arm and the flank converge into a single tapered tip and the pen turns back on itself, the same split জ and ঞ take. `arm` runs from the matra crossing into the V and up into the point, `bowl` restarts at the junction just below it and ends 2 units short of the midpoint of the left arm's flat-cut terminal, and `matra` is last. Neither tip touches the headline: below the matra the letter hangs from the stem alone. `arm` ends one unit past the skeleton's spur, on the apex of the ink wedge: that is where the pen turns, and it also carries the stroke to 78.9 canvas units. The guide drops a dot every 6 units from a stroke's start, so a stroke that stops just short of a multiple of the spacing — 77.9 at the spur's end — draws nearly a whole spacing of bare path past its last dot, which showed on the device as a grey tail hanging off the tip. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).

- **1.9 ঢ** — a shade taller than it is wide (aspect 0.95), so fitted by height like ট and ঠ. Below the headline the glyph is one continuous movement of ~156 canvas units: down the long stem, round the bottom bowl, up the right flank and over the top into the filled ball that ends the curl. That is too long for one pass, so it is split at the foot, where the stem stops being straight and the curve begins — the split ট takes, and the only landmark below the matra a child can see. `stem` is therefore the straight run from the headline to the foot, `bowl` carries every curve and is drawn into the centre of the ball the way ক's lobe is drawn into its own ball, and `matra` is the full headline inset 2 units from each end cap, last. Every guide sample is on ink; traced on the RMX3624 it completed at 98.6% (PERFECT).

---

# 3. Step 2 — Practice Sets and Averaged Progress

## Requirement

One screen practises **one character repeated 5 or 10 times**. The child traces every repetition; the screen shows the set's progress; the character's overall progress is the **average** of all its repetitions.

## Implement

- **Set model.** A `PracticeSet` is one `Exercise` plus a set size. Set size is 5 or 10, chosen from a small selector on the Practice entry (default 5). Vowels, consonants, English and math all use it; drawings keep single-attempt practice.
- **Screen.** Repetitions are shown as a scrollable grid/list of tracing cells (one `TracingInputCanvas` per cell, each with its own dotted guide). Each cell is traced independently, is marked done when complete, and shows its own score. Reset clears one cell; a set-level control restarts the set.
- **State (MVI).** Extend `PracticeState`/`PracticeEvent` with per-cell state (`cells: List<CellState>` — attempt id, score, completed) and events such as `CellCompleted(index, score)`, `CellReset(index)`, `SetSizeChanged(size)`. The ViewModel owns the state; Composables stay stateless.
- **Set progress.** Shown on the Practice screen as completed cells / total and the running average score of completed cells. This is derived state in the ViewModel, not stored.
- **Character progress = average.** Every completed cell is persisted through the existing `ProgressRepository.savePracticeResult` (one `PracticeResult` per cell). The character's progress is the average score across all its recorded cells, computed in the repository (a DAO `AVG` over `practice_sessions`, exposed on `ExerciseProgress` as `averageScore`). Prefer this over adding stored columns so no Room migration is needed; if a migration turns out to be required, add it with an exported schema and a migration test.
- **Consumers.** Progress per-exercise rows and the category bars use the averaged value; mastery (`MasteryRule`) keeps its meaning and is re-evaluated against the average once the set is complete. Existing scoring tests stay unmodified.
- **Result.** The Result screen after a set shows the set average and per-cell scores.
- The tracing engine and scoring are unchanged; only how many canvases the screen hosts changes.

## Definition of Done

- [ ] Practice shows 5 cells by default and 10 when selected
- [ ] Each cell traces, scores and resets independently
- [ ] Set progress (done/total, running average) is shown and correct
- [ ] Every completed cell is saved through `ProgressRepository`
- [ ] Character progress is the average of its cell scores and appears on Progress
- [ ] Existing tracing and scoring tests pass unmodified
- [ ] Unit tests cover cell state transitions, set average, and the repository average (including a character with no cells)
- [ ] Verified by running the app: finish a 5-cell set, then a 10-cell set, and check Progress

---

# 4. Step 3 — English Characters

## Requirement

Add English letters: lowercase **a–z** and capital **A–Z**, practised like the Bengali letters.

## Implement

- Add `ExerciseType.ENGLISH_SMALL` and `ExerciseType.ENGLISH_CAPITAL`; add `EnglishSmallExercises.kt` and `EnglishCapitalExercises.kt`, registered in `ExerciseCatalog`. Ids `english-small-a`, `english-capital-a`, and so on; titles are the glyphs.
- Stroke geometry is derived from a rendered glyph using the same workflow. Use a print/school-style single-stroke-friendly font so `a`, `g`, `y` follow the shape children are taught; record the chosen font in the source header comment. Author natural stroke order and direction (top-to-bottom, left-to-right, counter-clockwise for round letters).
- Category plumbing: an English category screen following `ConsonantsScreen`/`ConsonantsViewModel`, a route in `BcDestination`/`BcNavHost` only if the existing pattern needs one, Home buttons for **Small letters** and **Capital letters**, and matching Progress buttons/sections (reuse the existing hub: `ProgressState.openCategory`).
- `LearningProgress`, `ProgressRepositoryImpl`, `HomeViewModel` and `ProgressViewModel` gain the new categories; overall progress includes them.
- Strings live in string resources, with the Bengali UI copy consistent with existing category names.
- Step 2 practice sets apply to these letters without extra code.

## Definition of Done

- [ ] 3.1 Category plumbing (type, screen, Home and Progress buttons, progress aggregation) proven with a–e and A–E
- [ ] 3.2 Small letters f–z added
- [ ] 3.3 Capital letters F–Z added
- [ ] Every guide sample lies on the glyph's ink; each letter completes at 95%+ on a device trace
- [ ] Catalog test asserts 26 small + 26 capital, unique ids, contiguous `order`
- [ ] ViewModel and progress-aggregation tests cover the new categories
- [ ] Verified by running the app: Home → English → practise → Progress

---

# 5. Step 4 — Math Characters

## Requirement

Add math characters: numbers **1 to 20**, plus the basic operators, practised like letters.

## Implement

- Add `ExerciseType.MATH`; `MathExercises.kt` registered in `ExerciseCatalog`. Ids `math-1` … `math-20`, `math-plus`, and so on.
- Digits 0–9 have their own derived strokes. **10–20 are composed from digit strokes** by a small content helper that lays two digits side by side inside the 0..100 canvas (scaled and offset), rather than 11 hand-derived glyphs. Composition keeps stroke order: left digit, then right digit.
- Operators `+ − × ÷ =` are added last as a separate sub-step.
- Category plumbing as in Step 3 (screen, Home button, Progress button/section, aggregation).
- Sequence for later steps: math items are ordered by numeric value so Step 5 can build "next number" blanks.

## Definition of Done

- [ ] 4.1 Digits 1–9 and category plumbing
- [ ] 4.2 10–20 composed from digit strokes, each fits within the canvas
- [ ] 4.3 Operators + − × ÷ =
- [ ] Guides sit on the rendered glyph; each item completes at 95%+ on a device trace
- [ ] Catalog test asserts the expected items, unique ids and numeric `order`
- [ ] Unit tests cover the composition helper (bounds, order) and the new category in aggregation
- [ ] Verified by running the app

---

# 6. Step 5 — Fill in the Blanks

## Requirement

Show a sequence with gaps, for example **a, _, c, d, _, e**. The child fills each gap by tracing the missing item.

## Implement

- **Sequences** come from the ordered catalog of a category: vowels, consonants, small letters, capital letters, numbers. Category is chosen from a new **Fill in the blanks** entry (Home button) then a category picker.
- **Generation** is a pure, seedable function (`BlankSequenceGenerator`) producing a window of 5–8 consecutive items with 1–3 blanks (never the first item, blanks may be adjacent only at the higher difficulty). Deterministic given a seed, so it is unit-testable.
- **Interaction.** Shown items are static glyphs. Each blank is a tracing cell (reusing the existing tracing canvas and scoring) with no dotted guide shown: the child recalls the item and traces it freehand, and a hint control reveals the dotted guide (using a hint lowers that blank's score cap; rule documented in code and tested).
- **State (MVI).** `FillBlanksState` (sequence, per-blank status), events (`BlankCompleted`, `HintUsed`, `NextSequence`), owned by `FillBlanksViewModel`.
- **Progress.** Each completed blank is saved through `ProgressRepository.savePracticeResult` against the missing item's exercise id, so it counts in that character's average (Step 2) and in category progress.
- A finished sequence shows a short result and a "Next sequence" action.

## Definition of Done

- [ ] Fill-in-the-blanks entry on Home, category picker, sequence screen
- [ ] Sequences generated from vowels, consonants, English and math with correct gaps
- [ ] Each blank traces and scores; hint reveals the guide and applies its rule
- [ ] Results feed per-character averages and category progress
- [ ] Unit tests: generator (seeded, bounds, blank positions, category ends), ViewModel transitions, hint scoring rule
- [ ] Verified by running the app for at least a Bengali and an English sequence

---

# 7. Step 6 — Integration and Regression

## Requirement

Confirm all new categories and modes behave as one coherent product.

## Implement

- Progress screen lists every category (Vowel, Consonant, Small, Capital, Math, Drawing) with correct bars; overall progress includes all.
- Home layout fits the added buttons on 720x1280 and 720x1600.
- Continue/resume points at the most recent unmastered exercise across all categories.
- Update `CriticalFlowTest`/androidTest and `ProgressRoomTest` for the new types.

## Definition of Done

- [ ] Home and Progress show every category; layouts fit on small and tall screens
- [ ] Full unit test suite passes
- [ ] Instrumented tests updated and run on the device
- [ ] Verified by running the app through one complete flow per category

---

# 8. Feature Completion Definition

A step is complete only when it satisfies:

- The architecture, code-quality, testing, and build/verification rules in `CLAUDE.md`.
- Its Definition of Done above.
- The pre-task and post-task briefs required by `CLAUDE.md` Section 14.
