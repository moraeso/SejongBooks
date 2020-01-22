module.exports = (sequelize, Datatypes) => {
  var bookreadlist = sequelize.define('bookreadlist', {
    userID: {
      type: Datatypes.STRING(20),
      allowNull: false,
      primaryKey: true
    },
    bookID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      primaryKey: true
    }
  },
  {
      classMethods: {},
      tableName: 'bookreadlist',
      freezeTableName: true,
      underscored: false,
      timestamps: false

    });
    return bookreadlist;

  };
