var stompClient = null;
var playerId = -1;
var joinedGame = -1;
var status = '';
var player = '';
var resultSet = null;
var started = false;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#game_messages").html("");
}

function connect(game_id) {
    console.log('connect with game_id ' + game_id);
    var socket = new SockJS('localhost:8080/messagepoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/move/' + joinedGame, function (message) {
            console.log('subscribe message ' + message);
            showMessage(JSON.parse(message.body));
        });
    });
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage(playerId, gameId, actionValue, isStart) {
    console.log("sending message");
    // stompClient.send("/app/room", {}, JSON.stringify({'name': $("#name").val()}));
    stompClient.send("/app/room/" + joinedGame, {}, JSON.stringify({
        'playerId': playerId,
        'gameId': gameId,
        'actionValue': actionValue,
        'isStart': isStart
    }));
}

function showMessage(message) {
    var p1 = player == 'player1' ? true : false;
    if(message.errorMessage){
        $("#game_messages").prepend("<tr><td>" + message.gameMessage + "</td></tr>");
        var yourTurn = p1 && status == "P1_MOVE" || !p1 && status == "P2_MOVE";
        if (yourTurn) {
            $('#move').prop("disabled", false);
        }
        return;
    }
    if (message.status) {
        status = message.status;
        if (!started && p1 && status == "P1_MOVE") {
            $('#game_view').show();
            $('#game_log').show();
            $('#move_list_view').show();
            $('#status_text').text('Game Started.');
            $('#create_view').hide();
            started = true;
        }
        var yourTurn = p1 && status == "P1_MOVE" || !p1 && status == "P2_MOVE";
        if (yourTurn) {
            $('#move').prop("disabled", false);
        }
        if (status == "P1_WINS" || status == 'P2_WINS') {
            $('#move').prop("disabled", true);
            var string = "";
            if (p1) {
                string = status == "P1_WINS" ? "You have Won!" : "You have lost."
            }
            else {
                string = status == "P2_WINS" ? "You have Won!" : "You have lost."
            }
            $('#status_text').text(string);
        }
        else if (status == "TIE") {
            $('#move').prop("disabled", true);
            $('#status_text').text("Tie Game");
        }
    }
    else {
        $('#status_text').text('An error occurred');
        return;
    }
    if (message.resultSet) {
        resultSet = message.resultSet;
        var start = p1 ? 0 : 7;
        var end = p1 ? 6 : 13;
        var k = 1;
        for (var i = start; i < end; i++) {
            var opPitNumber = (i + 7) % 14;
            $('#pit' + k + '').val(message.resultSet[i]);
            $('#op_pit' + k + '').val(message.resultSet[opPitNumber]);
            k = k + 1;
        }
        var yourKalah = p1 ? 6 : 13;
        var opKalah = p1 ? 13 : 6;
        $('#kalah').val(message.resultSet[yourKalah]);
        $('#op_kalah').val(message.resultSet[opKalah]);
    }

    console.log('status: ' + status);
    $("#game_messages").prepend("<tr><td>" + message.gameMessage + "</td></tr>");
}
function createPlayer() {
    console.log($("#username").val());
    var json = JSON.stringify({'username': $("#username").val()});
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/game/player',
        contentType: "application/json; charset=utf-8",
        data: json,
        success: function (data, textStatus, jqXHR) {
            console.log("success " + data.id + " , text status :" + textStatus);
            playerId = Number(data.id);
            $("#game_join").show();
            $("#create_view").show();
            $("#create_player_view").hide();
            $('#status_text').text('Good. Now create a game or join a game.');

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("An Error Occured.");
            console.log("failed " + errorThrown + " jq : " + jqXHR + " textstatus " + textStatus);
        },
        dataType: 'json'
    });
}

function createGame() {
    var json = JSON.stringify({'name': $("#game_name").val(), 'createdBy': playerId});
    console.log(json);
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/game/create',
        contentType: "application/json; charset=utf-8",
        data: json,
        success: function (data, textStatus, jqXHR) {
            console.log("success " + data.id + " , text status :" + textStatus);
            joinedGame = Number(data.id);
            connect(joinedGame);
            player = 'player1';
            $("#game_join").hide();
            $("#create").prop('disabled', true);
            $('#player_text').text('Player 1');
            $('#status_text').text('Game created. You are Player 1. Waiting for Player 2 to join...');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('#status_text').text('Game cannot be created.');
            console.log("failed " + errorThrown + " jq : " + jqXHR + " textstatus " + textStatus);
        },
        dataType: 'json'
    });

}
function listGames() {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080/game/list',
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            var options = [];
            if (data.length < 1) {
                $("#join").prop("disabled", true);
                $('#status_text').text('There is no available game. Please create one.');
                return;
            }
            for (i = 0; i < data.length; i++) {
                options.push("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
            }
            $('#game_list').find('option').remove().end();
            $('#game_list').append(options.join(""));
            $("#join").prop("disabled", false);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("failed " + errorThrown + " jq : " + jqXHR + " textstatus " + textStatus);
        },
        dataType: 'json'
    });

}

function joinGame() {
    console.log($("#game_list").val() + ' selected');
    var gameId = Number($("#game_list").val());
    var json = JSON.stringify({'gameId': gameId, 'playerId': playerId});
    console.log(json);
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/game/join',
        contentType: "application/json; charset=utf-8",
        data: json,
        success: function (data, textStatus, jqXHR) {
            console.log("joined");
            joinedGame = gameId;
            connect(gameId);
            player = 'player2';
            $('#player_text').text('Player 2');
            $("#create_view").hide();
            $("#start_view").show();
            $('#status_text').text('Joined into game. You are Player 2. Please start the game.');

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('An error occurred');
            console.log("failed " + errorThrown + " jq : " + jqXHR + " textstatus " + textStatus);
        },
        dataType: 'json'
    });
}
function move() {
    var actionNumber = Number($("#move_list").val());
    var isP1 = player == 'player1';
    if (status == 'P1_WINS' || status == 'P2_WINS' || status == 'TIE') {
        alert('Game Finished! ' + status);
    }
    else if (isP1 && status != 'P1_MOVE' || !isP1 && status != 'P2_MOVE') {
        alert('Not your turn!');
        $('#status_text').text('Not your turn!');
    }
    else {
        if (!isP1) {
            actionNumber = actionNumber + 7;
        }
        sendMessage(playerId, joinedGame, actionNumber, false);
        $("#move").prop("disabled", true);
    }
}

function startGame() {
    sendMessage(playerId, joinedGame, 0, true);
    if (!started) {
        $("#create_view").hide();
        $("#game_join").hide();
        $('#game_view').show();
        $('#game_log').show();
        $('#move_list_view').show();
        $("#start_view").hide();
        $('#status_text').text('Game Started.');
        started = true;
    }
    $("#move").prop("disabled", true);
}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#register").click(function () {
        createPlayer();
    });
    $("#create").click(function () {
        createGame();
    });
    $("#list").click(function () {
        listGames();
    });
    $("#join").click(function () {
        joinGame();
    });
    $("#start").click(function () {
        startGame();
    });
    $("#move").click(function () {
        move();
    });
});

