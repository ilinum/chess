# Chess

An implementation of chess in Java


<img src="https://cloud.githubusercontent.com/assets/5924452/10867908/4d93ba0c-808c-11e5-885f-b7b366533b3f.png" width="50%" height="50%"/>


# Running
To run just the UI, with two human players at the same computer:
```bash
$ ./gradlew runUI
```


## Playing against the chess engine

If you'd like to play against a chess engine, you need to **install Docker**.
The rest of the instructions assume you have docker installed and running.

In order to play against the chess engine, you need to:
```bash
$ ./gradlew startEngine 
# Engine starts on port 1337.
$ ./gradlew runUI --args="--engine-port-black 1337"
# Starts the UI with the engine playing black and human playing white.
```
Note that you can play against any engine that supports the [Universal Chess Interface](https://gist.github.com/DOBRO/2592c6dad754ba67e6dcaec8c90165bf) protocol.


If you'd like to run see two chess engines  play against each other:
```bash
$ ./gradlew startEngine
# Starts our engine.
$ docker run -d -p 1338:8080 damon/chess-engine
# Starts stockfish engine on port 1338.
$ ./gradlew runUI --args="--engine-port-black 1337 --engine-port-white 1338"
# Watch our engine get crushed by stockfish.
```
