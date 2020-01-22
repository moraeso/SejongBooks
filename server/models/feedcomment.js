module.exports = (sequelize, Datatypes) => {
  var feedcomment = sequelize.define('feedcomment', {
    feedCommentID:{
      type:Datatypes.INTEGER,
      allowNull : false,
      autoIncrement: true,
      primaryKey: true
    },
    feedID: {
      type: Datatypes.INTEGER,
      allowNull: false
    },
    userID: {
      type: Datatypes.STRING(20),
      allowNull: false
    },
    feedCommentString: {
      type: Datatypes.STRING(200),
      allowNull: false
    }
  },{
      classMethods: {},
      tableName: 'feedcomment',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });
  return feedcomment;

};
