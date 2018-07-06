var express = require("express");
var mysql = require('mysql');
var app = express();
app.use(express.static(__dirname + '/'));
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
server.listen(process.env.PORT || 3000);

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database: "chatwithstranger"
});


io.sockets.on('connection', function (socket) {
	
  console.log("NOTICE: NEW USER CONNECTED! "+socket.id);

    socket.on('CLIENT_SEND_USER_PASS_LOGIN', function(data){
    //socket.un = data;
  	checkUserAndPass(data,socket);
  });

    socket.on('CLIENT_SEND_USER_CHECK_EXISTS', function(data){
    //socket.un = data;
  	checkUserExists(data,socket);
  });

    socket.on('CLIENT_SEND_AVATAR', function(data){
    //socket.un = data;
  	upImageToServer(data,socket);
  });

    socket.on('CLIENT_SEND_REQUEST_REGISTER', function(data){
    //socket.un = data;
  	registerUser(data,socket);
  });

  
});

/////////////////////////////////////////////////////////////

function checkUserAndPass(userPass,socket)
{
	var arr = userPass.split("-");
  con.query("SELECT * FROM `tblusers` WHERE USER = '"+arr[0]+"' AND PASSWORD = '"+arr[1]+"'", function (err, result, fields) {
    if (err) throw err;
    socket.emit('SERVER_SEND_RESULT_LOGIN',result);
    console.log(result);
    if (result.length == 1) {
    	con.query("UPDATE `tblusers` SET `IS_ACTIVE` = 1 WHERE `USER` = '"+arr[0]+"'", function (err, result, fields) {
    	if (err) throw err;});
    }
  });
	//console.log(userPass);
}

function checkUserExists(user,socket){
	console.log(user);
	con.query("SELECT * FROM `tblusers` WHERE USER = '"+user+"'", function (err, result, fields) {
    if (err) throw err;
    	socket.emit('SERVER_SEND_RESULT_CHECK_EXISTS',result);
  });
}

function getFileNameImage(id)
{
  return "images/avatar/" + id.substring(2) + getMilis() + ".png";
}

function getMilis()
{
  var date = new Date();
  var milis = date.getTime();
  return milis;
}

function upImageToServer(arrByte, socket){
	console.log(arrByte);
	if (arrByte == null) {
		socket.emit('SERVER_SEND_RESULT_AVATAR',"null");
		//console.log(arrByte);
	}else{
		var url = getFileNameImage(socket.id);
	  	fs.writeFile(url, arrByte, function(err) {
	    if(err) {
	        return console.log(err);
	    }else{
	    	socket.emit('SERVER_SEND_RESULT_AVATAR',"/"+url);
	    	//console.log(url);
	    }
	  });
	}
}

function registerUser(data,socket){
	var arr = data.split("-");
	console.log(data);
	con.query("SELECT * FROM `tblusers` WHERE USER = '"+arr[0]+"'", function (err, result, fields) {
    if (err) throw err;
    	console.log(result.length);
    	if (result.length == 0) {
    		con.query("INSERT INTO `tblusers` VALUES (null,'"+arr[0]+"','"+arr[1]+"','','"+arr[2]+"','',"+arr[3]+",0,'"+arr[4]+"','')", function (err, result, fields) {
		    if (err){
		    	socket.emit('SERVER_SEND_RESULT_REGISTER',"false");
		    	console.log("false");
		    }else{
		    	socket.emit('SERVER_SEND_RESULT_REGISTER',"true");
		    	console.log("true");
		    }	
		  });
    	}
  });




 	// con.query(data, function (err, result, fields) {
  //   if (err) throw err;
  //   	socket.emit('SERVER_SEND_RESULT_REGISTER',result);
  // });
}
