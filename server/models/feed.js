module.exports = (sequelize, Datatypes) => {
  var feed = sequelize.define('feed', {
      feedID: {
        type: Datatypes.INTEGER,
        allowNull: false,
        autoIncrement: true,
        primaryKey: true
      },
      userID: {
        type: Datatypes.STRING(20),
        allowNull: false
      },
      feedString: {
        type: Datatypes.STRING(200),
        allowNull: false
      },
      feedPIC:{
        type:Datatypes.STRING(50),
      }
  },
  {
      classMethods: {},
      tableName: 'feed',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });
  return feed;

};
