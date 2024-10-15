package bot

import (
	"context"
	"github.com/notnil/chess"
	"log"
	"math"
	"math/rand"
)

type MiniMax struct{}

func (m *MiniMax) GetMove(ctx context.Context, position *chess.Position) (*chess.Move, error) {
	bestScore := math.MinInt32
	// Depth is picked randomly. We should read pass in the value into this method based on UCI command, if provided.
	depth := 3
	var bestMoves []*chess.Move
	for _, move := range position.ValidMoves() {
		score := m.scoreMove(ctx, position.Update(move), depth)
		if position.Turn() == chess.Black {
			score *= -1
		}
		log.Printf("Score for move %s is %d\n", move.String(), score)
		if score == bestScore {
			bestMoves = append(bestMoves, move)
		}
		if score > bestScore {
			bestScore = score
			bestMoves = []*chess.Move{move}
		}
	}
	bestMove := bestMoves[rand.Intn(len(bestMoves))]
	log.Printf("Best move is %s with score %d\n", bestMove.String(), bestScore)
	return bestMove, nil
}

func (m *MiniMax) scoreMove(ctx context.Context, position *chess.Position, depth int) int {
	if depth == 0 {
		score := evalBoard(position)
		if position.Turn() == chess.White {
			score *= -1
		}
		return score
	}
	bestScore := math.MinInt32
	for _, move := range position.ValidMoves() {
		score := m.scoreMove(ctx, position.Update(move), depth-1) * -1
		if score > bestScore {
			bestScore = score
		}
	}
	return bestScore
}
