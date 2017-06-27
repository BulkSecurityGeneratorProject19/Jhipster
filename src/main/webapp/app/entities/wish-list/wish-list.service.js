(function() {
    'use strict';
    angular
        .module('shopParasApp')
        .factory('WishList', WishList);

    WishList.$inject = ['$resource'];

    function WishList ($resource) {
        var resourceUrl =  'api/wish-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
