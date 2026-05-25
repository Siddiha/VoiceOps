package main

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHealth(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/health", nil)
	w := httptest.NewRecorder()

	Health(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("expected 200, got %d", w.Code)
	}

	var body map[string]string
	json.Unmarshal(w.Body.Bytes(), &body)
	if body["status"] != "ok" {
		t.Errorf("expected status ok, got %s", body["status"])
	}
}

func TestSummarise_success(t *testing.T) {
	payload := `{"text": "Today was a really good day. I finished my work early and went for a walk."}`
	req := httptest.NewRequest(http.MethodPost, "/summarise", bytes.NewBufferString(payload))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	Summarise(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("expected 200, got %d", w.Code)
	}

	var resp SummariseResponse
	if err := json.Unmarshal(w.Body.Bytes(), &resp); err != nil {
		t.Fatalf("could not decode response: %v", err)
	}
	if resp.Cleaned == "" {
		t.Error("expected non-empty cleaned text")
	}
	if resp.WordCount == 0 {
		t.Error("expected non-zero word count")
	}
}

func TestSummarise_emptyText(t *testing.T) {
	payload := `{"text": ""}`
	req := httptest.NewRequest(http.MethodPost, "/summarise", bytes.NewBufferString(payload))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	Summarise(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("expected 400 for empty text, got %d", w.Code)
	}
}

func TestSummarise_wrongMethod(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/summarise", nil)
	w := httptest.NewRecorder()

	Summarise(w, req)

	if w.Code != http.StatusMethodNotAllowed {
		t.Errorf("expected 405, got %d", w.Code)
	}
}

func TestSummarise_invalidJSON(t *testing.T) {
	req := httptest.NewRequest(http.MethodPost, "/summarise", bytes.NewBufferString(`not-json`))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	Summarise(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("expected 400 for invalid JSON, got %d", w.Code)
	}
}

func TestSummarise_truncatesLongText(t *testing.T) {
	longText := bytes.Repeat([]byte("word "), 400)
	payload, _ := json.Marshal(map[string]string{"text": string(longText)})

	req := httptest.NewRequest(http.MethodPost, "/summarise", bytes.NewBuffer(payload))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	Summarise(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("expected 200, got %d", w.Code)
	}

	var resp SummariseResponse
	json.Unmarshal(w.Body.Bytes(), &resp)
	if !resp.Truncated {
		t.Error("expected truncated=true for 400-word input")
	}
	if resp.WordCount > 300 {
		t.Errorf("expected at most 300 words, got %d", resp.WordCount)
	}
}
