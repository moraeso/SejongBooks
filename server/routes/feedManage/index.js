const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const feedImgUpload = multer({
  storage: multer.diskStorage({
    destination: function (request, file, cb) {
      cb(null, './feedImages/');
    },
    filename: function (request, file, cb) {
      cb(null, new Date().valueOf() + path.extname(file.originalname));
    }
  }),
});

router.post('/all', controller.getFeedList);
router.post('/upload',controller.postFeed);
router.post('/like',controller.markLike);

router.use('/image',express.static('./feedImages'));

router.post('/uploadimage',feedImgUpload.single('image'),function(request, response) {
/*
  console.log(__dirname);
  console.log(request.file);
  console.log(request.body.userID);
  console.log(request.file.filename);
  */

  var image= request.file.filename;
  console.log(request.body);

  var imagePath='./feed/image/'.concat(image);
  console.log(imagePath);

  var query = 'UPDATE feed SET feedPIC=? WhERE feedID=?';

  const uploadFeedImage = () => {
      console.log('****UPLOAD USER Image process****');
      return models.sequelize.query(query,
        {
          replacements: [imagePath,request.body.feedID],
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
  .then(uploadFeedImage)
  .then(respond)

});



module.exports = router;
