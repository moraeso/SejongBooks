const express = require('express');
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {feed} = require('../../models');

exports.getFeedList = function(request, response) {

 const findFeedList = () => {
   return feed.findAll()
 };

 const respond=(feeds)=>{
//   console.log(books);
   response.send(feeds);
 }

 auth.isLoggedIn(request, response)
 .then(auth.verifyToken)
 .then(findBookList)
 .then(respond)
}
