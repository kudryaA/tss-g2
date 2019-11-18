const { runScenario } = require('./models/scenario');
const parallel = require('run-parallel')
 

const configuration = {
  host: 'http://0.0.0.0:7000'
};

async function runParalel() {
  await runScenario(configuration);
}

let arr = [];
for (let i = 0; i <= 100; i++) {
  arr.push(runParalel);
}

parallel(arr);