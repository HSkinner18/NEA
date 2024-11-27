var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var gameRooms = {};

server.listen(8080, function(){
    console.log("Server is now running...");
});

app.get('/', (req, res) => {
  res.send('<h1>Pool Server</h1>');
});

io.on('connection', function(socket){
    console.log("Player Connected!");

    socket.on('playerMoved', function(data) {
    let roomCode = data.roomCode;
    data.id = socket.id;  // Ensure the data contains the correct player ID

    // Update this player's position in the server's room state
    let room = gameRooms[roomCode];
    if (room) {
        for (let i = 0; i < room.players.length; i++) {
            if (room.players[i].id === data.id) { // Match player by ID
                room.players[i].x = data.x;
                room.players[i].y = data.y;
                break;
            }
        }

        // Broadcast movement to other players in the room
        socket.broadcast.to(roomCode).emit('playerMoved', data);
    }
});

    socket.on('CueballMoved', function(data){
	let roomCode = data.roomCode;
	data.id = socket.id;

	let room = gameRooms[roomCode];
	if (room) {
		for(let i = 0; i < room.Cueballs.length; i++) {
			if(room.Cueballs[i].id === data.id) {
				room.Cueballs[i].x = data.x
				room.Cueballs[i].y = data.y
				break;
			}
		}

		socket.broadcast.to(roomCode).emit('CueballMoved', data);
	}
    
});

    socket.on('createGame', function(callback){
    	let roomCode = generateRoomCode();
    	gameRooms[roomCode] = {
    		players: [],
    		cueballs: []
    	};
    	socket.join(roomCode);
    	console.log(`Room created: ${roomCode}`);
    	callback(roomCode);
    });

    socket.on('joinGame', function(roomCode, callback) {
    if (gameRooms[roomCode]) {
        socket.join(roomCode);
        let newPlayer = new Player(socket.id, 0, 0);
        let newCueball = new Cueball(socket.id, 0, 0);
        gameRooms[roomCode].players.push(newPlayer);
        gameRooms[roomCode].cueballs.push(newCueball);
        console.log(`Player ${socket.id} joined room ${roomCode}`);

        // Send the full list of players to the new player
        socket.emit('getPlayers', gameRooms[roomCode].players);
        socket.emit('getCueballs', gameRooms[roomCode].cueballs);

        // Notify other players in the room about the new player
        socket.broadcast.to(roomCode).emit('newPlayer', { id: socket.id, x: newPlayer.x, y: newPlayer.y });

        callback({ success: true });
    } else {
        callback({ success: false, message: 'Room does not exist' });
    }
});

    socket.on('requestPlayers', function(roomCode) {
        if (gameRooms[roomCode]) {
            socket.emit('getPlayers', gameRooms[roomCode].players); // Send the player list to the new player
            socket.broadcast.to(roomCode).emit('newPlayer', { id: socket.id }); // Notify others in the room of the new player
        }
    });


    socket.on('disconnect', function() {
        console.log("Player Disconnected");
        
        // Find the room where the player was and remove them
        for (let roomCode in gameRooms) {
            let room = gameRooms[roomCode];
            for (var i = 0; i < room.players.length; i++) {
                if (room.players[i].id === socket.id) {
                    room.players.splice(i, 1); // Remove the player from the room
                    socket.broadcast.to(roomCode).emit('playerDisconnected', { id: socket.id }); // Notify others in the room
                    break;
                }
            }

            // If the room is empty, delete it
            if (room.players.length === 0) {
                delete gameRooms[roomCode];
                console.log(`Room ${roomCode} deleted because its empty`);
            }
        }
    });

});

function generateRoomCode(){
	return Math.random().toString(36).substr(2, 5).toUpperCase();
}

function Player(id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}

function Cueball(id, x, y) {
	this.id = id
	this.x = x;
	this.y = y;
}  