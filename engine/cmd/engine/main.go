package main

import (
	"flag"
	"fmt"
	"github.com/ilinum/chess/pkg/bot"
	"github.com/ilinum/chess/pkg/uci"
	"log"
	"maps"
	"net"
	"os"
	"slices"
	"strings"
)

func main() {
	var port int
	var strategy string
	flag.IntVar(&port, "port", 1337, "tcp port to listen on")
	strategies := strings.Join(slices.Collect(maps.Keys(bot.Bots)), ", ")
	flag.StringVar(
		&strategy,
		"strategy",
		"minimax",
		fmt.Sprintf(
			"which strategy to use, allowed values: %s",
			strategies,
		),
	)
	flag.Parse()
	if _, ok := bot.Bots[strategy]; !ok {
		fmt.Printf("unknown strategy %s, allowed strategies: %s\n", strategy, strategies)
		os.Exit(-1)
	}
	listener, err := net.Listen("tcp", fmt.Sprintf(":%d", port))
	if err != nil {
		fmt.Println("Error starting server:", err)
		return
	}
	defer listener.Close()

	log.Printf("Server listening on port %d\n", port)

	for {
		conn, err := listener.Accept()
		if err != nil {
			log.Printf("Error accepting connection: %v\n", err)
			continue
		}

		go func() {
			server := uci.NewServer(conn, conn, bot.Bots[strategy])
			err := server.Serve()
			if err != nil {
				log.Printf("Error serving request connection: %v\n", err)
			}
		}()
	}
}
