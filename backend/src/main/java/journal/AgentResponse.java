package journal;

public class AgentResponse {

    private String mood;
    private String[] tasks;
    private String summary;

    public AgentResponse() {}

    public AgentResponse(String mood, String[] tasks, String summary) {
        this.mood    = mood;
        this.tasks   = tasks;
        this.summary = summary;
    }

    public String   getMood()    { return mood; }
    public String[] getTasks()   { return tasks; }
    public String   getSummary() { return summary; }
}