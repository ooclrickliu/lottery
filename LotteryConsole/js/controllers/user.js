app.controller('UserController', ['$scope', 'Users', 'Permission', '$filter',
  function ($scope, Users, Permission, $filter) {
    var listALL = function(){
      Users.listAll().success(function (response) {
        $scope.usersList = response.data;
        $scope.userItem = $filter('orderBy')($scope.usersList, 'name')[0];
        $scope.userItem.selected = true;
        $scope.selectItem($scope.userItem);
      });

      $scope.filter = '';
      $scope.createUser = false;
      $scope.selectItem = function (userItem) {
        $scope.createUser = false;
        angular.forEach($scope.usersList, function (item) {
          item.selected = false;
        });
        $scope.userItem = userItem;
        $scope.userItem.selected = true;

        Users.getPermissionListoUser(userItem.id).success(function (response) {
          $scope.userInstancesInfo = response.data;
        });
      };
    }

    listALL();
    $scope.changePermissionsState = function (instanceState, instanceInfo, userItem) {
      if (instanceState == true) {
        Permission.grant(instanceInfo.permission.id,userItem.id).success(function (response) {
            instanceInfo.state = 'GRANTED';
        });
      } else {
        Permission.revoke(instanceInfo.permission.id,userItem.id).success(function (response) {
            instanceInfo.state = 'UNGRANT';
        });
      }
    };

    $scope.createAdminUser = function () {
      Users.create($scope.name, $scope.password).success(function (response) {
          var item = response.data;
          $scope.usersList.push(item);
          $scope.selectItem(item);
          $scope.name = '';
          $scope.password = '';
          $scope.repeatPassword = '';
      });
    };

    $scope.showDeleteUser = function (user) {
      $scope.deleteModalUser = user;
      $scope.deleteHintMessage = "确定删除用户" + $scope.deleteModalUser.userName + "\?";
    };

    $scope.removeUser = function () {
      var index = $scope.usersList.indexOf($scope.deleteModalUser);
      if (index != -1) {
        Users.delete($scope.deleteModalUser.id).then(function (response) {
            $scope.usersList.splice(index, 1);
            listALL();
        });
      }
    };

    $scope.checkPasswdFormat = function (value) {
      if (value == 0) {
        if ($scope.repeatPassword != null) {
          $scope.form.repeatPassword.$error.dontmatch = ($scope.password != $scope.repeatPassword);
        }
      } else {
        $scope.form.repeatPassword.$error.dontmatch = ($scope.password == $scope.repeatPassword);
      }
    };

  }]);