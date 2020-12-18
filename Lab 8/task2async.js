// modules
const walkdir = require('walkdir');
const fs = require('fs');
const async = require('async');
const {performance} = require('perf_hooks');
const { count } = require('console');


let globalCount = 0;
// counting lines in file
// adding callback to given function
function countLinesInFile(file , cb) {
    var count = 0;
    fs.createReadStream(file).on('data', function(chunk) {
        count = chunk.toString('utf8')
        .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
        .length-1;
        globalCount += count;
    }).on('end', function() {
        console.log(file, count);
        cb();
    }).on('error', function(err) {
        console.error(err);
        cb();
    });
}

// getting paths
var paths = walkdir.sync('traceroute');

// returning task for given path
function getPathTask(path) {
    const task = (cb) => countLinesInFile(path, cb);
    return task;
}

var pathTasks = paths.map(path => getPathTask(path));

// pattern 
function asyncPaths(tasks, cb) {
    var time = performance.now();
    var counter = tasks.length;
    var callback = function() {
        counter--;
        if (counter == 0) {
            console.log("Time elapsed (in milliseconds): ", performance.now() - time);
            cb();
        }
    }
    for (var i=0; i<tasks.length; i++) {
        tasks[i](callback);
    }
}

asyncPaths(pathTasks, () => console.log("Global count: ", globalCount));