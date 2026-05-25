package journal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AIAgentTest {

    @Test
    void agentResponse_fieldsStoreCorrectly() {
        AgentResponse r = new AgentResponse(
            "happy",
            new String[]{"buy milk", "call mom"},
            "A good day overall"
        );
        assertEquals("happy",    r.getMood());
        assertEquals(2,          r.getTasks().length);
        assertEquals("buy milk", r.getTasks()[0]);
    }

    @Test
    void journalEntry_appliesAgentResponse() {
        JournalEntry entry = new JournalEntry("Finished my project, feeling great!");
        AgentResponse r    = new AgentResponse(
            "excited",
            new String[]{"push to GitHub", "write tests"},
            "Productive and exciting day"
        );
        entry.applyAgentResponse(r);
        assertEquals("excited", entry.getMood());
        assertTrue(entry.getTasks().contains("push to GitHub"));
    }
}
