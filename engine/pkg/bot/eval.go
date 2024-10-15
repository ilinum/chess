package bot

import (
	"github.com/notnil/chess"
	"math"
)

func evalBoard(board *chess.Position) int {
	if board.Status() == chess.Checkmate {
		if board.Turn() == chess.White {
			return math.MaxInt32
		}
		return math.MinInt32
	}
	board.Turn()
	squares := board.Board().SquareMap()
	values := map[chess.PieceType]int{
		chess.Queen:  100,
		chess.Rook:   50,
		chess.Bishop: 30,
		chess.Knight: 30,
		chess.Pawn:   10,
	}
	result := 0
	for _, piece := range squares {
		val := values[piece.Type()]
		if piece.Color() == chess.Black {
			val *= -1
		}
		result += val
	}

	return result
}
