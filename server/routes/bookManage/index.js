const express = require('express');
const router = express.Router();

const controller = require('./controller');

router.post('/list', controller.getBookList);
router.post('/info', controller.getBookInfo);

module.exports = router;
