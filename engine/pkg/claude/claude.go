package claude

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
)

type message struct {
	Role    string `json:"role"`
	Content string `json:"content"`
}

type apiRequest struct {
	Model     string    `json:"model"`
	MaxTokens int       `json:"max_tokens"`
	Messages  []message `json:"messages"`
}

type apiResponse struct {
	Content []struct {
		Text string `json:"text"`
	} `json:"content"`
	Error struct {
		Message string `json:"message"`
	} `json:"error,omitempty"`
}

func Ask(question string) (string, error) {
	messages := []message{
		{
			Role:    "user",
			Content: question,
		},
	}

	model := "claude-3-5-sonnet-20240620"
	requestBody := apiRequest{
		Model:     model,
		Messages:  messages,
		MaxTokens: 16,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		return "", fmt.Errorf("error marshaling request: %v", err)
	}

	req, err := http.NewRequest("POST", "https://api.anthropic.com/v1/messages", bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("error creating request: %v", err)
	}

	req.Header.Set("x-api-key", os.Getenv("ANTHROPIC_API_KEY"))
	req.Header.Set("anthropic-version", "2023-06-01")
	req.Header.Set("content-type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("error making request: %v", err)
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("error reading response: %v", err)
	}

	var apiResponse apiResponse
	if err := json.Unmarshal(body, &apiResponse); err != nil {
		return "", fmt.Errorf("error unmarshaling response: %v", err)
	}

	if apiResponse.Error.Message != "" {
		return "", fmt.Errorf("API error: %s", apiResponse.Error.Message)
	}

	if len(apiResponse.Content) > 0 {
		return apiResponse.Content[0].Text, nil
	}

	return "", fmt.Errorf("no content in response")
}
