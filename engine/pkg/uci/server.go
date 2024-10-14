package uci

import (
	"bufio"
	"context"
	"errors"
	"fmt"
	"github.com/ilinum/chess/pkg/bot"
	"github.com/notnil/chess"
	"io"
	"log"
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
		msg := s.read()
		posFields := strings.Fields(msg)
		if len(posFields) < 1 || posFields[0] != "position" {
			continue
		}
		if len(posFields) < 2 || posFields[1] != "startpos" {
			return errors.New("only allowed to start from position startpos, got: " + msg)
		}
		pos := chess.StartingPosition()
		if len(posFields) >= 3 {
			if posFields[2] != "moves" {
				return errors.New(
					"invalid position command, expected 'position startpos moves', got: " + msg,
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
		msg = s.read()
		if !strings.HasPrefix(msg, "go") {
			return errors.New("expected 'go.*, got: " + msg)
		}

		move, err := s.bot.GetMove(context.Background(), pos)
		if err != nil {
			return err
		}

		s.write(fmt.Sprintf("bestmove %s", s.notation.Encode(pos, move)))
	}

	return s.scanner.Err()
}

func (s *Server) performStartupProtocol() error {
	if !s.scanner.Scan() {
		return s.scanner.Err()
	}
	msg := s.read()
	if msg != "uci" {
		return errors.New("expected 'uci' command, got: " + msg)
	}

	s.write("id name Chess Engine")
	s.write("id author Stas Ilinskiy")
	s.write("uciok")
	for s.read() != "isready" {
		if !s.scanner.Scan() {
			return s.scanner.Err()
		}
	}
	s.write("readyok")
	return nil
}

func (s *Server) write(msg string) {
	_, _ = s.writer.WriteString(msg + "\n")
	_ = s.writer.Flush()
	log.Printf("[uci][snd] %s\n", msg)
}

func (s *Server) read() string {
	msg := s.scanner.Text()
	log.Printf("[uci][rcv] %s\n", msg)
	return msg
}
