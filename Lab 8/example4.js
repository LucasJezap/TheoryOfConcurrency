async function printAsync(s) {
    var delay = Math.floor((Math.random()*1000)+500);
    return new Promise((resolve) => {
        setTimeout(function() {
            console.log(s);
            resolve();
        }, delay);
    })
 }
 
 async function loop(m) {
     for (i=0; i<m; i++) {
         await printAsync("1");
         await printAsync("2");
         await printAsync("3");
     }
 }
 
 loop(4);