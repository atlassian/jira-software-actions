# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Compatibility
The library offers compatibility contracts on the Java API and the POM.

### Java API
The API covers all public Java types from `com.atlassian.performance.tools.jirasoftwareactions.api` and its subpackages:

  * [source compatibility]
  * [binary compatibility]
  * [behavioral compatibility] with behavioral contracts expressed via Javadoc

[source compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#source_compatibility
[binary compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#binary_compatibility
[behavioral compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#behavioral_compatibility

### POM
Changing the license is breaking a contract.
Adding a requirement of a major version of a dependency is breaking a contract.
Dropping a requirement of a major version of a dependency is a new contract.

## [Unreleased]
[Unreleased]: https://bitbucket.org/atlassian/jira-software-actions/branches/compare/master%0Drelease-1.2.0

### Added
- Let virtual users use their own filter predicates instead of the built-in `isWorthVisiting` method

### Deprecated
- Deprecate `NonEmptyBoardMemory` 
- Deprecate `isWorthVisiting` method in the `Board`

## [1.2.0] - 2018-10-29
[1.2.0]: https://bitbucket.org/atlassian/jira-software-actions/branches/compare/release-1.2.0%0Drelease-1.1.0

### Added
- Let virtual users memorize more details about boards:
  - is it a scrum board?
  - how many issues does it display?
  - how many issues are in the backlog?
- Let virtual users avoid empty boards. Resolve [JPERF-211].

### Fixed
- Avoid empty boards in the `JiraSoftwareScenario`.

### Deprecated
- Deprecate `AgileBoardIdMemory` in favor of `Memory<Board>`.

[JPERF-211]: https://ecosystem.atlassian.net/browse/JPERF-211

## [1.1.0] - 2018-10-15
[1.1.0]: https://bitbucket.org/atlassian/jira-software-actions/branches/compare/release-1.1.0%0Drelease-1.0.0

### Added
- New action that visits backlog of Scrum boards

### Fixed
- Decrease log level for actions complaining about skipping run, which resolves [JPERF-162].

[JPERF-162]: https://ecosystem.atlassian.net/browse/JPERF-162

## [1.0.0] - 2018-09-04
[1.0.0]: https://bitbucket.org/atlassian/jira-software-actions/branches/compare/release-1.0.0%0Drelease-0.1.1

### Changed
- Define public API for the module.
- Consume stable API of `jira-actions`

### Fixed
- Add this changelog.
