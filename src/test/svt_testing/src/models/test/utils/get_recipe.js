const FormData = require('form-data');

exports.getRecipe = async(fetch, configuration, id) => {
  const { host } = configuration;
  const formData = new FormData();
  formData.append('recipeId', id);
  const answer = await (fetch(`${host}/recipe`, {
    method: 'POST',
    body: formData,
    timeout: 0
  }));
  const res = await (answer).json();
  return res;
};