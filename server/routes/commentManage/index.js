const express = require('express');
const router = express.Router();
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');


router.post('/list', controller.getCommentList);
router.post('/upload',controller.postComment);


module.exports = router;
