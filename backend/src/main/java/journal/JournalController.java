package journal;

import java.util.List;
import org.springframework.web.bind.annotation.*;

// @RestController = this class handles HTTP requests
// @RequestMapping = all routes in here start with /journal
@RestController
@RequestMapping("/journal")
@CrossOrigin(origins = "*") // allows frontend to call this API
public class JournalController {

    private final JournalService service;

    public JournalController(JournalService service) {
        this.service = service;
    }

    // POST /journal
    // Body: { "text": "Today was a good day..." }
    // Saves the entry and returns it
    @PostMapping
    public JournalEntry create(@RequestBody EntryRequest req) {
        return service.save(req.text());
    }

    // GET /journal
    // Returns all saved journal entries
    @GetMapping
    public List<JournalEntry> getAll() {
        return service.getAll();
    }

    // Simple record to hold the request body
    record EntryRequest(String text) {}

}