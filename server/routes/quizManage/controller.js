const express = require('express');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {answer} = require('../../models');

exports.checkAnswer = function(request, response) {
  let bookID=request.body.bookID;
  var sql="SELECT * FROM answer WHERE bookID=?"

  const getAnswer=()=>{
    return models.sequelize.query(sql,
      {
        replacements: [bookID],
        type: models.sequelize.QueryTypes.SELECT,
        raw: true
      });
  }
  const respond=(answers)=>{
    //console.log(answers);
    let dab=answers[0];
    console.log(dab.A1);
    response.send(dab);
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
    .then(getAnswer)
    .then(respond);
}
