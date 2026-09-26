# New strings — UI redesign (Step 15)

Every string resource the redesign added or reworded, in Bangla (`values/strings.xml`) and English
(`values-en/strings.xml`), compared with `master`. `%1$s`, `%1$d` and `%%` are filled in by the app. A plural
shows each English form; Bangla uses one form for every count. Rows marked *same in both* are
`translatable="false"`: marks, glyphs and the bilingual lines of decision D3, shown the same in both languages.

## Added

| Resource | Bangla | English |
|---|---|---|
| `logo_letter` | অ | *(same in both — not translated)* |
| `splash_tagline` | লেখো, শেখো, বেড়ে ওঠো | Write, learn, grow |
| `title_grown_ups` | বড়দের জন্য | For grown-ups |
| `nav_learn` | শিখি | Learn |
| `tile_attempts` | %d বার | one: %d try / other: %d tries |
| `tile_continue_here` | এখান থেকে | Start here |
| `header_learned` | %dটি শিখেছ | %d learned |
| `header_done` | %dটি শেষ | %d done |
| `header_running` | %dটি চলছে | %d in progress |
| `language_english_mark` | A | *(same in both — not translated)* |
| `home_question` | আজ কী শিখবে? | What will you learn today? |
| `home_mark_bangla` | অ | *(same in both — not translated)* |
| `home_mark_english` | Aa | *(same in both — not translated)* |
| `home_bangla_caption` | বর্ণ, সংখ্যা, খেলা | Letters, numbers, games |
| `home_english_caption` | ছোট ও বড় হাতের অক্ষর | Small and capital letters |
| `home_drawing_caption` | রেখা, গোল, বাড়ি | Lines, circles, houses |
| `home_continue_meta` | %1$s · | %1$s · |
| `home_mastery_remaining` | আর %1$d বার লিখলে “%2$s” শেখা হয়ে যাবে | one: Write it %1$d more time to learn “%2$s” / other: Write it %1$d more times to learn “%2$s” |
| `home_mastery_score` | একবার %1$d%% পেলে “%2$s” শেখা হয়ে যাবে | Score %1$d%% once to learn “%2$s” |
| `hub_learn_by_writing` | লিখে শেখো | Learn by writing |
| `hub_learn_by_playing` | খেলে শেখো | Learn by playing |
| `hub_math_shared` | “%1$s” বাংলাতেও আছে — অগ্রগতি একটাই। | “%1$s” is in Bangla too — the progress is shared. |
| `model_offer_title` | একবার নামিয়ে নাও | Download it once |
| `model_internet_note` | ইন্টারনেট লাগবে। বড়দের জিজ্ঞেস করো। | This needs the internet. Ask a grown-up. |
| `model_checking_title` | একটু দাঁড়াও | One moment |
| `model_no_internet_title` | ইন্টারনেট নেই | No internet |
| `model_downloading_title` | নামানো হচ্ছে | Downloading |
| `model_waiting_for_internet_title` | ইন্টারনেটের অপেক্ষা | Waiting for the internet |
| `model_failed_title` | নামানো যায়নি | Download failed |
| `onboarding_language_question` | অ্যাপটা কোন ভাষায় দেখবে? | *(same in both — not translated)* |
| `onboarding_language_question_english` | Which language should the app use? | *(same in both — not translated)* |
| `onboarding_change_later` | পরে “বড়দের জন্য” থেকে বদলানো যাবে। | You can change this later in “For grown-ups”. |
| `onboarding_start` | শুরু করো | Let's start |
| `about_mark_bangla` | অ | *(same in both — not translated)* |
| `about_mark_latin` | a | *(same in both — not translated)* |
| `about_mark_numbers` | ১+২ | *(same in both — not translated)* |
| `language_title` | অ্যাপের ভাষা | *(same in both — not translated)* |
| `language_title_english` | App language | *(same in both — not translated)* |
| `grown_ups_language_label` | অ্যাপের ভাষা · App language | *(same in both — not translated)* |
| `language_chip_description` | অ্যাপের ভাষা: %1$s | App language: %1$s |
| `language_sheet_note` | বেছে নিলেই বদলে যাবে। অগ্রগতি মুছবে না। | It changes as soon as you pick. Your progress stays. |
| `grown_ups_counting` | অগ্রগতি কীভাবে গোনা হয় | How progress is counted |
| `grown_ups_stars` | প্রতিটা অক্ষরের তারা আসে সবচেয়ে ভালো লেখা থেকে। | Each letter's stars come from its best writing. |
| `mastery_rule` | %1$d বার লিখলে আর অন্তত একবার %2$d%% পেলে অক্ষরটা শেখা হয়ে যায়। | A letter is learned after it is written %1$d times with at least one score of %2$d%%. |
| `grown_ups_on_phone` | সব অগ্রগতি এই ফোনেই থাকে। কোনো অ্যাকাউন্ট লাগে না। | All progress stays on this phone. No account is needed. |
| `list_number` | %1$d | %1$d |
| `practice_category_position` | %1$s · %2$d/%3$d | %1$s · %2$d/%3$d |
| `restart_title` | আবার শুরু করবে? | Start again? |
| `restart_message` | যা লিখেছ, সব মুছে যাবে। | Everything you wrote will be wiped. |
| `restart_confirm` | হ্যাঁ, মুছে দাও | Yes, wipe it |
| `restart_dismiss` | না, লিখতে থাকি | No, keep writing |
| `result_score_caption` | এবারের স্কোর | This try's score |
| `fill_blanks_easy_caption` | ফাঁকা ঘর দূরে দূরে | Blanks are far apart |
| `fill_blanks_hard_caption` | পাশাপাশি ফাঁকা ঘরও থাকে | Blanks can be side by side |
| `fill_blanks_hint_title` | সাহায্য লাগবে? | Need a hint? |
| `fill_blanks_hint_message` | ফোঁটা দেখালে লেখা সহজ হবে। তবে এই ঘরে সবচেয়ে বেশি %1$d%% পাবে। | The dots make it easier to write. But this box can then get at most %1$d%%. |
| `fill_blanks_hint_confirm` | ফোঁটা দেখাও | Show the dots |
| `fill_blanks_hint_dismiss` | না, নিজে লিখি | No, I'll write it myself |
| `learn_section_english` | A–Z | *(same in both — not translated)* |

## Reworded

The Bangla wording follows `design/DESIGN_SPEC.md`; the English follows it in meaning.

| Resource | Bangla (before → now) | English (before → now) |
|---|---|---|
| `drawing_circle` | বৃত্ত → গোল | Circle → Circle |
| `drawing_square` | বর্গ → চারকোনা | Square → Square |
| `drawing_triangle` | ত্রিভুজ → তিনকোনা | Triangle → Triangle |
| `status_mastered` | আয়ত্ত হয়েছে → শিখে ফেলেছ | Mastered → Learned |
| `model_offer` | শূন্যস্থান পূরণ তোমার হাতের লেখা পড়ে। এর জন্য একবার প্রায় ৫০ এমবি নামাতে হবে, নামানো শেষ হলেই এটি খুলবে। → শূন্যস্থান পূরণ খেলতে হাতের লেখা চেনার ফাইল লাগবে। একবার নামালে পরে ইন্টারনেট ছাড়াই চলবে। | Fill the Blanks reads your handwriting. It needs a one-time download of about 50 MB, and opens once the download is done. → Fill the Blanks needs a file to read handwriting. Download it once and it works without the internet after that. |
| `model_downloading` | নামানো হচ্ছে। এক মিনিটের মতো লাগতে পারে। → এক মিনিটের মতো লাগতে পারে। এটা লুকিয়ে রাখলেও নামানো চলবে। | Downloading. This can take a minute. → This can take a minute. It keeps going if you hide this. |
| `model_not_now` | এখন না → পরে | Not now → Later |
| `result_perfect_headline` | দারুণ! 🎉 → দারুণ! | Great Job! 🎉 → Great Job! |
| `result_low_headline` | চলো আবার অনুশীলন করি! → ভালো চেষ্টা! | Let's Practice Again! → Good Try! |
| `result_low_message` | ধীরে ফোঁটাগুলো ধরে চলো। তুমি পারবে! → আরেকবার লিখলে আরও তারা পাবে। | Follow the dots slowly. You can do it! → Write it once more to earn more stars. |
| `result_next` | পরেরটি → পরেরটি: %1$s | Next → Next: %1$s |
| `fill_blanks_not_recognized` | ঠিক হয়নি। আবার শুরু করো। → প্রায় হয়েছে! “আবার শুরু” চেপে আবার লেখো। | Not quite. Tap Reset and try again. → Almost! Tap “Reset” and write it again. |
| `learn_no_voice_message` | এই ফোনে %1$s বলার কণ্ঠ নেই, তাই কিছু শোনা যাবে না। ফোনের সেটিংসে লেখা থেকে কথা (টেক্সট-টু-স্পিচ) খুলে %1$s কণ্ঠ নামিয়ে নাও। → এই ফোনে %1$s কথা বলার ভয়েস নেই। ফোনের সেটিংস থেকে ইনস্টল করতে বড়দের বলো। | This phone has no %1$s voice, so nothing can be heard. Open text-to-speech in the phone's settings and download the %1$s voice. → This phone has no %1$s voice. Ask a grown-up to install one in the phone's settings. |

## Removed

These belonged to screens or wording the redesign replaced (the drawer, About, Welcome, the one-line splash
message, the old status words and result wording):
`splash_message`, `title_home`, `action_menu`, `drawer_progress`, `drawer_about`, `status_attempts`, `status_started`, `status_practicing`, `model_dialog_title`, `result_try_again`, `learn_no_voice_title`
