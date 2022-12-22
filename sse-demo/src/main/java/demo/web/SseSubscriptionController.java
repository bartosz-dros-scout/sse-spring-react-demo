package demo.web;

import demo.subscription.SubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/subscription")
public class SseSubscriptionController {

    private final SubscriptionService service;

    public SseSubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin("*")
    public SseEmitter getDelayedResponse() {
        return service.subscribe();
    }

    @PostMapping("/notify")
    @CrossOrigin("*")
    public ResponseEntity notify(@RequestBody SubscriptionService.Notification notification) {
        service.notify(notification);
        return ResponseEntity.ok().build();
    }

}
