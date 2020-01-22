module.exports = (sequelize, Datatypes) => {
  var review = sequelize.define('review', {
    reviewID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      autoIncrement: true,
      primaryKey: true
    },
    reviewUserID: {
      type: Datatypes.STRING(20),
      allowNull: false
    },
    reviewBookID: {
      type: Datatypes.INTEGER,
      allowNull: false
    },
    reviewString: {
      type: Datatypes.STRING(200),
      allowNull: false
    },
    reviewStar: {
      type: Datatypes.DOUBLE,
      allowNull: false
    }
  },
  {
      classMethods: {},
      tableName: 'review',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });

  return review;

};
