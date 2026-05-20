package main

import (
	"encoding/json"
	"io"
	"net/http"
	"strings"

	"github.com/voiceops/go-service/textutil"
)

type SummariseRequest struct {
	Text string `json:"text"`
}

type SummariseResponse struct {
	Cleaned   string `json:"cleaned"`
	WordCount int    `json:"wordCount"`
	Truncated bool   `json:"truncated"`
}

func Summarise(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "only POST allowed", http.StatusMethodNotAllowed)
		return
	}

	body, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, "could not read body", http.StatusBadRequest)
		return
	}
	defer r.Body.Close()

	var req SummariseRequest
	if err := json.Unmarshal(body, &req); err != nil {
		http.Error(w, "invalid JSON", http.StatusBadRequest)
		return
	}

	if strings.TrimSpace(req.Text) == "" {
		http.Error(w, "text is empty", http.StatusBadRequest)
		return
	}

	cleaned, truncated := textutil.CleanAndTruncate(req.Text, 300)
	wordCount := textutil.CountWords(cleaned)

	resp := SummariseResponse{
		Cleaned:   cleaned,
		WordCount: wordCount,
		Truncated: truncated,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func Health(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.Write([]byte(`{"status":"ok"}`))
}
