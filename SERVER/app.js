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

    socket.on('CLIENT_CREATE_CONVERSATION', function(data){
    //socket.un = data;
    console.log("CLIENT_CREATE_CONVERSATION" + data);
    createConversation(data,socket);
  });

    socket.on('CLIENT_GET_CONVERSATIONS', function(data){
    //socket.un = data;
    getConversations(data,socket);
  });

    socket.on('CLIENT_GET_MESSAGES', function(data){
    //socket.un = data;
    getMessages(data,socket);
  });

    socket.on('CLIENT_SEND_TEXT_MESSAGE', function(data){
    //socket.un = data;
    sendTextMessage(data,socket);
  }); 

    socket.on('CLIENT_SEND_PHOTO_MESSAGE', function(data){
    //socket.un = data;
    sendPhotoMessage(data,socket);
  }); 


    socket.on('CLIENT_GET_ALL_USER_ONLINE', function(data){
    //socket.un = data;
    getAllUserOnline(data,socket);
  });

    socket.on('CLIENT_GET_GUEST_CONVERSATION', function(data){
    //socket.un = data;
    getGuestConversation(data,socket);
  });

    socket.on('CLIENT_GET_ALL_REQUEST_FRIEND', function(data){
    //socket.un = data;
    getAllRequestFriend(data,socket);
  });

    socket.on('CLIENT_DELETE_REQUEST_FRIEND', function(data){
    //socket.un = data;
    deleteRequestFriend(data,socket);
  });

    socket.on('CLIENT_ACCEPT_REQUEST_FRIEND', function(data){
    //socket.un = data;
    acceptRequestFriend(data,socket);
  });

    socket.on('CILENT_SEND_REQUEST_ADD_FRIEND', function(data){
    //socket.un = data;
    sendRequestAddFriend(data,socket);
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
      con.query("SELECT IDUSER,IS_ACTIVE FROM `tblusers` WHERE `USER` = '"+arr[0]+"'", function (err, result, fields) {
        if (err) throw err;
          io.sockets.emit('SERVER_SEND_STATE_ON_OFF',result); 
      });
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

            con.query("INSERT INTO `tblrequestfriends` VALUES (null,"+id+")", function (err, result, fields) {
            if (err){

            }else{

            }
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
        con.query("SELECT IDUSER,IS_ACTIVE FROM `tblusers` WHERE IDUSER = "+data + "", function (err, result, fields) {
        if (err) throw err;
          io.sockets.emit('SERVER_SEND_STATE_ON_OFF',result); 
      });
    }   
  });

  
}

function getFriends(data,socket){
    con.query("SELECT `IDCONTACT` FROM `tblcontacts` WHERE `IDUSER` =  "+data+ "", function (err, result, fields) {
    if (err){

    }else{
        var id = result[0].IDCONTACT;
        con.query("SELECT * FROM tblusers INNER JOIN tbluserscontact on tblusers.IDUSER = tbluserscontact.IDUSER WHERE tbluserscontact.IDCONTACT = "+result[0].IDCONTACT+ "", function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_RESULT_FRIENDS',{arr: result,idContact: id});
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

function createConversation(data,socket){
    con.query("SELECT tblconversation.IDCONVERSATION, tblconversation.TITLE, tblconversation.CREATED_AT, tblconversation.IDUSER AS 'USER', tblparticipants.IDUSER AS 'GUEST' FROM tblconversation INNER JOIN tblparticipants ON tblconversation.IDCONVERSATION = tblparticipants.IDCONVERSATION WHERE (tblconversation.IDUSER = "+data.idUser+" OR tblparticipants.IDUSER = "+data.idUser+") AND (tblconversation.IDUSER = "+data.idGuest+" OR tblparticipants.IDUSER = "+data.idGuest+") GROUP BY tblconversation.IDCONVERSATION", function (err, result, fields) {
    if (err) throw err;
    if (result.length == 0){
        con.query("INSERT INTO `tblconversation` VALUES (null,'"+data.title+"','"+data.time+"',"+data.idUser+ ")", function (err, result, fields) {
        if (err) throw err;
            con.query("SELECT `IDCONVERSATION` FROM `tblconversation` WHERE `TITLE` = '"+data.title+"'", function (err, result, fields) {
            if (err) throw err;
            var idConversation = result[0].IDCONVERSATION; 
                con.query("INSERT INTO `tblparticipants` VALUES (null,"+idConversation+","+data.idGuest+")", function (err, result, fields) {
                if (err){
                  socket.emit('SERVER_SEND_RESULT_CREATE_CONVERSATION',"false");
                }else{
                  socket.emit('SERVER_SEND_RESULT_CREATE_CONVERSATION',"true");
                }

                });
            });
        });
    }else{
      socket.emit('SERVER_SEND_RESULT_CREATE_CONVERSATION',"true");
    }
    });
}

function getConversations(data,socket){
    con.query("SELECT tblconversation.IDCONVERSATION, tblconversation.TITLE"
      +", tblconversation.CREATED_AT, tblconversation.IDUSER AS 'USER'"
      +", tblparticipants.IDUSER AS 'GUEST' FROM tblconversation "
      +"INNER JOIN tblparticipants ON tblconversation.IDCONVERSATION "
      +"= tblparticipants.IDCONVERSATION WHERE tblconversation.IDUSER = "+data+" "
      +"OR tblparticipants.IDUSER = "+data+" GROUP BY tblconversation.IDCONVERSATION", function (err, result, fields) {
    if (err){
      socket.emit('SERVER_SEND_RESULT_CONVERSATIONS',"false");
    }else{
      socket.emit('SERVER_SEND_RESULT_CONVERSATIONS',result);
      console.log(result);
    }   
  });
}

// function getMessages(data,socket){
//       con.query("SELECT tblmessages.IDMESSAGES, tblmessages.IDCONVERSATION"
//         +", tblmessages.IDUSER, tblmessages.TEXT, tblmessages.PHOTO"
//         +", tblmessages.CREATED_AT, tblusers.IMAGE FROM `tblmessages` "
//         +"INNER JOIN tblusers ON tblmessages.IDUSER = tblusers.IDUSER "
//         +"WHERE tblmessages.IDCONVERSATION = "+data+" ORDER BY tblmessages.IDMESSAGES ASC", function (err, result, fields) {
//     if (err){
//       socket.emit('SERVER_SEND_RESULT_MESSAGES',"false");
//     }else{
//       socket.emit('SERVER_SEND_RESULT_MESSAGES',{arr: result, idCon: data});
//       console.log({arr: result, idCon: data});
//     }   
//   });
// }

function getMessages(data,socket){
    var arr = data.split("-");
      con.query("SELECT tblconversation.IDCONVERSATION FROM tblconversation INNER JOIN tblparticipants ON tblconversation.IDCONVERSATION = tblparticipants.IDCONVERSATION WHERE (tblconversation.IDUSER = "+arr[0]+" OR tblparticipants.IDUSER = "+arr[0]+") AND (tblconversation.IDUSER = "+arr[1]+" OR tblparticipants.IDUSER = "+arr[1]+") GROUP BY tblconversation.IDCONVERSATION", function (err, result, fields) {
      if (err) throw err;
        var id = result[0].IDCONVERSATION;
        con.query("SELECT tblmessages.IDMESSAGES, tblmessages.IDCONVERSATION"
        +", tblmessages.IDUSER, tblmessages.TEXT, tblmessages.PHOTO"
        +", tblmessages.CREATED_AT, tblusers.IMAGE FROM `tblmessages` "
        +"INNER JOIN tblusers ON tblmessages.IDUSER = tblusers.IDUSER "
        +"WHERE tblmessages.IDCONVERSATION = "+id+" ORDER BY tblmessages.IDMESSAGES ASC", function (err, result, fields) {
        if (err){
          socket.emit('SERVER_SEND_RESULT_MESSAGES',"false");
        }else{
          socket.emit('SERVER_SEND_RESULT_MESSAGES',{arr: result, idCon: id});
          console.log({arr: result, idCon: id});
        }   
      }); 
  });
}

function sendTextMessage(data,socket){
    con.query("INSERT INTO `tblmessages` VALUES (null,"+data.idConversation+","+data.idUser+",'"+data.text+"','','','','','"+data.time+"')", function (err, result, fields) {
    if (err){
    }else{

          con.query("SELECT tblmessages.IDMESSAGES, tblmessages.IDCONVERSATION"
          +", tblmessages.IDUSER, tblmessages.TEXT, tblmessages.PHOTO"
          +", tblmessages.CREATED_AT, tblusers.IMAGE FROM `tblmessages` "
          +"INNER JOIN tblusers ON tblmessages.IDUSER = tblusers.IDUSER "
          +"WHERE tblmessages.IDCONVERSATION = "+data.idConversation+" AND tblmessages.IDMESSAGES > "+data.idLastMess+" ORDER BY tblmessages.IDMESSAGES ASC", function (err, result, fields) {
          if (err){
            socket.emit('SERVER_SEND_UPDATE_MESSAGES',"false");
          }else{
            io.sockets.emit('SERVER_SEND_UPDATE_MESSAGES',{arr: result, idCon: data.idConversation});
            io.sockets.emit('SERVER_SEND_NEW_MESSAGE',{idFriend: data.idReciever, idCon: data.idConversation});
            console.log({idFriend: data.idReciever, idCon: data.idConversation});
          }   
        }); 
    }   
  });
}

function sendPhotoMessage(data,socket){
      var url = getFileNameImage(socket.id);
      fs.writeFile(url, new Buffer(data.photo), function(err) {
      if(err) {
        
      }else{

          con.query("INSERT INTO `tblmessages` VALUES (null,"+data.idConversation+","+data.idUser+",'','/"+url+"','','','','"+data.time+"')", function (err, result, fields) {
          if (err){

          }else{
              con.query("SELECT tblmessages.IDMESSAGES, tblmessages.IDCONVERSATION"
              +", tblmessages.IDUSER, tblmessages.TEXT, tblmessages.PHOTO"
              +", tblmessages.CREATED_AT, tblusers.IMAGE FROM `tblmessages` "
              +"INNER JOIN tblusers ON tblmessages.IDUSER = tblusers.IDUSER "
              +"WHERE tblmessages.IDCONVERSATION = "+data.idConversation+" AND tblmessages.IDMESSAGES > "+data.idLastMess+" ORDER BY tblmessages.IDMESSAGES ASC", function (err, result, fields) {
              if (err){
                socket.emit('SERVER_SEND_UPDATE_MESSAGES',"false");
              }else{
                io.sockets.emit('SERVER_SEND_UPDATE_MESSAGES',{arr: result, idCon: data.idConversation});
                io.sockets.emit('SERVER_SEND_NEW_MESSAGE',{idFriend: data.idReciever, idCon: data.idConversation});
                console.log({idFriend: data.idReciever, idCon: data.idConversation});
              }   
            }); 
          }   
        });

      }
    });
}

function getAllUserOnline(data,socket){
        con.query("SELECT * FROM tblusers WHERE tblusers.IDUSER != "+data+" AND tblusers.IS_ACTIVE = 1", function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_RESULT_USER_ONLINE',result);
        }
      });
}


function getGuestConversation(data,socket){
        con.query("SELECT * FROM tblusers", function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_RESULT_GUEST_CONVERSATION',result);
          console.log(result);
        }
      });
}

function getAllRequestFriend(data,socket){
        con.query("SELECT tblrequestfriends.IDUSER, tblusers.FULLNAME, tblusers.IMAGE, tblrequestfriends.IDREQUESTFRIEND FROM tblrequestfriends INNER JOIN tbluserrequestfriend on tblrequestfriends.IDREQUESTFRIEND = tbluserrequestfriend.IDREQUESTFRIEND INNER JOIN tblusers on tblrequestfriends.IDUSER = tblusers.IDUSER WHERE tbluserrequestfriend.IDUSER = " +data, function (err, result, fields) {
        if (err){

        }else{
          socket.emit('SERVER_SEND_ALL_REQUEST_FRIEND',result);
          console.log(result);
        }
      });
}

function deleteRequestFriend(data,socket){
    var arr = data.split("-");
        con.query("DELETE FROM `tbluserrequestfriend` WHERE IDREQUESTFRIEND = "+arr[0]+" AND IDUSER = "+arr[2], function (err, result, fields) {
        if (err){
        }else{
        }
      });
}

function acceptRequestFriend(data,socket){
    var arr = data.split("-");
        con.query("DELETE FROM `tbluserrequestfriend` WHERE IDREQUESTFRIEND = "+arr[0]+" AND IDUSER = "+arr[2], function (err, result, fields) {
        if (err){
        }else{
                con.query("SELECT `IDCONTACT` FROM `tblcontacts` WHERE IDUSER = "+arr[2], function (err, result, fields) {
                if (err){
                }else{
                    var idContact = result[0].IDCONTACT;
                    var date = new Date();
                    con.query("INSERT INTO `tbluserscontact` VALUES (null,"+idContact+","+arr[1]+",'"+date+"')", function (err, result, fields) {
                      if (err){
                      }else{

                      }
                    });
                }
              });


                con.query("SELECT `IDCONTACT` FROM `tblcontacts` WHERE IDUSER = "+arr[1], function (err, result, fields) {
                if (err){
                }else{
                    var idContact = result[0].IDCONTACT;
                    var date = new Date();
                    con.query("INSERT INTO `tbluserscontact` VALUES (null,"+idContact+","+arr[2]+",'"+date+"')", function (err, result, fields) {
                      if (err){
                      }else{

                      }
                    });
                }
              });
        }
      });
}

function sendRequestAddFriend(data,socket){
  var arr = data.split("-");
  var date = new Date();
        con.query("SELECT `IDREQUESTFRIEND` FROM `tblrequestfriends` WHERE IDUSER = "+arr[0], function (err, result, fields) {
        if (err){

        }else{
          console.log(result);
            var id = result[0].IDREQUESTFRIEND;
              con.query("INSERT INTO `tbluserrequestfriend` VALUES (null,"+id+","+arr[1]+",'"+date+"')", function (err, result, fields) {
              if (err){

              }else{
                  
              }
            });
        }
      });
}



//SELECT IDMESSAGES,IDCONVERSATION,IDUSER,TEXT,PHOTO,CREATED_AT FROM `tblmessages` WHERE IDCONVERSATION = 9

//SELECT * FROM tblusers INNER JOIN tbluserscontact ON tblusers.IDUSER = tbluserscontact.IDUSER WHERE tbluserscontact.IDCONTACT = 12SELECT * FROM tblusers INNER JOIN tbluserscontact ON tblusers.IDUSER = tbluserscontact.IDUSER WHERE tbluserscontact.IDCONTACT = 12 AND tblusers.IS_ACTIVE = 1



