/**
 * Created by jinzhong.zheng on 2016/2/23.
 */
testCase.factory('testService', ['$http','$q',
    function($http, $q){
        return {
            getApiList:function()
            {
                return $http({
                    method:'GET',
                    url:'resource/json/DoorbellServiceApi.json'
                });
            },

            runTest: function(method, url, param){
                url = "../" + url;
                if(method == 'POST'){
                    var deferred = $q.defer();
                    $http({
                        method:'POST',
                        url:url,
                        data: param
                    }).success(function(response, status){
                        while(status != 200);
                        deferred.resolve(response);
                    }).error(function(response, status){
                        deferred.reject(response);
                    });

                    return deferred.promise;
                }else{
                    var deferred1 = $q.defer();
                    $http({
                        method:'GET',
                        url:url,
                    }).success(function(response, status){
                        while(status != 200);
                        deferred1.resolve(response);
                    }).error(function(response){
                        deferred1.reject(response, status);
                    });

                    return deferred1.promise;
                }
            },

        }

    }
]);