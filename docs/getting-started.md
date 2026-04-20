# Getting Started

This guide shows the end-to-end integration flow for InteropHub login and callback handling.

## 1. Configure the client

```java
HubClientConfig config = new HubClientConfig(
    "https://example.org/step",
    "https://example.org/hub",
    "step",
    8000,
    12000
);
InteropHubClient client = InteropHubClientFactory.create(config);
```

Configuration fields:

- `stepExternalUrl`: Public base URL of your application.
- `hubExternalUrl`: Public base URL of InteropHub.
- `appCode`: Your app code recognized by InteropHub.
- `connectTimeoutMs`: Connection timeout for exchange calls.
- `readTimeoutMs`: Read timeout for exchange calls.

## 2. Start login

Build a Hub login URL from the current page URL and redirect the browser.

```java
String loginUrl = client.buildLoginUrl(currentUrl);
response.sendRedirect(loginUrl);
```

The generated URL includes:

- `app_code`
- `return_to` (your app callback URL)
- `state`
- `requested_url`

## 3. Handle callback and exchange code

When Hub calls your callback endpoint with `code`, exchange it:

```java
HubExchangeResult result = client.exchangeCode(code, userIpAddress);
```

On success, inspect user info:

```java
if (result.isSuccess() && result.hasRequiredUserInfo()) {
  HubUserInfo user = result.getUserInfo();
  // create your local session
}
```

Required fields for `hasRequiredUserInfo()`:

- `name`
- `organization`
- `title`
- `email`

## 4. Determine safe redirect target

Use the helper to enforce same-origin and base-path validation.

```java
HubRedirectDecision decision = client.determineRedirectDecision(
    result.getRequestedUrlFromHub(),
    "/home"
);
response.sendRedirect(decision.getTarget());
```

If requested URL is invalid or outside your configured base URL, fallback is used.

## 5. Error handling recommendations

- Treat non-2xx exchange status as authentication failure.
- Log `httpStatus`, `errorMessage`, and correlation details, but do not log sensitive payloads at high verbosity in production.
- Provide user-friendly fallback behavior when exchange fails.

## 6. Logging recommendations

This library uses SLF4J API only. Configure your logging backend in the consuming application (Logback, Log4j2, etc.).
