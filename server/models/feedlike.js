module.exports = (sequelize, Datatypes) => {
  var feedlike = sequelize.define('feedlike', {
    feedID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    userID: {
      type: Datatypes.STRING(20),
      allowNull: false
    }
  },
  {
      classMethods: {},
      tableName: 'feedlike',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });
  return feedlike;

};
