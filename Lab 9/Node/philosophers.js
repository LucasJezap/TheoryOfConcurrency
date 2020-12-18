// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

var async = require('async');

var Fork = function() {
    this.state = 0;
    return this;
}

var Arbitrator = function(philosophers_count) {
    this.count = philosophers_count - 1;
    return this;
}

Arbitrator.prototype.acquire = async function(cb) {
    time = 1;
    await sleep(1);
    while (this.count == 0) {
        time *= 2;
        await sleep(Math.min(Math.floor(Math.random() * (time)), 2^10));
    }
    this.count -= 1;
}

Arbitrator.prototype.release = function() {
    this.count += 1;
}

function sleep(ms) {
    return new Promise((resolve) => {
        setTimeout(resolve, ms);
    });
}

Fork.prototype.acquire = async function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
    time = 1;
    await sleep(1);
    while (this.state != 0) {
        time *= 2;
        await sleep(Math.min(Math.floor(Math.random() * (time)), 2^10));
    }
    this.state = 1;
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = async function(count, timesx) {
    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
    return new Promise(async resolve => {
        for (var i=0; i<count; i++) {
            const start = process.hrtime();
            await this.forks[this.f1].acquire();
            await this.forks[this.f2].acquire();
            this.forks[this.f1].release();
            this.forks[this.f2].release();

            await sleep(0);
            const [seconds, nanoseconds] = process.hrtime(start);
            timesx[this.id] += seconds * 1e9 + nanoseconds;
        }
        resolve(false);
    });
}

Philosopher.prototype.startAsym = async function(count, timesx) {
    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
    return new Promise(async resolve => {
        for (var i=0; i<count; i++) {
            const start = process.hrtime();
            if (this.id % 2 == 0) {
                await this.forks[this.f2].acquire();
                await this.forks[this.f1].acquire();
            }
            else {
                await this.forks[this.f1].acquire();
                await this.forks[this.f2].acquire();
            }
            this.forks[this.f1].release();
            this.forks[this.f2].release();

            await sleep(0);
            const [seconds, nanoseconds] = process.hrtime(start);
            timesx[this.id] += seconds * 1e9 + nanoseconds;
        }
        resolve(false);
    });
}

Philosopher.prototype.startConductor = async function(count, arbitrator, timesx) {
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
    return new Promise(async resolve => {
        for (var i=0; i<count; i++) {
            const start = process.hrtime();
            await arbitrator.acquire();
            await this.forks[this.f1].acquire();
            await this.forks[this.f2].acquire();
            this.forks[this.f1].release();
            this.forks[this.f2].release();
            arbitrator.release();

            await sleep(0);
            const [seconds, nanoseconds] = process.hrtime(start);
            timesx[this.id] += seconds * 1e9 + nanoseconds;
        }
        resolve(false);
    });
}


// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// a nie każdego z osobna

async function getTwoForks(forks, f1, f2) {
    time = 1;
    await sleep(1);
    while (forks[f1].state != 0 || forks[f2].state != 0) {
        time *= 2;
        await sleep(Math.min(Math.floor(Math.random() * (time)), 2^10));
    }
    forks[f1].state = 1;
    forks[f2].state = 1;
}

Philosopher.prototype.startStarvation = async function(count, timesx) {
    return new Promise(async resolve => {
        for (var i=0; i<count; i++) {
            const start = process.hrtime();
            await getTwoForks(this.forks, this.f1, this.f2);
            this.forks[this.f1].release();
            this.forks[this.f2].release();
    
            await sleep(0);
            const [seconds, nanoseconds] = process.hrtime(start);
            timesx[this.id] += seconds * 1e9 + nanoseconds;
        }
        resolve(false);
    });
}

strategies = ['naive', 'asym', 'conductor', 'starvation'];
strategy = 1;
var cycles = 10;
var N = 5;
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}


async function run(timesx, strategy, philosophers_count) {
    const promises = [];
    var arbitrator = new Arbitrator(philosophers_count);
    for (var i = 0; i < N; i++) {
        if (strategy === 'naive')  promises.push(philosophers[i].startNaive(cycles, timesx));
        else if (strategy === 'asym')  promises.push(philosophers[i].startAsym(cycles, timesx));
        else if (strategy === 'conductor')  promises.push(philosophers[i].startConductor(cycles, arbitrator, timesx));
        else if (strategy === 'starvation')  promises.push(philosophers[i].startStarvation(cycles, timesx));
    }
    return Promise.all(promises);
}

async function calculateTimes(timesx, strategy, philosophers_count) {
    const x = await run(timesx, strategy, philosophers_count);
    for (var i =0; i<philosophers_count; i++) {
        timesx[i] /= cycles;
        var seconds = Math.floor(timesx[i] / 1e9);
        var milliseconds = Math.floor((timesx[i] % 1e9) / 1e6);
        var microseconds = Math.floor((timesx[i] % 1e6) / 1e3);
        var nanoseconds = (timesx[i] % 1e3);
        console.log("Philosopher " + i + " average time waiting: \n" +
                    "seconds: " + seconds + "\nmilliseconds: " + milliseconds + 
                    "\nmicroseconds: " + microseconds + "\nnanoseconds: " + nanoseconds);
    }
}

timesx = []
for (var i=0; i<N; i++) {
    timesx.push(0.0);
}

calculateTimes(timesx, strategies[strategy], N);
console.log("Number of cycles: " + cycles);
console.log("Strategy: " + strategies[strategy]);