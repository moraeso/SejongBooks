module.exports = (sequelize, Datatypes) => {
  var answer = sequelize.define('answer', {
    bookID: {
      type: Datatypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    A1: {
      type: Datatypes.INTEGER
    },
    A2: {
      type: Datatypes.INTEGER
    },
    A3: {
      type: Datatypes.INTEGER
    },
    A4: {
      type: Datatypes.INTEGER
    },
    A5: {
      type: Datatypes.INTEGER
    },
    A6: {
      type: Datatypes.INTEGER
    },
    A7: {
      type: Datatypes.INTEGER
    },
    A8: {
      type: Datatypes.INTEGER
    },
    A9: {
      type: Datatypes.INTEGER
    },
    A10: {
      type: Datatypes.INTEGER
    },
    A11: {
      type: Datatypes.INTEGER
    },
    A12: {
      type: Datatypes.INTEGER
    },
    A13: {
      type: Datatypes.INTEGER
    },
    A14: {
      type: Datatypes.INTEGER
    },
    A15: {
      type: Datatypes.INTEGER
    },
    A16: {
      type: Datatypes.INTEGER
    },
    A17: {
      type: Datatypes.INTEGER
    },
    A18: {
      type: Datatypes.INTEGER
    },
    A19: {
      type: Datatypes.INTEGER
    },
    A20: {
      type: Datatypes.INTEGER
    }
  },{
      classMethods: {},
      tableName: 'answer',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });

    answer.findAnswerByBookID = function(bookID) {
     console.log('****answer finding process****');
     return answer.findOne({
       where: { bookID: bookID}
     })
   }

  return answer;

};
