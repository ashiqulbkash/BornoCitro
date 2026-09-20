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
