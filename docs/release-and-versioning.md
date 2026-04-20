# Release and Versioning

## Versioning model

Use semantic versioning:

- Major: Breaking API or behavior changes.
- Minor: Backward-compatible feature additions.
- Patch: Backward-compatible fixes only.

## Release checklist

1. Run unit tests locally.
2. Build jar, sources jar, and javadoc jar.
3. Update changelog/release notes.
4. Tag release in source control.
5. Publish artifact to repository.

## Consumer paths

### Local development consumption

```bash
mvn clean install
```

Then in consumer project:

```xml
<dependency>
  <groupId>org.immregistries</groupId>
  <artifactId>interophub-client</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Repository consumption

Publish to internal repository (Nexus/Artifactory/GitHub Packages) and configure consumer `settings.xml`/`distributionManagement` as needed.

## Backward compatibility expectations

Public API includes:

- `InteropHubClient`
- `InteropHubClientFactory`
- `HubClientConfig`
- `HubExchangeResult`
- `HubRedirectDecision`
- `HubUserInfo`

Breaking changes to these types require major version increment and migration notes.

## Recommended project hygiene

- Keep changelog updated.
- Keep CI running `mvn test` on pull requests.
- Avoid adding runtime logging backend dependencies in the library.
- Document any new fallback reasons or response parsing behavior changes.
