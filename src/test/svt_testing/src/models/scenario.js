const nodeFetch = require('node-fetch');
const fetch = require('fetch-cookie')(nodeFetch);
const { testAuth } = require('./test/test_auth');
const { testRecipe } = require('./test/test_recipe');

exports.runScenario = async (configuration) => {
  const result = [];
  await testAuth(fetch, configuration, result);
  for (let i = 0; i < 100; i++) {
    await testRecipe(fetch, configuration, result);
  }
  console.log(result);
  console.log(result.filter(item => item.status).length / result.length);
};