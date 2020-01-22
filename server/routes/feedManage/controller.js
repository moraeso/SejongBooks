const express = require('express');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {feed} = require('../../models');

exports.getFeedList = function(request, response) {
var userID=request.body.userID;

const sql=`SELECT A.*,B.COMMENTCOUNT as COMMENTCOUNT FROM
(SELECT feed.*, count(feedlike.feedID) as LIKECOUNT,COUNT(IF(feedlike.userID=?,'1',NULL)) as IFLIKE FROM GoDok.feed LEFT JOIN GoDok.feedlike ON GoDok.feed.feedID = GoDok.feedlike.feedID
GROUP BY GoDok.feed.feedID) A
LEFT JOIN
(SELECT feed.*, count(feedcomment.feedID) as COMMENTCOUNT FROM GoDok.feed LEFT JOIN GoDok.feedcomment ON GoDok.feed.feedID = GoDok.feedcomment.feedID
GROUP BY GoDok.feed.feedID) B
ON A.feedID = B.feedID order by feedID desc`

 const findFeedList = () => {
   return models.sequelize.query(sql,
     {
       replacements: [userID],
       type: models.sequelize.QueryTypes.SELECT,
       raw: true
     });
 };

 const respond=(feeds)=>{
//   console.log(books);
   feedList=feeds;
   response.send(feeds);
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
 .then(findFeedList)
 .then(respond)
}


exports.postFeed= function(request, response) {
var userID=request.body.userID;
var feedString=request.body.feedString;
var sql=`INSERT INTO feed (userID,feedString) VALUES (?,?)`

 const uploadFeed = () => {
   return models.sequelize.query(sql,
     {
       replacements: [userID, feedString],
       type: models.sequelize.QueryTypes.INSERT,
       raw: true
     });
 };

 const respond=(feeds)=>{
//   console.log(books);
   response.json({msg:"success",
                  feedID: feeds[0]});
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
 .then(uploadFeed)
 .then(respond)
}

exports.markLike = function(request, response) {

  var userID=request.body.userID;
  var feedID=request.body.feedID;

  var sql=`INSERT INTO feedlike (feedID,userID) VALUES (?,?)`

   const markLike = () => {
     return models.sequelize.query(sql,
       {
         replacements: [feedID,userID],
         type: models.sequelize.QueryTypes.INSERT,
         raw: true
       });
   };

   const respond=(result)=>{
  //   console.log(books);
     response.json({msg:"success",
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
   .then(markLike)
   .then(respond)
}
