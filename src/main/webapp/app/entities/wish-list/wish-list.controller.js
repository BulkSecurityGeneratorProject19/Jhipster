(function() {
    'use strict';

    angular
        .module('shopParasApp')
        .controller('WishListController', WishListController);

    WishListController.$inject = ['WishList'];

    function WishListController(WishList) {

        var vm = this;

        vm.wishLists = [];

        loadAll();

        function loadAll() {
            WishList.query(function(result) {
                vm.wishLists = result;
                vm.searchQuery = null;
            });
        }
    }
})();
