package uci

import (
	"bufio"
	"context"
	"errors"
	"fmt"
	"github.com/ilinum/chess/pkg/bot"
	"github.com/notnil/chess"
	"io"
	"strings"
)

type Server struct {
	scanner  *bufio.Scanner
	writer   *bufio.Writer
	notation *chess.UCINotation
	bot      bot.Bot
}

func NewServer(in io.Reader, out io.Writer, bot bot.Bot) *Server {
	return &Server{
		scanner:  bufio.NewScanner(in),
		writer:   bufio.NewWriter(out),
		bot:      bot,
		notation: &chess.UCINotation{},
	}
}

func (s *Server) Serve() error {
	err := s.performStartupProtocol()
	if err != nil {
		return err
	}
	for s.scanner.Scan() {
		posFields := strings.Fields(s.scanner.Text())
		if len(posFields) < 1 || posFields[0] != "position" {
			continue
		}
		if len(posFields) < 2 || posFields[1] != "startpos" {
			return errors.New("only allowed to start from position startpos, got: " + s.scanner.Text())
		}
		pos := chess.StartingPosition()
		if len(posFields) >= 3 {
			if posFields[2] != "moves" {
				return errors.New(
					"invalid position command, expected 'position startpos moves', got: " + s.scanner.Text(),
				)
			}
			for _, moveStr := range posFields[3:] {
				move, err := s.notation.Decode(pos, moveStr)
				if err != nil {
					return err
				}
				pos = pos.Update(move)
			}
		}

		if !s.scanner.Scan() {
			return s.scanner.Err()
		}
		if !strings.HasPrefix(s.scanner.Text(), "go") {
			return errors.New("expected 'go.*, got: " + s.scanner.Text())
		}

		move, err := s.bot.GetMove(context.Background(), pos)
		if err != nil {
			return err
		}

		_, _ = s.writer.WriteString(fmt.Sprintf("bestmove %s\n", s.notation.Encode(pos, move)))
		_ = s.writer.Flush()
	}

	return s.scanner.Err()
}

func (s *Server) performStartupProtocol() error {
	if !s.scanner.Scan() {
		return s.scanner.Err()
	}
	if s.scanner.Text() != "uci" {
		return errors.New("expected 'uci' command, got: " + s.scanner.Text())
	}

	_, _ = s.writer.WriteString("id name Chess Engine\n")
	_, _ = s.writer.WriteString("id author Stas Ilinskiy\n")
	_, _ = s.writer.WriteString("uciok\n")
	_ = s.writer.Flush()
	for s.scanner.Text() != "isready" {
		if !s.scanner.Scan() {
			return s.scanner.Err()
		}
	}
	_, _ = s.writer.WriteString("readyok\n")
	_ = s.writer.Flush()
	return nil
}
