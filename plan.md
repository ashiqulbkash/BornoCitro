# Barnacitro — Implementation Plan

> **Purpose:** This document lists the requirements and execution order for the current task. It defines **what** to build. `CLAUDE.md` defines **how**.
>
> **Agent usage:** Execute incrementally, one numbered step at a time. Do not implement the whole plan in one pass.

---

# 0. Agent Execution Instructions

General development conventions — architecture, code quality, testing, build/verification, git, and task-reporting requirements — are defined in `CLAUDE.md` and apply to every step below. This section adds only the rules specific to executing this plan.

## PLAN EXECUTION RULES

1. Read the entire `plan.md` before making changes.
2. Implement exactly **one numbered step at a time**. Do not implement multiple steps unless explicitly instructed.
3. Do not move to the next step automatically unless explicitly instructed.
4. Do not use placeholder implementations for a real feature.
5. Keep the tracing engine independent from ViewModel, Room, Hilt, and Compose UI implementation details.
6. Before changing behavior, inspect the existing code and reuse the existing patterns (progress calculation, Home screen category buttons, etc.).

## STEP COMPLETION

A step is complete only when its Definition of Done is satisfied.

Do not mark a step complete merely because the application compiles.

After completing a step, **STOP and wait for the next instruction.**

---

# 1. Context

Practice shows the complete letter/drawing as a final dotted shape from the first touch. The child traces over it. Ink already written stays on screen. Tracing progress is calculated by the existing tracing engine and progress calculation logic.

The four steps below refine this experience and extend the Progress screen.

---

# 2. Step 1 — Reset Drawing

## Requirement

If the user makes a mistake while tracing over the dots, tapping the **Reset** button must clear the previously drawn path.

The tracing progress shown while drawing must also be reset.

## Implement

- Reset clears all ink drawn so far in the current attempt.
- Reset returns the tracing progress display to its initial (0) state.
- Reset goes through the existing MVI flow: `UI → Event → ViewModel → State → UI`.
- Reset must not persist anything to Room.

## Definition of Done

- [ ] Tapping Reset removes every drawn path from the canvas
- [ ] The progress shown while drawing returns to its initial state
- [ ] After Reset the user can trace again from the beginning
- [ ] Unit test covers the reset state transition
- [ ] Verified by running the app and using Reset mid-trace

---

# 3. Step 2 — Resume and Completion Behavior

## Requirement

1. **Resume.** The user can stop tracing at any point and resume from where they left off.
   Currently, stopping midway through a step forces the user to repeat that step from the beginning. This behavior must change.

2. **Completion.** The entire letter/drawing is displayed as a complete final shape. Tracing is considered complete once the user has traced the entire letter/drawing, **regardless of how they trace it** (any stroke order, any stroke split).

3. **Outside-the-path tracing.** Tracing outside the dotted path continues to be calculated using the existing progress calculation logic. That logic is not changed.

## Implement

- Keep partial progress when the user lifts their finger mid-step; a new touch continues from the existing progress instead of restarting the step.
- Determine completion from total coverage of the whole shape, not from an ordered stroke-by-stroke sequence.
- Keep the existing score/progress calculation for off-path points unchanged.
- Keep the tracing engine free of ViewModel, Room, Hilt and Compose dependencies.

## Definition of Done

- [ ] Lifting the finger mid-step and touching again continues from the previous progress
- [ ] No step has to be repeated from the beginning after an interruption
- [ ] Tracing the whole shape completes the exercise in any order
- [ ] Off-path tracing is still scored by the existing progress calculation
- [ ] Existing scoring tests still pass unmodified
- [ ] New unit tests cover resume, any-order completion and off-path tracing
- [ ] Verified by running the app: stop midway, resume, and finish

---

# 4. Step 3 — Tracing Instruction

## Requirement

No blinking dots. Show a simple instruction message instead, such as **“Practice tracing over the dots”**.

## Implement

- Remove the blinking-dot animation from Practice.
- Show the instruction message on the Practice screen.
- The message is a string resource, not hardcoded in the Composable.

## Definition of Done

- [ ] No dot blinks on the Practice screen
- [ ] “Practice tracing over the dots” is displayed
- [ ] The text comes from a string resource
- [ ] Any test or code that depended on the blinking behavior is updated
- [ ] Verified by running the app

---

# 5. Step 4 — Progress Screen

## Requirement

Add **Vowel** and **Consonant** buttons to the Progress screen, following the same UI pattern as the Home screen.

When the user enters either section, show the individual progress for the respective vowels or consonants.

## Implement

- Reuse the Home screen's category button component and layout pattern.
- Vowel button opens the per-vowel progress list.
- Consonant button opens the per-consonant progress list.
- Each list shows individual progress per exercise, sourced from the existing `ProgressRepository`.
- Handle loading, empty and error states explicitly.
- Follow MVI: `ProgressScreen → ProgressViewModel → ProgressRepository`.
- Add routes only if the existing navigation pattern requires them.

## Definition of Done

- [ ] Progress screen shows Vowel and Consonant buttons styled like Home
- [ ] Vowel section lists each vowel with its individual progress
- [ ] Consonant section lists each consonant with its individual progress
- [ ] Loading, empty and error states are handled
- [ ] Unit tests cover the ViewModel state transitions and progress mapping
- [ ] Verified by running the app

---

# 6. Feature Completion Definition

A step is complete only when it satisfies:

- The architecture, code-quality, testing, and build/verification rules in `CLAUDE.md`.
- Its Definition of Done above.
- The pre-task and post-task briefs required by `CLAUDE.md` Section 14.
