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
  if (id) {
    const recipe = (await getRecipe(fetch, configuration, id)).obj;
    result.push({
      action: 'Get recipe',
      status: recipe.id == id
    });
    await deleteRecipe(fetch, configuration, id);
    const statusAfterDelete = (await getRecipe(fetch, configuration, id)).status;
    result.push({
      action: 'Delete recipe',
      status: !statusAfterDelete
    });
  }

};