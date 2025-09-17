const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

app.use(express.static('public'));

io.on('connection', (socket) => {
    console.log('Client connected');

    socket.on('data', (data) => {
        console.log('Received data:', data);
        // Broadcast to all clients
        io.emit('update', data);
    });

    socket.on('force_open', () => {
        // Send to Android
        io.emit('force_open');
    });

    socket.on('disconnect', () => {
        console.log('Client disconnected');
    });
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});