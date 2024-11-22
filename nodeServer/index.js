const express = require('express');
const axios = require('axios');
const WebSocket = require('ws');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();
const PORT = 5000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Replace with your external API base URL and WebSocket URL
const EXTERNAL_API_URL = "wss://ws.finnhub.io?token=csomuhpr01qt3r34doe0csomuhpr01qt3r34doeg";
const EXTERNAL_WEBSOCKET_URL = "wss://example.com/socket";

// Global WebSocket connection to external API
let externalWs;
let clientConnections = new Set();

// Establish WebSocket Connection to External API
const connectToExternalWebSocket = () => {
  externalWs = new WebSocket(EXTERNAL_WEBSOCKET_URL);

  externalWs.on('open', () => {
    console.log('Connected to External WebSocket');
  });

  externalWs.on('message', (message) => {
    const data = JSON.parse(message);

    // Broadcast data to all connected frontend clients
    clientConnections.forEach((client) => {
      client.send(JSON.stringify(data));
    });
  });

  externalWs.on('close', () => {
    console.log('External WebSocket disconnected. Reconnecting...');
    setTimeout(connectToExternalWebSocket, 5000); // Retry connection
  });

  externalWs.on('error', (error) => {
    console.error('WebSocket Error:', error.message);
  });
};

// Start WebSocket Connection
connectToExternalWebSocket();

// Routes
app.get('/api/external-data', async (req, res) => {
  try {
    const response = await axios.get(`${EXTERNAL_API_URL}/data`, {
      params: req.query, // Pass query parameters if required
    });
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/subscribe', (req, res) => {
  const { symbols } = req.body;

  if (externalWs && externalWs.readyState === WebSocket.OPEN) {
    externalWs.send(JSON.stringify({ action: 'subscribe', symbols }));
    res.status(200).send('Subscribed successfully');
  } else {
    res.status(500).send('WebSocket not connected');
  }
});

// WebSocket Server for Frontend
const wss = new WebSocket.Server({ noServer: true });

wss.on('connection', (client) => {
  clientConnections.add(client);
  console.log('Frontend client connected.');

  client.on('close', () => {
    clientConnections.delete(client);
    console.log('Frontend client disconnected.');
  });
});

// Upgrade HTTP to WebSocket for frontend
const server = app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

server.on('upgrade', (request, socket, head) => {
  wss.handleUpgrade(request, socket, head, (ws) => {
    wss.emit('connection', ws, request);
  });
});
