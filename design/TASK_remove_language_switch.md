# Task: Remove the language switch — BornoChitra is Bangla-only

## Context for Claude Code

BornoChitra (বর্ণচিত্র) is a Jetpack Compose + Material 3 Android app, already built from `DESIGN_SPEC.md` (v1).
The design has changed: **the app UI is now Bangla only.** Bangla is the default and the only UI language. Remove every language-change control, screen, state and resource described below.

**Do NOT change anything else.** The English *learning content* stays: the English hub, small/capital letters, English numbers, Fill-the-blanks (English) and Listen-and-learn (English, "A for apple") are all still part of the app. Only the *UI language* is fixed to Bangla.

Class and file names below come from the design spec and the original project overview. Your project may use different names, so search for them (e.g. `Language`, `Locale`, `LanguageSwitch`, `AppCompatDelegate.setApplicationLocales`, `LocaleListCompat`, `bc_ic_globe`, `HorizontalPager` in Welcome) and confirm before deleting.

---

## 1. Behavior changes

1. The app always shows Bangla UI (`bn`), on every launch, on every Android version.
2. Stop reading and writing any saved language preference. A user who had chosen **English** before this update must see **Bangla** after it: clear or override the old saved choice (including any per-app locale set through `AppCompatDelegate.setApplicationLocales`) so it doesn't stick.
3. Nothing in the UI can change the language any more.
4. Text-to-speech in Listen-and-learn keeps using the **content's** language (Bangla letters → Bangla voice, English letters → English voice), not the UI language. Make sure it doesn't break.
5. No other logic changes (scoring, mastery, progress, Fill-the-blanks, navigation rules).

---

## 2. Screen changes

### 2.1 DELETE — Onboarding page 1 "Language"
Remove the whole page: logo 104dp, "অ্যাপটা কোন ভাষায় দেখবে?", "Which language should the app use?", the two language choice cards (বাংলা / English), the caption "পরে “বড়দের জন্য” থেকে বদলানো যাবে।", the page indicator and its "এগিয়ে চলো" button.

### 2.2 CHANGE — Welcome (was Onboarding page 2) becomes a single screen
- Remove the `HorizontalPager` and the page indicator (dots).
- Layout top to bottom: padding **32dp top** (was 40) / 20dp sides / 24dp bottom, vertical gap 16dp.
  1. **New header row** (gap 14dp, vertically centered): app logo **64dp** + text "স্বাগতম!" (`displaySmall`: Baloo Da 2, 40sp / 50sp line height, weight 800).
  2. "বর্ণচিত্র কী?" (`headlineMedium`) + intro text (`bodyMedium`), gap 6dp. Unchanged.
  3. The 4 feature cards, gap 10dp. Unchanged.
  4. Bottom: full-width Primary button "শুরু করো →". Unchanged behavior: mark Welcome as seen (`OnboardingStore.hasSeenWelcome`), open Home, remove Welcome from the back stack.
- Remove the language item from the Welcome state/ViewModel.

### 2.3 CHANGE — Home top bar
- Delete the **Language chip** (globe icon + "বাংলা", content description "ভাষা বদলাও").
- The top bar is now: start padding 16dp, gap 10dp, logo circle 36dp + "বর্ণচিত্র" (`titleLarge`, primary color). The right side stays empty.
- Remove the Language-sheet show/hide state and event from `HomeUiState` / `HomeViewModel`.

### 2.4 DELETE — Language bottom sheet
Delete the `ModalBottomSheet` that opened from the Home chip: globe header, "অ্যাপের ভাষা", "App language", 2 choice cards, caption "বেছে নিলেই বদলে যাবে। অগ্রগতি মুছবে না।".

### 2.5 CHANGE — Grown-ups tab (বড়দের জন্য)
- Delete the first section: label "অ্যাপের ভাষা · App language" + the segmented switch (বাংলা | English).
- Remaining content (padding 4dp top / 20dp sides / 20dp bottom, gap 22dp):
  1. "অগ্রগতি কীভাবে গোনা হয়" card. Unchanged.
  2. "পরিচিতি" / About section. Unchanged.
- Tab name, icon and position in the bottom navigation bar are unchanged.
- Remove the language item from this screen's state/ViewModel.

### 2.6 Must stay exactly as they are
Splash, Bangla hub, English hub, category grids, Drawing grid, Practice, Restart dialog, Result (great and low), Fill-the-blanks picker / model dialog / sequence / hint sheet / summary, Listen and learn, Progress tab, Progress detail, bottom navigation bar.

---

## 3. Navigation changes

| Flow | Before | After |
|---|---|---|
| First launch | Splash → Onboarding (Language) → Onboarding (About) → Home | **Splash → Welcome → Home** |
| Later launches | Splash → Home | unchanged |
| Home → Language chip | opens Language sheet | **removed** (delete route / state) |
| Grown-ups → language switch | changes language | **removed** |

```
App start ─► System splash ─► BrandSplash
   first launch? ─ yes ─► Welcome ──শুরু করো──► Home   (Welcome removed from back stack)
                └ no ─────────────────────────► Home
```
The bottom bar (শিখি · অগ্রগতি · বড়দের জন্য) and every other route are unchanged.

---

## 4. Delete these components (if nothing else uses them)

| Component | Was used on | Action |
|---|---|---|
| Language chip (40dp tall, globe 20dp + label, `surfaceContainer`) | Home top bar | delete |
| `BcLanguageSwitch` (segmented: track + 48dp segments, animated thumb) | Grown-ups tab, onboarding | delete |
| Choice card, **row variant** (lead tile 44–56dp + label + radio) | Onboarding language page, Language sheet | delete this variant only |
| Page indicator (8dp dots, 24dp active) | Onboarding | delete |

**Keep:**
- `BcChoiceButton` **column variant** (Easy / Hard in Fill the blanks)
- the bottom-sheet component (the Hint sheet uses it)
- all other `core/ui` components

---

## 5. Resources

### Colors
**Nothing removed.** Keep every `ColorScheme` role and every extended color. `englishContainer` / `onEnglishContainer` are still used by the English subject card, English row leads, the Info banner and the No-internet dialog state.

### Typography
**Nothing removed.** Keep **Andika**: it's still used for English letters (tiles, tracing, English hub). It's just no longer used for the word "English" as a language label.

### Icons
| File | Action |
|---|---|
| `res/drawable/bc_ic_globe.xml` | **delete** (only used by the Language chip and the Language sheet) |
| the other 24 `bc_ic_*` icons | keep |

### Strings to delete (check each one isn't used anywhere else first)
| String | Was used on |
|---|---|
| অ্যাপটা কোন ভাষায় দেখবে? | Onboarding language page |
| Which language should the app use? | Onboarding language page |
| পরে “বড়দের জন্য” থেকে বদলানো যাবে। | Onboarding language page |
| এগিয়ে চলো (**only** the onboarding page-1 use) | Onboarding language page |
| অ্যাপের ভাষা / App language | Language sheet, Grown-ups tab |
| বেছে নিলেই বদলে যাবে। অগ্রগতি মুছবে না। | Language sheet |
| ভাষা বদলাও (content description) | Home language chip |
| বাংলা / English **as language-option labels** | switch, chip, sheet, onboarding |

**Keep** "বাংলা" as the title of the Bangla subject card and the Bangla hub, and "ইংরেজি" for the English hub.
If the English UI translations (`values-en` / default English `values/strings.xml`) are only there for UI localization, ask the user before deleting them. Make sure Bangla strings are what the app shows by default.
No new strings are added.

---

## 6. Acceptance checklist

- [ ] Fresh install: Splash → Welcome (one page, logo + স্বাগতম! at the top, no dots) → শুরু করো → Home. Back from Home exits the app.
- [ ] Second launch goes straight to Home.
- [ ] Home top bar shows only the logo + বর্ণচিত্র. No chip, no sheet.
- [ ] Grown-ups tab shows only "how progress is counted" + About.
- [ ] No screen offers a language change. Searching the UI code for the language switch/chip finds nothing.
- [ ] Updating from a build where English was selected shows Bangla UI.
- [ ] English hub, English letter practice, English Fill-the-blanks and English Listen-and-learn (TTS) still work.
- [ ] `bc_ic_globe.xml`, the language chip, `BcLanguageSwitch`, the row choice-card variant and the page indicator are gone (or confirmed still used elsewhere).
- [ ] The project builds with no unused-resource or unused-import warnings from the removed items. Existing tests pass, and any tests that covered language switching are removed or updated.
- [ ] Light and dark mode both look right on the changed screens.
