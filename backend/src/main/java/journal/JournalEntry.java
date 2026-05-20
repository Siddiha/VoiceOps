package journal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String text;
    private String mood;
    private LocalDateTime createdAt;
    private String summary;
    private String tasks;

    public JournalEntry() {}

    public JournalEntry(String text) {
        this.text = text;
        this.mood = "unknown";
        this.createdAt = LocalDateTime.now();
    }

    public void applyAgentResponse(AgentResponse r) {
        this.mood    = r.getMood();
        this.summary = r.getSummary();
        this.tasks   = String.join(", ", r.getTasks());
    }

    public Long getId()                 { return id; }
    public String getText()             { return text; }
    public String getMood()             { return mood; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getSummary()          { return summary; }
    public String getTasks()            { return tasks; }

    public void setMood(String mood)    { this.mood = mood; }
}
