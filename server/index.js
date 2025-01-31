var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var gameRooms = {};

server.listen(8080, function(){
    console.log("Server is now running...");
});


// Handle client connections
io.on('connection', function(socket) {
    console.log("Player Connected!");

    // Player movement handler
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

	console.log("Received cueballMoved event", data);

    let roomCode = data.roomCode
    let room = gameRooms[roomCode];

    console.log(room);
       
   

	
	room.Cueball.x = data.x
	room.Cueball.y = data.y


	console.log("Emitting cueballMoved:", cueballData);
	socket.broadcast.to(roomCode).emit('cueballMoved', cueballData);

   
  
	});

    // Create a new game room
    socket.on('createGame', function(callback) {
        let roomCode = generateRoomCode();
        gameRooms[roomCode] = {
            players: [],
            Cueball: {
            	x: 100,
            	y: 200
            }
        };
        socket.join(roomCode);

        let newPlayer = new Player(socket.id, 0, 0);
        gameRooms[roomCode].players.push(newPlayer);


        console.log(`'Room created: ${roomCode}`);
        callback(roomCode);
    });

    // Join an existing game room
    socket.on('joinGame', function(roomCode, callback) {
        if (gameRooms[roomCode]) {
            socket.join(roomCode);
            let newPlayer = new Player(socket.id, 0, 0);
            gameRooms[roomCode].players.push(newPlayer);

             

            console.log(`Player ${socket.id} joined room ${roomCode}`);

           
           let playersToSend = gameRooms[roomCode].players.filter(p => p.id !== socket.id);
            socket.emit('getPlayers',playersToSend);
            socket.emit('getCueballs', gameRooms[roomCode].cueball);

            
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

    // Request player list
    socket.on('requestPlayers', function(roomCode) {
        if (gameRooms[roomCode]) {
            socket.emit('getPlayers', gameRooms[roomCode].players);
            socket.broadcast.to(roomCode).emit('newPlayer', { id: socket.id });
        }
    });

    // Handle disconnections
    socket.on('disconnect', function() {
        console.log("Player Disconnected");

        
        for (let roomCode in gameRooms) {
            let room = gameRooms[roomCode];
            for (var i = 0; i < room.players.length; i++) {
                if (room.players[i].id === socket.id) {
                    room.players.splice(i, 1); // Remove the player from the room
                    socket.broadcast.to(roomCode).emit('playerDisconnected', { id: socket.id }); // Notify others in the room
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

function Cueball(id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}

