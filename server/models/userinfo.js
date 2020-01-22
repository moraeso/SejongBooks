module.exports = (sequelize, Datatypes) => {
  var userinfo = sequelize.define('userinfo', {
    userID: {
      type: Datatypes.STRING(20),
      allowNull: false,
      primaryKey: true
    },
    userPW: {
      type: Datatypes.STRING(100)
    },
    userPIC: {
      type: Datatypes.STRING(30)
    }
  },{
      classMethods: {},
      tableName: 'userinfo',
      freezeTableName: true,
      underscored: false,
      timestamps: false

  });

  userinfo.findUserByID = function(userID) {
   console.log('****finding process****');
   return userinfo.findOne({
     where: { userID: userID}
   })
 }

  return userinfo;

};
