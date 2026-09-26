# BornoChitra icons (25)

24×24 grid, 2dp round stroke (matches the design spec). Colour is black in the files; tint it at use (Compose `Icon` tints automatically).

- `drawable/` — Android Vector Drawables. Copy into `app/src/main/res/drawable/`, use `painterResource(R.drawable.bc_ic_globe)`.
- `svg/` — same icons as SVG (`currentColor`), for Figma or Android Studio's Vector Asset import.
- `preview.png` — contact sheet.

Filled icons: `play`, `star_filled` (and the dot in `wifi`/`wifi_off`). `star_empty` is the 1.6dp outline used for empty Result stars.
Sizes from the spec: 24 default, 22 in buttons, 20 in chips, 16 in small chips / badge, 14 corner speaker.

| File | Used for |
|---|---|
| `bc_ic_pencil` | Learn tab (শিখি) |
| `bc_ic_people` | Grown-ups tab (বড়দের জন্য) |
| `bc_ic_globe` | Language chip, Language sheet |
| `bc_ic_chevron_right` | Activity rows |
| `bc_ic_chevron_left` | RTL mirror / spare |
| `bc_ic_arrow_back` | Top bar back (stroke style match) |
| `bc_ic_arrow_forward` | Next / Start / Next sequence buttons |
| `bc_ic_reset` | আবার শুরু, Try again, Restart dialog |
| `bc_ic_bulb` | Help / Hint, Tip banner |
| `bc_ic_download` | Model dialog |
| `bc_ic_wifi` | Needs-internet banner |
| `bc_ic_wifi_off` | Model dialog: no internet / waiting |
| `bc_ic_phone` | Grown-ups: progress stays on this phone |
| `bc_ic_house` | Onboarding feature card (drawing) |
| `bc_ic_speaker` | Listen game card, example-word button |
| `bc_ic_speaker_small` | Corner mark on Listen letter button (14dp) |
| `bc_ic_play` | Continue card play button |
| `bc_ic_check` | Success banner |
| `bc_ic_check_bold` | Mastered badge, learned chip (16dp) |
| `bc_ic_star_outline` | Progress tab icon |
| `bc_ic_star_filled` | Result / summary stars (filled) |
| `bc_ic_star_empty` | Result / summary stars (empty, outlineVariant) |
| `bc_ic_info` | Info banner |
| `bc_ic_alert` | Error banner |
| `bc_ic_shapes` | Drawing subject glyph / row lead |
