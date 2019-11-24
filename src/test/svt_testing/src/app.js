const { runScenario } = require('./models/scenario');
const parallel = require('run-parallel')
 

const configuration = {
  host: 'http://g2.sumdu-tss.site'
};

async function runParalel() {
  await runScenario(configuration);
}

runParalel();