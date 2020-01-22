const express = require('express');
const express = require('express');
const router = express.Router();
var multer = require('multer');
const path = require('path');
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');

const router = express.Router();

const controller = require('./controller');



router.post('/all', controller.getFeedList);
router.post('/image',express.static('./feedImages'));


module.exports = router;
