package main

import (
	"flag"
	"fmt"
	"github.com/ilinum/chess/pkg/bot"
	"github.com/ilinum/chess/pkg/uci"
	"log"
	"net"
)

func main() {
	var port int
	flag.IntVar(&port, "port", 1337, "tcp port to listen on")
	flag.Parse()
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
			server := uci.NewServer(conn, conn, bot.Bots["random"])
			err := server.Serve()
			if err != nil {
				log.Printf("Error serving request connection: %v\n", err)
			}
		}()
	}
}
