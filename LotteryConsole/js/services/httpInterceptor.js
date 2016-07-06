angular.module('app').factory('responseRejector', ['$q', '$rootScope', '$window', function ($q, $rootScope, $window) {
  var responseRejector = {
    'request': function (request) {
      if(request.url.indexOf(serverUrl) >= 0){
        request.url += "?clientType=web";
      }
      return request;
    },

    'requestError': function (rejection) {
      return $q.reject(rejection);
    },

    'response': function (response) {
      if(response.data.stateCode == undefined){
        return response;
      }
      switch (response.data.stateCode) {
        case 'SUCCESS':
        case 'success':
        {
          break;
        }
        case 'NotLogin':
        case 'InvalidAccess':
        {
          $window.location.href = 'index.html';
          return $q.reject(response);
          break;
        }
        default:
        {
          toast('error', '', $rootScope.errCodeMap[response.data.stateCode], null);
          console.log(response.data);
          return $q.reject(response);
        }
      }
      return response;
    },

    'responseError': function (rejection) {
      return $q.reject(rejection);
    },
  };

  return responseRejector;
}]);