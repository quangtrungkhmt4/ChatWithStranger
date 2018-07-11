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

  //   socket.on('CLIENT_SEND_USER_CHECK_EXISTS', function(data){
  //   //socket.un = data;
  // 	checkUserExists(data,socket);
  // });

    socket.on('CLIENT_SEND_AVATAR', function(data){
    //socket.un = data;
  	upImageToServer(data,socket);
  });


    socket.on('CLIENT_SEND_REQUEST_REGISTER', function(data){
    //socket.un = data;
  	registerUser(data,socket);
  });

    socket.on('CLIENT_SEND_NEW_NAME', function(data){
    //socket.un = data;
    changeName(data,socket);
  });

    socket.on('CLIENT_SEND_NEW_PASS', function(data){
    //socket.un = data;
    changePass(data,socket);
  });

    socket.on('CLIENT_SEND_NEW_EMAIL', function(data){
    //socket.un = data;
    changeEmail(data,socket);
  });

    socket.on('CLIENT_SEND_NEW_PHONE', function(data){
    //socket.un = data;
    changePhone(data,socket);
  });

    socket.on('CLIENT_SEND_NEW_GENDER', function(data){
    //socket.un = data;
    changeGender(data,socket);
  });

    socket.on('LOGOUT', function(data){
    //socket.un = data;
    logout(data,socket);
  });

    socket.on('CLIENT_GET_FRIENDS', function(data){
    //socket.un = data;
    getFriends(data,socket);
  });

    socket.on('CLIENT_GET_BLOCKS', function(data){
    //socket.un = data;
    getBlocks(data,socket);
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
	console.log(arrByte.bytes);
	if (arrByte.bytes == null) {
		socket.emit('SERVER_SEND_RESULT_AVATAR',"null");
		//console.log(arrByte);
	}else{
		var url = getFileNameImage(socket.id);
	  	fs.writeFile(url, new Buffer(arrByte.bytes), function(err) {
	    if(err) {
	        socket.emit('SERVER_SEND_RESULT_AVATAR',"null");
	    }else{
          con.query("UPDATE `tblusers` SET `IMAGE` = '/"+url+"' WHERE `IDUSER` = "+arrByte.id + "", function (err, result, fields) {
          if (err) {
            socket.emit('SERVER_SEND_RESULT_AVATAR',"null");
          }else{
            socket.emit('SERVER_SEND_RESULT_AVATAR',"/"+url);
          }

        });
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
    		con.query("INSERT INTO `tblusers` VALUES (null,'"+arr[0]+"','"+arr[1]+"','','"+arr[2]+"','',"+arr[3]+",0,'"+arr[4]+"','"+arr[5]+"')", function (err, result, fields) {
		    if (err){
		    	socket.emit('SERVER_SEND_RESULT_REGISTER',"false");
		    	console.log("false");
		    }else{
		    	socket.emit('SERVER_SEND_RESULT_REGISTER',"true");
		    	console.log("true");

          con.query("SELECT `IDUSER` FROM `tblusers` WHERE `USER` = '"+arr[0]+"'", function (err, result, fields) {
          if (err) throw err;

            var id = result[0].IDUSER;
            
            con.query("INSERT INTO `tblcontacts` VALUES (null,"+id+")", function (err, result, fields) {
            if (err) throw err;
              
          });

            con.query("INSERT INTO `tblreports` VALUES (null,"+id+")", function (err, result, fields) {
            if (err) throw err;
              
          });

        });

		    }	
		  });
    	}else{
          socket.emit('SERVER_SEND_RESULT_REGISTER',"false");
          console.log("false");
      }
  });

}

function changeName(data,socket){
  var arr = data.split("-");
  console.log(arr[0] + "----" + arr[1]);
    con.query("UPDATE `tblusers` SET `FULLNAME` = '"+arr[0]+"' WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CHANGE_NAME',"false");
    }else{
        con.query("SELECT `FULLNAME` FROM `tblusers` WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
        if (err) throw err;
          socket.emit('SERVER_SEND_RESULT_CHANGE_NAME',result[0].FULLNAME);
          console.log(result[0].FULLNAME);
      });
    }
  });
}

function changePass(data,socket){
  var arr = data.split("-");
  con.query("SELECT `PASSWORD` FROM `tblusers` WHERE `IDUSER` = " + arr[2], function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CHANGE_PASS',"false");
    }else{
      if (result[0].PASSWORD == arr[1]) {
          con.query("UPDATE `tblusers` SET `PASSWORD` = '"+arr[0]+"' WHERE `IDUSER` = "+arr[2] + "", function (err, result, fields) {
          if (err){
            socket.emit('SERVER_SEND_RESULT_CHANGE_PASS',"false");
          }else{
              con.query("SELECT `PASSWORD` FROM `tblusers` WHERE `IDUSER` = "+arr[2] + "", function (err, result, fields) {
              if (err) throw err;
                socket.emit('SERVER_SEND_RESULT_CHANGE_PASS',result[0].PASSWORD);
                console.log(result[0].PASSWORD);
            });
          } 
            
        });
      }else{
        socket.emit('SERVER_SEND_RESULT_CHANGE_PASS',"false");
      }
    }
  });
}

function changeEmail(data,socket){
  var arr = data.split("-");
  console.log(arr[0] + "----" + arr[1]);
    con.query("UPDATE `tblusers` SET `EMAIL` = '"+arr[0]+"' WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CHANGE_EMAIL',"false");
    }else{
        con.query("SELECT `EMAIL` FROM `tblusers` WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
        if (err) throw err;
          socket.emit('SERVER_SEND_RESULT_CHANGE_EMAIL',result[0].EMAIL);
          console.log(result[0].EMAIL);
      });
    }
  });
}

function changePhone(data,socket){
  var arr = data.split("-");
  console.log(arr[0] + "----" + arr[1]);
    con.query("UPDATE `tblusers` SET `PHONE` = '"+arr[0]+"' WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CHANGE_PHONE',"false");
    }else{
        con.query("SELECT `PHONE` FROM `tblusers` WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
        if (err) throw err;
          socket.emit('SERVER_SEND_RESULT_CHANGE_PHONE',result[0].PHONE);
          console.log(result[0].PHONE);
      });
    }
  });
}

function changeGender(data,socket){
  var arr = data.split("-");
  console.log(arr[0] + "----" + arr[1]);
    con.query("UPDATE `tblusers` SET `GENDER` = "+arr[0]+" WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CHANGE_GENDER',-1);
    }else{
        con.query("SELECT `GENDER` FROM `tblusers` WHERE `IDUSER` = "+arr[1] + "", function (err, result, fields) {
        if (err) throw err;
          socket.emit('SERVER_SEND_RESULT_CHANGE_GENDER',result[0].GENDER);
          console.log(result[0].GENDER);
      });
    }
  });
}

function logout(data,socket){
  con.query("UPDATE `tblusers` SET `IS_ACTIVE` = 0 WHERE `IDUSER` = "+data + "", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_LOGOUT',"false");
    }else{
      socket.emit('SERVER_SEND_RESULT_LOGOUT',"true");
    }   
  });
}

function getFriends(data,socket){
    con.query("SELECT `IDCONTACT` FROM `tblcontacts` WHERE `IDUSER` =  "+data+ "", function (err, result, fields) {
    if (err){

    }else{

        con.query("SELECT * FROM tblusers INNER JOIN tbluserscontact on tblusers.IDUSER = tbluserscontact.IDUSER WHERE tbluserscontact.IDCONTACT = "+result[0].IDCONTACT+ "", function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_RESULT_FRIENDS',result);
        }

      });
    }

  });
}

function getBlocks(data,socket){
    con.query("SELECT `IDREPORT` FROM `tblreports` WHERE `IDUSER` =  "+data+ "", function (err, result, fields) {
    if (err){

    }else{

        con.query("SELECT * FROM tblusers INNER JOIN tbluserreport on tblusers.IDUSER = tbluserreport.IDUSER WHERE tbluserreport.IDREPORT = "+result[0].IDREPORT+ "", function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_RESULT_BLOCKS',result);
        }

      });
    }

  });
}