# Controlled Fixtures

Repository fixtures are reusable qualitative probes, not benchmark truth.

Summarization fixtures follow the ML Kit `ARTICLE` input boundary: each baked
article contains more than 400 characters, and at least one article contains
300 or more words for the documented best-performing range. Shorter valid
articles remain useful boundary probes but must not be presented as equivalent
quality evidence.

Each `TestCase` provides:

- a stable fixture ID
- one configured capability
- a derived character-count size band: `SHORT`, `MEDIUM`, or `LONG`
- a qualitative check describing the expected preservation boundary
- an optional more specific expected-output guideline

When a user selects a baked fixture, its stable ID is persisted in the
experiment context. Arbitrary pasted input intentionally retains a null
`fixtureId`.

Fixture IDs must remain stable after publication. Add a new ID rather than
silently changing the meaning of an existing fixture.
