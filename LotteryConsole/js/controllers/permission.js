app.controller('PermissionController', ['$scope', 'Permission', '$filter',
    function ($scope, Permission, $filter) {
        Permission.listAll().then(function (response) {
            $scope.usersList = response.data.data;
            $scope.userItem = $filter('orderBy')($scope.usersList, 'name')[0];
            $scope.userItem.selected = true;
            $scope.selectItem($scope.userItem);
        });
        $scope.filter = '';
        $scope.createAdmin = false;
        $scope.selectItem = function (userItem) {
            angular.forEach($scope.usersList, function (item) {
                item.selected = false;
            });
            $scope.userItem = userItem;
            $scope.userItem.selected = true;
            $scope.userInstancesInfo = new Array();
            Permission.getUserByPermissionId($scope.userItem.permissionId).then(function (response) {
                $scope.permissionUserList = response.data.data;
            });
        };

        $scope.showDeletePerUser = function (user) {
            $scope.deleteModalUser = user;
        };
        $scope.changePermissionsState = function (instanceInfo,userItem) {
            if(instanceInfo){
                Permission.revoke(userItem.id, instanceInfo.id).then(function (response) {
                    if (response.data.stateCode == 'SUCCESS') {
                        //instanceInfo.state = 'GRANTED';
                        Permission.getUserByPermissionId($scope.userItem.permissionId).then(function (response) {
                            $scope.permissionUserList = response.data.data;
                        });
                    }
                });
            }

        };
    }]);
