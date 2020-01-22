module.exports = (sequelize, Datatypes) => {
  var bookinfo = sequelize.define('bookinfo', {
    bookID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      unique: true,
      autoIncrement: true,
      primaryKey: true
    },
    bookName: {
      type: Datatypes.STRING(25),
      allowNull: false
    },
    bookInfo: {
      type: Datatypes.STRING(1000),
      allowNull: false
    },
    bookType: {
      type: Datatypes.STRING(10),
      allowNull: false
    },
    bookPage: {
      type: Datatypes.INTEGER,
      allowNull: false
    },
    bookDate: {
      type: Datatypes.INTEGER
    },
    bookAuthor: {
      type: Datatypes.STRING(20)
    },
    bookPublisher:{
      type : Datatypes.STRING(20)
    },
    bookStar:{
      type : Datatypes.DOUBLE
    },
    bookCount:{
      type : Datatypes.INTEGER
    },
    problem:{
      type:Datatypes.INTEGER
    }
  }
  ,{
      classMethods: {},
      tableName: 'bookinfo',
      freezeTableName: true,
      underscored: false,
      timestamps: false

    });
      return bookinfo;
  };
