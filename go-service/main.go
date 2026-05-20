package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	fmt.Println("Go service starting on port 9090...")

	http.HandleFunc("/summarise", Summarise)
	http.HandleFunc("/health", Health)

	log.Fatal(http.ListenAndServe(":9090", nil))
}
