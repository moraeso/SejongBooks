const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const config = require('./javascripts/config');
const models = require('./models');
var mysql = require('mysql');
var dbconfig = require('./config/dbconfig.js');
var fs =require('fs');
var multer = require('multer');
const path = require('path');

const userManageRouter = require('./routes/userManage/index.js');
const bookManageRouter = require('./routes/bookManage/index.js');
const feedManageRouter = require('./routes/feedManage/index.js');
const reviewManageRouter = require('./routes/reviewManage/index.js');
const commentManageRouter = require('./routes/commentManage/index.js');
const quizManageRouter = require('./routes/quizManage/index.js');
const app = express();


models.sequelize.sync()
    .then(() => {
        console.log('✓ DB connection success.');
        console.log(' Press CTRL-C to stop\n');
    })
    .catch(err => {
        console.error(err);
        console.log('✗ DB connection error. Please make sure DB is running');
        process.exit();
    });


app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());


app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.header('Access-Control-Allow-Headers', 'content-type, x-access-token');
    res.header('Access-Control-Allow-Headers', 'content-type, id');
    next();
});


app.use(morgan('dev'));

app.set('jwt-secret', config.secret);

// test route
app.get('/', function(req, res) {
    res.send(`
    <html>
    <head>
        <title>default page</title>
    </head>
    <body>
    <p>Welcome to Voice GODOK</p>
    </body>
    </html>
    `)
    //res.json({ message: 'welcome to Vocie Paper!' });
});
app.use('/user',userManageRouter);
app.use('/book',bookManageRouter);
app.use('/feed',feedManageRouter);
app.use('/review',reviewManageRouter);
app.use('/comment',commentManageRouter);
app.use('/quiz',quizManageRouter);

app.use('/cover',express.static('./book_cover'));

app.listen(4444);
console.log('server start');
