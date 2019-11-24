const { deleteRecipe } = require('./utils/delete_recipe');
const { addRecipe } = require('./utils/add_recipe');
const { getRecipe } = require('./utils/get_recipe');

exports.testRecipe = async (fetch, configuration, result) => {
  const id = await addRecipe(fetch, configuration);
  //console.log(id);
  result.push({
    action: 'Add recipe',
    status: id ? true : false
  });

};