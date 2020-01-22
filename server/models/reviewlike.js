module.exports = (sequelize, Datatypes) => {
  var reviewlike = sequelize.define('reviewlike', {
    reviewID: {
      type: Datatypes.INTEGER,
      allowNull: false,
    },
    userID: {
      type: Datatypes.STRING(20),
      allowNull: false
    }
  },
  {
      classMethods: {},
      tableName: 'reviewlike',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });
  return reviewlike;

};
