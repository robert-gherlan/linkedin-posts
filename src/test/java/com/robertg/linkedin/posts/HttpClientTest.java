package com.robertg.linkedin.posts;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class HttpClientTest {

    private static HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                .build();

    @Test
    void testGetHttpRequest() throws URISyntaxException, InterruptedException, IOException {
        final var httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://httpbin.org/anything?param=value"))
                .header("Accept", "application/json")
                .header("Custom-Header","custom value")
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();

        final var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.statusCode()).isEqualTo(200);
        assertThat(httpResponse.headers().map()).isNotEmpty();
        final var paramValue = JsonPath.read(httpResponse.body(), "$.args.param");
        assertThat(paramValue).isEqualTo("value");

        final var customHeaderValue = JsonPath.read(httpResponse.body(), "$.headers.Custom-Header");
        assertThat(customHeaderValue).isEqualTo("custom value");
    }

    @Test
    void testAsyncPostHttpRequest() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        final var requestBody = """
                {
                "key": "value",
                "key1": "value1"
                }
                """;
        final var httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://httpbin.org/anything?param=value"))
                .header("Accept", "application/json")
                .header("Custom-Header","custom value")
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        final var httpResponseAsync = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        final var httpResponse = httpResponseAsync.get(1, TimeUnit.MINUTES);

        assertThat(httpResponse.statusCode()).isEqualTo(200);
        assertThat(httpResponse.headers().map()).isNotEmpty();

        final var keyRequestBodyValue = JsonPath.read(httpResponse.body(), "$.json.key");
        assertThat(keyRequestBodyValue).isEqualTo("value");
    }

    @Disabled("A new echo websocket server is required to make this test passing.")
    void testWebSocket() throws ExecutionException, InterruptedException {
        var echoed = new AtomicBoolean(false);
        var listener = new WebSocket.Listener() {
            @Override
            public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
                webSocket.request(1);
                System.out.println(data);
                return CompletableFuture.completedFuture(data)
                        .thenAccept(message -> echoed.set("Last Message.".contentEquals(message)));
            }
        };

        var uri = URI.create("wss://echo.websocket.org");
        var webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(uri, listener)
                .get();

        webSocket.sendText("First Message.", false);
        webSocket.sendText("Last Message.", true);
        await().untilTrue(echoed);
    }
}
