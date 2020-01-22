const express = require('express');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {review} = require('../../models');

exports.getReviewList = function(request, response) {
  var bookID = request.body.bookID;
  var userID = request.body.userID;
  const sql=`SELECT review.*,count(reviewlike.reviewID) as LIKECOUNT,COUNT(IF(reviewlike.userID=?,'1',NULL)) as IFLIKE FROM GoDok.review LEFT JOIN
GoDok.reviewlike ON review.reviewID = reviewlike.reviewID WHERE reviewBookID = ? GROUP BY review.reviewID order by reviewID desc; `;
  const findReviewList = () => {
   return models.sequelize.query(sql,
     {
       replacements: [userID, bookID],
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
 .then(findReviewList)
 .then(respond)
}

exports.postReview = function(request, response) {

  var userID=request.body.userID;
  var bookID=request.body.bookID;
  var reviewString=request.body.reviewString;
  var reviewStar=request.body.reviewStar;

  var sql=`INSERT INTO review (reviewUserID,reviewBookID,reviewString,reviewStar) VALUES (?,?,?,?)`

   const uploadReview = () => {
     return models.sequelize.query(sql,
       {
         replacements: [userID, bookID,reviewString,reviewStar],
         type: models.sequelize.QueryTypes.INSERT,
         raw: true
       });
   };

   const sql2=`UPDATE GoDok.bookinfo SET bookStar = (SELECT ROUND(AVG(reviewStar),1) FROM GoDok.review WHERE reviewBookID = ?) WHERE bookID=? `

   const refreshReviewStar = () => {
    return models.sequelize.query(sql2,
      {
        replacements: [bookID,bookID],
        type: models.sequelize.QueryTypes.UPDATE,
        raw: true
      });
   };


   const respond=(result)=>{
  //   console.log(books);
     response.send(result);
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
   .then(uploadReview)
   .then(refreshReviewStar)
   .then(respond)
}

exports.markLike = function(request, response) {

  var userID=request.body.userID;
  var reviewID=request.body.reviewID;

  var sql=`INSERT INTO reviewlike (reviewID,userID) VALUES (?,?)`

   const markLike = () => {
     return models.sequelize.query(sql,
       {
         replacements: [reviewID,userID],
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

exports.myReview = function(request, response) {

  var userID=request.body.userID;

  var sql=`SELECT A.*
FROM
(
SELECT review.*,count(reviewlike.reviewID) as LIKECOUNT,COUNT(IF(reviewlike.userID=?,'1',NULL)) as IFLIKE FROM GoDok.review LEFT JOIN
GoDok.reviewlike ON review.reviewID = reviewlike.reviewID GROUP BY review.reviewID
) A WHERE reviewUserID = ?;`

   const myReview = () => {
     return models.sequelize.query(sql,
       {
         replacements: [userID,userID],
         type: models.sequelize.QueryTypes.SELECT,
         raw: true
       });
   };

   const respond=(result)=>{
  //   console.log(books);
     response.json({msg:"success",
                    code:200,
                    result:result});
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
   .then(myReview)
   .then(respond)
}

exports.myLike = function(request, response) {

  var userID=request.body.userID;

  var sql=`SELECT A.*
FROM
(
SELECT review.*,count(reviewlike.reviewID) as LIKECOUNT,COUNT(IF(reviewlike.userID=?,'1',NULL)) as IFLIKE FROM GoDok.review LEFT JOIN
GoDok.reviewlike ON review.reviewID = reviewlike.reviewID GROUP BY review.reviewID
) A WHERE IFLIKE=1;`

   const myLike = () => {
     return models.sequelize.query(sql,
       {
         replacements: [userID],
         type: models.sequelize.QueryTypes.SELECT,
         raw: true
       });
   };

   const respond=(result)=>{
  //   console.log(books);
     response.json({msg:"success",
                    code:200,
                    result : result});
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
   .then(myLike)
   .then(respond)
}
