package bot

import (
	"context"
	"errors"
	"github.com/notnil/chess"
	"math/rand"
)

type Random struct{}

func (r *Random) GetMove(ctx context.Context, position *chess.Position) (*chess.Move, error) {
	moves := position.ValidMoves()
	if len(moves) == 0 {
		return nil, errors.New("no available moves found")
	}
	return moves[rand.Intn(len(moves))], nil
}
