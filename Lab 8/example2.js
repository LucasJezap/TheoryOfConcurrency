function printAsync(s, cb) {
    var delay = Math.floor((Math.random() * 1000) + 500);
    setTimeout(function () {
      console.log(s);
      if (cb) cb();
    }, delay);
  }
  
  
  function execute(tasks) {
  
    function iterate(index) {
      // tasks are finished
      if (index === tasks.length) {
        console.log("done");
        return;
      }
  
      // set the current task
      const task = tasks[index];
  
      /* executes the current task passing the 'iterate' function as a callback, it will be called by the task itself */
      task(() => iterate(index + 1));
    }
  
    return iterate(0);
  }
  
  
  function task1(cb) {
    printAsync("1", function () {
      cb();
    });
  }
  
  function task2(cb) {
    printAsync("2", function () {
      cb()
    });
  }
  
  function task3(cb) {
    printAsync("3", function () {
      cb()
    });
  }
  
  execute([task1, task2, task3]);