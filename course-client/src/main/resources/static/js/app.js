angular.module('xhz', ['ui.router', 'xhz.controller'])

.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider

	.state('show', {
		url: '/show',
		templateUrl: 'show.html',
		controller: 'showCtrl'
	})

	$urlRouterProvider.otherwise('/show');
});