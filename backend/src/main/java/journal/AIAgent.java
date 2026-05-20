package journal;

import com.google.gson.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIAgent {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${go-service.url}")
    private String goServiceUrl;

    private final OkHttpClient http = new OkHttpClient();
    private final Gson gson         = new Gson();

    public AgentResponse analyse(String journalText) throws Exception {
        String cleanedText = cleanTextViaGoService(journalText);

        String prompt = """
            You are a journal assistant.
            Read the entry below. Reply ONLY with this JSON, nothing else:
            {
              "mood": "one word: happy/sad/anxious/calm/excited/tired",
              "tasks": ["task1", "task2"],
              "summary": "one sentence"
            }

            Journal entry:
            """ + cleanedText;

        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", prompt);

        JsonArray msgs = new JsonArray();
        msgs.add(msg);

        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.add("messages", msgs);
        body.addProperty("max_tokens", 200);

        Request req = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(RequestBody.create(body.toString(),
                  MediaType.get("application/json")))
            .build();

        try (Response res = http.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                System.out.println("OpenAI error: " + res.body().string());
                return fallback();
            }

            JsonObject json = gson.fromJson(res.body().string(), JsonObject.class);
            String aiText   = json.getAsJsonArray("choices")
                                  .get(0).getAsJsonObject()
                                  .getAsJsonObject("message")
                                  .get("content").getAsString();

            return gson.fromJson(aiText.trim(), AgentResponse.class);
        }
    }

    private String cleanTextViaGoService(String text) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("text", text);

            Request req = new Request.Builder()
                .url(goServiceUrl)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(),
                      MediaType.get("application/json")))
                .build();

            try (Response res = http.newCall(req).execute()) {
                if (!res.isSuccessful()) return text;

                JsonObject json = gson.fromJson(res.body().string(), JsonObject.class);
                return json.get("cleaned").getAsString();
            }
        } catch (Exception e) {
            System.out.println("Go service unavailable, using raw text: " + e.getMessage());
            return text;
        }
    }

    private AgentResponse fallback() {
        return new AgentResponse("unknown", new String[]{}, "Analysis unavailable");
    }
}
