# Step 1 — Build the Go binary
FROM golang:1.21-alpine AS build

WORKDIR /app

# Copy go.mod first for dependency caching
COPY go.mod .
RUN go mod download

# Copy source code
COPY . .

# Build a single binary file
# CGO_ENABLED=0 makes it fully static — runs on any Linux
RUN CGO_ENABLED=0 GOOS=linux go build -o go-service .

# ---

# Step 2 — Run with a tiny image
# scratch is completely empty — just your binary, nothing else
# This makes the final image only ~10MB instead of ~300MB
FROM alpine:3.19

WORKDIR /app

# Copy binary from build stage
COPY --from=build /app/go-service .

# Port 9090
EXPOSE 9090

# Run the binary
CMD ["./go-service"]