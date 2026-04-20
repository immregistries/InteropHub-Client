# StepIntoCDSI Integration Guide

This guide maps current StepIntoCDSI usage to the extracted InteropHub-Client library.

## Current integration points in StepIntoCDSI

- `AuthSessionSupport` creates a singleton client with `HubClientConfig` and exposes helper methods.
- `LoginServlet` performs callback `code` exchange and safe redirect decision.

## Property mapping

StepIntoCDSI resolves these from `software-version.properties` and Maven filtering:

- `step.external.url` -> `HubClientConfig.stepExternalUrl`
- `hub.external.url` -> `HubClientConfig.hubExternalUrl`
- hardcoded app code `step` -> `HubClientConfig.appCode`
- timeouts 8000/12000 -> `HubClientConfig.connectTimeoutMs` / `readTimeoutMs`

## Dependency update in StepIntoCDSI

Add dependency in StepIntoCDSI `pom.xml`:

```xml
<dependency>
  <groupId>org.immregistries</groupId>
  <artifactId>interophub-client</artifactId>
  <version>${interophub.client.version}</version>
</dependency>
```

## Source migration checklist

1. Remove `org.immregistries.interophub.client` source files from StepIntoCDSI.
2. Ensure imports in `AuthSessionSupport` and `LoginServlet` remain unchanged and resolve from dependency.
3. Build InteropHub-Client and install/publish it.
4. Build StepIntoCDSI and verify login flow behavior.

## Runtime verification checklist

- Hub login URL still includes expected query parameters.
- Callback exchange still maps user fields correctly.
- Redirect logic still rejects off-origin and out-of-base-path URLs.
