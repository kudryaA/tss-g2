const FormData = require('form-data');

exports.deleteRecipe = async(fetch, configuration, id) => {
  const { host } = configuration;
  const formData = new FormData();
  formData.append('recipeId', id);
  const answer = await (fetch(`${host}/delete/recipe`, {
    method: 'POST',
    body: formData
  }));
  const res = await (answer).json();
};