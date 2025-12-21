package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.NotificationRequest;
import ptit.com.enghub.entity.Notification;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.service.NotificationService;
import ptit.com.enghub.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public Page<Notification> getNotifications(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return service.listForUser(PageRequest.of(page, size));
    }

    @PostMapping
    public Notification createNotification(@RequestBody NotificationRequest req) {
        return service.create(req);
    }

    @PostMapping("/{id}/read")
    public void markRead(@PathVariable UUID id) {
        service.markAsRead(id);
    }

    @GetMapping("/unread-count")
    public long unreadCount() {
        return service.countUnread();
    }
}
