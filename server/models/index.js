'use strict';

const fs = require('fs');
const path = require('path');
const Sequelize = require('sequelize');
const basename = path.basename(__filename);
const env = process.env.NODE_ENV || 'development';
const config = require(__dirname + '/../config/config.json')[env];
const db = {};

let sequelize;
if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.username, config.password, config);
}

fs
  .readdirSync(__dirname)
  .filter(file => {
    return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
  })
  .forEach(file => {
    const model = sequelize['import'](path.join(__dirname, file));
    db[model.name] = model;
  });

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

db.bookinfo = require('./bookinfo')(sequelize, Sequelize);
db.bookreadlist = require('./bookreadlist')(sequelize, Sequelize);
/*
db.feed = require('./feed')(sequelize, Sequelize);
db.feedcomment = require('./feedcomment')(sequelize, Sequelize);
db.feedlike = require('./feedlike')(sequelize, Sequelize);
db.review = require('./review')(sequelize, Sequelize);
db.reviewlike = require('./reviewlike')(sequelize, Sequelize);
db.userinfo = require('./userinfo')(sequelize, Sequelize);
*/

module.exports = db;
