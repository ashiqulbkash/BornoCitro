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
- [x] 1.10 ণ
- [x] 1.11 ত
- [x] 1.12 থ
- [x] 1.13 দ
- [x] 1.14 ধ
- [x] 1.15 ন
- [x] 1.16 প
- [x] 1.17 ফ
- [x] 1.18 ব
- [x] 1.19 ভ
- [x] 1.20 ম
- [x] 1.21 য
- [x] 1.22 র
- [x] 1.23 ল
- [x] 1.24 শ
- [x] 1.25 ষ
- [x] 1.26 স
- [x] 1.27 হ
- [x] 1.28 ড়
- [x] 1.29 ঢ়
- [x] 1.30 য়
- [x] 1.31 ৎ
- [x] 1.32 ং
- [x] 1.33 ঃ
- [x] 1.34 ঁ

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

- **1.8 ড** — wider than it is tall (aspect 1.23), so fitted by width like ক, জ and ঝ. Below the full headline the letter is one continuous movement: down the short middle stem, right and down through the shallow V, up to the sharp point at the top right, then back down the right flank, round the bottom and up the long left arm to its flat cut. That is ~190 canvas units, too long for one pass, so it is split at the point — where the arm and the flank converge into a single tapered tip and the pen turns back on itself, the same split জ and ঞ take. `arm` runs from the matra crossing into the V and up into the point, `bowl` starts again at that same point and ends 2 units short of the midpoint of the left arm's flat-cut terminal, and `matra` is last. Neither tip touches the headline: below the matra the letter hangs from the stem alone. The turn is at (77,41), the highest point of the wedge that is still a full pen thick. The first pass ran `arm` up the skeleton's spur to the ink's apex at (79,35) and restarted `bowl` at the junction below it; the ink at that apex is only the taper where the two edges of the turn converge — 0.3 canvas units of half-thickness against the pen's 4.71 — so those last dots hung outside the stroke and forked away from the bowl's first dot, which is what showed on the device. Ending both strokes at the turn gives them a shared dot and leaves `arm` 72.3 canvas units long, just past 12 dot spacings: the guide drops a dot every 6 units from a stroke's start, so a stroke that stops just short of a multiple of the spacing draws nearly a whole spacing of bare path past its last dot, which shows on the device as a grey tail hanging off the tip. Every guide sample is on ink; traced on the RMX3624 it completed at 99% (PERFECT).

- **1.9 ঢ** — a shade taller than it is wide (aspect 0.95), so fitted by height like ট and ঠ. Below the headline the glyph is one continuous movement of ~156 canvas units: down the long stem, round the bottom bowl, up the right flank and over the top into the filled ball that ends the curl. That is too long for one pass, so it is split at the foot, where the stem stops being straight and the curve begins — the split ট takes, and the only landmark below the matra a child can see. `stem` is therefore the straight run from the headline to the foot, `bowl` carries every curve and is drawn into the centre of the ball the way ক's lobe is drawn into its own ball, and `matra` is the full headline inset 2 units from each end cap, last. Every guide sample is on ink; traced on the RMX3624 it completed at 98.6% (PERFECT).

- **1.10 ণ** — taller than it is wide (aspect 0.86), so fitted by height like ট, ঠ and ঢ. Structurally it is গ: a long post, a short headline bar only to the right of the post, the post rising above it, and everything else hanging off the post's left. Here that is a single spiral of ~98 canvas units — out of the post at mid-height where the letter's arm tapers into it, over the top, down the left flank, along the bottom and curling inwards into the filled ball at its centre. That is short enough to hold in one pass, so unlike জ's and ঞ's spirals it is not split; the ball is a blunt filled terminal, not a pen cut, so the skeleton collapses it to one point and `loop` is drawn into that centre the way ক's lobe is drawn into its own ball. `stem` follows, inset 2 units from each end cap as গ's is, and `matra` — the short bar right of the post — is last. The post is the letter's topmost stroke, not the matra, so the device mapping was measured with `nio_calib.py` (34/34 predicted dots on the drawn guide). Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

- **1.11 ত** — wider than it is tall (aspect 1.27), so fitted by width like ক, জ, ঝ and ড. It is the first consonant whose headline does not touch the rest of the letter: the font leaves five canvas units of blank between the bar and the body, so nothing below the matra hangs off it. The body is a single spiral of ~173 canvas units — in from the flat-cut terminal at the top left, down the long left arm, round the bottom, up the right flank, over the top and curling inwards into the filled ball at the letter's centre. Far too long for one pass, and unlike জ, ঞ and ড it turns smoothly from end to end, so it has no cusp, corner or neck to break at: the heading changes by at most 8.7° per 6 canvas units anywhere along it. It is split at the foot instead — the lowest point of the bowl, at (56,82), where the letter sits on the writing line — which is the one landmark below the headline a child can see and the split ঢ takes, and which leaves `arm` 70 units and `bowl` 92. `arm` starts 2 units short of the midpoint of its flat-cut terminal, the skeleton having forked into a prong for each corner of the cut, as ছ's cut ends do. `bowl` ends at the centre of the ball that closes the spiral: a blunt filled terminal, not a pen cut — its largest inscribed disc is 11.6 canvas units against the pen's 4.78 — so the skeleton collapses it to one point and the curl is drawn into that centre the way ক's lobe is drawn into its own ball. `matra` is the full headline inset 2 units from each end cap, last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.12 থ** — taller than it is wide (aspect 0.89), so fitted by height like ট, ঠ, ঢ and ণ. Structurally it is গ and ণ once more: a long post rising above the headline, a matra bar only to the right of the post, and the rest of the letter hanging off the post's left. Here that rest is a spiral with a filled ball curled inside it, whose outer end sweeps down and right into the post's foot — about 147 canvas units in all, more than a child can hold in one pass. The two parts meet at the loop's leftmost point, where the two arms leave in a 54° fork and a pen running through swings round by 126°, so that corner is both the only landmark below the loop a child can see and the natural place to break; it leaves `loop` 92 units and `tail` 55. `loop` is written first, from the corner counter-clockwise — along the bottom, up the right flank, back left over the top, down the left and curling inwards into the ball — the way ও's bowl is written, and `tail` restarts at that same corner and runs down to the post's foot. The corner is taken at (25,54), the point on the skeleton's spur farthest from the junction where the ink is still a full pen thick: the spur runs on to (19,50), but the ink there has thinned to 0.27 canvas units against the pen's 4.39 — that is the round cap of the turn, not a path the pen's centre ever travels — and a guide taken to the spur's tip would hang outside the stroke, which is what ড's first pass got wrong. `loop` ends at (22,34), the centre of the ball that closes the spiral, whose largest inscribed disc is 7.09 canvas units against the pen's 4.39; the skeleton collapses it to a point two units past the centre, so the curl is truncated at its closest approach and drawn straight in. `stem` is inset 2 units from each end cap as গ's and ণ's are, and `matra` — the short bar right of the post — is last. The post is the letter's topmost stroke, not the matra, so the device mapping was measured with `nio_calib.py` (43/43 predicted dots on the drawn guide). Every guide sample is on ink; traced on the RMX3624 it completed at 99.3% (PERFECT).

- **1.13 দ** — a shade taller than it is wide (aspect 0.91), so fitted by height like ট, ঠ, ঢ, ণ and থ. Below the full headline the glyph is one continuous movement of ~148 canvas units: down the left stem, into the sharp V at the bottom, up the long diagonal to the peak at the top right, then back down the post to the baseline. Too long for one pass, so it is split at the peak — the letter's highest point below the headline, where the pen turns back on itself — the split ড, জ and ঞ take: `arm` runs from the headline through the V and up into the peak, `post` starts again at that point and descends, `matra` is last. The V's vertex is the point (31,55) where the stem's and the diagonal's centrelines cross; the ink runs on to a tip at (31,66), but that overhang is the corner's outer taper, not a path the pen's centre travels — the skeleton's spur into it thins from 4.9 to 1.87 canvas units against the pen's 4.88 — and following it would both hang dots outside the stroke (ড's first pass) and double the guide back over itself for no ink. The peak is at (68,30), the highest point of the wedge between the diagonal and the post that is still a full pen thick; the skeleton's own arch tops out two units lower, dragged down by the ink filled in below the peak, and stopping there would leave the glyph's pointed cap uncovered. The post ends 1 unit short of the midpoint of its slanted end cap rather than the usual 2, so its 60.2 units clear ten dot spacings: at the 2-unit inset it stopped just short of a multiple of the 6-unit spacing and drew ড's grey tail past its last dot, which the device screenshot showed. Every guide sample is on ink; traced on the RMX3624 it completed at 98.5% (PERFECT).

- **1.14 ধ** — a shade taller than it is wide (aspect 0.90), so fitted by height like ট, ঠ, ঢ, ণ, থ and দ. Structurally it is গ, ণ and থ again: a post on the right, a matra bar only to its right, and the body hanging off the post's left. Here the body is a wide "<" whose two ends both run into the post — an arm leaving the post's side at (68,29) where it tapers in, down-left to the corner at (20,51), then a long sweep down-right into the post's foot at (68,82) — with a loop spiralling up out of the arm's middle, over the top-left and inwards to the blunt tongue that ends it. The body is one movement of ~116 canvas units: at the junction (35,39) the arm and the sweep leave in a 15° fork, so a pen running through barely changes heading, while the loop leaves at 96° to the arm and 69° to the sweep — the glyph itself says arm-and-sweep is the pen's path and the loop is joined onto it, so `body` is written first and `loop` starts on it, the way ক's knot starts on a stem it has yet to draw. The corner is the junction where the two centrelines cross; the ink runs on up-left to a tip at (13,49) and the skeleton grows a spur into it, but that is the corner's outer mitre — it thins from 4.9 to 0.92 canvas units against the pen's 4.89, and a guide taken out along it spikes back on itself. `loop` ends at (42,20), the lowest point inside the tongue still a full pen thick: the tongue is wider than it is tall, so the skeleton stops at its top edge instead of running into it, and the curl is drawn in as ক's lobe is drawn into its ball. `post` and `matra` each stop 1 to 3 units short of their end cap, whichever leaves the least bare path past the stroke's last dot. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

  The device mapping needed a new calibration script (`band_calib.py`): ধ's loop tops out on the headline, so the topmost row of dots is not the matra alone and `calib_practice.py` reads the wrong scale off it, while `nio_calib.py` solves for the scale and the dot radius together out of the guide's bounding box and is ill-conditioned for a nearly square guide (ধ is 65.5 x 72 canvas units; it came out 3% high). The dot radius is now measured on its own as the largest disc that fits inside the drawn guide (16.2 px — the old band-height estimate was inflated by strokes descending through the band), and the scale follows from the guide's bounding box in x and in y independently as a check on itself. দ's and ধ's screenshots then agree to 0.06%: sx = 32.3 + 6.543x, sy = 501.2 + 6.543y.

- **1.15 ন** — the first letter derived from the *phone's own* font rather than the scratchpad's. Noto Sans Bengali redrew ন between v2.001, which the device ships (`/system/fonts/NotoSansBengali-VF.ttf`, what Android draws the reference character with) and v3.011, which `noto2.ttf` holds: v3 ends the curl early in a filled ball, v2 carries the spiral a half-turn further round a closed counter and ends it in a flat slanted cut. A guide derived from v3 ended in the middle of the counter the device leaves white, which the side-by-side crop of the Practice screenshot showed at once and `check.py` could not — it was checking against the wrong glyph. `glyph.py` now renders from `noto_device.ttf`, the file pulled off the phone, with `BC_FONT` to override it. The letter itself renders square (aspect 1.004), so it is fitted by height like ঢ, দ and ধ. Above the baseline it is ঢ — a full headline with a post from it to the foot — and below it ণ: one curl of 86 canvas units, short enough to hold in one pass, so it is not split. It leaves the post at mid-height where the arm tapers in, arches over the crest, comes down the left flank and sweeps along the bottom, stopping 2 units short of the midpoint of the flat cut, as ছ's and ত's cut ends do — the skeleton forks into a prong for each corner of the cut, (38,66) and (33,74). The post stops 3 units short of its end cap and the matra, inset from each cap so the bar is a whole 13 dot spacings, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

  Re-checking the 19 letters already shipped against the device's v2 font: every one of their guides still lies within its own pen half-width of the ink, so none of them is drawn against the wrong shape; ছ (worst gap 3.6 units against a 4.68 pen), ঞ (3.1 / 3.59) and ধ (3.1 / 4.78) drift the most and are worth a look if they are ever revisited.

- **1.16 প** — a shade wider than it is tall (aspect 1.023), but height-fitting leaves its ink at x 7..92, comfortably inside the canvas and the same size as every other consonant, where a width fit would stretch it to y 4..96 — so it is fitted by height like ন, ঢ, দ and ধ. Structurally it is গ, ণ, থ and ধ again: a post rising above the headline, a matra bar only to its right, and the body hanging off the post's left. The body is a triangular sail — a hood from the post up over the top and down to a sharp turn at the far left, a tongue back right out of that turn to the sail's bottom corner, and an arm hanging off the hood and running down-left past that corner to a flat cut. Which bands the pen runs through is read off the angles the branches leave each junction at, as ধ's were: at the hood's right end the hood and the link into the post leave at 43° and the arm at 75°, so the hood flows into the post and the arm is joined onto it; at the bottom corner the arm and its tail leave at 34° and the tongue at 59°, so the arm runs through and the tongue ends on it. That gives `sail` (post, hood, turn, tongue) 107 canvas units — one pass — and `arm` 43. The far-left corner is a genuine turn, not a terminal: the skeleton crosses it at full pen thickness and its two spurs, to (11,35) and (19,36), are the corner's outer mitre and the ink's wedge into the counter, thin places the pen's centre never travels, as ধ's mitre at (13,49) is. That mitre is why the guide turns nine units short of the letter's leftmost ink — at a turn this sharp the outer corner runs out to twice the pen's half-width. `arm` stops 2 units short of the midpoint of the flat cut that closes it, from (25,59.5) to (31.5,66), as ছ's, ত's and ন's cut ends do; the post is inset 2 units from each end cap as ণ's is, and the matra is last. Every guide sample is on ink; on the RMX3624 the calibration landed every predicted dot centre 15.2 px inside a 16.1 px dot, and the trace completed at 99.7% (PERFECT), with the ADVANCED tip shown on the Result screen.

- **1.17 ফ** — wider than it is tall (aspect 1.379), so fitted by width with y centred like জ, ঝ, ড and ত. It is a full headline, a post hanging free below it, a bowl off the post's top, and a zigzag sail on the left; like ত the right half does not touch the bar — the post's top is a flat cut five canvas units clear of it. `sail` is one movement of 107 canvas units, inside one pass: down the short stem from the matra, down-right to the first corner, back down-left to the second, then the long diagonal down-right into the post's foot. Each of its three sharp corners grows a skeleton spur into the ink's outer mitre — (10,29), (40,42) and (10,51), thinning to 0.2-1.4 canvas units against the pen's 3.93 — so the pen's centre turns at the junction and never travels out along the spur, as ধ's and প's corners do. `bowl` is joined onto the post rather than continuous with it, which is what the angles at the post's top say: the two halves of the post leave at 34° and the bowl at 57°, so the post runs straight through and the bowl starts on it, the way ণ's loop starts on its own post. It ends 2 units short of the midpoint of the vertical flat cut that closes it, from (69,58) to (69,66.5), with a skeleton prong into each corner. `post` runs from its top cut to its foot — 54 units, exactly 9 dot spacings — and `matra`, inset 2 units from each end cap for a whole 15 spacings, is last. Every guide sample is on ink; on the RMX3624 the x and y scales agreed to 0.02% and the trace completed at 99.7% (PERFECT).

  The calibration's worst-dot check reads 0.0 px for `bowl`'s first dot: it sits on the post, and where two dots overlap the blended purple falls outside the colour mask. Marking the predicted centres on the screenshot shows every one of them, that dot included, in the middle of a drawn dot.

- **1.18 ব** — all but square (aspect 0.989), so fitted by height like ন, ঢ, দ and ধ. It is ধ's body without the loop: a full headline, a post from the headline to the foot, and one wide "<" hanging off the post's left whose two ends both run back into the post. `body` is one movement of 119 canvas units, inside one pass as ধ's 116-unit body is: the arm leaves the post at (72,26) where it tapers in, runs down-left to the corner at (23,48), and the sweep carries on down-right into the post's foot at (72,81). The corner is the junction where the two centrelines cross; the ink runs on up-left to a tip and the skeleton grows a spur into it, but that is the corner's outer mitre — it thins from 4.94 to 0.74 canvas units against the pen's 4.93 — so the pen's centre turns at the junction, as ধ's and প's do. `post` stops 3 units short of its end cap and `matra`, inset 2 units from each cap for a whole 13 dot spacings, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

  The first trace run saved nothing: `monkey` did not bring the app to the front, so every tap and swipe landed on the launcher and ended up scrolling its app drawer. The trace script now proves the app is foreground (`dumpsys window | grep mCurrentFocus` names com.bornochitra) before it navigates, and retries the launch if it is not.

- **1.19 ভ** — wider than it is tall (aspect 1.280), so fitted by width with y centred like ত, জ, ঝ, ড and ফ. Like ত and ফ the headline stands clear of the rest of the letter, and below it ভ is a single spiral of 191 canvas units: in from the flat cut at the top left, down the long left flank, round the foot, up the right flank, round the sharp corner at the top right, then back down-left into the inner bowl and up to the flat cut at the top middle. Far past one pass, so it is split twice, both at landmarks a child can see. The outer arc alone is 139 units — at the ceiling — so it breaks at the foot, the lowest point of the bowl where the letter sits on the writing line, the split ত and ঢ take, leaving `arm` 71 and `bowl` 62. The second break is the corner at the top right, where the outer arc and the inner hook leave at 66° and the pen turns back on itself, the split জ, ঞ and ড take; the skeleton's spur there runs up-right into the corner's outer mitre and thins from 5.01 to 0.56 canvas units against the pen's 4.49, so the pen's centre turns at the junction. `hook` is 50 units. Both free ends are flat cuts with a prong into each corner: `arm` starts 2 units short of the midpoint between its prongs, `hook` ends 3 short of its own — it is the only one of the three whose end is a free tip, the other two handing straight over to the next stroke, and 3 leaves the least bare path past its last dot, as দ's and ধ's ends are tuned. `matra`, inset 2 units from each cap for a whole 15 dot spacings, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.20 ম** — a shade wider than it is tall (aspect 1.032); as with প, height-fitting leaves its ink at x 7..93, comfortably inside the canvas and the same size as every other consonant, where a width fit would stretch it to y 4.5..95.5 — so it is fitted by height like ন, ব, ঢ, দ and ধ. It is a full headline, a post from the headline to the foot, and a body of two bands meeting at a junction under the middle of the letter at (43,47): an arc hanging from the matra at x=20 that curves down-right into it, and a long band running from the post at (73,67) up-left through it, round the big left bowl and back down to a flat cut at the bottom.

  Which two of the three bands the pen runs through had to be measured over a **6-unit baseline** rather than the skeleton's last few pixels: read from three points the arms came out 51° and 59° apart and said almost nothing. Over 6 units the bowl and the sweep into the post turn only 27° into each other, against 65° for arc-to-sweep and 87° for arc-to-bowl, so the bowl and the sweep are one movement of 90 canvas units and `arc`, 48 units, is joined onto it — the same reading ধ's and প's junctions got. Where the arc leaves the matra is a turn, not a terminal: the skeleton's spur runs down-left to (13.5,24) into the ink's outer mitre and thins from 5.25 to 0.15 canvas units against the pen's 4.93, as ফ's (10,29) mitre does. `bowl` ends 2 units short of the midpoint of the flat cut that closes it, whose corners carry a prong each, (37.5,85) and (40,75.6). `arc` is written first, hanging from the headline, then `bowl`, then `post`, then `matra`. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT). This closes প-বর্গ.

- **1.21 য** — অন্তঃস্থ য. জ already holds `consonant-jo`, so this one is `consonant-yo`, which also leaves য় (1.30) a name. A shade wider than it is tall (aspect 1.038); as with প and ম, height-fitting leaves its ink at x 7..93, inside the canvas and the same size as every other consonant, where a width fit would stretch it past y 4..96 — so fitted by height. It is ফ's zigzag sail under a full headline, with a post hanging from the bar rather than ফ's free one, and no bowl: down a short stem from the matra, down-right to the first corner, back down-left to the second, then a long diagonal down-right into the post's foot. All of that is 139 canvas units in one movement — at the ceiling, where ভ's outer arc was — so it breaks at the second corner (22.5,52.3), both a landmark a child can see and the point where the zigzag stops and the sweep to the post begins, leaving `sail` 69 units and `tail` 64; ফ keeps the same shape in one stroke only because its own sail is 107. Each of the three corners grows a skeleton spur into the ink's outer mitre — (15.3,22.7), (52.1,38.5) and (15.1,49.6), thinning to 0.33-1.63 canvas units against the pen's 4.85 — so the pen's centre turns at the junction, as ফ's, ধ's and প's corners do. `post` stops 3 units short of its end cap and `matra` is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

- **1.22 র** — all but square (aspect 0.981), so fitted by height like ব, ন, ঢ, দ and ধ. Above the baseline it is ব exactly — full headline, a post from the headline to the foot, and one wide "<" hanging off the post's left — with a filled dot added under the body's left, which is a separate piece of ink and so takes a stroke of its own, the pattern the plan calls for on ড়, ঢ় and য়. `body` is 116 canvas units in one pass; its corner is the junction where the two centrelines cross, and the skeleton's spur past it runs into the ink's outer mitre, thinning from 5.08 to 0.33 canvas units against the pen's 4.89, so the pen turns at the junction as ব's does.

  **The first dot in the alphabet needed a rule.** A dot is a disc, not a path — its largest inscribed circle is 7.80 canvas units at (35,82) — and a guide has to be something a finger can follow, so `dot` is a small ring inside that disc: radius 4.8, drawn with `StrokePoints.arc` exactly as `drawing-circle` is. That leaves 2.65 canvas units of ink under every sample, so the guide's own dots sit well inside the blob, and its 30.2 units come to five dot spacings, so the ring's last dot closes back onto its first. `check.py` reports the ring's distance from the centreline as 4.8 units, which is just the radius: a disc skeletonizes to a single point, so there is no centreline to be near. The dot is written after the body and the post — where a writer adds it — and the matra last. Traced on the RMX3624 it completed at 99.7% (PERFECT).

  Two device-script fixes this step: the phone had gone to sleep, so the launch landed on a dark, locked screen with the notification shade holding the focus and the guard correctly refused to go on; the guard now sends `KEYCODE_WAKEUP` and `wm dismiss-keyguard` before each attempt. And the catalog test's consonant string picked up a stray U+09BC nukta when it was edited by hand (`ময` + `র` came out as `ময়র`); it is now edited by code point and asserted at 27 characters.

- **1.23 ল** — wider than it is tall (aspect 1.207), so fitted by width with y centred like ত, ড, ঝ and ভ. Under a full headline it is a post on the right and a body of two humps meeting at (49,44), with a short tab hanging down from that meeting point and ending in a flat cut; the left hump carries on past its crest into a big hook — down the far-left flank, round the bottom and back up-right to a second flat cut inside the curl — and the right hump runs from the meeting point into the post. Three bands meet there, so which two the pen runs through was measured over a 6-unit baseline, as ম's junction had to be: the left hump and the tab turn 50° into each other, against 56° for hump-to-tab and 74° for hump-to-hump. So `curl` is one movement of 80 canvas units — in from the tab's cut, up to the meeting point, over the left hump and round the hook — and `hump`, 34 units, is joined onto it, as প's arm and ম's arc are onto theirs. `curl` ends inside the hook rather than starting there, the way ন's, ণ's and ত's curls end inside their own; both its ends are flat cuts with a prong into each corner, so each stops 2 units short of the midpoint between the prongs. `post` is 72 units — exactly 12 dot spacings — and `matra`, inset 2 units from each cap for a whole 15, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

- **1.24 শ** — all but square (aspect 1.011), so fitted by height like ন, প, ম, য and ব. Above the baseline it is গ, ণ, থ, ধ and প again — a post rising above a short matra bar only to its right — but the body is new: a bowtie whose four arms cross at a waist at (36.5,33), two rising (to a flat cut at the top left and into the post) and two falling (to flat cuts at the bottom left and bottom right).

  **That waist is a crossing, not two curves kissing**, and the two readings give different letters. The skeleton renders it as a 6-unit vertical link between two junctions, so the arms were paired over a 6-unit baseline as ম's and ল's were: top-left to bottom-right turns 0.1° and post to bottom-left 1.2°, against 81° and 80° for the same-side pairings; the ink agrees, with the counters biting in from both sides and leaving a neck the two bands share. So শ is two strokes that cross, each running straight through the link: `sweep` from the top-left cut down through the waist to the bottom-right cut (60 units) and `arm` from the post, over the crest and down through the waist to the bottom-left cut (75). Both cover the same few units of ink, which is what a crossing is — and on the device that shows as one set of dots at the neck. All three free ends are flat cuts with a prong into each corner, each inset 2 units from the midpoint between them; `post` is inset 2 from each cap as ণ's is, and the short `matra` right of the post is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.8% (PERFECT).

- **1.25 ষ** — মূর্ধন্য ষ, `consonant-ssho` after ট/ঠ/ড/ণ's doubling. A shade wider than it is tall (aspect 1.050), so fitted by height like প, ম and য. On the page it is য with one bar added: full headline, a post from the bar to the foot, a stem from the matra at x=21, a zigzag to two corners, a long diagonal into the post's foot, and a new bar from the upper corner (48,35) across to the post at (74,48).

  **That bar changes how the letter is written, so ষ is not য plus a stroke.** Three bands meet at the upper corner, and over a 6-unit baseline the band coming down from the matra turns only 42.6° into the bar, against 75.2° into the zigzag's next leg (leg-to-bar is 62.2°). So the pen runs from the matra through that corner and out along the bar into the post — `arm`, 69 canvas units — and `sweep` starts on it at the same corner, runs down-left to the second corner and on down-right into the post's foot, 95 units. The split does double duty: stem, zigzag and diagonal together come to 144 units, past what one pass can hold, which is why য breaks its own zigzag too. Both corners are turns, not terminals — each grows a spur into the ink's outer mitre, (14.4,22.9) and (14.6,49.6), thinning to 0.21 and 0.33 canvas units against the pen's 4.83 — so the pen turns at the junction. `post` stops 3 units short of its cap; `matra`, inset 2 from each cap for a whole 14 dot spacings, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.26 স** — দন্ত্য স. Wider than it is tall (aspect 1.129), so fitted by width with y centred like ঝ, ল, ড and ভ. Under a full headline it is a post on the right and one long S on the left: a stem hangs from the matra at x=15, turns at (15,21) and sweeps right and down to the middle of the letter at (46,49), then turns back left and round the lower bowl to a flat cut at (14,59); a bar joins that middle point to the post.

  **The first junction where the measurement did not decide.** Three bands meet in the middle and the 6-unit angles tie: the stem's band turns 46.5° into the bowl and the bowl turns 45.3° into the bar, with stem-to-bar out of the running at 88.3°; the band widths are nearly as close (7.9, 8.5, 9.3 canvas units). What settles it is the letter's own construction — the body hangs from the headline and sweeps down in one S with the bar joined onto it, the reading ম, ফ, য and ষ all take. So `body` runs from the matra through the middle to the cut, 103 canvas units, and `arm` starts on it at the middle and runs to the post, 39. The turn under the matra is a turn, not a terminal (its spur runs to (9.7,25) into the outer mitre, thinning from 4.77 to 0.30 against the pen's 4.76), and the bowl's end is a flat cut with a prong per corner, inset 2 units. `matra`, a whole 15 dot spacings, is last. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.27 হ** — taller than it is wide (aspect 0.804), so fitted by height like ট, ঠ and ণ, and the first consonant since ঞ with **no post at all**: below a full headline it is one big bowl, a short stem up to the bar, a little hook at the upper left and two tails at the bottom. Both junctions read cleanly over a 6-unit baseline. Where the stem, the hook and the bowl meet, hook-to-bowl turns only 26.6°, against 71° for stem-to-bowl and 82° for stem-to-hook; where the bowl, the left tail and the descender meet, bowl-to-tail turns 29.3°, against 100.7° for bowl-to-descender and 50° for tail-to-descender. So the main movement is one pass of 110 canvas units — in from the hook's flat cut, up to the top junction, clockwise round the whole bowl and out along the left tail to its own cut — with `stem` (16) and `descender` (40) joined onto it at those junctions, as প's arm, ম's arc and স's bar are onto theirs; `stem` is written before `descender` because it sits higher, and the matra is last. All three free ends are flat cuts with a prong per corner: the hook's cut starts the bowl 2 units short of its prongs' midpoint, while the two ends that *finish* a stroke stop 3 units short, which of the usual 1-to-3 range leaves the least bare path past the last dot (2.3 units on the bowl, 4.0 on the descender, whose 40 units sit awkwardly between six and seven dot spacings whatever the inset). Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT). **This closes অন্তঃস্থ/ঊষ্ম — 32 of the 39 consonants are in the catalog, with only the seven অন্যান্য left.**

- **1.28 ড়** — ড with a dot under it, and the first of the three dotted letters. র holds `consonant-ro`, so this retroflex one doubles its consonant as ট/ঠ/ড/ণ/ষ do: `consonant-rro` (leaving `-rrho` for ঢ় and `-yyo` for য়). Because the dot hangs below the baseline the letter as a whole is **taller than it is wide (aspect 0.950) where ড alone is 1.23**, so ড় is fitted by *height* and its body is drawn at a different scale from ড's own entry — same shape, different numbers. Above the dot it is ড exactly: `arm` from the matra crossing down the stem, through the shallow V and up to the point at (72,32), and `bowl` from that same point down the right flank, round the bottom and up the long left arm to its flat cut; the point is the junction, not the skeleton's spur to (74,28), which runs into the taper where the turn's edges meet — ড's first-pass mistake. `dot` is a ring inside the disc as র's is: disc 6.35 canvas units at (51,84), ring radius 3.8, leaving ~2.5 units of ink under every sample and measuring 23.9 units round — four evenly spaced dots that close the ring.

  `bowl` stops **3** units short of its cut's midpoint rather than 2: rounding the path to whole canvas units quantises where the tip lands, and across the 1-to-3 range the bare path past the last dot goes 6.0, 5.1, 3.8. Half a unit would leave only 1.0, but then the last dot's own radius would hang outside the ink at the cut — the fault ড's first pass had, so the shorter bare tail loses to the dot staying inside the letter. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

  Note for the closing sub-step: ড়, ঢ় and য় are two code points each (letter + U+09BC), so the catalog test's consonant title string will end up 42 code points long for 39 letters.

- **1.29 ঢ়** — ঢ with a dot under it, `consonant-rrho` after ড়'s `consonant-rro`. The dot pulls the letter's proportions down with it — aspect 0.759 against ঢ's own 0.95 — and both are height-fitted, so the body is simply drawn smaller here than in ঢ's entry. Below the headline the body is one movement of 128 canvas units (down the straight stem, round the foot, up the right flank, over the top and curling back left), close to what one pass can hold, so it breaks where ঢ breaks: at the foot, where the stem stops running straight and the curve begins — `stem` 48 units, `bowl` 79. `dot` is a ring inside the disc as ড়'s is (disc 6.33 at (50,84), ring radius 3.8, 23.9 units round, four evenly spaced dots).

  **The curl's end is not the filled ball ঢ's own entry describes.** That entry was derived from Noto Sans Bengali v3; in the v2 the phone ships, the terminal is a blunt end whose largest inscribed disc is 4.48 canvas units against the pen's 3.97 — barely wider than the band — and the skeleton forks into a prong per corner, so `bowl` is ended the way a flat cut is. Checking ঢ itself in the device font gives the same picture (5.80 against a 5.14 pen, two prongs), so **ঢ's shipped KDoc describes a ball the device does not draw**; its guide still sits within a pen half-width of the v2 ink, as the 1.15 audit found, so this is a stale description rather than a wrong guide — worth fixing if ঢ is ever revisited. `bowl` stops 1 unit short of the prongs' midpoint rather than 2, as দ's post does: across the 1-to-3 range the bare path past its last dot goes 0.8, 5.8, 4.8. Every guide sample is on ink; traced on the RMX3624 it completed at 99.7% (PERFECT).

- **1.30 য়** — য with a dot under it, `consonant-yyo` after য's `consonant-yo`, and the last of the three dotted letters. Unlike ড় and ঢ়, whose dots pulled them onto a different fit from ড's and ঢ's, the dot barely moves these proportions — aspect 1.028 against য's own 1.038, both height-fitted — so the body is drawn at almost the same scale as য's entry. Above the dot it is য exactly: a full headline, a post hanging from the bar, and ফ's zigzag sail (down a short stem from the matra, down-right to the first corner, back down-left to the second, then a long diagonal down-right into the post's foot). That is 138.5 canvas units in one movement, at the ceiling where য's own 138.7 sits, so it breaks at the same place — the second corner, (22.7,52), where the zigzag stops and the sweep to the post begins — leaving `sail` 67 units and `tail` 63. Each of the three corners grows a skeleton spur into the ink's outer mitre, to (15.3,22.7), (52.1,38.2) and (15.4,49.2), thinning to 0.21-1.62 canvas units against the pen's 4.81, so the pen's centre turns at the junction and never travels out along the spur. `dot` is a ring inside the disc as র's is, but the disc here is the largest of the four — 7.80 canvas units at (32,82) against ড়'s and ঢ়'s 6.3 — so the ring takes র's radius of 4.8 rather than their 3.8, leaving 2.93 units of ink under every sample and measuring 30.2 units round: five dot spacings, so the ring's last dot closes back onto its first. `post` stops 3 units short of its end cap and `matra` is last, as য's are. Every guide sample is on ink; traced on the RMX3624 it completed at 99.6% (PERFECT). **This closes the dotted letters — 35 of the 39 consonants are in the catalog, with ৎ ং ঃ ঁ left.**

- **1.31 ৎ** — খণ্ড ত, one letter and not a conjunct, `consonant-khanda-to`; the first of the four অন্যান্য shapes. Taller than it is wide (aspect 0.786), so fitted by height like ট, ঠ, ণ and হ, and like ঙ and ঞ it has no headline bar at all — the top of the letter is a dome, not a matra — so no stroke is a matra.

  The skeleton makes the whole letter a single open path of 191 canvas units: in from the tongue's cut inside the bowl, right along the tongue, up the right flank, over the dome from right to left, down the left flank and out along the long tail. That is the rotational sense ত, ঢ and ণ are all written in, over the top from right to left; ত spirals inward to the ball at its centre, and ৎ spirals outward because its free end has been drawn down into a descender, so the pen starts at the tongue's cut and finishes at the tail's tip rather than the other way about.

  191 units is far past one pass, and — like ত — the movement turns smoothly from end to end, by at most 31° per 6 canvas units, so it has no cusp, corner or neck to break at. It is split at the letter's leftmost point instead, (22,32), where the pen is travelling straight down and the bowl's descent turns into the sweep out to the tail: the same extremum ত and ঢ break at (their bowls sit upright, so for them it is the foot), the one landmark below the dome a child can see, leaving `bowl` 102 units and `tail` 83. Both free ends are flat cuts with a skeleton prong into each corner — the prongs sit 9.1 canvas units apart against a band 9.0 wide, and the ink under each tip has thinned to 0.14-0.41 units against the pen's 4.51 — so `bowl` starts 2 units short of the midpoint between its prongs and `tail` stops 3 short.

  **The tail's 3 is ড়'s trade-off, and it needed the guide's own dot size to settle it.** A guide dot has a radius of 2.5 canvas units (`DottedPathCanvas`'s `dotRadius`), so an inset under 2.5 hangs part of the last dot out past the cut. 1 unit leaves much the least bare path of the 1-to-3 range — 0.7 units against 3's 4.6 — but the ink under that tip is only 1.16 units half-thick, so over half the dot would sit outside the letter, which is ড's first-pass fault; 3 is the shortest inset that keeps the whole dot inside the ink, and its 4.6 units of bare path are what হ's descender already ships with. Every guide sample is on ink; on the RMX3624 the x and y scales agreed to 0.14% and the trace completed at 99.8% (PERFECT).

- **1.32 ং** — অনুস্বার, `consonant-anusvar`, and the first of the three marks. A mark has no base to sit on, so the shaper renders it beside a U+25CC dotted-circle placeholder and `glyph.render` was cropping the two together. It now drops the placeholder for any Mn/Mc/Me character, splitting on the widest blank run in the ink's column profile — the gutter between placeholder and mark is far wider than any gap inside the mark itself. Re-rendering all 33 shipped letters against the change gives byte-identical masks, so nothing already derived moves.

  Much taller than it is wide (aspect 0.528), so fitted by height like ট, ঠ, ণ, হ and ৎ; no headline, so no stroke is a matra. The mark is two separate pieces of ink — a closed ring and, clear of it, a crescent tail — so it is two strokes, ring first because it sits above.

  `ring` is a genuine annulus with a centreline of its own, not a disc like র's dot: the band is 3.77 canvas units half-thick even at its thinnest, and `check.py` reports its centreline distance as 0.22 rather than the radius a disc gives back. Measured, that centreline is a circle to within a canvas unit — centre (46.3,24.0), mean radius 13.17, 13.02-13.32 by octant, 12.64-13.59 overall — so it is authored with `StrokePoints.arc` at 0° sweeping 360°, as `drawing-circle` and র's dot are. The radius is nudged to 13.4, still inside the measured spread, to make the ring 84.2 units round: a whole 14 dot spacings, so its last dot closes onto its first.

  `tail` is 49 units, one pass: from the thin tip below the ring's left, down through the crescent's belly and out to the flat cut at the bottom right. Three branches meet in the belly at (35.3,52.3) and over a 6-unit baseline the tip turns only 25° into the sweep, against 89° tip-to-spur and 66° spur-to-sweep, so the pen runs straight through; the third branch, to (28.7,53.5), is the ink's outer mitre, thinned to 0.44 units against the 4.93 under the junction.

  **Both of the tail's ends were set by the guide's own dot radius**, the measurement ৎ's tail first needed: `DottedPathCanvas`'s `dotRadius` is 2.5 canvas units. The tail's top is a taper with no terminal to inset from — half-thickness 0.28, 1.09, 1.70, 2.40, 2.98 at y = 46-50 — so the stroke starts at the highest point where the ink is still 2.5 units half-thick, the last place the first dot lies wholly inside the letter; running it to the skeleton's tip at (34.5,47.1) would hang it outside, as ড's first pass did. At the bottom cut a 3-unit inset both keeps the last dot inside the ink and leaves the least bare path past it (1.0 unit, where 1 and 2 leave 3.2 and 2.4 with the dot hanging out). Every guide sample is on ink; on the RMX3624 the x and y scales agreed to 0.05% and the trace completed at 99.7% (PERFECT).

  **Open question for 1.33 and 1.34: the reference character shows its dotted circle.** Android renders the bare mark exactly as the shaper does, so the grid cell and the Practice heading read `◌ং` while the guide traces only ং. It is the standard way to print a diacritic in isolation and the placeholder is visually distinct from the mark, so it was left as it is rather than changed unasked. Probing the device font through HarfBuzz, prefixing the title with U+00A0 suppresses the placeholder for all three of ং ঃ ঁ, where ZWSP, ZWNJ and a plain space do not; it costs the NBSP's advance width, nudging the glyph right of centre, and would need the catalog test's alphabet string to compare on the mark alone.

- **1.33 ঃ** — বিসর্গ, `consonant-bisargo`, the second mark and the simplest shape in the catalog: two rings, one above the other, and nothing else. Very much taller than it is wide (aspect 0.454), so fitted by height like ং, ট, ঠ, ণ, হ and ৎ; no headline, so no stroke is a matra.

  Both are annuli with centrelines of their own, not discs like র's dot — each band is 4.02 canvas units half-thick at its thinnest against a 4.37 pen — and the font draws one shape twice: the two centrelines measure identically, centres (50.0,25.1) and (50.0,71.8), mean radius 14.03 and 14.04, 13.88-14.19 by octant. So both are authored with `StrokePoints.arc` at 0° sweeping 360°, as ং's ring, `drawing-circle` and র's dot are, upper first — the order a writer marks them in.

  **The radius is chosen for the dots, not just the ink.** 14.3 is inside the measured spread of 13.50-14.51 and 0.27 off the mean, which the 4.02-unit band swallows, and it makes the 32-segment polyline the arc samples to 89.7 canvas units round: dots land at 0, 6 … 84, so each ring carries 15 evenly spaced dots and the gap closing it is 5.7, one spacing to within 5%. The measured mean of 14.0 would instead leave a 3.8-unit closing gap, two dots visibly bunched at the start. (ং's own ring is the other case: its 84.1 units put a 15th dot 0.06 short of the first, exactly on top of it, which is invisible.)

  Every guide sample is on ink, centreline distance at most 0.74 units; on the RMX3624 the x and y scales agreed to 0.25% and the trace completed at 99.6% (PERFECT). The reference character shows its dotted circle here too, as ং's does — the open question above stands for 1.34.

- **1.34 ঁ** — চন্দ্রবিন্দু, `consonant-chandrabindu`, the last of the three marks and the 39th consonant.

  **It forced the placeholder handling to change.** ং and ঃ are spacing marks, so the shaper sets the dotted circle *beside* them and 1.32's widest-column-gap split separated the two; ঁ is non-spacing and is drawn *over* the circle, overlapping it in x, so no column split can work. `glyph.render` now gives every Mn/Mc/Me character a U+00A0 base instead — the only base tried that leaves no placeholder for all three marks, where ZWSP, ZWNJ and a plain space still leave it on ং and ঃ. All 33 letters re-render byte-identical against the pre-mark `glyph.py`, and ং and ঃ byte-identical against the gap-split version they were derived with, so nothing already shipped moves.

  Much wider than it is tall (aspect 1.876), so fitted by width with y centred like জ, ঝ, ড, ত, ফ, ভ, ল and স — the only one of the three marks that is. No headline, so no stroke is a matra. Two pieces of ink and so two strokes, written চন্দ্র then বিন্দু: the crescent first, the dot into its cup afterwards, the order র, ড়, ঢ় and য় add their dots in.

  `crescent` is one movement of 116 canvas units, inside one pass: down the left horn from its flat cut, round the bottom of the cup and up the right horn to its own cut. The band around it is a thick 8.4-9.5 units half-thick throughout, so no part of it is a hairline. Both horns end in flat cuts with a prong into each corner — 17.4 and 17.6 units apart against a band about 15.4 wide — and each end stops 3 units short of the midpoint between its prongs: that is both the shortest inset of the 1-to-3 range keeping the end dot inside the ink (3.97 and 3.29 units of ink there against the guide dot's 2.5 radius, where 2 leaves 2.08) and the one leaving the least bare path past the last dot, 1.9 against 4.8 at 2 and 5.9 at 1.

  `dot` is a filled disc, not an annulus like ং's and ঃ's rings: it skeletonizes to a single point, so the guide is a small ring inside it drawn with `StrokePoints.arc`, as র's, ড়'s, ঢ়'s and য়'s dots are, and `check.py` reports its centreline distance as the radius — expected for a disc. The disc's largest inscribed circle is 11.55 canvas units at (50.0,36.3), the widest dot in the alphabet against র's 7.80 and ড়'s 6.35, and the ring takes radius 6.7 (58% of it), leaving 4.9 units of ink under every sample and measuring 42.0 units round: seven whole dot spacings, so its last dot closes onto its first. Every guide sample is on ink; on the RMX3624 the x and y scales agreed to 0.18% and the trace completed at 99.6% (PERFECT).

  **Step 1 closing checks.** The catalog test now asserts the alphabet is complete — 11 vowels and 39 consonants — as its own case, and the consonant title string is 42 code points for 39 letters, the three nuktas being ড় ঢ় য় exactly as the 1.28 note predicted. The Consonants screen scrolls to a full 13x3 grid ending ৎ ং ঁ at both **720x1600** and **720x1280** (360x640dp, set with `wm size` and reset afterwards), with nothing clipped at either size. The Home and Progress consonant bars both read **90%** against 35 of 39 letters completed on the device — 35/39 is 89.7%, where a denominator of 38 would show 92% and 36 would show 97% — and overall reads 67% for 37 of 39+11+5=55, so both bars are computed against 39. `completedRatio` divides by the catalog list itself, so this follows by construction as well as by measurement. **Step 1 is complete: all 39 Bengali consonants are practicable.**

---

# 3. Step 2 — Practice Sessions and Averaged Result

## Requirement

The child enters the Practice screen and traces the character. When they finish, the Result screen offers **Try Again** or **View Progress**. The child may try as many times as they like; every try in that visit (the *session*) is tracked, and the Result screen reports the session average: **"You tried n time(s). Your overall progress is n%."**

## Implement

- **Session.** A session is the run of tries on one character, starting when the child opens it from a category screen and ending when they leave it (Next, View Progress or Back). It is not stored; it lives only in navigation state, so no Room change or migration is needed.
- **State (MVI).** `PracticeState` gains `sessionScores: List<Float>` (the score of every completed try so far). `PracticeViewModel` seeds it from the `scores` navigation argument (empty on a fresh visit) and appends the score on `ExerciseCompleted`. Composables stay stateless.
- **Navigation.** The Practice and Result routes take an optional `scores` argument (comma-separated, default empty). Practice passes the updated scores to Result; **Try Again** opens Practice again with those scores; **Next** starts a fresh session (no scores).
- **Result.** `ResultViewModel` reads the same argument and exposes the try count and the rounded average as `sessionAttempts` and `sessionAveragePercent`. The screen shows the message above under the score of the latest try (singular "1 time" for one try).
- **Per-try persistence is unchanged.** Every completed try is still saved through `ProgressRepository.savePracticeResult`, so Progress and mastery behave as before.
- The tracing engine and scoring are unchanged; drawings use the same flow.

## Definition of Done

- [ ] Finishing a try opens Result with **Try Again** and **View Progress**
- [ ] Try Again starts a new try and keeps the session's earlier scores
- [ ] Result shows "You tried n time(s). Your overall progress is n%." with n% the average of the session's tries
- [ ] Next starts a fresh session; leaving the screen discards the session
- [ ] Every completed try is still saved through `ProgressRepository`
- [ ] Existing tracing and scoring tests pass unmodified
- [ ] Unit tests cover session score accumulation, encode/decode, and the Result average and try count
- [ ] Verified by running the app: try a letter three times and check the Result message

---

# 4. Step 3 — English Characters

## Requirement

Add English letters: lowercase **a–z** and capital **A–Z**, practised like the Bengali letters.

## Implement (sub-steps 3.1–3.52, **one character per step**)

- Each sub-step adds exactly **one** character, then stops. Do not add a second character in the same step.
- Sub-steps 3.1–3.26 add the small letters a–z, in alphabetical order; 3.27–3.52 add the capitals A–Z. Ids are `english-small-<letter>` and `english-capital-<letter>`, each with the next `order` value within its type; titles are the glyphs.
- Derive each character's strokes from a rendered glyph with the **Step 1 per-letter recipe** (toolchain, ink-bbox fit — by width for characters wider than tall — `check.py`, overlay review, `apply_<letter>.py`, catalog test update, device trace). Do not redraw by eye.
- Font: the first sub-step (3.1) picks a print/school-style font so that `a`, `g`, `y` follow the single-storey shape children are taught, and records it in the source header comment of both new files. Every later English character uses the same font. The device-font lesson from 1.15 applies: check the derivation against the glyph the phone itself would draw, not only the scratchpad font.
- Author natural stroke order and direction (top-to-bottom, left-to-right, counter-clockwise for round letters). There is no headline, so no stroke is a `-matra`. A dot or crossbar that is separate ink (`i`, `j`, `t`, `f`, capital `E` `F` `H` `A` and the like) is a stroke of its own, written where a writer adds it; dots use the ring rule from 1.22 (`StrokePoints.arc` inside the inscribed disc).
- Keep one stroke under roughly 140 canvas units and split only at a landmark the child can see (Step 1 authoring conventions).
- **Plumbing rides with the first character of each type**, so no category is ever shown empty and no placeholder is used:
  - **3.1 (`a`)** also adds `ExerciseType.ENGLISH_SMALL`, `EnglishSmallExercises.kt` registered in `ExerciseCatalog`, an English small-letters screen following `ConsonantsScreen`/`ConsonantsViewModel`, a route in `BcDestination`/`BcNavHost` only if the existing pattern needs one, the Home button **Small letters**, the matching Progress button/section (reuse the hub: `ProgressState.openCategory`), the `LearningProgress`, `ProgressRepositoryImpl`, `HomeViewModel` and `ProgressViewModel` changes, and string resources with Bengali UI copy consistent with the existing category names.
  - **3.27 (`A`)** does the same for `ExerciseType.ENGLISH_CAPITAL`, `EnglishCapitalExercises.kt` and the **Capital letters** button/section, reusing what 3.1 built rather than duplicating it.
- Step 2 practice sessions apply to these letters without extra code.
- The last small-letter sub-step (3.26) also confirms the small-letters screen scrolls and lays out 26 items at 720x1280 and 720x1600, and that Home/Progress measure the small bar against 26. 3.52 does the same for 26 capitals and confirms overall progress includes both English categories.

## Per-character recipe

Follow the Step 1 recipe and its authoring conventions; differences for English:

- **Normalization.** Same rule as consonants: fit the ink bbox to y 7..90 with x centred, or by width (x 3..97, y centred) when the glyph is wider than tall. Each character is fitted on its own, so `a` and `l` are drawn at the same canvas size; the catalog test's 0..100 bounds and (50, 50) centring checks apply unchanged. Wide capitals (`M`, `W`) and wide small letters (`m`, `w`) usually need the width fit — check the aspect the render prints.
- **Tests to update for every character.** `ExerciseCatalogTest`'s per-exercise stroke-count map, plus the type's alphabet string (`abcde…` / `ABCDE…`) once that assertion exists. Record the unit-test baseline after 3.1.
- **Device check.** Navigate Home → the new category button → the character's cell; re-measure the canvas mapping from a screenshot (the guide has no headline, so use `nio_calib.py`, or `band_calib.py` where it is ill-conditioned for a near-square guide). A faithful trace scores 95–99.

## Sub-steps

Small letters:

- [x] 3.1 a
- [x] 3.2 b
- [x] 3.3 c
- [x] 3.4 d
- [x] 3.5 e
- [x] 3.6 f
- [x] 3.7 g
- [x] 3.8 h
- [x] 3.9 i
- [x] 3.10 j
- [x] 3.11 k
- [x] 3.12 l
- [x] 3.13 m
- [x] 3.14 n
- [x] 3.15 o
- [x] 3.16 p
- [x] 3.17 q
- [x] 3.18 r
- [x] 3.19 s
- [x] 3.20 t
- [ ] 3.21 u
- [ ] 3.22 v
- [ ] 3.23 w
- [ ] 3.24 x
- [ ] 3.25 y
- [ ] 3.26 z

Capital letters:

- [ ] 3.27 A
- [ ] 3.28 B
- [ ] 3.29 C
- [ ] 3.30 D
- [ ] 3.31 E
- [ ] 3.32 F
- [ ] 3.33 G
- [ ] 3.34 H
- [ ] 3.35 I
- [ ] 3.36 J
- [ ] 3.37 K
- [ ] 3.38 L
- [ ] 3.39 M
- [ ] 3.40 N
- [ ] 3.41 O
- [ ] 3.42 P
- [ ] 3.43 Q
- [ ] 3.44 R
- [ ] 3.45 S
- [ ] 3.46 T
- [ ] 3.47 U
- [ ] 3.48 V
- [ ] 3.49 W
- [ ] 3.50 X
- [ ] 3.51 Y
- [ ] 3.52 Z

## Definition of Done (applies to every sub-step)

- [ ] The one character is added with strokes derived from its rendered glyph
- [ ] Every guide sample lies on the glyph's ink
- [ ] It completes at 95%+ on a faithful synthetic trace on the device
- [ ] `ExerciseCatalogTest`'s stroke-count map (and alphabet string) include the new character, and its id, `order`, canvas-bounds and centring assertions pass
- [ ] Existing ViewModel tests still pass
- [ ] Verified by running the app

Additionally, for the plumbing sub-steps (3.1 and 3.27):

- [ ] Type, catalog registration, category screen, Home button, Progress button/section and progress aggregation are in place for the category
- [ ] ViewModel and progress-aggregation tests cover the new category

Step 3 as a whole is done when all 52 sub-steps are checked, the catalog test asserts 26 small + 26 capital with unique ids and contiguous `order` per type, both category bars are computed against 26, and the flow Home → English → practise → Progress has been run on the device.

## Notes

_(Add one note per sub-step as it is completed, as in Step 1.)_

- **3.1 a** — `english-small-a`, and the English small-letters plumbing.

  **Font: Andika Bold** (SIL International, OFL 1.1), a print font designed for children learning to read, whose `a`, `g` and `y` are the single-storey school shapes. The phone draws Latin text in Roboto, whose `a` and `g` are double-storey, so a guide derived from any school font would sit under a different glyph unless the app draws that font itself: `res/font/andika_bold.ttf` is bundled unmodified (licence in `assets/licenses/andika-OFL.txt`) as `BcLatinLetterFontFamily`, and a single-letter Latin title (`String.letterFontFamily()`) is drawn in it on the Practice/Result heading, the category grid tile and the Progress row. Bengali titles and drawing names keep the theme font, so nothing already shipped moves. Bold is the weight the letter styles ask for, so the guides are derived from Andika *Bold* — the exact glyph the device shows, which the Practice screenshot confirms. Every later English character, and the digits in Step 4, use the same face.

  **Toolchain rebuilt.** `/tmp` had been cleared, so no scratchpad held the Step 1 scripts; `glyph.py` (render + canvas fit, `BC_FONT` override, Andika Bold by default), `skel_branches.py`, `check.py` (off-ink samples, centreline distance, ink under every guide dot against the 2.5-unit dot radius, bare path past the last dot), `overlay.py`, `calib.py` (dot radius from the largest inscribed disc, scale from the guide bbox in x and y independently) and `trace.py` were rewritten from the recipe, plus `dev.py` for the device (wake, portrait lock, foreground check, tap by on-screen text from a `uiautomator` dump).

  a is a shade wider than it is tall (aspect 1.005); height-fitting leaves its ink at x 8..92, so it is fitted by height like প and ম. Andika's a is a bowl closed onto a straight stem, written round first and counter-clockwise, then the stem top to bottom. The bowl is 167 canvas units in one movement and turns smoothly all the way, so it is split at its lowest point, (32,80), where the letter sits on the line — the split ত and ঢ take — into `arc` (112, from the stem's top corner over the top and down the left) and `bowl` (56, along the bottom and up the diagonal into the stem at mid-height), sharing the dot at the foot. The skeleton spurs at (11,56) and (32,88) are the bulges where Andika's stroke thickens, not paths. The stem is taken from the ink's row centres (x 75.6 over the upper half, leaning to 80 at the foot) rather than the skeleton, which turns into the bowl at the top and runs off into the foot's outer corner at the bottom; it starts 3.5 units inside its flat top and stops 3 above its flat foot, keeping both end dots wholly on the ink. Every guide sample is on ink; min ink under any dot 3.5 units. On the RMX3624 the letter Practice mapping is now `sx = 31.8 + 6.563x`, `sy = 536.3 + 6.563y` — 35 px lower than for Bengali letters, because Andika's line box under the heading differs — with x and y scales agreeing to 0.11%; the trace completed at 97.3% (PERFECT), and Home, the small-letters grid and the Progress section all showed it. Unit-test baseline after 3.1: **355 tests, 0 failures**.

- **3.2 b** — `english-small-b`, much taller than wide (aspect 0.617), so fitted by height. A straight stem with a bowl off its lower half; the skeleton draws the bowl and the lower stem as one closed loop, leaving the bowl at (32,52) and closing into the stem's foot at (32,79). Written as taught: `stem` top to bottom — a straight `StrokePoints.line` down its centre at x=32, 3 units inside the flat top (y 7) and foot (y 87) — then `bowl` out of the stem, up-right over the top, clockwise down the right and round the bottom back into the foot, 105 canvas units in one pass. The skeleton's other branch runs up-left into the top-left corner of the slanted cut that caps the stem; that is the cut's corner, not a path. Every guide sample is on ink, min ink under any dot 3.1 units; guide bbox centre (50,47). On the RMX3624 the mapping matched 3.1's (`sx = 32.3 + 6.554x`, `sy = 536.5 + 6.554y`, x/y scales agreeing to 0.01%) and the trace completed at 98.6% (PERFECT).

- **3.3 c** — `english-small-c`, taller than wide (aspect 0.791), fitted by height. One counter-clockwise curve between two flat cuts, each forking the skeleton into a prong per corner. As a single stroke it is 142 canvas units — past the ceiling য and ভ were already split at (139) — and it turns smoothly throughout, so it is split at its leftmost point, where the pen travels straight down and the skeleton grows a spur into the thick left of the bowl (ৎ's split): `top` 70 units, `bottom` 72. The split sits one unit above the exact extremum so `bottom` is a whole 12 dot spacings and its last dot lands on its tip (bare path 0.1 instead of 5.1). Both ends stop 3 units short of their cut's midpoint: 1, 2 and 2.5 (once rounded to whole units) leave the top end's dot hanging past the cut. Every guide sample is on ink, min ink under any dot 3.0.

  **New case: centring the guide rather than the ink.** Andika's c is much heavier on the left than at its terminals, so with the ink bbox centred the centreline's bbox centre is x=54, outside the catalog test's 3-unit tolerance. The whole guide is translated 4 units left (bbox x 25..75, centre (50, 48.5)); the canvas draws only the guide, so this changes placement, not shape, and every dot keeps its position on the ink. Expect the same on other one-sided letters (e, r, s, and some capitals). On the RMX3624 the mapping matched 3.1's (x/y scales agreeing to 0.26%, every predicted dot centre 14.8 px inside a 16 px dot) and the trace completed at 98.6% (PERFECT).

- **3.4 d** — `english-small-d`, much taller than wide (aspect 0.671), fitted by height. b mirrored with a's foot: a flat-topped stem on the right (x 66.8 straight down to y 70, then leaning out to a slanted foot cut, row centre 70.6 at y 88) and a closed bowl on its left, which the skeleton joins to the stem at y 45 and y 73. Written round first like a: `bowl` from the stem, counter-clockwise over the top, down the left and back into the stem, 107 canvas units in one pass; then `stem` top to bottom, taken from the ink's row centres as a's is (the skeleton runs off into the foot's outer corner), 3 units inside the flat top and ~3.5 above the foot. Every guide sample is on ink, min ink under any dot 3.1; guide bbox centre (50, 47.5), so no re-centring was needed. On the RMX3624 the mapping matched 3.1's (`sx = 32.5 + 6.553x`, `sy = 536.6 + 6.553y`, x/y scales agreeing to 0.15%; the worst predicted dot centre, 10.2 px inside, is at the bowl/stem junction where the overlapping dots blend out of the colour mask) and the trace completed at 97.4% (PERFECT). 355 unit tests, 0 failures.

- **3.5 e** — `english-small-e`, taller than wide (aspect 0.868), fitted by height. Written as one movement — across the crossbar left to right, sharply up the right side, counter-clockwise over the top and down the left, round the bottom and up into the tail's flat cut — which is ~218 canvas units, so it is split at the two landmarks the child can see: the corner where the bar turns up into the bowl, and the leftmost point where the bar's own left end meets the side (c's split). `bar` (48, a straight `line`), `top` (91) and `bottom` (79), `top` and `bottom` sharing the dot at the split. The spur down-right from the bar's corner runs into the corner of the flat cut under the bar, not a path. The tail stops 3 units short of its cut's midpoint: 1 lands the last dot in the same place but leaves 2.7 units of bare path, 3 leaves 0.7. Every guide sample is on ink, min ink under any dot 4.0. Heavier on the left like c, so the guide is moved 4 units left (bbox centre 53.5 → 49.5). On the RMX3624 the mapping matched 3.1's (`sx = 32.3 + 6.559x`, `sy = 536.1 + 6.559y`, x/y scales agreeing to 0.14%; every predicted dot centre ≥14.8 px inside the purple except the bar's first, 5.4 px, where it overlaps the `top`/`bottom` dots at the shared left point) and the trace completed at 98.1% (PERFECT). 355 unit tests, 0 failures.

- **3.6 f** — `english-small-f`, much taller than wide (aspect 0.515), fitted by height. A stem that curls into a hook ending in a flat slanted cut, plus a crossbar that is separate ink. Written as taught: `stem` from the hook's tip, left over the top and straight down (89 canvas units, one pass), then `bar` left to right. The hook's cut forks the skeleton into a prong per corner and the stroke starts 3 units short of the cut's midpoint — 1 and 2 leave the first dot hanging past the cut (min ink under it 0.9 / 1.9), and 2.5 rounds to the same point as 3. Below the hook the stem is straight at x 46 (ink row centre, leaning only to 46.6 at the foot), so it is one segment, stopping 3 above the flat foot where the skeleton runs off into the foot's outer corner; reaching further to land the last dot on the end would hang it past the foot, so 5.1 units of bare path remain. The bar is on its ink's centreline (y 44), x 32..62, about 3 inside each end and a whole 5 dot spacings. Every guide sample is on ink, min ink under any dot 3.0; guide bbox centre (49.5, 50), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.0 + 6.564x`, `sy = 536.1 + 6.564y`, x/y scales agreeing to 0.23%; the worst predicted dot centre, 8.5 px inside, is where the bar's dots overlap the stem's) and the trace completed at 99.8% (PERFECT), saved as a new `practice_session` row. 355 unit tests, 0 failures.

- **3.7 g** — `english-small-g`, much taller than wide (aspect 0.636), fitted by height. Andika's single-storey g is a's bowl closed onto a stem that drops into a hook ending in a flat slanted cut; the bowl's arch runs straight into the stem's top-right corner, so no stem top stands above it. Written round first like a and d: `bowl` from the stem just under its top (69,18), counter-clockwise over the top, down the left, round the bottom and back into the stem at y 48 (107 units); then `stem` from 4 units under its top edge, straight at x 69 (ink row centre, stem ink x 61..76.4), curling left along the bottom into the hook (101 units). The hook's cut forks the skeleton into a prong per corner; the tail stops 3 short of the cut's midpoint — 1 and 2 hang the last dot past the cut (min ink 2.1 / 2.3), 2.5 and 3 both fit, and 3 leaves 4.7 units of bare path to 2.5's 5.2. Every guide sample is on ink, min ink under any dot 3.6; guide bbox centre (50.5, 48), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.1 + 6.564x`, `sy = 535.8 + 6.564y`, x/y scales agreeing to 0.11%, worst predicted dot centre 10.4 px inside, at the bowl/stem junction) and the trace completed at 98.1% (PERFECT), a new `practice_session` row. A letter never tried before shows the `FIRST_ATTEMPT` tip ("Try tracing slowly.") on Practice; that is expected. 355 unit tests, 0 failures.

- **3.8 h** — `english-small-h`, much taller than wide (aspect 0.619), fitted by height. A straight stem (ink row centre x 32.4, flat top at y 7 and flat foot at y 90) and an arch leaving it at mid-height, over the shoulder and straight down the right leg (x 68.4) to its own flat foot. Written as taught: `stem` top to bottom (a `line`, 77 units), then `arch` out of the stem where the skeleton joins the two — b's bowl start — over and down in one pass (78 units). The arch follows the skeleton until the leg turns straight at y 61 and is one segment from there; the skeleton stops 7 units above the foot, where the flat cut forks it. Both straight runs stop 3 units inside their flat ends (y 10 and 87): 2 inside hangs the stem's end dot past the cut (min ink 2.1 top / 2.0 foot). Every guide sample is on ink, min ink under any dot 3.1; guide bbox centre (50, 48.5), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 31.9 + 6.561x`, `sy = 536.2 + 6.561y`, x/y scales agreeing to 0.15%, every predicted dot centre ≥14.8 px inside a 15.8 px dot) and the trace completed at 98.2% (PERFECT), a new `practice_session` row; Result read "You tried 1 time. Your overall progress is 98%." 355 unit tests, 0 failures.

- **3.9 i** — `english-small-i`, far taller than wide (aspect 0.223), fitted by height. The first English letter with a dot: two separate pieces of ink, a straight stem (row centre x 50, flat ends at y 35.8 and 90) and a round dot above it, written as a writer adds them — `stem` top to bottom, then `dot`. The stem is a `line` from y 39 to 87, 3 units inside each end: 2 hangs the end dot past the cut (min ink 2.4 top / 2.0 foot), and 39..87 is a whole 8 dot spacings, so the last dot lands on the end. The dot follows the 1.22 ring rule: its largest inscribed circle is 9.25 units at (50, 16), and the ring is `StrokePoints.arc` radius 5.73 — 62% of that, and the radius whose circumference is exactly 6 dot spacings, so the last dot closes onto the first — started at the top and swept counter-clockwise (`startDeg = -90f`, `sweepDeg = -360f`), the direction the plan gives round English letters (র's ring started at 0° and ran clockwise). Every guide sample is on ink, min ink under any dot 3.0; guide bbox centre (50, 48.6). On the RMX3624 the mapping matched 3.1's (`sx = 32.4 + 6.562x`, `sy = 535.6 + 6.562y`, x/y scales agreeing to 0.16%, every predicted dot centre ≥14.8 px inside a 16 px dot) and the trace completed at 98.1% (PERFECT), a new `practice_session` row. 355 unit tests, 0 failures.

- **3.10 j** — `english-small-j`, far taller than wide (aspect 0.357), fitted by height. i with a hook: a stem (row centre x 58) straight down from its slanted top, curling left along the bottom into a flat cut, and a dot above. Written as i: `stem` top to bottom and round the hook in one pass (67 units), then `dot`. The stem starts 3 inside its top edge (y 29.1 at x 58 → y 32; 31 hangs the first dot out, ink 2.0) and is one straight segment to y 68, where the curl begins, then the skeleton. The cut forks the skeleton into a prong per corner; the hook stops 2.5 short of their midpoint — 1 and 2 leave the tip over 1.6 and 2.5 units of ink, and 2.5 leaves 0.8 units of bare path to 3's 1.0. **Smaller dot, smaller ring:** j's dot has an inscribed circle of only 7.12 (i's is 9.25), so a 5-spacing ring (r 4.77) would sit its guide dots 2.1 units from the edge, past the 2.5 dot radius; the ring is 4 spacings (r 3.82), still counter-clockwise from the top. Every guide sample is on ink, min ink under any dot 3.0; guide bbox centre (50.4, 47.6). On the RMX3624 the mapping matched 3.1's (`sx = 30.9 + 6.579x`, `sy = 536.1 + 6.579y`; x and y scales differ by 0.54% because the guide is only 23 units wide, which moves no dot by more than 0.2 px here; every predicted dot centre ≥13.9 px inside a 16 px dot) and the trace completed at 98.1% (PERFECT), a new `practice_session` row. 355 unit tests, 0 failures.

- **3.11 k** — `english-small-k`, much taller than wide (aspect 0.629), fitted by height. h's stem (x 32, y 10 to 87) and an angle: an arm down-left from a flat top and a leg down-right to a flat foot, meeting at a point that touches the stem's right edge (ink x 39.1), not its centreline. Written as taught: `stem` top to bottom, then `angle` in to the stem and out again in one movement (67 units, a 3-point polyline). The diagonals' ends lie on lines fitted through the ink's row centres (arm `x = -0.942y + 99.75`, leg `x = 0.805y - 5.73`); the skeleton instead spurs into the outer corner of each flat end and joins the point to the stem with a short horizontal link, neither of which is a path. Each end stops 3 units inside its cut — arm (62, 40) under its top at y 37.1, leg (64, 87) — as 2 leaves 2.0 units of ink under the end dot.

  **Redone twice after review on the device.** The first pass put the point where the two row-centre lines cross, (43, 60), leaving a wide gap (~40 px) between the angle's dots and the stem's, so the angle looked detached from the stem the glyph fuses it onto. Moving it to the stem's ink edge (39, 60) — the furthest in a straight diagonal stays wholly on ink — still left ~13 px, and the user asked for **no gap**. The user also spotted a **missing last dot on the stem**: 77 units is not a whole number of 6-unit spacings, so the last dot fell 5 units short and a bare line trailed below it (h's stem, 10..87, has the same 5-unit tail).

  Final geometry (`k_final.py`): every stroke is a whole number of spacings, so a dot lands on each end and on the point. Stem y 9.5..87.5 (78 units; 2.5 inside each flat end, where the end dot's radius just fits, min ink 2.50). Angle (60,42) → (36,60) → (62,85) before the shift: the arm is exactly 30 (a 3-4-5 triangle, so float rounding cannot drop the dot on the point — `DottedPathSampler` only places a dot once a full spacing is walked) and the leg 36.07. The point's dot overlaps the stem's; the two dots either side of it spill into the white notches above and below the join (min ink 1.95 against the 2.5 dot radius), the price of a closed join the user asked for. The whole guide is shifted 1 unit right (stem x 33) so its bbox centre is (48, 48.5) rather than exactly on the catalog test's 3-unit limit. Lesson for K, R and any branch fused to a stem's edge: judge the join and the stroke ends on the device screenshot — close the join, and size strokes to whole spacings so no bare line trails the last dot.

  355 unit tests, 0 failures. The device check was deferred once (the phone was in use) and done with 3.12: on the RMX3624 the Practice screen shows the angle closed onto the stem and a dot on the stem's foot; the mapping matched 3.1's (`sx = 31.5 + 6.565x`, `sy = 535.8 + 6.565y`, x/y scales agreeing to 0.13%, every predicted dot centre ≥14.9 px inside a 16.1 px dot) and the trace completed at 98.1% (PERFECT), a new `practice_session` row.

- **3.12 l** — `english-small-l`, far taller than wide (aspect 0.198), fitted by height. Andika's l is a plain straight stem, no foot or tail: row centre x 50 from a flat top at y 7 to a flat foot at y 90, so it is one stroke, top to bottom. Sized by the rule 3.11's review set — a whole number of dot spacings, so a dot lands on each end and no bare line trails — as y 9.5 to 87.5 (78 units, 13 spacings), 2.5 inside each end, exactly where the end dot's radius fits (min ink under any dot 2.50). Guide bbox centre (50, 48.5). `calib.py` cannot fit a zero-width guide (no x scale), so the mapping just measured on k in the same layout was checked against the l screenshot instead with `verify_map.py`: every predicted dot centre ≥15.0 px inside a 15.7 px dot. The trace completed at 100% (PERFECT), a new `practice_session` row. 355 unit tests, 0 failures.

- **3.13 m** — `english-small-m`, the first English letter wider than tall (aspect 1.443), so fitted by width (ink x 3..97, y 17.4..82.6). h with a second arch: three straight legs (ink row centres x 14.9, 51.5, 88.2; the stem's flat top at y 18.8, all three flat feet at y 82.6) joined by two shoulders. Written as taught: `stem` top to bottom, then `arch1` out of the stem over the first shoulder and down the middle leg, then `arch2` out of the middle leg over the second shoulder and down the right leg — the pen lifts where each arch leaves its leg, as in h. Each arch follows the skeleton until its leg turns straight (y 38 / 46) and is one segment to the foot; the skeleton's spur up-left from the stem runs into the top-left corner of the slanted cut on the stem's top, not a path. Sized by 3.11's rule: every stroke is a whole number of spacings. The stem is y 24..78 (54 units; 60 would not fit between the flat ends with both end dots inside), every foot stops at the stem's foot height, and each arch starts a few units down its leg (y 40.5 / 40, below the skeleton junction at 36.5) — which makes both 90 units, exactly 15 spacings — with its first dot overlapping the leg's dots, so both joins are closed. Every guide sample is on ink, min ink under any dot 4.25; guide bbox centre (51.6, 51), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 31.5 + 6.572x`, `sy = 535.3 + 6.572y`, x/y scales agreeing to 0.04%; the worst predicted dot centre, 6.3 px inside, is where an arch's first dots overlap its leg's) and the trace completed at 97.5% (PERFECT), a new `practice_session` row (id 37). 355 unit tests, 0 failures.

  **Redone after the p review (Step 3.16): overlapping dots at the joins.** Each arch started between two dots of the leg it leaves (y 40.5 and 40), so its first dot overlapped both and each join read as a clump. Now each arch leaves its leg **exactly on** one of that leg's dots (`m_build.py`, with the search in `joinfit.py`): the stem is y 23..77, `arch1` leaves the stem's dot at y 35 and `arch2` leaves arch1's dot at y 40.5 on the middle leg, cutting straight across to the shoulder rather than following the skeleton's first points, which hug the leg and put its second dot against arch1's dot above. Each foot height is free inside the ink rather than tied to the stem's foot — that is what lets both arches be whole spacings (14 and 15) without scaling them: feet at y 77 (stem), 76.8 (middle) and 78.6 (right), under the flat feet at y 82.6. Every other arch dot clears the dots already drawn by ≥5.35 units. Every guide sample is on ink, min ink under any dot 4.1; guide bbox centre (51.6, 50.8). On the RMX3624 (`sx = 31.8 + 6.558x`, `sy = 536.3 + 6.558y`) the screenshot shows a single dot at each join, every predicted dot centre is ≥15.0 px inside a 16.1 px dot (6.3 px at a join before), and the trace completed at 97.4% (PERFECT), a new `practice_session` row (id 46).

- **3.14 n** — `english-small-n`, a shade taller than wide (aspect 0.988), fitted by height. m with one arch: two straight legs (ink row centres x 24.2 and 79.8; the stem's flat top at y 8.6, both flat feet at y 90) joined by a shoulder. Written as m is: `stem` top to bottom, then `arch` out of the stem, over the shoulder and down the right leg, following the skeleton until the leg turns straight at y 50 and one segment to the foot; the skeleton's spur up-left from the stem runs into the corner of the stem's slanted top cut, not a path. The stem is y 13.5..85.5 (72 units, 12 spacings; 13 would not fit inside the flat ends with both end dots on ink) and the arch's foot stops at the same height. The arch start is searched as in m (`n_build.py`): starting at y 33 (just above the junction at y 34) and at y 41 (below it) both make it a whole number of spacings, and y 41 was taken — 126 units, 21 spacings, still under the 140 ceiling — so it leaves the stem the way m's arches do, its first dot overlapping the stem's. Every guide sample is on ink, min ink under any dot 4.5; guide bbox centre (52.1, 49.5), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.3 + 6.561x`, `sy = 535.6 + 6.561y`, x/y scales agreeing to 0.15%). Every predicted dot centre is ≥14.8 px inside a 16.1 px dot except the stem dot at the junction (2.0 px), where the arch's connecting line crosses it and splits the colour mask (`worst_dots.py`). The trace completed at 98.1% (PERFECT), a new `practice_session` row (id 38). 355 unit tests, 0 failures.

  **Redone after the p review (Step 3.16): overlapping dots at the join.** The arch started at y 41, between two stem dots, so the join read as a clump. Now it leaves the stem **exactly on** a stem dot (`n_build.py`, with the search in `joinfit.py`): the stem is y 12..84 (still 12 spacings), and the arch leaves its dot at y 36, just below the skeleton junction at y 34, cutting straight across to the shoulder rather than following the skeleton's first points, which hug the stem and put the arch's second dot against the stem dot above. The arch body is scaled about its centre by 1.003 so the arch, ending at the stem's foot height, is a whole 20 spacings (120 units); its second dot clears the stem's by 5.27 units. Every guide sample is on ink, min ink under any dot 3.5; guide bbox centre (52.1, 48). On the RMX3624 (`sx = 31.1 + 6.567x`, `sy = 536.2 + 6.567y`) the screenshot shows a single dot at the join, every predicted dot centre is ≥14.6 px inside a 16 px dot (2.0 px at the join before), and the trace completed at 97.8% (PERFECT), a new `practice_session` row (id 45).

- **3.15 o** — `english-small-o`, taller than wide (aspect 0.921), fitted by height. One closed ring, written counter-clockwise from the top. Its centreline is ~190 units and turns smoothly all the way, so it is split at the top and at the bottom, where the letter sits on the line (a's split): `left` from the top down the left side to the bottom, then `right` from the bottom up the right and back to the top, closing the ring onto the first dot. The skeleton gives the ring as four quarter branches meeting at top, left, bottom and right, each junction growing a short spur into the bulge where Andika's stroke thickens — not paths. For a dot to land on both split points each half must be a whole number of spacings, so the ring is scaled about its centre by 1.0095 to 192 units (16 + 16 spacings), moving it under half a unit outward against a 9.1-unit pen half-width; a leftover draft `o_build.py` from an earlier session had a fixed 204-unit target that its scale search could not reach, and now picks the nearest multiple of 12 instead. Every guide sample is on ink, max centreline distance 0.4, min ink under any dot 8.5, bare path past each last dot 0.0; guide bbox centre (50, 48.4), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.2 + 6.567x`, `sy = 535.4 + 6.567y`, x/y scales agreeing to 0.27%, every predicted dot centre ≥14.3 px inside a 16.1 px dot) and the trace completed at 98.3% (PERFECT), a new `practice_session` row (id 40). 355 unit tests, 0 failures.

- **3.16 p** — `english-small-p`, much taller than wide (aspect 0.687), fitted by height. b turned upside down: a straight stem (ink row centre x 31.8, flat top at y 8.1 — the bowl's top at y 7 is the ink bbox top — and flat foot at y 90) with a closed bowl off its upper half, joined to the stem by the skeleton at y 25 and y 54. Written as taught: `stem` top to bottom, then `bowl` out of the stem, up-right over the top, clockwise down the right and round the bottom back into the stem. The skeleton's spur up-left from the upper junction runs into the corner of the stem's top, not a path. Sized by 3.11's rule: the stem is y 13.5..85.5 (72 units, 12 spacings, nearly centred between the flat ends; 78 would not fit with both end dots inside), which puts stem dots at y 25.5 and 55.5, beside the two junctions, and the bowl runs exactly between those two stem dots.

  **Redone after review on the device: overlapping dots.** The first pass started the bowl at y 27.5 (searched as n's arch is) and ended it at y 54, so at the upper join its first dot sat between the stem dots at 25 and 31 and overlapped both, and at the lower join its last dot half-covered the stem dot at 55 — a clump of dots at each join, and the user asked for it fixed. Now each end of the bowl lands **exactly on** a stem dot, so each join is closed but reads as one dot (`p_build.py`): the stem's start is chosen so that it has dots beside both junctions, 5 spacings apart. The bowl leaves the stem straight for the arch, skipping the skeleton's kink at the notch, because that kink put the bowl's second dot 4.7 units from the stem dot above (two 2.5-radius dots touch under 5). The skeleton body is scaled about its centre by 1.021 (the factor nearest 1 that passes the checks) so the bowl is a whole 19 spacings (114 units), and every bowl dot either coincides with a stem dot or clears it by ≥5.2 units. Every guide sample is on ink, min ink under any dot 3.9; guide bbox centre (51.5, 49.5), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 31.4 + 6.565x`, `sy = 535.5 + 6.565y`, x/y scales agreeing to 0.04%). Every predicted dot centre is now ≥15.2 px inside a 16.1 px dot; the first pass had a 2.2 px worst case where the overlapping dots blended. The cell sits below the fold at 720x1600, so the grid is swiped once before tapping it. The first pass traced at 98.5% (id 42); the revised guide completed at 98.3% (PERFECT), a new `practice_session` row (id 43). 355 unit tests, 0 failures.

- **3.17 q** — `english-small-q`, much taller than wide (aspect 0.652), fitted by height. g with a straight descender: a closed bowl whose arch runs into the stem's slanted top-right corner (top edge y 9.3 above the stem's centre), and a straight stem (ink row centre x 68.1) down to a flat foot at y 90; the skeleton's spur from the foot to (76.9, 89.9) is the foot's outer corner, not a path. Written round first, as a, d and g are: `bowl` from the stem's top, left over the top, counter-clockwise down the left and round the bottom back into the stem at the lower junction (y 50); then `stem` top to bottom. Built with 3.16's join rule from the start (`q_build.py` on `joinfit.fit`): the stem is y 13..85 (72 units, 12 spacings; 13 would not fit with both end dots inside), the bowl starts exactly on the stem's first dot and ends exactly on its dot at y 49, and its skeleton body is scaled about its centre by 1.026 so it is a whole 18 spacings (108 units), every other bowl dot clearing the stem's by ≥5.45 units. `_find` matches a branch by its end points within 1 unit, and here the long outer branch shares both ends with the short return branch, so the return is picked by length.

  **Float rounding drops a dot.** The first device run showed the stem with 12 dots and a bare line below: at y 12.7..84.7 the stem is 72 units exactly, but `12.7f` and `84.7f` put it a hair under 72 in float32, and `DottedPathSampler` only places a dot once a full spacing has been walked. The stem's ends are now kept to halves, which float32 holds exactly (as p, m and n already were). New scratchpad check `kt_dots.py` replays `StrokePoints` and `DottedPathSampler` in float32 over every stroke in `EnglishSmallExercises.kt` and prints each one's dot count and the bare path past its last dot, so this shows up before the device. Run over all 17 letters, every stroke from k on ends on a dot (a–j predate 3.11's whole-spacing rule; the ~5.9 on the i and j rings is the last dot closing onto the first). Every guide sample is on ink, min ink under any dot 3.5; guide bbox centre (49.3, 49), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.4 + 6.560x`, `sy = 535.8 + 6.560y`, x/y scales agreeing to 0.19%, every predicted dot centre ≥14.6 px inside a 16.1 px dot). The screenshot shows a dot on the stem's foot and one dot at each join, and the trace completed at 97.4% (PERFECT), a new `practice_session` row (id 47). 355 unit tests, 0 failures.

- **3.18 r** — `english-small-r`, taller than wide (aspect 0.857), fitted by height. A straight stem (ink row centre x 29.6, flat top at y 8.6, flat foot at y 90) and an arm that leaves it at the skeleton junction (29.8, 35.3), rises over the shoulder (65, 18.4) and comes down to a slanted flat cut, which forks the skeleton into a prong per corner. The spur up to (65.2, 9.4) runs into the shoulder's bulge and the one from the stem's top into the corner of its slanted cut; neither is a path. Written as taught: `stem` top to bottom, then `arm` out of the stem, over the shoulder and down towards the cut, as n's arch is. Built with 3.16's join rule and 3.17's half-unit ends (`r_build.py`): the stem is y 13.5..85.5 (72 units, 12 spacings, centred between the flat ends), and the arm leaves its dot at y 37.5, cutting straight across to the shoulder because the skeleton's first points hug the stem. **The cut inset is the free variable**, so nothing is reshaped: the arm stops 5.1 units short of the prongs' midpoint, which makes it a whole 10 spacings (60 units) with every other arm dot clear of the stem's. The alternative, a 3.1 inset, only works with the stem at y 15..87, leaving a 6.4-unit gap under its flat top against 3 at the foot, so the centred stem was taken. `kt_dots.py`: 13 stem dots, bare path 0; the arm's last dot sits 0.27 before its end. Every guide sample is on ink, min ink under any dot 4.5. The guide's bbox centre was x 52.6, close to the catalog test's 3-unit limit, so the whole guide is moved 2 units left as c and e are (centre (50.6, 49.5)); the canvas draws only the guide, so this moves placement, not shape, and the device scripts use the shifted `r_app.py`. On the RMX3624 the mapping matched 3.1's (`sx = 31.3 + 6.577x`, `sy = 535.0 + 6.577y`, x/y scales agreeing to 0.08%, every predicted dot centre ≥14.9 px inside a 15.8 px dot); the screenshot shows one dot at the join and a dot on each stroke end, and the trace completed at 98.6% (PERFECT), a new `practice_session` row (id 48). 355 unit tests, 0 failures.

- **3.19 s** — `english-small-s`, taller than wide (aspect 0.808), fitted by height. One movement between two slanted flat cuts: from the top-right cut left over the top, down the left, along the diagonal spine, round the bottom bowl and out to the lower-left cut. Each cut forks the skeleton into a prong per corner (top junction (70.2, 19.1), bottom (26.2, 75.9)), and the spurs at the top, left, right and bottom run into the bulges where Andika's stroke thickens; none of those is a path. As one stroke it is ~174 units, past the ceiling, so it is split where the spine crosses the letter's centre — the landmark between the two bowls — into `top` (84 units, 14 spacings) and `bottom` (90, 15), sharing the dot there. **Both cut insets are the free variables** (`s_build.py`): they make the whole path a whole number of spacings without reshaping the skeleton, and the pair whose larger inset is smallest is taken, so both ends sit alike — 4.4 and 4.4, where minimising their sum gave a lopsided 3.1 / 5.6. The split then sits on the whole spacing nearest the spine's midpoint, so both halves are whole spacings too. `kt_dots.py`: 15 + 16 dots, bare path 0.1 / 0.2. Every guide sample is on ink, min ink under any dot 4.6; guide bbox centre (48, 48.5), so no re-centring. On the RMX3624 the mapping matched 3.1's (`sx = 32.2 + 6.555x`, `sy = 536.2 + 6.555y`, x/y scales agreeing to 0.14%, every predicted dot centre ≥14.8 px inside a 16.1 px dot); the screenshot shows an evenly dotted s with the split invisible, and the trace completed at 98.7% (PERFECT), a new `practice_session` row (id 49). 355 unit tests, 0 failures.

- **3.20 t** — `english-small-t`, much taller than wide (aspect 0.586), fitted by height. A straight stem (ink x 37.4..54.6, flat top at y 7) curling at the foot into a hook that ends in a vertical flat cut at x 74.2, and a crossbar that is separate ink (x 25.8..73.4, y 26.4..38.9). Written as taught: `stem` top to bottom and round the hook in one pass, then `bar` left to right. The skeleton runs the hook into the cut's top corner and spurs down into the foot's bulge, neither a path, so past the curl the hook follows the ink's column centres (`t_cols.py`). **The crossing follows 3.16's join rule:** the stem is at x 46.5, half a unit right of its ink centre, so the bar, x 28.5..70.5 (42 units, 7 spacings, the most that fits with both end dots on ink), has a dot exactly on it, and the stem starts at y 9.5, the end dot's radius under the flat top, which puts its fifth dot at y 33.5 inside the bar's ink, so the bar runs there; every other bar dot is ≥6 units from any stem dot. The stem is a whole 16 spacings (96 units), and reaching that took three free variables (`t_build.py`): the top is pinned by the crossing, and at the minimum 2.5 cut inset the stem was 0.08 short. What made it fit was letting the end ease up to 4 units up the ~14-unit cut, as the glyph's hook turns up; the hand-over height from straight stem to curl barely moves the length. It stops 2.5 short of the cut (min ink under its last dot 2.61). **The catalog test caught a vertical off-centre** (bbox y 9.5..82.7, centre 46.1, outside the 3-unit tolerance; I had checked only x): the whole guide is moved 4 units down (`t_app.py`), the vertical counterpart of c's shift, keeping every end on halves. `kt_dots.py`: 17 + 8 dots, bare path 0. Every guide sample is on ink. On the RMX3624 the mapping matched 3.1's (`sx = 32.0 + 6.562x`, `sy = 535.4 + 6.562y`, x/y scales agreeing to 0.34%, every predicted dot centre ≥15.0 px inside a 16 px dot); the screenshot shows one dot at the crossing and a dot on every stroke end, and the trace completed at 99.6% (PERFECT), a new `practice_session` row (id 50). 355 unit tests, 0 failures.

---

# 5. Step 4 — Math Characters

## Requirement

Add math characters: numbers **1 to 20**, plus the basic operators, practised like letters.

## Implement (sub-steps 4.1–4.25, **one item per step**)

- Each sub-step adds exactly **one** item, then stops. Do not add a second item in the same step.
- Add `ExerciseType.MATH`; `MathExercises.kt` registered in `ExerciseCatalog`. Ids `math-1` … `math-20`, then `math-plus`, `math-minus`, `math-times`, `math-divide`, `math-equals`; titles are the glyphs (`+ − × ÷ =`).
- **Digits 1–9 (4.1–4.9)** are derived from a rendered glyph with the Step 1 per-letter recipe, one digit per sub-step, using the font chosen in 3.1 (record it in the source header comment). Author natural stroke order and direction (`1` top to bottom, `2` the hook then the base, and so on). No headline, so no stroke is a `-matra`; `StrokePoints.line` for straight runs, the dense skeleton polyline for curves.
- **10–20 (4.10–4.20) are composed from digit strokes** by a small content helper that lays two digits side by side inside the 0..100 canvas (scaled and offset), rather than 11 hand-derived glyphs. Composition keeps stroke order: left digit, then right digit. The digit **0** is needed only by 10 and 20 and has no catalog entry of its own; it is derived in 4.10, together with the helper, and reused by 20. Composed strokes get ids that start with the exercise id and stay unique; the composed guide's bbox centre stays within 3 units of (50, 50) and every point stays in 0..100.
- **Operators `+ − × ÷ =` (4.21–4.25)** are derived from the rendered glyph like the digits. `+` is two strokes (horizontal, then vertical), `−` one, `×` two diagonals, `÷` a bar then its two dots (ring rule from 1.22), `=` two bars top then bottom. `−` and `=` are far wider than tall, so they use the width fit; check the render's aspect for each.
- **Category plumbing rides with 4.1 (`1`)** so the category is never shown empty: the type, catalog registration, math category screen following `ConsonantsScreen`/`ConsonantsViewModel`, Home button, Progress button/section, and the `LearningProgress`, `ProgressRepositoryImpl`, `HomeViewModel` and `ProgressViewModel` changes, with string resources and Bengali UI copy consistent with the existing category names.
- Ordering: math items are ordered by numeric value (`order` 1–20 for the numbers, then 21–25 for the operators, contiguous from 1 within the type) so Step 5 can build "next number" blanks from the numbers alone.
- The last sub-step (4.25) also confirms the math screen scrolls and lays out 25 items at 720x1280 and 720x1600, and that Home/Progress measure the math bar against 25.

## Per-character recipe

Follow the Step 1 recipe and the Step 3 differences (no headline, per-item ink-bbox fit, `nio_calib.py` for the device mapping). In addition:

- **Digits and operators** update `ExerciseCatalogTest`'s per-exercise stroke-count map for the new id.
- **Composed numbers (10–20)** need no glyph derivation; instead each sub-step adds the id to the catalog, checks the composed guide on the device, and completes a trace at 95%+. A composed number is not checked against a rendered two-digit glyph's skeleton: it is checked that each half sits on its own digit's ink after scaling, which the helper's unit test covers for bounds and order.

## Sub-steps

Digits:

- [ ] 4.1 1 (also the math category plumbing)
- [ ] 4.2 2
- [ ] 4.3 3
- [ ] 4.4 4
- [ ] 4.5 5
- [ ] 4.6 6
- [ ] 4.7 7
- [ ] 4.8 8
- [ ] 4.9 9

Composed numbers:

- [ ] 4.10 10 (also the digit 0 strokes and the composition helper)
- [ ] 4.11 11
- [ ] 4.12 12
- [ ] 4.13 13
- [ ] 4.14 14
- [ ] 4.15 15
- [ ] 4.16 16
- [ ] 4.17 17
- [ ] 4.18 18
- [ ] 4.19 19
- [ ] 4.20 20

Operators:

- [ ] 4.21 +
- [ ] 4.22 −
- [ ] 4.23 ×
- [ ] 4.24 ÷
- [ ] 4.25 =

## Definition of Done (applies to every sub-step)

- [ ] The one item is added (derived from its rendered glyph for digits and operators; composed from digit strokes for 10–20)
- [ ] Every guide sample lies on the glyph's ink
- [ ] It completes at 95%+ on a faithful synthetic trace on the device
- [ ] `ExerciseCatalogTest`'s stroke-count map includes the new item, and its id, numeric `order`, canvas-bounds and centring assertions pass
- [ ] Existing ViewModel tests still pass
- [ ] Verified by running the app

Additionally:

- [ ] 4.1: category plumbing is in place and covered by ViewModel and progress-aggregation tests
- [ ] 4.10: the composition helper is unit-tested for bounds (every composed point in 0..100) and stroke order (left digit, then right digit); 11–20 reuse it without change

Step 4 as a whole is done when all 25 sub-steps are checked, the catalog test asserts the expected 25 items with unique ids and numeric `order`, and the math bar on Home and Progress is computed against 25.

## Notes

_(Add one note per sub-step as it is completed, as in Step 1.)_

---

# 6. Step 5 — Fill in the Blanks

## Requirement

Show a sequence with gaps, for example **a, _, c, d, _, e**. The child fills each gap by tracing the missing item.

## Implement

- **Sequences** come from the ordered catalog of a category: vowels, consonants, small letters, capital letters, numbers. Category is chosen from a new **Fill in the blanks** entry (Home button) then a category picker.
- **Generation** is a pure, seedable function (`BlankSequenceGenerator`) producing a window of 5–8 consecutive items with 1–3 blanks (never the first item, blanks may be adjacent only at the higher difficulty). Deterministic given a seed, so it is unit-testable.
- **Interaction.** Shown items are static glyphs. Each blank is a tracing cell (reusing the existing tracing canvas and scoring) with no dotted guide shown: the child recalls the item and traces it freehand, and a hint control reveals the dotted guide (using a hint lowers that blank's score cap; rule documented in code and tested).
- **State (MVI).** `FillBlanksState` (sequence, per-blank status), events (`BlankCompleted`, `HintUsed`, `NextSequence`), owned by `FillBlanksViewModel`.
- **Progress.** Each completed blank is saved through `ProgressRepository.savePracticeResult` against the missing item's exercise id, so it counts in category progress.
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
