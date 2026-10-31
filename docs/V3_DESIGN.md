# v3 structure and behavior

The original rich editor, calendar/alarms, native inference service, exact-revision approval system, and branding are retained. The application shell now gives routine work a shared Material 3 hierarchy and keeps secondary controls in contextual sheets.

## Navigation and sketches

```text
Phone                                  Tablet (600 dp rail; 840 dp note split)
┌ ProSpaceHub / section ▾   ⚙ ┐         ┌ rail ┬ Notes list ┬ Editor      ⚙ ┐
│ Today: actions / resume     │         │ Today│ Search     │ Title         │
│ Continue here               │         │ Plan │ Notebook   │ Save status   │
│ Ready tasks / today's agenda│         │ Notes│ Note cards │ Text / tools  │
├─────────────────────────────┤         │ Read │            │               │
│Today Planner Notes Read  AI │         │ AI   │            │               │
└─────────────────────────────┘         └──────┴────────────┴───────────────┘

Planner: [Ready] [Tasks] [Projects] [Calendar]
  Search → [time window] [project] [ordering]
  Task → details / prerequisites / blocker / focus / optional AI
  Project → progress / blockers / tasks / notes / conversations

Reading: [Library] [Journey]                         [+ Import]
  Library → search / filters / recent → reader
  Reader → position / contents / bookmarks / contextual tools
  Journey → goals / heatmap / current / sessions / finished timeline

Assistant: conversation picker / project / new conversation
  Bounded message window → message field → optional approved tools
```

The title menu exposes every main destination, including deliberately hidden navigation items. Settings stays reachable independently of navigation customization. Typed, versioned saved state retains destination and planner/note routes; each destination also retains its local UI state. Notes have durable recovery drafts; the current chat draft uses saved state. Reading's private positions remain available through Recent even when the operating system starts a fresh activity.

## Shared presentation

Comfortable spacing, 48 dp Material controls, readable title/body hierarchy, restrained green branding, 8/12/20 dp corner shapes, and light/dark/system themes form the common vocabulary. Custom accents are adjusted for readable text contrast; text size, editor fonts, compact spacing, and background choices remain available. Large forms scroll above the keyboard, and the note editor hides secondary controls while writing. Navigation and chat options collapse around the keyboard, with Note details and Conversation options in contextual sheets.

Lists animate insertion, removal, and placement after their saved state changes. Progress uses bounded short transitions. System animation scale and System/Reduced/Off app settings gate these effects; reduced motion removes list travel and retains brief fades. Deletions offer Undo and soft-deleted items remain in Recovery. Inference uses a separate process and background-priority worker; it never drives navigation or animation.

## Persistence and normal app logic

Room schema5 adds projects, prerequisite edges, labels, templates, conversations/messages, focus sessions, resume markers, recovery drafts, and reading entities through explicit migrations. Legacy task completion and dates remain compatible. Template copies remap internal task, parent, dependency and note-link IDs; external prerequisites become visible blockers. Calendar-day offsets preserve local deadline time across DST. Templates allow up to 1,000 tasks, 200 notes, 5,000 links and 1 MB serialized data, with a preflight text bound.

Prerequisites form a transactional DAG. Deleted or unfinished prerequisites block work; restoring/completing them recomputes availability. Manual blockers remain separate. Ready filtering/order, estimates, timing, progress, file handling, and statistics use ordinary code. Manual task moves atomically rebalance persisted order.

Focus uses elapsed realtime within a boot, persisted checkpoints, revision-guarded commands and Android alarms. Same-boot process recovery continues elapsed timing; reboot recovery pauses at the last known checkpoint. Handoffs can be written independently for notes, tasks or projects. Breaks remain separate from focus totals.

## Optional AI and reader boundaries

Small-model skills receive one short source, at most a brief project description and eight allowed labels. The native input budget is 512 tokens; results have per-skill output limits. Suggestions are cancellable, editable and explicitly applied. Source/revision/selection checks reject stale edits; generated text cannot authorize writes. Saved conversations use 200-message windows and at most four recent context messages.

Reader resources use native ZIP/range access, isolated local WebView frames, native PDFium or bounded comic bitmaps. The app has no network permission. See [READING.md](READING.md) for the direct/conversion/unsupported matrix and memory limits, and [VALIDATION.md](../VALIDATION.md) for tested behavior and remaining device-specific uncertainty.
