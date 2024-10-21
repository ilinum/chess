package bot

import (
	"context"
	"fmt"
	"github.com/notnil/chess"
	"log"
	"strings"
)

type LLM struct {
	ask func(string) (string, error)
}

func (l *LLM) GetMove(ctx context.Context, position *chess.Position) (*chess.Move, error) {
	question := "I'd like to play chess with you. Here's the position in FEN notation:\n" +
		"\"%s\".\n" +
		"What is the next best move?\n" +
		"Please only respond with the move with extended algebraic notation (no other output)."
	question = fmt.Sprintf(question, position.String())
	response, err := l.ask(question)
	if err != nil {
		return nil, err
	}
	log.Printf("Got the following response from LLM: %s", response)
	response = strings.Replace(response, "-", "", -1)
	notation := &chess.AlgebraicNotation{}
	return notation.Decode(position, response)
}
