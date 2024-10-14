package main

import (
	"fmt"
	"github.com/ilinum/chess/pkg/bot"
	"github.com/ilinum/chess/pkg/uci"
	"net"
)

func main() {
	listener, err := net.Listen("tcp", ":8080")
	if err != nil {
		fmt.Println("Error starting server:", err)
		return
	}
	defer listener.Close()

	fmt.Println("Server listening on port 8080")

	for {
		conn, err := listener.Accept()
		if err != nil {
			fmt.Println("Error accepting connection:", err)
			continue
		}

		go func() {
			server := uci.NewServer(conn, conn, bot.Bots["random"])
			err := server.Serve()
			if err != nil {
				fmt.Println("Error serving request connection:", err)
			}
		}()
	}
}
