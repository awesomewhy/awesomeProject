const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/chat', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
};
export function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function sendMessage(content) {
    stompClient.send("/app/message", {}, JSON.stringify({ content: content }));
}

export function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

export function sendName() {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

export function connect() {
    stompClient.activate();
}