# InteropHub-Client

Reusable Java client library for InteropHub authentication callback exchange and safe post-login redirect decisions.

## What this library provides

- Build InteropHub login URLs using your app's base URL and app code.
- Exchange callback `code` values with InteropHub.
- Parse exchange responses into typed result objects.
- Validate and normalize redirect targets so redirects stay within your configured app base URL.

## Maven dependency

```xml
<dependency>
	<groupId>org.immregistries</groupId>
	<artifactId>interophub-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Quick start

```java
import org.immregistries.interophub.client.HubClientConfig;
import org.immregistries.interophub.client.HubExchangeResult;
import org.immregistries.interophub.client.HubRedirectDecision;
import org.immregistries.interophub.client.InteropHubClient;
import org.immregistries.interophub.client.InteropHubClientFactory;

HubClientConfig config = new HubClientConfig(
		"https://example.org/step",   // your app external URL
		"https://example.org/hub",    // InteropHub external URL
		"step",                       // app_code
		8000,                          // connect timeout ms
		12000                          // read timeout ms
);

InteropHubClient client = InteropHubClientFactory.create(config);

String loginUrl = client.buildLoginUrl("https://example.org/step/home");
// redirect browser to loginUrl

HubExchangeResult exchange = client.exchangeCode(code, userIp);
if (exchange.isSuccess() && exchange.hasRequiredUserInfo()) {
	HubRedirectDecision decision = client.determineRedirectDecision(
			exchange.getRequestedUrlFromHub(),
			"/home"
	);
	// redirect to decision.getTarget()
} else {
	// handle failure, inspect exchange.getHttpStatus() and exchange.getErrorMessage()
}
```

## Security notes

- Always set `stepExternalUrl` to the public base URL that should receive redirects.
- Keep fallback targets local (for example `/home`).
- Do not trust `requested_url` from callback parameters directly. Use `determineRedirectDecision(...)`.

## Documentation

- [Getting started](docs/getting-started.md)
- [API contract](docs/api-contract.md)
- [StepIntoCDSI integration guide](docs/integration-stepintocdsi.md)
- [Release and versioning](docs/release-and-versioning.md)
