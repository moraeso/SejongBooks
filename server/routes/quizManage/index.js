const express = require('express');
const router = express.Router();
const controller = require('./controller');
const models = require('../../models');
const auth = require('../../javascripts/auth');


router.post('/answer', controller.checkAnswer);
router.use('/exam',express.static('./quiz'));

module.exports = router;
