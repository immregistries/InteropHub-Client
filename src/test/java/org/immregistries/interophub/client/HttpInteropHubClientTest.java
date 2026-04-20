package org.immregistries.interophub.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HttpInteropHubClientTest {

    private HttpInteropHubClient createClient() {
        HubClientConfig config = new HubClientConfig(
                "https://example.org/step",
                "https://example.org/hub",
                "step",
                8000,
                12000);
        return new HttpInteropHubClient(config);
    }

    @Test
    void shouldBuildHubHomeAndExchangeUrls() {
        HttpInteropHubClient client = createClient();
        assertEquals("https://example.org/hub/home", client.getHubHomeUrl());
        assertEquals("https://example.org/hub/api/auth/exchange", client.getHubAuthExchangeUrl());
    }

    @Test
    void shouldBuildLoginUrlWithExpectedParameters() {
        HttpInteropHubClient client = createClient();

        String loginUrl = client.buildLoginUrl("https://example.org/step/home?x=1");

        assertTrue(loginUrl.startsWith("https://example.org/hub/home?"));
        assertTrue(loginUrl.contains("app_code=step"));
        assertTrue(loginUrl.contains("return_to=https%3A%2F%2Fexample.org%2Fstep%2Flogin"));
        assertTrue(loginUrl.contains("requested_url=https%3A%2F%2Fexample.org%2Fstep%2Fhome%3Fx%3D1"));
        assertTrue(loginUrl.contains("state="));
    }

    @Test
    void shouldAcceptRequestedUrlWithinConfiguredBasePath() {
        HttpInteropHubClient client = createClient();

        HubRedirectDecision decision = client.determineRedirectDecision(
                "https://example.org/step/home?x=1#top",
                "/home");

        assertEquals("/step/home?x=1#top", decision.getTarget());
        assertEquals("none", decision.getFallbackReason());
    }

    @Test
    void shouldFallbackWhenRequestedUrlIsDifferentHost() {
        HttpInteropHubClient client = createClient();

        HubRedirectDecision decision = client.determineRedirectDecision(
                "https://evil.example.com/step/home",
                "/home");

        assertEquals("/home", decision.getTarget());
        assertTrue(decision.getFallbackReason().contains("host"));
    }

    @Test
    void shouldFallbackWhenRequestedUrlOutsideConfiguredPath() {
        HttpInteropHubClient client = createClient();

        HubRedirectDecision decision = client.determineRedirectDecision(
                "https://example.org/other/path",
                "/home");

        assertEquals("/home", decision.getTarget());
        assertTrue(decision.getFallbackReason().contains("outside configured Step base path"));
    }
}
