'use strict';
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');

var stompClient = null;
var username = null;
var email =null;
var user=null;
var pictureURL = null;
var startUpName = $('#startUpName').val().trim();
var today = null;
var dateFormat = null;
console.log(startUpName);

function connect() {
    username = $('#userLogin').val().trim();
    email = $('#userEmail').val().trim();
    pictureURL = $('#userPhoto').val().trim();
    console.log(username);
    var socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);

}

connect();

function onConnected() {

    // Subscribe to the startup topic
    stompClient.subscribe('/topic/private.' + startUpName, onMessageReceived);
    // User registration on the server
    stompClient.send("/app/" + startUpName + "/chat.register",
        {},
        JSON.stringify({
            sender: {
               login: username,
               email: email,
               pictureURL: pictureURL
            }, type: 'ONLINE',
            chat:startUpName

        })
    );
    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        today = new Date();

        dateFormat = today.getDate() +"/" + (today.getMonth()+1) + "/" + today.getFullYear();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        var dateTime = dateFormat+' '+time;
        var chatMessageDTO = {
            date: dateTime,
            sender: {
                login: username,
                email: email,
                pictureURL: pictureURL
            },
            content: messageInput.value,
            type: 'CHAT',
            chat: startUpName
        };
        stompClient.send("/app/" + startUpName + "/chat.send", {}, JSON.stringify(chatMessageDTO));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    if (message.type === 'ONLINE') {
        messageElement.classList.add('event-message');
        message.content = message.sender.login + ' is online!';
    } else if (message.type === 'OFFLINE') {
        messageElement.classList.add('event-message');
        message.content = message.sender.login + ' is offline!';
    } else {
        messageElement.classList.add('chat-message');
        var photoURL = message.sender.pictureURL;
        var avatarContent = document.createElement("img");
        avatarContent.setAttribute("src", photoURL);
        avatarContent.setAttribute("onerror", "if (this.src!='/icons/default.png') this.src = '/icons/default.png';");
        avatarContent.classList.add('dimensions');

        messageElement.appendChild(avatarContent);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender.login);
        var userDate = document.createElement('div');

        userDate.classList.add('date');
        var userDateText = document.createTextNode(message.date);
        userDate.appendChild(userDateText);
        var space = document.createElement('br');
        usernameElement.appendChild(usernameText);
        usernameElement.appendChild(userDate);
        messageElement.appendChild(usernameElement);
        messageElement.append(space);

    }


    var s = document.createElement('br');
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    messageElement.append(s);


    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit', sendMessage, true);