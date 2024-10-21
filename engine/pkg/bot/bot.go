package bot

import (
	"context"
	"github.com/ilinum/chess/pkg/claude"
	"github.com/notnil/chess"
)

type Bot interface {
	GetMove(context.Context, *chess.Position) (*chess.Move, error)
}

var Bots = map[string]Bot{
	"claude":  &LLM{ask: claude.Ask},
	"random":  &Random{},
	"minimax": &MiniMax{},
}
