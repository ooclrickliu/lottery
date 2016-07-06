app.controller('ResourceController', ['$scope', 'Resource', function ($scope, Resource) {
    var listAll = function () {
        Resource.getList().then(function (response) {
            $scope.items = response.data.data;
            for(var i = 0;i <$scope.items.length; i++){
                if($scope.items[i].price ==0){
                    $scope.items[i].price ='免费';
                }
                if( $scope.items[i].size == 9999999999){
                    $scope.items[i].size = '无限';
                }else if($scope.items[i].type == 'SPACE'){
                    var value = $scope.items[i].size;
                    $scope.items[i].size = value/1024/1024;
                }else if($scope.items[i].type == 'FLOW'){
                    var value = $scope.items[i].size;
                    $scope.items[i].size = value/60/60;
                }
            }
        });
    }
    listAll();

    $scope.editResource = function () {
        if($scope.editResourceItem.type.toUpperCase() == 'SPACE'){
            var value =$scope.editResourceItem.size*1024*1024;
        }else if($scope.editResourceItem.type.toUpperCase() == 'FLOW'){
            var value = $scope.editResourceItem.size*60*60;
        }
        var messageType = {
            code: $scope.editResourceItem.code,
            unit: $scope.editResourceItem.unit.toUpperCase(),
            name: $scope.editResourceItem.name,
            size: value,
            type: $scope.editResourceItem.type.toUpperCase(),
            price: $scope.editResourceItem.price
        };
        var request = $scope.editResourceItem.id ? Resource.update($scope.editResourceItem.id, messageType)
            : Resource.create(messageType);
        request.then(function (response) {
            if (response.data.stateCode == 'SUCCESS') {
                listAll();
            }
        });
    };
    $scope.removeItem = function () {
        var index = $scope.items.indexOf($scope.deleteModalItem);
        if (index != -1) {
            Resource.remove($scope.deleteModalItem.id).then(function (response) {
                if (response.data.stateCode == 'SUCCESS') {
                    $scope.items.splice(index, 1);
                }
            });
        }
    }
    $scope.showUpdateDialog = function (item) {
        $scope.editResourceItem = item;
    }

    $scope.showDeleteModal = function (item) {
        $scope.deleteModalItem = item;
        $scope.deleteHintMessage = "确定删除商品：\"" + $scope.deleteModalItem.name + "\"";
    }
}]);
