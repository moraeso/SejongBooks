const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');
const auth = require('../../javascripts/auth');


const {userinfo} = require('../../models');
const saltRounds = 10;

exports.register = function(request, response) {
  const userID = request.body.userID;
  const userPassword = request.body.userPW;
  const userImage = request.body.profileString;

  let user;

  const encrypt = (foundResult) => {
    if(foundResult != undefined){
      response.json({
        registerStatus: 'existingID',
        code : 101
      })
    }

    return bcrypt.hash(userPassword, saltRounds);
  }

  const create = (hashedPassword) => {
    console.log('****creating process****');
    console.log(hashedPassword);
    return userinfo.create({
      userID: userID,
      userPW: hashedPassword,
      userPIC: userImage
    })
  }

  const respond = () => {
    response.json({
      registerStatus: 'registerSuccess',
      code: 200
    })
  }

  userinfo.findUserByID(userID)
  .then(encrypt)
  .then(create)
  .then(respond)
}

exports.login = (request, response) => {
  const userID = request.body.userID;
  const userPassword = request.body.userPW;
  const secret = request.app.get('jwt-secret');
  let list;
  let user;

  const query = `SELECT * FROM GoDok.bookreadlist WHERE userID=? `;

  const decrypt = (foundResult) => {
    if(foundResult != undefined){
      user = foundResult;
      console.log('****decrypting process****');

      return bcrypt.compareSync(userPassword, foundResult.userPW)
    } else {
      response.json({
        loginStatus: "invalid ID",
        code: 201
      })
      console.log('invalid ID');
    }
  }

  const getReadBookList = (authenticated) => {
    if(authenticated){
      console.log('****get read book list***');

      return models.sequelize.query(query,
        {
          replacements: [user.userID],
          type: models.sequelize.QueryTypes.SELECT,
          raw: true
        });
    } else {
      response.json({
        loginStatus: "inccorectPassword",
        code: 202
      })
    }
  }

  const createToken = (readBookList) => {
    console.log('****token creating process****');
    list=readBookList;
    //console.log("user : " , user);

    var token = jwt.sign(
      {
        ID: user.userID
      },
      secret,
      {
        expiresIn: '24h',
        issuer: 'http://15011066.iptime.org',
        subject: 'userInfo'
      })

    console.log('token successfuly created');

    return token
  }

  const respond = (token) => {
    response.json({
      loginStatus: "success",
      code: 200,
      id: user.userID,
      pw: user.userPW,
      profileString: user.userPIC,
      token: token,
      bookList: list
    })
    console.log('login succeed');
  }

  userinfo.findUserByID(userID)
  .then(decrypt)
  .then(getReadBookList)
  .then(createToken)
  .then(respond)
}

exports.getUserInfo=(request,response) =>{

  userID=request.body.userID;

  const respond = (userInfo) => {
  response.json({userInfo:userInfo,
            code:200});
  }

  auth.isLoggedIn(request, response)
  .then(auth.verifyToken)
  .catch(function(error){
    console.log('unavailable token')
    console.log(error);
    response.json({
     authStatus: 'unavailable tokennn',
     code: 503
     })
    throw new Error('close');
  })
  userinfo.findUserByID(userID)
  .catch(function(error){
    response.json({
      msg:'query error occcured',
      code:500
    })
  })
  .then(respond);
}

exports.readBook=(request,response) =>{

  var userID=request.body.userID;
  var bookID=request.body.bookID;
  var isInsert = request.body.isInsert;

  if(isInsert==1){
  var sql=`UPDATE bookinfo SET bookCount = bookCount + 1 WHERE bookid = ?; `;
  var sql2=`INSERT INTO bookreadlist (userID, bookID) VALUES (?,?) `
}
else{
  sql=`UPDATE bookinfo SET bookCount = bookCount + -1 WHERE bookid = ?; `
  sql2= `DELETE FROM bookreadlist WHERE userID=? AND bookID=? `
}

  const readBook = () => {
    if(isInsert==1){
    return models.sequelize.query(sql2,
      {
        replacements: [userID,bookID],
        type: models.sequelize.QueryTypes.INSERT,
        raw: true
      }).catch(function(error){
        response.json({
          msg:'query error occcured',
          code:500
        })
        throw new Error('close');
      })
    }
    else{
      return models.sequelize.query(sql2,{
        replacements: [userID,bookID],
        type: models.sequelize.QueryTypes.DELETE,
        raw: true
      }).catch(function(error){
        response.json({
          msg:'query error occcured',
          code:500
        })
        throw new Error('close');
      })
    }
  };

    const refreshBookCount = () => {
      return models.sequelize.query(sql,
        {
          replacements: [bookID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        });
    };

    const respond=(result)=>{
      response.send(result);
    };
  auth.isLoggedIn(request, response)
  .then(auth.verifyToken)
  .catch(function(error){
    console.log('unavailable token')
    console.log(error);
    response.json({
     authStatus: 'unavailable tokennn',
     code: 503
     })
    throw new Error('close');
  })
  .then(readBook)
  .then(refreshBookCount)
  .then(respond);
}
