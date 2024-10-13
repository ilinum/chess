package main

import (
	"github.com/ilinum/chess/pkg/bot"
	"github.com/ilinum/chess/pkg/uci"
	"os"
)

func main() {
	server := uci.NewServer(os.Stdin, os.Stdout, bot.Bots["random"])
	err := server.Serve()
	if err != nil {
		panic(err)
	}
}
