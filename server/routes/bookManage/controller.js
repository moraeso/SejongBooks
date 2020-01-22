const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const {bookinfo} = require('../../models');

exports.getBookList = function(request, response) {
var userID=request.body.userID;
var bookList;
var readList;
const query = `SELECT * FROM GoDok.bookreadlist WHERE userID=? `

 const findBookList = () => {
   return bookinfo.findAll()
 };

 const findReadList=(books)=>{
   bookList=books;
   return models.sequelize.query(query,
           {
             replacements: [userID],
             type: models.sequelize.QueryTypes.SELECT,
             raw: true
           });
 }

 const respond=(reads)=>{
//   console.log(books);
    readList=reads;
   response.json({bookList:bookList,
                  readList:readList
                    });
 }

 findBookList()
 .then(findReadList)
 .then(respond)
}

exports.getBookInfo = function(request, response) {

const bookID=request.body.bookID;

 const findBookList = () => {
   return bookinfo.findOne({
     where: { bookID: bookID}
   })
 };

 const respond=(book)=>{
//   console.log(books);
   response.send(book);
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
 .then(findBookList)
 .then(respond)
}
