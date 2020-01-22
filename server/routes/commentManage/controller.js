const express = require('express');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {feedcomment} = require('../../models');

exports.getCommentList = function(request, response) {
  var sql=`SELECT * FROM feedcomment WHERE feedID=?`
  var feedID=request.body.feedID;

  const commentList=()=>{
    return models.sequelize.query(sql,
            {
              replacements: [feedID],
              type: models.sequelize.QueryTypes.SELECT,
              raw: true
            });

    }

    const respond =(comments)=>{
      response.send(comments);
    }

  auth.isLoggedIn(request, response)
  .then(auth.verifyToken)
  .catch(function(error){
    console.log(error)
    response.json({
      authStatus: 'unavailable tokennn',
      code: 503
    })
    throw new Error('close');
  })
  .then(commentList)
  .then(respond)
}

exports.postComment = function(request, response) {
  var feedID=request.body.feedID;
  var userID=request.body.userID;
  var feedCommentString=request.body.feedCommentString;

  var sql=`INSERT INTO feedcomment (feedID,userID,feedCommentString) VALUES (?,?,?)`

   const uploadComment = () => {
     return models.sequelize.query(sql,
       {
         replacements: [feedID, userID,feedCommentString],
         type: models.sequelize.QueryTypes.INSERT,
         raw: true
       });
   };
   const respond =(result)=>{
     response.send(result);
   };

   auth.isLoggedIn(request, response)
   .then(auth.verifyToken)
   .catch(function(error){
     console.log(error)
     response.json({
       authStatus: 'unavailable tokennn',
       code: 503
     })
     throw new Error('close');
   })
   .then(uploadComment)
   .then(respond)

}
