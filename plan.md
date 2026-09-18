# Bengali Kids Tracing App — Implementation Plan

> **Purpose:** This document is the master implementation plan for an Android application that teaches Bengali vowels (স্বরবর্ণ), consonants (ব্যঞ্জনবর্ণ), and basic drawing through interactive dotted-line tracing.
>
> **Agent usage:** This file is designed to be executed incrementally by Claude Code or another coding agent. The agent must not implement the whole application in one pass.

---

# 0. Agent Execution Instructions

You are implementing this Android application according to this `plan.md`.

General development conventions — architecture, code quality, testing, build/verification, git, and task-reporting requirements — are defined in `CLAUDE.md` and apply to every step below. This section adds only the rules specific to executing this plan.

## PLAN EXECUTION RULES

1. Read the entire `plan.md` before making changes.
2. Implement exactly **one numbered step at a time**. Do not implement multiple steps unless explicitly instructed.
3. Do not move to the next step automatically unless explicitly instructed.
4. Do not use placeholder implementations for a real feature unless the current step explicitly requires a prototype.
5. Keep the tracing engine independent from ViewModel, Room, Hilt, and Compose UI implementation details (see Section 4).

## STEP COMPLETION

A step is complete only when its Definition of Done is satisfied.

Do not mark a step complete merely because the application compiles.

After completing a step, **STOP and wait for the next instruction.**

---

# 1. Product Overview

## Goal

Build a child-friendly Android application that teaches:

- স্বরবর্ণ — Bengali vowels
- ব্যঞ্জনবর্ণ — Bengali consonants
- Basic drawing and hand-control exercises

The primary learning mechanism is **guided tracing**.

Each letter or drawing is represented by a dotted/stroke path. The child traces the path using their finger. The application evaluates the tracing, assigns a score, stores the result, and shows learning progress over time.

## Core Learning Philosophy

The application should encourage:

```text
See
 ↓
Understand
 ↓
Trace
 ↓
Practice
 ↓
Receive Feedback
 ↓
Practice Again
 ↓
Improve
 ↓
Master
```

The application should not feel like an exam.

Low scores should encourage another attempt rather than make the child feel that they failed.

---

# 2. Core Features

## Learning

1. Bengali vowels (স্বরবর্ণ)
2. Bengali consonants (ব্যঞ্জনবর্ণ)
3. Dotted tracing
4. Multiple strokes per letter
5. Stroke-order guidance
6. Whole-letter practice — the complete letter is shown at once and traced over
7. Drawing exercises
8. Practice-based learning

## Progress

1. Overall progress
2. Category progress
3. Exercise-level progress
4. Practice attempt count
5. Best score
6. Latest score
7. Completion state
8. Mastery state

## Scoring

Tracing should produce:

- LOW
- MEDIUM
- PERFECT

Example initial thresholds:

```text
0–59   → LOW
60–89  → MEDIUM
90–100 → PERFECT
```

Thresholds must be configurable and should not be hardcoded throughout the UI.

## Tips

Provide simple guidance such as:

- Follow the dots.
- Start from the highlighted dot.
- Move your finger slowly.
- Try to stay close to the dotted line.
- Complete all strokes.

Tips should be positive and age-appropriate.

---

# 3. Technical Requirements

## Required Stack

- Kotlin
- Jetpack Compose
- Material 3 where appropriate
- Navigation Compose
- Hilt
- Room
- Kotlin Coroutines
- Kotlin Flow / StateFlow
- Android Architecture Components
- MVI architecture
- Gradle Version Catalog
- Unit tests
- UI/instrumentation tests where appropriate

## General Android Requirements

Follow current Android development best practices:

- Lifecycle-aware state collection
- Proper coroutine scopes
- No blocking work on the main thread
- Immutable UI state
- Proper state restoration where relevant
- Accessibility support
- Responsive UI
- Proper resource management
- Release/R8 compatibility
- Avoid unnecessary dependencies

---

# 4. Architecture

Use a pragmatic layered architecture with MVI.

```text
┌─────────────────────────────┐
│        Compose UI           │
│  Stateless where practical  │
└──────────────┬──────────────┘
               │ Event
               ↓
┌─────────────────────────────┐
│         ViewModel           │
│            MVI              │
└──────────────┬──────────────┘
               │
               ↓
┌─────────────────────────────┐
│     Domain / Use Cases      │
└──────────────┬──────────────┘
               │
               ↓
┌─────────────────────────────┐
│        Repository           │
└──────────────┬──────────────┘
               │
               ↓
┌─────────────────────────────┐
│          Room DB            │
└─────────────────────────────┘
```

Tracing is a separate subsystem:

```text
Finger Input
     ↓
Tracing Engine
     ↓
Trace Result
     ↓
Score Calculator
     ↓
Practice Result
     ↓
Repository
     ↓
Room
```

The tracing engine must not depend on ViewModel, Room, Hilt, or Compose UI implementation details.

---

# 5. Project Structure

Start with a simple structure.

If the application is initially small, these can be packages within the app module.

```text
app/
    navigation/
    di/
    presentation/

core/
    common/
    model/
    database/
    tracing/
    ui/

feature/
    home/
    tips/
    progress/
    vowels/
    consonants/
    drawing/
    practice/
    result/
```

## Future Modularization

Do not immediately create many Gradle modules.

Move a package into a separate Gradle module only when there is a concrete benefit such as:

- Independent reuse
- Build isolation
- Clear ownership
- Reduced dependency coupling

---

# 6. Dependency Direction

Preferred dependency direction:

```text
feature
   ↓
core-ui
core-model
core-common
   ↓
core-database
```

Features should not depend on another feature's internal implementation.

Shared models and reusable domain components belong in core/shared packages.

---

# 7. MVI Standard

Every feature should follow the same basic pattern.

Example:

```kotlin
data class PracticeState(
    val exercise: Exercise? = null,
    val currentStroke: Int = 0,
    val score: Float = 0f,
    val isTracing: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null
)
```

Events:

```kotlin
sealed interface PracticeEvent {
    data object Retry : PracticeEvent
    data object NextStroke : PracticeEvent
    data object Finish : PracticeEvent
    data object TryAgain : PracticeEvent
}
```

The Composable:

- Reads state
- Displays state
- Sends events

The ViewModel:

- Receives events
- Coordinates business operations
- Updates state

The Repository:

- Handles data access

The Domain layer:

- Contains business rules and calculations

---

# 8. Domain Models

Exercise type:

```kotlin
enum class ExerciseType {
    VOWEL,
    CONSONANT,
    DRAWING
}
```

Difficulty:

```kotlin
enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}
```

Score:

```kotlin
enum class ScoreLevel {
    LOW,
    MEDIUM,
    PERFECT
}
```

Exercise:

```kotlin
data class Exercise(
    val id: String,
    val title: String,
    val type: ExerciseType,
    val difficulty: Difficulty,
    val strokes: List<Stroke>,
    val order: Int
)
```

Stroke:

```kotlin
data class Stroke(
    val id: String,
    val points: List<Point>
)
```

These models are conceptual and can be adjusted when the tracing implementation is prototyped.

---

# 9. Exercise Content Architecture

Do not hardcode letters inside Composables.

Exercise definitions should be data-driven.

Conceptually:

```text
content/
    vowels/
        অ
        আ
        ই
        ঈ
        উ
        ঊ
        ঋ
        এ
        ঐ
        ও
        ঔ

    consonants/
        ক
        খ
        গ
        ঘ
        ঙ
        ...

    drawings/
        line
        curve
        circle
        square
        triangle
        house
        flower
        ...
```

Each exercise may contain:

- ID
- Bengali character/title
- Exercise type
- Stroke definitions
- Stroke order
- Difficulty
- Display order
- Optional audio
- Optional tip
- Optional preview image

Static exercise definitions should initially be bundled with the application.

Room should primarily store **learning activity and progress**, not static exercise definitions.

---

# 10. Room Database

Use Room for persisted progress.

## ExerciseProgressEntity

Conceptually:

```text
exerciseId
attemptCount
completedCount
bestScore
lastScore
scoreLevel
lastPracticedAt
isMastered
```

## PracticeSessionEntity

Conceptually:

```text
id
exerciseId
score
scoreLevel
duration
completed
createdAt
```

Relationship:

```text
Exercise
   │
   └── ExerciseProgress
           │
           └── PracticeSession[]
```

The exact schema should be finalized while implementing the database step.

## Database Rules

- No Room access from Composables
- No direct DAO access from ViewModels
- Use repositories
- Use suspend functions/Flow appropriately
- Avoid database writes on every touch event
- Persist a practice result after an exercise attempt/completion

---

# 11. Repository Layer

Example:

```kotlin
interface ExerciseRepository {
    fun observeExercises(
        type: ExerciseType
    ): Flow<List<Exercise>>

    suspend fun getExercise(
        id: String
    ): Exercise?
}
```

```kotlin
interface ProgressRepository {
    fun observeProgress(): Flow<LearningProgress>

    fun observeExerciseProgress(
        exerciseId: String
    ): Flow<ExerciseProgress?>

    suspend fun savePracticeResult(
        result: PracticeResult
    )
}
```

ViewModels should not know Room DAO details.

---

# 12. Screen Architecture Rule

Every screen should be implemented as a complete vertical feature slice.

A screen is not considered complete if only its UI is created.

Where applicable, implementation should include:

```text
Screen
 ↓
UI State
 ↓
Events
 ↓
ViewModel
 ↓
Use Case
 ↓
Repository
 ↓
DAO / Data Source
 ↓
Tests
```

For static screens, unnecessary layers should not be created.

For example, a completely static Tips screen does not need a repository just to satisfy an architectural rule.

Avoid architecture for architecture's sake.

---

# 13. Screen List

Initial screens:

```text
Tips
Home
Vowels
Consonants
Drawing
Practice
Result
Progress
```

Navigation:

```text
Tips
Home
Vowels
Consonants
Drawing
Practice/{exerciseId}
Result/{sessionId}
Progress
```

Pass stable IDs through navigation instead of large objects.

---

# 14. Implementation Roadmap

The application should be implemented in controlled vertical slices.

```text
Phase 1
Foundation

Phase 2
Design System

Phase 3
Navigation

Phase 4
Tips

Phase 5
Home

Phase 6
Exercise Content

Phase 7
Vowels

Phase 8
Tracing Engine Prototype

Phase 9
Tracing Engine Tests / Refinement

Phase 10
Practice

Phase 11
Exercise Content Correction

Phase 12
Scoring

Phase 13
Result

Phase 14
Progress

Phase 15
Consonants

Phase 16
Drawing

Phase 17
Mastery

Phase 18
Contextual Tips

Phase 19
Animations / Feedback

Phase 20
Accessibility / Performance

Phase 21
Final Testing / Release
```

Phase 11 was added after Step 11 revealed that most exercise content had incorrect stroke geometry. Correcting it before Scoring avoids tuning tolerances against wrong paths.

---

# 15. Step 1 — Project Bootstrap

## Objective

Create the initial Android project and establish the technical foundation.

## Implement

- Kotlin project
- Jetpack Compose
- Material 3
- Version Catalog
- Hilt
- Room
- Navigation Compose
- Coroutines
- Basic theme
- Application class
- Hilt setup
- Room database setup
- Build types
- R8 configuration
- Test configuration

## Deliverable

Application launches successfully with a basic Compose screen.

## Definition of Done

- [ ] Project builds
- [ ] App installs
- [ ] App launches
- [ ] Hilt starts successfully
- [ ] Room initializes successfully
- [ ] Compose works
- [ ] Basic unit test passes

---

# 16. Step 2 — Design System

## Objective

Create the reusable visual foundation.

## Implement

- Colors
- Typography
- Shapes
- Spacing
- Dimensions
- Buttons
- Cards
- Top app bar
- Progress indicators
- Empty states
- Feedback components

## Child-Friendly Requirements

- Large touch targets
- Large Bengali characters
- Clear visual feedback
- Limited text
- Simple navigation
- Friendly visual hierarchy

## Deliverable

Reusable Compose design components with previews.

---

# 17. Step 3 — Navigation Foundation

## Objective

Create navigation routes before implementing full screens.

## Implement

- Navigation graph
- Route definitions
- Navigation arguments
- Placeholder screens

## Deliverable

All planned screens can be reached through navigation.

---

# 18. Step 4 — Tips Screen

## Objective

Teach the child how tracing works.

## Content

```text
How to Practice

1. Follow the dots.
2. Start from the highlighted dot.
3. Move your finger slowly.
4. Stay close to the dotted line.
5. Complete all strokes.
```

## Architecture

For static content:

```text
TipsScreen
```

A ViewModel/repository is optional unless the requirements become dynamic.

## Deliverable

A simple onboarding/tutorial experience.

---

# 19. Step 5 — Home Screen

## Objective

Create the main learning dashboard.

## UI

```text
Welcome!

Continue Learning

[ স্বরবর্ণ ]
[ ব্যঞ্জনবর্ণ ]
[ আঁকা ]

Your Progress
████████░░ 80%
```

## Data Flow

```text
HomeScreen
    ↓
HomeViewModel
    ↓
ProgressRepository
    ↓
Room
```

## State

```kotlin
data class HomeState(
    val overallProgress: Float = 0f,
    val vowelProgress: Float = 0f,
    val consonantProgress: Float = 0f,
    val drawingProgress: Float = 0f,
    val continueExerciseId: String? = null
)
```

## Deliverable

Home displays real persisted progress.

---

# 20. Step 6 — Exercise Content System

## Objective

Create the data-driven exercise system.

## Implement

- Exercise model
- Stroke model
- Exercise provider
- Initial vowel content
- Initial consonant content
- Initial drawing content
- Exercise ordering
- Difficulty metadata

## Important

Do not implement the complete tracing engine yet.

First verify that exercise definitions can be loaded and rendered as dotted previews.

## Deliverable

Exercises are loaded from structured data rather than hardcoded UI.

---

# 21. Step 7 — Vowels Screen

## Objective

Display Bengali vowels and their progress.

## Example

```text
স্বরবর্ণ

[ অ ] [ আ ] [ ই ]
[ ঈ ] [ উ ] [ ঊ ]
[ ঋ ] [ এ ] [ ঐ ]
[ ও ] [ ঔ ]
```

Each exercise should display its current learning state.

Example:

```text
অ
Completed

আ
2 attempts

ই
Mastered
```

## Data Flow

```text
VowelsScreen
    ↓
VowelsViewModel
    ↓
ExerciseRepository
ProgressRepository
    ↓
Room / Static Content
```

## Deliverable

Child can select a vowel and enter practice.

---

# 22. Step 8 — Consonants Screen

## Objective

Display Bengali consonants.

Reuse the exercise-list UI from vowels.

Only the exercise category/data should change.

## Deliverable

Child can browse and select consonants.

---

# 23. Step 9 — Drawing Screen

## Objective

Provide dotted drawing exercises for hand-control development.

## Initial Content

Start with:

- Straight line
- Curved line
- Circle
- Square
- Triangle
- House
- Flower
- Simple object/animal

Do not create dozens of drawings initially.

## Deliverable

Child can select a drawing and enter practice.

---

# 24. Step 10 — Tracing Engine Prototype

> **This is the most technically important part of the application.**

Do not integrate the tracing engine into the entire application immediately.

Build an isolated prototype first.

## Objective

Prove that tracing feels:

- Smooth
- Responsive
- Accurate enough
- Forgiving
- Child-friendly

## Engine Responsibilities

1. Render dotted guide paths
2. Receive finger/pointer movement
3. Sample touch points
4. Calculate distance from expected path
5. Calculate path coverage
6. Handle multiple strokes
7. Track stroke order
8. Detect stroke completion
9. Calculate accuracy
10. Produce a trace result
11. Show every stroke's guide at once so the whole letter is traced in one pass

## Prototype Architecture

```text
Expected Stroke Path
        ↓
Tracing Surface
        ↑
   Finger Input
        ↓
Tracing Engine
        ↓
Trace Result
```

## IMPORTANT

Do not put scoring/business rules directly into the Canvas.

The tracing engine should produce measurement data.

The score calculator should transform that data into a learning score.

The tracing engine is a specialized, performance-sensitive subsystem — do not force it into the standard screen architecture used by other features.

Tracing behavior must ultimately be validated on a physical Android device; an emulator alone is insufficient for validating touch feel.

---

# 25. Step 10.1 — Tracing Domain Models

Create models for:

```text
Point
Stroke
TracePoint
StrokeTraceResult
TraceResult
```

Keep them independent from Compose.

## Deliverable

Domain models compile and have unit tests where appropriate.

---

# 26. Step 10.2 — Dotted Path Renderer

Create a Compose Canvas prototype that can render:

```text
• • • • • • •
```

along an expected stroke.

## Requirements

- Configurable dot spacing
- Configurable dot size
- Configurable path width
- Support straight paths
- Support curves

## Deliverable

A static dotted path can be rendered correctly.

---

# 27. Step 10.3 — Pointer / Touch Tracking

Implement pointer tracking.

Capture:

- Start
- Move
- End
- Cancel

Do not write anything to Room.

Do not perform heavy calculations on every event.

## Deliverable

Finger movement is visualized smoothly.

---

# 28. Step 10.4 — Point-to-Path Distance

Implement the algorithm that determines how close a user's finger is to the expected stroke.

Conceptually:

```text
Finger Point
     ↓
Nearest point on expected path
     ↓
Distance
```

Define a configurable tolerance.

The tolerance should be forgiving enough for children.

## Tests

Test:

- Exact path
- Slight deviation
- Large deviation
- Boundary cases

---

# 29. Step 10.5 — Path Coverage

Determine how much of the expected path the child has successfully traced.

Example:

```text
Expected path: 100%
Traced path:    82%

Coverage: 82%
```

Avoid calculating coverage solely from the number of touch points because touch sampling density depends on finger speed/device.

Use path distance/segments or another normalized method.

## Deliverable

Reliable coverage measurement.

---

# 30. Step 10.6 — Multi-Stroke Tracking

Bengali letters can contain multiple strokes.

An attempt runs in a single pass. Every stroke's guide is shown at once, so the child sees the complete letter and practises on top of it:

```text
Whole letter — all guides shown at once

Stroke 1
   ↓
Stroke 2
   ↓
Stroke 3
   ↓
Exercise complete
```

There are no step-by-step practice versions; the letter is never built up one guide at a time.

The strokes are still traced in their teaching order, so stroke order can be tracked and scored.

A single-stroke exercise behaves the same way — its one guide is already the whole letter.

Track:

- Current stroke
- Completed strokes
- Stroke coverage
- Stroke accuracy
- Incorrect stroke behavior

## Deliverable

Multiple-stroke exercises work correctly with the complete letter shown throughout.

---

# 31. Step 10.7 — Trace Score Calculator

Create a dedicated scoring component/use case.

Example:

```text
TraceResult
     ↓
Score Calculator
     ↓
0–100 score
     ↓
ScoreLevel
```

Initial classification:

```text
0–59   LOW
60–89  MEDIUM
90–100 PERFECT
```

Keep thresholds configurable.

## Important

Do not make tracing speed the primary scoring factor.

Accuracy and completion should matter more for young children.

---

# 32. Step 10.8 — Tracing Prototype with Bengali অ

Use Bengali `অ` as the first real test letter.

Validate:

- Correct stroke path
- Starting point
- Stroke order
- Dotted rendering
- Finger interaction
- Coverage
- Score
- Complete letter shown at once while tracing

## Authoring Stroke Geometry

Do not approximate a letter's shape by eye. A guide that only loosely resembles the letter teaches the wrong hand movement, and the error is not obvious until it is seen on a device next to the rendered character.

Derive the stroke points from the rendered glyph itself:

```text
Render the glyph
        ↓
Upscale / binarize
        ↓
Skeletonize to the centreline
        ↓
Read ordered waypoints
        ↓
Normalize into the 0..100 canvas
```

Rules:

- The guide must follow the pen centreline, never the outer contour.
- Include the matra (headline) when the letter has one — it is the most recognizable part of the character.
- Keep the letter's true proportions; centre it rather than stretching it to fill the square.

`অ` is three strokes: bowl, then stem, then matra — Bengali draws the headline last. Every other exercise is brought up to this standard in Steps 11.1–11.17.

## Deliverable

A real Bengali letter can be traced in the prototype with the complete letter shown at once.

---

# 33. Step 10.9 — Tracing Prototype with Bengali ক

Use `ক` because it provides a different stroke structure.

Validate that the tracing engine is not accidentally specialized for `অ`.

`ক` now carries its real four-stroke geometry (Step 11.11) and all of its guides are shown together, with no engine change. The single-stroke case is still covered — by the `Circle` drawing in Step 10.10, whose one guide is already the whole shape.

## Deliverable

At least two structurally different Bengali letters work.

---

# 34. Step 10.10 — Tracing Prototype with a Drawing

Use a simple drawing such as:

```text
Circle
```

or:

```text
House
```

Validate that the same tracing engine can support non-letter exercises.

A drawing is practised exactly like a letter: every stroke's guide is shown at once and traced over. Nothing on screen describes the exercise in words, so drawings need no wording of their own.

## Deliverable

The same tracing engine supports letters and drawings.

---

# 35. Step 10.11 — Tracing Engine Integration

Only after the prototype is stable should it be integrated into the real Practice feature.

## Deliverable

Reusable tracing component + domain engine.

---

# 36. Step 11 — Practice Screen

## Objective

Connect real exercises to the tracing engine.

## UI Concept

```text
← Back

          অ

  all strokes' dots shown
```

The complete letter is on screen from the first touch. There are no step-by-step practice versions to work through first, and no step or stroke caption above the canvas.

Ink from strokes already written stays on screen, so the letter visibly builds up as the child writes it.

Potential controls:

- Reset
- Back
- Hint
- Next
- Try Again

Keep the interaction simple.

## Data Flow

```text
PracticeScreen
      ↓
PracticeViewModel
      ↓
ExerciseRepository
ProgressRepository
      ↓
Tracing Engine
```

The ViewModel coordinates the exercise lifecycle.

The tracing engine handles touch/path calculations.

## Deliverable

A child can complete a real exercise.

---

# 37. Step 11.1–11.17 — Exercise Content Correction

> **Model requirement:** this block must be executed with **Claude Opus**. Deriving stroke geometry means reading a glyph's shape, judging whether a traced path matches it, and catching a wrong result that still looks plausible. A weaker model produces confident, wrong-shaped paths — which is exactly the defect being fixed here.

## Why this sits between Step 11 and Step 12

Step 11 made a real exercise traceable. It also made visible that roughly 90% of the exercise content does not render a correct dotted path — only `অ` has geometry derived from its glyph.

Everything downstream measures a child's trace **against** that path:

```text
wrong guide path
      ↓
Step 12 Scoring     tolerance tuned against a shape that is wrong
      ↓
Step 13 Result      a number that means nothing
      ↓
Step 14 Progress    mastery awarded for tracing the wrong shape
```

Fixing the content later would mean re-tuning scoring and invalidating any progress already stored. It is cheaper and more honest to fix it here.

These sub-steps keep the numbering of Steps 12–25 unchanged.

## Prerequisites

Two items were outstanding from the Step 11 work. Both are now closed:

```text
P1  Verify whole-letter practice on a physical device
    done — আ's four strokes were traced on a device during
    Step 11.1 and ended on the Result screen

P2  Fix the step label for non-letters
    done — the step label was removed altogether, so no
    wording has to suit letters and drawings alike
```

## Known Defects

The placeholder content shares a small number of root causes, not 20 unrelated bugs:

```text
1. No matra          every letter except অ is missing its headline bar
2. Eyeballed shapes  zigzag polylines that only loosely suggest the letter
3. Copied bodies     আ/ই/ঈ, উ/ঊ, এ/ঐ, ও/ঔ reuse a body that was never correct
4. Single stroke     most letters are one stroke, not their real stroke split
```

## Per-Exercise Procedure

One exercise per sub-step. Do not batch them, and verify each before moving on.

```text
1. Render the glyph and derive the centreline   (method: Step 10.8)
2. Decide stroke split and order                 (matra last)
3. Write the points, normalized into 0..100
4. Overlay the result on the glyph and compare
5. Build + unit test
6. Check on a physical device against the
   character shown above the canvas
7. Confirm the complete letter is shown while tracing
```

## Per-Exercise Definition of Done

- [ ] Geometry derived from the glyph, not approximated
- [ ] Matra present where the letter has one
- [ ] Stroke count and order correct
- [ ] Guide follows the pen centreline
- [ ] Proportions preserved, letter centred in the canvas
- [ ] Unit test pins stroke ids and starting point
- [ ] Verified on a physical device against the rendered character
- [ ] Complete letter shown at once for multi-stroke exercises

## Sub-Steps

Vowels — `অ` is already done and is the reference implementation. These follow teaching order, which also front-loads the MVP vowel set:

```text
11.1   আ    vowel-aa    done — 4 strokes (bowl, stem, আ-কার, matra)
11.2   ই    vowel-i     done — 3 strokes (hook, body, matra)
11.3   ঈ    vowel-ii    done — 4 strokes (hook, bowl, tail, matra)
11.4   উ    vowel-u     done — 4 strokes (hook, stem, bowl, matra)
11.5   ঊ    vowel-uu    done — 5 strokes (hook, stem, bowl, arm, matra)
```

**Checkpoint after 11.5.** `অ আ ই ঈ উ ঊ` is the MVP vowel set from Section 51. With these correct the whole learning loop can be exercised honestly end to end. If schedule pressure appears, Steps 12–14 may run here and the remaining sub-steps resume afterwards — but no exercise ships with placeholder geometry.

That checkpoint has passed: Steps 11.6–11.10 ran without needing it, so every vowel — the MVP set and the rest — now carries glyph-derived geometry. The consonants and drawings (11.11–11.17) are what remain.

```text
11.6   ঋ    vowel-ri    done — 6 strokes (curl, diagonal, sweep, stem, hook, arm)
11.7   এ    vowel-e     done — 3 strokes (curl, stem, base)
11.8   ঐ    vowel-oi    done — 4 strokes (curl, stem, base, kar)
11.9   ও    vowel-oa    done — 2 strokes (curl, sweep)
11.10  ঔ    vowel-au    done — 3 strokes (curl, sweep, kar)
```

Consonants:

```text
11.11  ক    consonant-ko    done — 4 strokes (knot, stem, lobe, matra)
11.12  খ    consonant-kho
11.13  গ    consonant-go
11.14  ঘ    consonant-gho
11.15  ঙ    consonant-ngo
```

Drawings:

```text
11.16  Line, Circle, Square, Triangle   verify only
11.17  House                            composite shape, re-derive if wrong
```

Drawings in 11.16 are defined by exact geometry — an arc is a circle, four lines are a square — so they carry no handwriting risk and need verification rather than redrawing. Correct them only if the device check shows a real defect.

## Deliverable

Every exercise renders a dotted path that matches the character or shape shown above it, verified on a physical device.

---

# 38. Step 12 — Scoring

> **Prerequisite:** Step 11.1–11.17. Scoring a trace against a wrong guide path produces meaningless numbers and mis-tuned tolerances.

## Objective

Produce a consistent learning score.

## Metrics

At minimum:

- Path coverage
- Distance from guide path
- Stroke completion
- Stroke order

Optional:

- Completion duration

Do not over-weight time.

## Scoring a Whole-Letter Attempt

A multi-stroke attempt produces one stroke result per stroke of the single whole-letter pass (Step 10.6).

Existing behavior carried in from Step 10.6 averages every stroke into one score. Choose the model deliberately in this step rather than inheriting it:

```text
A. Equal weight      average every stroke equally (current)
B. Weighted          some strokes count for more
```

Whichever is chosen:

- Keep the weighting configurable, not hardcoded.
- A single-stroke exercise must still score correctly.
- A retried stroke should not be able to drag the score below an honest reflection of the final attempt.

## Output

```text
Accuracy: 94
Level: PERFECT
```

```text
Accuracy: 76
Level: MEDIUM
```

```text
Accuracy: 48
Level: LOW
```

## Deliverable

Same scoring logic works for letters and drawings.

---

# 39. Step 13 — Result Screen

## Perfect

```text
Great Job! 🎉

অ

Perfect!

94%

[Practice Again]
[Next]
```

## Medium

```text
Good Try!

76%

[Try Again]
[Next]
```

## Low

```text
Let's Practice Again!

48%

[Try Again]
```

Do not use language that makes a child feel punished for a low score.

## Data Flow

```text
Practice completed
      ↓
Save Practice Result
      ↓
Room
      ↓
Result Screen
```

## Deliverable

Every practice attempt has a clear result.

---

# 40. Step 14 — Progress Screen

## Objective

Show learning progress.

## UI

```text
My Progress

Overall
████████░░ 80%

স্বরবর্ণ
██████████ 100%

ব্যঞ্জনবর্ণ
██████░░░░ 60%

Drawing
███████░░░ 70%
```

Exercise-level:

```text
অ      ⭐⭐⭐
আ      ⭐⭐
ই      ⭐⭐⭐
ঈ      ⭐
```

## Data Flow

```text
ProgressScreen
      ↓
ProgressViewModel
      ↓
ProgressRepository
      ↓
Room
```

Room should provide aggregate queries where practical.

## Deliverable

Progress survives app restart.

---

# 41. Step 15 — Learning Completion and Mastery

Do not treat "opened" or "attempted once" as "learned."

Recommended conceptual states:

```text
Not Started
    ↓
Started
    ↓
Practicing
    ↓
Completed
    ↓
Mastered
```

Initial mastery rule can be:

```text
Completed at least N times
AND
Best score >= mastery threshold
```

Example:

```text
3 successful practices
AND
best score >= 80%
```

Keep this configurable.

---

# 42. Step 16 — Drawing Practice

Use the same tracing and scoring infrastructure.

```text
Drawing
   ↓
Stroke Paths
   ↓
Tracing Engine
   ↓
Score
   ↓
LOW / MEDIUM / PERFECT
   ↓
Progress
```

Multi-stroke drawings end with the same whole-shape pass as letters (Step 10.6).

Do not implement a separate tracing engine for drawings unless requirements genuinely differ.

---

# 43. Step 17 — Contextual Tips

After the core learning loop works, introduce contextual guidance.

Examples:

```text
Tip:
Try tracing slowly.

Tip:
Start from the highlighted dot.

Tip:
Keep your finger close to the dots.
```

Tips may appear:

- Before the first attempt
- After repeated low scores
- When the child misses a stroke repeatedly
- After completing a difficult exercise

Do not overwhelm the child with messages.

---

# 44. Step 18 — Animations and Feedback

Only after core functionality is stable.

Potential enhancements:

- Starting-dot animation
- Current-dot highlighting
- Stroke progression
- Completion animation
- Success animation
- Gentle haptic feedback
- Optional sound
- Progress animation

Performance takes priority over decorative animation.

---

# 45. Step 19 — Accessibility and Child-Friendly UX

Requirements:

- Large touch targets
- Clear Bengali text
- Large letter rendering
- Readable typography
- High visual clarity
- Avoid tiny controls
- Avoid color-only feedback
- Meaningful content descriptions
- Support different screen sizes
- Consider font scaling
- Avoid animations that interfere with tracing

Test on multiple device sizes.

---

# 46. Step 20 — Offline-First Behavior

The learning experience should work without internet.

Local data:

```text
Exercise Content
Progress
Practice Sessions
Settings
```

No network should be required for:

- Viewing letters
- Tracing
- Scoring
- Viewing progress

Cloud synchronization can be added later if required.

---

# 47. Step 21 — Testing Strategy

> **This is a standing reference, not a gate.** `CLAUDE.md` requires tests alongside every behaviour change, so this list is applied continuously from Step 1 onward. Reaching this step means auditing coverage against the list, not starting to write tests.

## Unit Tests

Test:

- Score calculation
- Score level classification
- Mastery calculation
- Progress aggregation
- Exercise ordering
- ViewModel state transitions
- Repository behavior

## Tracing Tests

Test:

- Straight line
- Curves
- Exact tracing
- Slightly inaccurate tracing
- Very inaccurate tracing
- Partial tracing
- Multiple strokes
- Wrong stroke order
- Accidental touches
- Repeated tracing
- Stroke boundaries
- Whole letter shown at once while tracing
- Single-stroke exercise

## Room Tests

Test:

- Save practice result
- Update best score
- Increment attempt count
- Aggregate progress
- Mastery calculation
- Persistence

## UI Tests

Critical flow:

```text
Home
 → Vowels
 → অ
 → Practice
 → Result
 → Progress
```

Drawing flow:

```text
Home
 → Drawing
 → Exercise
 → Practice
 → Result
```

---

# 48. Step 22 — Performance

> **This is a standing constraint, not a gate.** These rules bind from Step 10 onward — tracing performance cannot be retrofitted at step 22. Reaching this step means measuring and confirming, not starting to care.

Tracing must feel real-time.

Avoid:

- Heavy allocations per pointer event
- Database writes during tracing
- Expensive recomposition during tracing
- Heavy calculations on the main thread
- Excessively dense paths

Recommended:

```text
Touch Event
    ↓
In-memory tracing calculation
    ↓
UI update
    ↓
Exercise completed
    ↓
Persist final result
```

Never persist every touch point to Room unless a future requirement explicitly needs raw trace recording.

---

# 49. Step 23 — Bengali Content Validation

The per-exercise correction work happens earlier, in Steps 11.1–11.17. This step is the **final sweep before release**: re-verify the whole catalogue in one pass, including the metadata that the correction steps do not cover (difficulty, ordering, audio).

Every letter must be validated.

For each letter verify:

- Correct Bengali character
- Correct stroke order
- Correct starting point
- Correct dotted path
- Correct number of strokes
- Matra (headline) included where the letter has one
- Guide follows the pen centreline, not the outer contour
- Correct proportions
- Correct difficulty
- Correct ordering
- Complete letter shown at once while tracing
- Correct pronunciation/audio if added

Author every letter with the method established in Step 10.8 — derived from the rendered glyph's skeleton, never approximated by eye. Check each letter on a device against the character shown above the canvas.

## Content Status

Tracked here so the catalogue's real state is visible at a glance. Update as Steps 11.1–11.17 land.

```text
21 exercises total

অ                  done — 3 strokes (bowl, stem, matra), derived from the glyph
আ                  done — 4 strokes (bowl, stem, আ-কার, matra), derived from the glyph
ই                  done — 3 strokes (hook, body, matra), derived from the glyph
ঈ                  done — 4 strokes (hook, bowl, tail, matra), derived from the glyph
উ                  done — 4 strokes (hook, stem, bowl, matra), derived from the glyph
ঊ                  done — 5 strokes (hook, stem, bowl, arm, matra), derived from the glyph
ঋ                  done — 6 strokes (curl, diagonal, sweep, stem, hook, arm), derived from
                   the glyph; the first vowel with no matra
এ                  done — 3 strokes (curl, stem, base), derived from the glyph; no matra
ঐ                  done — 4 strokes (curl, stem, base, kar), derived from its own glyph,
                   not shared with এ
ও                  done — 2 strokes (curl, sweep), derived from the glyph; no matra
ঔ                  done — 3 strokes (curl, sweep, kar), derived from its own glyph,
                   not shared with ও
11 vowels          all done — no vowel carries placeholder geometry
ক                  done — 4 strokes (knot, stem, lobe, matra), derived from the glyph; the first
                   letter wide enough that its ink is fitted by width and centred vertically
                   rather than fitted by height
4 consonants       placeholder geometry    → Steps 11.12–11.15
5 drawings         unverified              → Steps 11.16–11.17
```

Because every stroke's guide is shown at once, a letter needs no per-letter engine work when its real multi-stroke geometry lands.

Anything still showing placeholder geometry at this step is a release blocker.

This is critical.

A technically correct tracing engine can still teach incorrect handwriting if the exercise stroke data is incorrect.

---

# 50. Step 24 — Analytics

Only add analytics after the learning experience is stable.

Potential events:

```text
exercise_started
exercise_completed
practice_repeated
score_received
exercise_mastered
drawing_completed
```

Do not collect unnecessary child-identifying information.

Analytics should focus on product interaction, not personal data.

---

# 51. Step 25 — Release Quality

Before release:

- [ ] Build debug
- [ ] Build release
- [ ] Run unit tests
- [ ] Run UI tests
- [ ] Run lint
- [ ] Verify R8
- [ ] Verify Room migrations
- [ ] Verify process recreation
- [ ] Verify app restart persistence
- [ ] Test multiple Android versions
- [ ] Test multiple screen sizes
- [ ] Test real physical devices
- [ ] Test touch responsiveness
- [ ] Verify Bengali rendering
- [ ] Verify accessibility
- [ ] Verify no main-thread blocking work
- [ ] Verify crash handling
- [ ] Verify all exercises

---

# 52. MVP Scope

Do not build the entire product before validating the core learning experience.

## MVP Screens

- Tips
- Home
- Vowels
- Practice
- Result
- Progress

## MVP Content

- 5–6 Bengali vowels
- 3–5 simple drawings

## MVP Technical Features

- Compose
- MVI
- Hilt
- Room
- Navigation
- Tracing engine
- Score calculation
- Progress persistence

## MVP Learning Loop

```text
Open App
   ↓
Tips
   ↓
Home
   ↓
Vowels
   ↓
অ
   ↓
Trace
   ↓
Score
   ↓
Save Result
   ↓
Progress
```

Only after this loop feels good should the app expand to all vowels, consonants, and larger drawing collections.

---

# 53. Feature Completion Definition

A feature is complete only when it satisfies:

- The architecture, code-quality, testing, and build/verification rules in `CLAUDE.md`.
- This plan's product-specific criteria:
  - [ ] Child-friendly UX
  - [ ] Accessibility considered
  - [ ] Its step's Definition of Done (where specified)

---

# 54. Git / Commit Strategy

Follow the git rules in `CLAUDE.md` (Section 11). Suggested commit messages per phase:

```text
feat: bootstrap android project
feat: add app design system
feat: add navigation foundation
feat: add tips screen
feat: add home screen
feat: add exercise content model
feat: add vowel exercise screen
feat: add tracing domain models
feat: add dotted path renderer
feat: add tracing pointer handling
feat: add path coverage calculation
feat: add tracing score calculator
feat: add practice screen
feat: add result screen
feat: add progress tracking
feat: add drawing exercises
```

---

# 55. Final Architecture

```text
                         ┌───────────────────────┐
                         │      Compose UI       │
                         └───────────┬───────────┘
                                     │
                                  Events
                                     ↓
                         ┌───────────────────────┐
                         │      ViewModel        │
                         │        (MVI)          │
                         └───────────┬───────────┘
                                     │
                          ┌──────────┴──────────┐
                          ↓                     ↓
                 ┌─────────────────┐   ┌──────────────────┐
                 │ Domain / UseCase │   │ Tracing Engine   │
                 └────────┬────────┘   └────────┬─────────┘
                          │                     │
                          ↓                     ↓
                 ┌────────────────────────────────────┐
                 │             Repository             │
                 └──────────────────┬─────────────────┘
                                    ↓
                           ┌─────────────────┐
                           │     Room DB     │
                           └─────────────────┘
```

## Learning System

```text
Static Exercise Content
        │
        ├── স্বরবর্ণ
        ├── ব্যঞ্জনবর্ণ
        └── Drawing
                ↓
        Tracing Engine
                ↓
        Trace Result
                ↓
        Score Calculator
                ↓
        Practice Result
                ↓
             Room
                ↓
           Progress
                ↓
            Mastery
```

---

# 56. Final Product Flow

The finished application should feel simple to a child:

```text
              Open App
                  ↓
                Tips
                  ↓
                Home
                  ↓
       ┌──────────┼──────────┐
       ↓          ↓          ↓
   স্বরবর্ণ    ব্যঞ্জনবর্ণ    আঁকা
       │          │          │
       └──────────┼──────────┘
                  ↓
             Select Item
                  ↓
              Practice
                  ↓
           Follow the Dots
                  ↓
             Get Score
                  ↓
        ┌─────────┴─────────┐
        ↓                   ↓
   Practice Again         Next
        │                   │
        └─────────┬─────────┘
                  ↓
              Progress
                  ↓
              Mastery
```

The engineering goal is equally clear:

```text
Compose
   ↓
MVI ViewModel
   ↓
Domain
   ↓
Repository
   ↓
Room
```

The tracing engine remains an independent subsystem that can be reused for:

```text
অ
আ
ক
খ
...
Circle
House
Flower
Animal
```

---

# 57. Most Important Principle

The application should not be considered successful merely because:

```text
All screens exist
+
Code compiles
+
Room works
```

The most important milestone is:

> **A child can comfortably trace a Bengali letter with their finger, understand the guidance, receive meaningful feedback, and want to practice again.**

Therefore:

1. Build the smallest complete learning loop.
2. Validate the tracing experience.
3. Refine scoring.
4. Persist progress.
5. Only then expand the content.

This keeps the project technically maintainable while ensuring that the actual educational interaction remains the primary focus.
