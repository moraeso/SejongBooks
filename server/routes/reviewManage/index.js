const express = require('express');
const router = express.Router();
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');


router.post('/', controller.getReviewList);
router.post('/upload',controller.postReview);
router.post('/like',controller.markLike);
router.post('/my',controller.myReview);
router.post('/mylike',controller.myLike)
module.exports = router;
