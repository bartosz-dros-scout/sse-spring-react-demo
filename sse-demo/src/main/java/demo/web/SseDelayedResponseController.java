package demo.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/long")
public class SseDelayedResponseController {

    @GetMapping()
    @CrossOrigin("*")
    public Flux<Response> getDelayedResponse() {
        return Flux.just(new Response("Some message 1"),
                        new Response("Some message 2"),
                        new Response("Some message 3"))
                .delayElements(Duration.of(2, ChronoUnit.SECONDS));
    }

    public static class Response {
        private String message;

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
