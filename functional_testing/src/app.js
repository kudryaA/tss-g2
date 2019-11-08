const { runScenario } = require('./models/scenario');


const configuration = {
  host: 'http://0.0.0.0:7000'
};

runScenario(configuration);