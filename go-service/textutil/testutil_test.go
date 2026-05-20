package textutil

import (
	"testing"
)

func TestCountWords(t *testing.T) {
	tests := []struct {
		input    string
		expected int
	}{
		{"hello world", 2},
		{"one two three four", 4},
		{"", 0},
		{"   spaces   everywhere ", 2},
	}

	for _, tt := range tests {
		got := CountWords(tt.input)
		if got != tt.expected {
			t.Errorf("CountWords(%q) = %d, want %d", tt.input, got, tt.expected)
		}
	}
}

func TestCleanAndTruncate_noTruncation(t *testing.T) {
	text := "Today was a good day"
	cleaned, truncated := CleanAndTruncate(text, 10)

	if truncated {
		t.Error("should not truncate short text")
	}
	if CountWords(cleaned) != 5 {
		t.Errorf("expected 5 words, got %d", CountWords(cleaned))
	}
}

func TestCleanAndTruncate_withTruncation(t *testing.T) {
	text := "one two three four five six seven eight nine ten eleven twelve"
	cleaned, truncated := CleanAndTruncate(text, 5)

	if !truncated {
		t.Error("should have truncated")
	}
	if CountWords(cleaned) != 5 {
		t.Errorf("expected 5 words, got %d", CountWords(cleaned))
	}
}

func TestCleanAndTruncate_removesExtraSpaces(t *testing.T) {
	text := "hello    world\n\nthis   is  a test"
	cleaned, _ := CleanAndTruncate(text, 100)

	// Should have no double spaces
	if len(cleaned) == len(text) {
		t.Error("extra spaces should have been removed")
	}
}
