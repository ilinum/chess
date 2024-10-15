package bot

import (
	"context"
	"github.com/notnil/chess"
)

type Bot interface {
	GetMove(context.Context, *chess.Position) (*chess.Move, error)
}

var Bots = map[string]Bot{
	"random":  &Random{},
	"minimax": &MiniMax{},
}
