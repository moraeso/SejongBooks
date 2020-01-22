const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const auth = require('../../javascripts/auth');
const models = require('../../models');

const controller = require('./controller');

const userImgUpload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './userImages/');
    },
    filename: function (request, file, cb) {
      cb(null, request.body.userID + path.extname(file.originalname));
    }
  }),
});

router.post('/info', controller.getUserInfo);
router.post('/register', controller.register);
router.post('/login', controller.login);
router.post('/read',controller.readBook);

router.use('/image',express.static('./userImages'));

router.post('/uploadimage',userImgUpload.single('image'),function(request, response) {
/*
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  */
  var image= request.file.filename;

  var imagePath='./user/image/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE userinfo SET userPIC=? WhERE userID=?';

  const uploadUserImage = () => {
      console.log('****UPLOAD USER Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,request.body.userID],
          type: models.sequelize.QueryTypes.UPDATE,
          raw: true
        })
        .catch(function(error){
          response.json({
            msg:'query error occcured',
            code:500
          })
        })
  }


    const respond = () => {
    response.json({msg:'success',
              path:imagePath,
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
   .then(uploadUserImage)
  .then(respond)

});

module.exports = router;
