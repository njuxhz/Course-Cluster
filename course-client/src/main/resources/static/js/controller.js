angular.module('xhz.controller', ['myservice'])

.controller('showCtrl', function ($scope, xhzService) {
    $scope.addShow = false;
    $scope.fileShow = false;
    $scope.scoreShow = false;
    $scope.editInfoShow = false;
    $scope.isLoading = true;
    $scope.leftPage = $scope.rightPage = false;
    $scope.page = 1;
    $scope.tmp = {};
    $scope.index = -1;
    $scope.indexInShow = -1;

	xhzService.getStudentsAsyn().then(function () {
        $scope.show = [];
	    $scope.data = xhzService.getStudents();
        $scope.page = 1;
        $scope.leftPage = false;
	    if($scope.data.length <= 10) {
	        for(var i = 0; i < $scope.data.length; i++) {
	            $scope.show.push($scope.data[i]);
            }
            $scope.rightPage = false;
        }else {
            for(var i = 0; i < 10; i++) {
                $scope.show.push($scope.data[i]);
            }
            $scope.rightPage = true;
        }
        $scope.isLoading = false;
    });

	$scope.left = function () {
	    $scope.page = $scope.page - 1;
	    $scope.show = [];
	    for(var i = 10*($scope.page - 1); i < 10*$scope.page; i++) {
	        $scope.show.push($scope.data[i]);
        }
        $scope.rightPage = true;
        if($scope.page > 1) $scope.leftPage = true;
        else  $scope.leftPage = false;
    };

	$scope.right = function () {
        $scope.page = $scope.page + 1;
        $scope.show = [];
        $scope.leftPage = true;
        if($scope.data.length <= 10*$scope.page) {
            for(var i = 10*($scope.page - 1); i < $scope.data.length; i++) {
                $scope.show.push($scope.data[i]);
            }
            $scope.rightPage = false;
        } else {
            for(var i = 10*($scope.page - 1); i < 10*$scope.page; i++) {
                $scope.show.push($scope.data[i]);
            }
            $scope.rightPage = true;
        }
    };

	$scope.change = function () {
	    $scope.tmp.total = parseFloat($scope.tmp.regular) +
            parseFloat($scope.tmp.project) +
            parseFloat($scope.tmp.final);
    };

    $scope.showEditInfo = function (item) {
        $scope.index = $scope.data.indexOf(item);
        $scope.indexInShow = $scope.show.indexOf(item);
        $scope.tmp = {};
        for(var key in item) {
            $scope.tmp[key] = item[key];
        }
        $scope.oldId = item.id;
        $scope.editInfoShow = true;
    };

    $scope.confirmEdit = function () {
        $scope.isLoading = true;
        xhzService.updateStudentAsyn($scope.tmp, $scope.oldId).then(function () {
            if(xhzService.isSucceed()) {
                $scope.show[$scope.indexInShow].id = $scope.data[$scope.index].id = $scope.tmp.id;
                $scope.show[$scope.indexInShow].name = $scope.data[$scope.index].name = $scope.tmp.name;
                $scope.show[$scope.indexInShow].major = $scope.data[$scope.index].major = $scope.tmp.major;
                $scope.indexInShow = $scope.index = -1;
            }
            $scope.isLoading = false;
            $scope.editInfoShow = false;
        });
    };

    $scope.showScore = function (item) {
        $scope.index = $scope.data.indexOf(item);
        $scope.indexInShow = $scope.show.indexOf(item);
        $scope.tmp = {};
        for(var key in item) {
            $scope.tmp[key] = item[key];
        }
        $scope.tmp.total = parseFloat($scope.tmp.regular) +
            parseFloat($scope.tmp.project) +
            parseFloat($scope.tmp.final);
        $scope.scoreShow = true;
    };

    $scope.confirmScore = function () {
        $scope.isLoading = true;
        xhzService.updateScoreAsyn($scope.tmp).then(function () {
            if(xhzService.isSucceed()) {
                $scope.show[$scope.indexInShow].regular = $scope.data[$scope.index].regular = $scope.tmp.regular;
                $scope.show[$scope.indexInShow].project = $scope.data[$scope.index].project = $scope.tmp.project;
                $scope.show[$scope.indexInShow].final = $scope.data[$scope.index].final = $scope.tmp.final;
                $scope.show[$scope.indexInShow].total = $scope.data[$scope.index].total = $scope.tmp.total;
                $scope.indexInShow = $scope.index = -1;
            }
            $scope.isLoading = false;
            $scope.scoreShow = false;
        });
    };

	$scope.showAdd = function () {
	    $scope.tmp = {};
	    $scope.addShow = true;
    };

    $scope.confirmAdd = function () {
        $scope.isLoading = true;
        xhzService.addStudentAysn($scope.tmp).then(function () {
            if(xhzService.isSucceed()) {
                $scope.data.push({
                    'id' : $scope.tmp.id,
                    'name' : $scope.tmp.name,
                    'major' : $scope.tmp.major,
                    'regular' : 0, 'project' : 0, 'final' : 0, 'total' : 0
                });
                if($scope.data.length == ($scope.page * 10 + 1)) $scope.rightPage = true;
                else if ($scope.data.length <= $scope.page * 10) $scope.show.push({
                    'id' : $scope.tmp.id,
                    'name' : $scope.tmp.name,
                    'major' : $scope.tmp.major,
                    'regular' : 0, 'project' : 0, 'final' : 0, 'total' : 0
                });
            } else alert("重复添加");
            $scope.isLoading = false;
            $scope.addShow = false;
        });
    };

    $scope.delete = function (item) {
        $scope.isLoading = true;
        xhzService.deleteStudentAysn(item).then(function () {
            if(xhzService.isSucceed()) {
                var index = $scope.data.indexOf(item);
                var indexInShow = $scope.show.indexOf(item);
                $scope.show.splice(indexInShow, 1);
                if($scope.show.length == 9 && $scope.rightPage) {
                    $scope.show.push($scope.data[$scope.page * 10]);
                    if($scope.data.length == (10*$scope.page + 1)) $scope.rightPage = false;
                }
                if($scope.show.length == 0 && $scope.page > 1) {
                    $scope.show = [];
                    $scope.page = $scope.page - 1;
                    $scope.rightPage = false;
                    for(var i = 10*($scope.page - 1); i < 10*$scope.page; i++) {
                        $scope.show.push($scope.data[i]);
                    }
                    if($scope.page > 1) $scope.leftPage = true;
                    else $scope.leftPage = false;
                }
                $scope.data.splice(index, 1);
                $scope.isLoading = false;
            }
        });
    };

    $scope.showFile = function () {
        $scope.tmp = {};
        $scope.fileShow = true;
    };

    $scope.confirmFile = function () {
        $scope.isLoading = true;
        var file = document.querySelector('input[type=file]').files[0];
        xhzService.uploadFileAsyn(file).then(function () {
            $scope.data = [];
            $scope.data = xhzService.getStudents();
            $scope.show = [];

            var dateStart = new Date(), dateEnd;
            while( ((dateEnd = new Date()) - dateStart) <= 200){
            }

            if($scope.data.length <= 10*$scope.page) {
                for(var i = 10*($scope.page - 1); i < $scope.data.length; i++) {
                    $scope.show.push($scope.data[i]);
                }
                $scope.rightPage = false;
            }else {
                for(var i = 10*($scope.page - 1); i < 10*$scope.page; i++) {
                    $scope.show.push($scope.data[i]);
                }
                $scope.rightPage = true;
            }
            $scope.fileShow = false;
            $scope.isLoading = false;
        });
    };

    $scope.disShowAdd = function () {
        $scope.addShow = false;
    };

    $scope.disShowFile = function () {
        $scope.fileShow = false;
    };

    $scope.disShowEditInfo = function () {
        $scope.editInfoShow = false;
    };

    $scope.disShowScore = function () {
        $scope.scoreShow = false;
    };
});