package journal;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JournalService {

    private final JournalRepository repo;
    private final AIAgent aiAgent;

    public JournalService(JournalRepository repo, AIAgent aiAgent) {
        this.repo = repo;
        this.aiAgent = aiAgent;
    }

    public JournalEntry save(String text) {
        JournalEntry entry = new JournalEntry(text);
        try {
            AgentResponse result = aiAgent.analyse(text);
            entry.applyAgentResponse(result);
        } catch (Exception e) {
            System.out.println("AI skipped: " + e.getMessage());
        }
        return repo.save(entry);
    }

    public List<JournalEntry> getAll() {
        return repo.findAll();
    }
}
