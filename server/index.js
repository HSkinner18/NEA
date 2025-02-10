var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
require('events').EventEmitter.defaultMaxListeners = 100;
var gameRooms = {};

server.listen(8080, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket) {
    console.log("Player Connected!");

    socket.on('playerMoved', function(data) {
        let roomCode = data.roomCode;
        data.id = socket.id;

        let room = gameRooms[roomCode];
        if (room) {
            for (let i = 0; i < room.players.length; i++) {
                if (room.players[i].id === data.id) {
                    room.players[i].x = data.x;
                    room.players[i].y = data.y;
                    break;
                }
            }

            io.to(roomCode).emit('playerMoved', data);
        }
    });

    socket.on('cueballMoved', function(data) {
        let roomCode = data.roomCode;
        let room = gameRooms[roomCode];

        if (!room) {
            console.error("Room not found:", roomCode);
            return;
        }
        if (!room.cueball) {
            console.error("Cueball not initialized in room:", roomCode);
            return;
        }

        try {
            room.cueball.x = data.x;
            room.cueball.y = data.y;

            let cueballData = {
                id: "cueball",
                x: data.x,
                y: data.y,
                roomCode: roomCode
            };

            socket.broadcast.to(roomCode).emit('cueballMoved', cueballData);
        } catch (error) {
            console.error("Error updating cueball position:", error);
        }
    });

   socket.on('ballsMoved', function(data) {
        console.log("Received ballsMoved event", data);

        let roomCode = data.roomCode;
        let room = gameRooms[roomCode];

        if (!room) {
            console.error("Room not found:", roomCode);
            return;
        }

        if (!room.balls) {
            console.error("Balls array not initialized in room:", roomCode);
            return;
        }

        console.log("Before update - room.balls:", room.balls);
        console.log("Data received for balls:", data.balls);

        try {
            // Ensure data.balls is not undefined
            if (!data.balls) {
                console.error("data.balls is undefined!");
                return;
            }

            // Update positions of all balls
            room.balls = data.balls;

            console.log("After update - room.balls:", room.balls);

            // Add unique IDs to each ball
            let ballsData = room.balls.map((ball, index) => ({
                id: "ball" + index,
                x: ball.x,
                y: ball.y
            }));

            console.log("Emitting ballsMoved:", ballsData);

            // Emit the updated positions of all balls
            io.to(roomCode).emit('ballsMoved', { balls: ballsData });
        } catch (error) {
            console.error("Error updating balls positions:", error);
        }
    });



    socket.on('createGame', function(callback) {
        let roomCode = generateRoomCode();
        gameRooms[roomCode] = {
            players: [],
            cueball: { x: 100, y: 200 },
            balls: Array(15).fill().map(() => ({ x: 100, y: 200 })) // Initialize balls array with default positions
        };

        socket.join(roomCode);

        let newPlayer = new Player(socket.id, 0, 0);
        gameRooms[roomCode].players.push(newPlayer);

        console.log(`Room created: ${roomCode}`);
        callback(roomCode);
    });

    socket.on('joinGame', function(roomCode, callback) {
        if (gameRooms[roomCode]) {
            socket.join(roomCode);
            let newPlayer = new Player(socket.id, 0, 0);
            gameRooms[roomCode].players.push(newPlayer);

            console.log(`Player ${socket.id} joined room ${roomCode}`);

            let playersToSend = gameRooms[roomCode].players.filter(p => p.id !== socket.id);
            socket.emit('getPlayers', playersToSend);
            socket.emit('getCueball', gameRooms[roomCode].cueball);

            if (gameRooms[roomCode].balls) {
                socket.emit('getBalls', gameRooms[roomCode].balls); // Send all balls
            } else {
                console.error("Balls array is not initialized!");
            }

            socket.broadcast.to(roomCode).emit('newPlayer', {
                id: socket.id,
                x: newPlayer.x,
                y: newPlayer.y
            });

            callback({ success: true });
        } else {
            callback({ success: false, message: 'Room does not exist' });
        }
    });


    socket.on('disconnect', function() {
        console.log("Player Disconnected");

        for (let roomCode in gameRooms) {
            let room = gameRooms[roomCode];
            for (var i = 0; i < room.players.length; i++) {
                if (room.players[i].id === socket.id) {
                    room.players.splice(i, 1);
                    socket.broadcast.to(roomCode).emit('playerDisconnected', { id: socket.id });
                    break;
                }
            }

            if (room.players.length === 0) {
                delete gameRooms[roomCode];
                console.log(`Room ${roomCode} deleted because it is empty`);
            }
        }
    });
});

function generateRoomCode() {
    return Math.random().toString(36).substr(2, 5).toUpperCase();
}

function Player(id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}
