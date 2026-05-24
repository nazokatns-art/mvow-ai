# M·VoW — DESIGN

> Visual language. Maps to MentorColors.kt (Android), MentorColors.swift (iOS), and CSS custom properties in docs/v2/preview/*.html.

## Theme

**Dark only — deliberate, not default.** Reasoning:

> "Uzbek user, late evening or early morning, dim room or pre-dawn light, contemplative mood about how the day went or how tomorrow will go. The screen needs to feel like a quiet sanctuary, not a fluorescent productivity tool."

Sentence forces dark. Confirmed.

## Color strategy

**Committed dark with gold accent carrying 30-40% of expressive surface area.**

The base is near-black void. Gold is the mentor's visible voice — used for hero text, primary actions, mentor speech accents, and active states. Emerald, crimson, sky, rose are scoped signals — each tied to one emotional role.

Never `#000` or `#fff`. All neutrals tinted slightly toward the brand hue (warm cream on the highlight side, deep navy on the dark side).

## Palette (OKLCH-tinted, hex shown for legacy)

### Surfaces — deep navy/charcoal void

| Token | Hex | Role |
|---|---|---|
| `surfaceVoid` | `#04060B` | Primary background (Compose) / `--void` |
| `surfaceVoid2` | `#0A0E16` | Subtle layer for grouped sections |
| `surfaceShadow` | `#11151D` | Inset wells, deeper recess |
| `surfaceSteel` | `#1A1F2A` | Card backgrounds |
| `surfaceIron` | `#232936` | Interactive surfaces |

### Text — warm cream family

| Token | Hex | Role |
|---|---|---|
| `textPrimary` | `#F5F2EC` | Body and headings (warm cream, not pure white) |
| `textBody` | `#B8BBC2` | Secondary text |
| `textMuted` | `#6B6E76` | Labels, captions |
| `textGhost` | `#3A3D44` | Borders, dividers, disabled |

### Gold — the mentor's voice (carries 30-40% expressive surface)

| Token | Hex | Role |
|---|---|---|
| `gold` | `#E8C77E` | Primary accent — mentor speech, active states |
| `goldFlash` | `#FFE9B5` | Highlights, current/now markers |
| `gold2` / `honey` | `#C7A36B` / `#D4A574` | Mid-warmth, secondary gold |
| `goldDeep` | `#8A6F44` | Quiet gold for labels, dim borders |
| `cream` | `#F0E5D2` | Primary action button background |

### Signal palette — scoped to one role each

| Token | Hex | Role |
|---|---|---|
| `emerald` | `#4A8A5C` | Confirmation, commitment |
| `emeraldBright` | `#6BAF7C` | Completed, done, streak success |
| `crimson` | `#B8334A` | Failure, alarm, MUQADDAS severity |
| `rose` | `#C28B8B` | Soft warning (tired energy, mid-task block) |
| `sky` | `#7CA8C9` | Rest, sleep, calm states |
| `twilight` | `#6B5C8E` | Evening planning, night flows |

Never use signal colors decoratively. Each must mean its scoped role.

## Typography

Four faces, each with a specific job. Never mix outside their lane.

| Family | Usage |
|---|---|
| **Cinzel** (display, bold) | Hero brand titles only ("M·VoW", "TABRIKLAYMAN") |
| **Cormorant Garamond** (medium italic + semibold) | Mentor voice. All mentor speech, identity statements, pivotal questions |
| **JetBrains Mono** (medium) | Labels, timers, data, stats. Anything numeric or system-voice |
| **Inter** (system sans-serif) | UI body, buttons, task names, navigation |

### Scale & hierarchy

- Display: 32-80px (hero counters, screen titles)
- Title: 20-24px (task names, screen headings)
- Body: 13-15px (mentor speech, descriptions)
- Label/data: 9-11px with letter-spacing 2-4 (JetBrains Mono caps)
- Caption: 7.5-9px (sub-labels, density stats)

Minimum 1.25 ratio between adjacent steps. Letter-spacing scales with size: hero (-1 to 0), labels (+2 to +4).

Body line length capped at 65-75ch (mentor speech often shorter — 30-50ch is fine).

## Spacing & layout

- Base unit: 4dp.
- Common increments: 4, 6, 8, 10, 12, 14, 18, 20, 24.
- Vary rhythm: section-level uses 18-20dp gap, intra-section uses 6-10dp. Same-padding monotony is the enemy.
- Container padding: 20dp horizontal on phone; tighter (12-14dp) for cards.
- Cards used SPARINGLY — only when they're truly the best affordance. No nested cards. Most content does not need a container.

## Elevation

Subtle. The void backdrop already provides depth.

- Resting card: `surfaceVoid` body + 1px hairline border in `textGhost` or `accent.opacity(0.3)`
- Active state: tinted background (accent ×0.05-0.18 opacity) + 1px border in solid accent
- Selected: tinted bg (accent ×0.15-0.20) + 1px border in accent
- Focus glow: drop-shadow with accent ×0.4-0.55 alpha, radius 6-12px (used sparingly on mentor orb)

## Border radius

Hard but not brutal:
- 4dp — buttons, chips (the discipline-friend look — committed, not soft)
- 8dp — cards, panels
- 14dp — chat bubbles only
- 999dp — pills, capsules

Never 16-24dp default rounding. That's SaaS cliché.

## Components

### Buttons

**MentorPrimaryButton** — full-width, 56dp tall, 4dp radius, cream background, void-color text, uppercase, letter-spacing 2, weight bold. Disabled state: surfaceIron background, textMuted text.

**MentorSecondaryButton** — same dimensions, transparent background, 1px textMuted border, body-text color, no uppercase.

**MentorGhostButton** — lowercase, textMuted, smaller (12sp), no background, no border. Used for "override request" or skip links.

### Pills

Capsule pills carry status. Format: `[optional leading dot] [text]` with letterspacing 3-4, font 9-10px, padding 12dp horizontal × 5dp vertical. Background: accent.opacity(0.10), border: accent solid 1px, text: accent.

### Mentor orb

The mentor's visible presence. Circle with halo glow:
- Core: radial gradient (light → mid → dark of the tone color)
- Halo: blurred circle 1.6× diameter at 0.45 opacity
- Breathing animation: scale 0.95 ↔ 1.08 over 3s, ease-in-out, repeat forever

Three tones: gold (default), crimson (alarm/refusal), emerald (commitment/success).

### Cards

When used:
- 8dp radius, 1px border (accent.opacity(0.3) or textGhost), tinted background (accent.opacity(0.05-0.06))
- 14dp internal padding
- Section labels: top-left, JetBrains Mono 9px, goldDeep, letterspacing 4

### Mentor speech blocks

Cormorant Garamond Italic 13-15px, with leading orb. Emphasis words (em) in gold + semibold, never bold-only.

### Brand seal divider

Used only at the top of screens with ceremonial weight (welcome, done, celebrate):
```
─── ✦ honey-italic-text ✦ ───
```
Two horizontal gradient lines (transparent → honey → transparent), centered glyph, italic small caps text.

## Motion

- Ease-out exponential (ease-out-quart / quint). No bounce, no elastic.
- Tween durations: 200-600ms for state changes, 1700ms for counter animations (hero numbers), 8s for slow ambient rotations (generating spinner).
- Mentor orb breathing: 3s ease-in-out infinite reverse.
- Never animate layout properties (width/height/margin). Use transform / opacity.

## Iconography

Symbolic glyphs over icon-font icons:
- ✦ — mentor voice, brand seal
- ☾ — prayer marker (with full word: "☾ Peshin", "☾ Asr", "☾ Shom"), night, rest
- ☀ — morning, awake
- ⊙ — focus, session, task
- ↻ — recurring/loop, "har kuni"
- ⚡ — high energy, micro-action
- ▥ ▦ ▧ ▩ — variations of "tiny step" task glyphs
- ✓ ✕ ● ○ — done / blocked / current / upcoming

Avoid Material icons except for nav (calendar, settings) where unambiguous.

## Anti-patterns (specific to this project)

Cross-register absolute bans apply (no gradient text, no side-stripe borders >1px as decorative accents, no glassmorphism default). Project-specific:

- **No "AI mentor" stock UI clichés** — no glowing brain icons, no chat with "How can I help you?" empty state, no robot avatar.
- **No red-on-black "danger" UI** for blocks — we use crimson but warmly (`B8334A`, not pure red). Bypass moments are conversations, not alarms.
- **No infantilizing illustrations.** Adult users. No mascot. No cartoon faces.
- **No streak shaming.** "12 KUN" is celebrated, not "you'll lose your streak!" framing.
- **No emoji-soup in mentor speech.** Single glyph max per sentence (☾ ⊙ ✦).
- **No translucent blur cards.** Solid surfaces + hairline borders only.
- **No category-reflex defaults.** Mentor app ≠ purple gradients. Discipline app ≠ militant red. Religious-allowed ≠ green calligraphy.
