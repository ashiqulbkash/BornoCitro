# Barnacitro — Claude Code Guidelines

## 1. General Rules

* Read and understand existing code before making changes.
* Follow the existing project structure, architecture, and conventions.
* Prefer simple, maintainable, and readable solutions.
* Reuse existing components, utilities, and patterns before creating new ones.
* Do not introduce unnecessary abstractions, dependencies, or files.
* Do not modify unrelated code.
* Preserve existing behavior unless the requirement explicitly changes it.
* Do not make architectural changes without a clear reason.
* Keep changes focused and incremental.
* Never assume an implementation is correct without inspecting the relevant code.

## 2. Architecture

* Follow a clean, modular, and maintainable architecture.

* Use **MVI (Model–View–Intent)** for feature-level state management.

* Maintain unidirectional data flow:

  `UI → Intent/Event → ViewModel → State → UI`

* Keep business logic out of Composables.

* ViewModels coordinate UI events and business logic.

* Repositories handle data access and data-source coordination.

* Keep data, domain, and presentation responsibilities separated where applicable.

* Do not allow UI components to directly access repositories, databases, or APIs.

* Keep feature-specific logic inside the appropriate feature/module.

* Prefer dependency inversion and interfaces where they provide real value.

* Avoid creating abstractions only for the sake of abstraction.

## 3. MVI Guidelines

* Represent screen state with an immutable `UiState`.
* Represent user actions with explicit `Intent`, `Event`, or equivalent types.
* ViewModels are responsible for processing intents/events and producing state.
* UI should render state and send user actions back to the ViewModel.
* Avoid mutable shared state.
* Avoid modifying state directly from Composables.
* Keep state transitions predictable and easy to test.
* Handle loading, success, empty, and error states explicitly where required.
* Avoid generic `else` branches when different states have different meanings.
* One source of truth should own each piece of UI state.

## 4. Jetpack Compose

* Follow modern Jetpack Compose best practices.
* Prefer small, focused, reusable Composables.
* Keep Composables as stateless as reasonably possible.
* Hoist state to the appropriate owner.
* Prefer unidirectional data flow.
* Do not put business logic inside Composables.
* Avoid unnecessary `remember`, `LaunchedEffect`, and `derivedStateOf`.
* Do not perform expensive work during composition.
* Use stable and meaningful keys for lazy lists.
* Use `remember` only when state actually needs to survive recomposition.
* Use `rememberSaveable` when state needs to survive configuration/process recreation where appropriate.
* Avoid unnecessary recompositions by keeping parameters stable and state localized.
* Do not pass a ViewModel deep into the Composable hierarchy.
* Prefer passing state and callbacks to child Composables.
* Follow the existing project design system and reusable UI components.
* Reuse existing components before creating new ones.
* Do not directly introduce Material components when an existing project-specific component should be used.
* Keep previews useful and representative where the project uses previews.
* Keep UI code focused on rendering and user interaction.

## 5. State and Coroutines

* Keep UI state immutable.
* Prefer `StateFlow` or the project's established reactive state mechanism.
* Use lifecycle-aware collection in Compose.
* Keep coroutine scopes tied to the appropriate lifecycle.
* Never use `runBlocking` for normal application flow.
* Never perform blocking or expensive work on the main thread.
* Move database, file, network, serialization, and other expensive operations to appropriate background dispatchers.
* Avoid unnecessary coroutine launches.
* Handle coroutine cancellation correctly.
* Do not swallow exceptions without a clear reason.

## 6. Repository and Data Layer

* Keep data-access responsibilities inside repositories/data sources.
* Do not expose database or API implementation details to the UI.
* Follow the existing API and database patterns.
* Keep DTOs, entities, and UI models separate when the architecture requires it.
* Use mapping functions between layers when appropriate.
* Handle remote and local data states explicitly.
* Keep transactions around operations that must be atomic.
* Avoid unnecessary database queries and repeated data transformations.

## 7. Code Quality

* Write clear, readable, idiomatic Kotlin.
* Prefer meaningful names over comments.
* Keep functions and classes focused on a single responsibility.
* Prefer immutability.
* Avoid deeply nested code.
* Prefer early returns when they improve readability.
* Avoid unnecessary scope functions and clever Kotlin tricks.
* Avoid duplicate logic.
* Do not add defensive code for impossible states without a reason.
* Do not add comments that simply explain what the code already says.
* Add comments only when they explain non-obvious reasoning or constraints.
* Follow existing formatting and naming conventions.
* Do not reformat unrelated code.
* Do not use deprecated APIs when a supported alternative exists.

## 8. Dependency Injection

* Follow the existing dependency-injection structure.
* Reuse existing dependencies and providers.
* Do not introduce a new DI pattern when an existing one is sufficient.
* Avoid unnecessary dependencies in constructors.
* Do not perform expensive or blocking work during dependency creation.
* Use appropriate dependency scopes.
* Do not add a dependency solely to solve a simple problem that can be handled with existing tools.

## 10. Testing

* Add or update tests when behavior changes.
* Prefer testing behavior rather than implementation details.
* Unit test ViewModels, business logic, repositories, and state transitions where appropriate.
* Test important success, empty, loading, and error scenarios.
* Test edge cases that can realistically occur.
* Keep tests deterministic and independent.
* Do not modify or remove tests merely to make an implementation pass.
* Do not mock unnecessarily when a simpler test approach is available.
* Follow the existing project's testing framework and patterns.

## 11. Build and Verification

After implementing a change:

1. Review the modified code.
2. Run relevant unit tests.
3. Run relevant module/application tests.
4. Build the affected module or application.
5. Fix compilation, test, or lint issues caused by the changes.
6. Verify that the implementation satisfies the requested behavior.

Do not claim that a change works unless it has been verified or can be directly established from the code.

If verification cannot be performed, clearly state what could not be verified.

Running the app on an emulator/device is not required to consider a step, action, or feature complete. Verification is based on code review, unit tests, and builds as described above.

## 12. Git and Existing Changes

* Do run any git commit
* After implementing a step, action, or feature, add any newly created files to git (`git add`) so they are tracked.
* Running unit tests affecting the changed file(s) is mandatory after implementing a step, action, or feature; do not skip this before considering the change complete.

## 13. Project Plan

* `plan.md` contains the requirements and execution plan for the current task/project.
* Read `plan.md` before implementing a planned task.
* Follow `plan.md` for **what** needs to be implemented.
* Follow this `CLAUDE.md` for **how** the project should be developed and maintained.
* Do not permanently move task-specific requirements from `plan.md` into `CLAUDE.md`.
* If the plan conflicts with the existing architecture, inspect the codebase and resolve the conflict before making major architectural changes.
* Do not implement features that are not required by the plan unless explicitly requested.

## 14. Implementation Approach

For every non-trivial task:

1. Understand the requirement.
2. Read `CLAUDE.md` and the relevant part of `plan.md`.
3. Inspect the existing implementation.
4. Search for similar patterns already used in the project.
5. Decide on the smallest appropriate change.
6. Implement incrementally.
7. Test and verify the changes.
8. Review the final implementation for unnecessary complexity.

## Most Important Principles

1. Follow MVI and unidirectional data flow.
2. Keep Composables focused on UI.
3. Keep business logic out of the UI layer.
4. Prefer existing project patterns over new abstractions.
5. Keep state immutable and predictable.
6. Keep expensive work off the main thread.
7. Write testable code.
8. Make the smallest change that correctly solves the requirement.
9. Do not over-engineer.
10. Keep the codebase simple, consistent, and maintainable.

