# Contributing

## Before opening a change

- Search existing issues and pull requests.
- Keep a change focused on one problem.
- Add or update the legacy smoke fixture under `compatibility/fixtures/legacy-smoke` when changing public module behavior.
- Test both maintained branches when the change touches shared APIs.
- Do not include generated build output, local run files, or credentials.

## Build checks

Run the project build before submitting a pull request:

```text
./gradlew build
```

Changes to triggers, wrappers, module loading, or JavaScript behavior should also update the fixture under `compatibility/fixtures/legacy-smoke` and its entry in `compatibility/manifest.json`. When a change needs a separate focused fixture, document how it is staged and exercised in the manifest.

## Commit messages

Use a short imperative summary. Explain compatibility decisions and behavior changes in the commit body when they are not obvious from the diff.

## Review expectations

Maintainers review correctness, API compatibility, platform behavior, security impact, and whether the change can be supported on both maintained Minecraft versions. A passing build is required but does not replace review.
