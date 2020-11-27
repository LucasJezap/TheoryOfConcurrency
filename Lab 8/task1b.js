function printAsync(s, cb) {
    var delay = Math.floor((Math.random() * 1000) + 500);
    setTimeout(function () {
      console.log(s);
      if (cb) cb();
    }, delay);
  }
  
  function task(n) {
    return new Promise((resolve, reject) => {
      printAsync(n, function () {
        resolve(n);
      });
    });
  }
  
  
  // 'then' returns a new Promise, therefore we can chain another 'then'.
  // In this case 'task(x)' directly returns a Promise object, however
  // 'then' could also return a value in which case it would be wrapped
  // in a Promise that would be automatically resolved with that value.
//   task(1).then((n) => {
//     console.log('task', n, 'done');
//     return task(2);
//   }).then((n) => {
//     console.log('task', n, 'done');
//     return task(3);
//   }).then((n) => {
//     console.log('task', n, 'done');
//     console.log('done');
//   });
  
  /*
  ** Zadanie:
  ** Napisz funkcje loop(m), ktora powoduje wykonanie powyzszej
  ** sekwencji zadan m razy.
  **
  */

// waterfall(tasks, callback)
// Runs the tasks array of functions in series, 
// each passing their results to the next in the array. 
// However, if any of the tasks pass an error to their own callback,
// the next function is not executed, and the main callback is
// immediately called with the error.
var async = require('async')
// need callback for waterfall
function printTasks(cb) {
    task(1).then((n) => {
        console.log('task', n, 'done');
        return task(2);
      }).then((n) => {
        console.log('task', n, 'done');
        return task(3);
      }).then((n) => {
        console.log('task', n, 'done');
        cb();
      });
}

tasks = [printTasks, printTasks, printTasks, printTasks]
async.waterfall(tasks, () => console.log("end of loop"))