# API Contract

## InteropHubClient

Interface: `org.immregistries.interophub.client.InteropHubClient`

Methods:

- `String buildLoginUrl(String requestedUrl)`
- `String getHubHomeUrl()`
- `String getHubAuthExchangeUrl()`
- `HubExchangeResult exchangeCode(String code, String userIp)`
- `HubRedirectDecision determineRedirectDecision(String requestedUrlFromHub, String fallbackTarget)`

### Behavioral guarantees

- `buildLoginUrl(...)` always includes `app_code`, `return_to`, `state`, and `requested_url` query parameters.
- `getHubHomeUrl()` resolves to `<hubExternalUrl>/home`.
- `getHubAuthExchangeUrl()` resolves to `<hubExternalUrl>/api/auth/exchange`.
- `exchangeCode(...)` returns a structured result even on failure.
- `determineRedirectDecision(...)` only accepts requested URLs that match configured scheme, host, port, and base path.

## HubClientConfig

Constructor:

- `HubClientConfig(String stepExternalUrl, String hubExternalUrl, String appCode, int connectTimeoutMs, int readTimeoutMs)`

Values are trimmed; null strings are treated as empty strings.

## HubExchangeResult

Fields exposed through getters:

- `success`
- `httpStatus`
- `responseBody`
- `errorMessage`
- `userInfo`
- `requestedUrlFromHub`
- `returnToFromHub`

### Required user info rule

`hasRequiredUserInfo()` is true only when all are non-blank:

- `name`
- `organization`
- `title`
- `email`

## HubRedirectDecision

- `target`: Redirect target chosen by decision logic.
- `fallbackReason`: Reason for fallback (`none` when not falling back).

## Versioning expectations

- Additive changes (new methods/fields) should be minor version updates.
- Breaking changes to signatures or behavior should be major version updates.
