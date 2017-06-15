var app = angular.module('myservice',[]);

app.factory('xhzService', function($http, $q){

    var students;
    var isSuccess;

    return {
        getStudentsAsyn : function () {
            students = [];
            var deferred = $q.defer();
            $http({
                url : '/studentClient',
                method : 'get'
            }).success(function (response) {
                for(var i = 0; i < response.length; i++) {
                    var item = {
                        'id' : response[i].id,
                        'name' : response[i].name,
                        'major' : response[i].major,
                        'regular' : response[i].regular,
                        'project' : response[i].project,
                        'final' : response[i].fin,
                        'total' : response[i].total
                    }
                    students.push(item);
                }
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        },
        getStudents : function () {
            return students;
        },
        isSucceed : function () {
            return isSuccess;
        },
        addStudentAysn : function (student) {
            isSuccess = false;
            var deferred = $q.defer();
            $http({
                url : '/studentClient',
                method : 'post',
                params : {
                    'id': student.id,
                    'name': student.name,
                    'major': student.major
                }
            }).success(function (response) {
                if(response == 1) isSuccess = true;
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        },
        deleteStudentAysn : function (student) {
            isSuccess = false;
            var deferred = $q.defer();
            $http({
                url : '/studentClient/' + student.id,
                method : 'delete'
            }).success(function (response) {
                if(response == 1) isSuccess = true;
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        },
        updateStudentAsyn : function (student, oldId) {
            isSuccess = false;
            var deferred = $q.defer();
            $http({
                url : '/studentClient/' + oldId,
                method : 'put',
                params : {
                    'id' : student.id,
                    'name' : student.name,
                    'major' : student.major,
                    'regular' : student.regular,
                    'project' : student.project,
                    'final' : student.final,
                    'total' : student.total
                }
            }).success(function (response) {
                if(response == 1) isSuccess = true;
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        },
        updateScoreAsyn : function (student) {
            isSuccess = false;
            var deferred = $q.defer();
            $http({
                url : '/scoreClient/' + student.id,
                method : 'put',
                params : {
                    'regular' : student.regular,
                    'project' : student.project,
                    'final' : student.final,
                    'total' : student.total
                }
            }).success(function (response) {
                if(response == 1) isSuccess = true;
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        },
        uploadFileAsyn : function (file) {
            students = [];
            var deferred = $q.defer();
            var fd = new FormData();
            fd.append('file', file);
            $http({
                url : '/file',
                method : 'post',
                data: fd,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (response) {
                for(var i = 0; i < response.length; i++) {
                    var item = {
                        'id' : response[i].id,
                        'name' : response[i].name,
                        'major' : response[i].major,
                        'regular' : response[i].regular,
                        'project' : response[i].project,
                        'final' : response[i].fin,
                        'total' : response[i].total
                    }
                    students.push(item);
                }
                deferred.resolve(response);
            }).error(function (response, status, headers, config){
            });
            return deferred.promise;
        }
    };
});