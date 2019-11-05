const nodeFetch = require('node-fetch');
const fetch = require('fetch-cookie')(nodeFetch);
const { testAuth } = require('./test/test_auth');
const { testRecipe } = require('./test/test_recipe');

exports.runScenario = async (configuration) => {
  const result = [];
  await testAuth(fetch, configuration, result);
  await testRecipe(fetch, configuration, result);
  console.log(result);
};