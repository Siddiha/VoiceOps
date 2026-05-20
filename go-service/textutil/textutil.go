package textutil

import (
	"regexp"
	"strings"
)

var multiSpace = regexp.MustCompile(`\s+`)

func CountWords(s string) int {
	s = strings.TrimSpace(s)
	if s == "" {
		return 0
	}
	return len(strings.Fields(s))
}

func CleanAndTruncate(text string, maxWords int) (string, bool) {
	cleaned := strings.TrimSpace(multiSpace.ReplaceAllString(text, " "))
	words := strings.Fields(cleaned)
	if len(words) <= maxWords {
		return cleaned, false
	}
	return strings.Join(words[:maxWords], " "), true
}
