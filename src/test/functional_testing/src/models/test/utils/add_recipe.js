const fs = require('fs');
const FormData = require('form-data');

exports.addRecipe = async (fetch, configuration, recipeName = '1', cookingSteps = '11',
  recipeComposition = '111', selectedTypes = 'dessert/healthy food/fish',
  publicationDate = '05/11/2019 14:10:10', image = fs.createReadStream('unnamed.jpg')) => {
    try {
      const { host } = configuration;
      const formData = new FormData();
      formData.append('recipeName', recipeName);
      formData.append('cookingSteps', cookingSteps);
      formData.append('recipeComposition', recipeComposition);
      formData.append('selectedTypes', selectedTypes);
      formData.append('publicationDate', publicationDate);
      formData.append('image', image);
      const answer = await (fetch(`${host}/add/recipe`, {
        method: 'POST',
        body: formData
      }));
      const res = await (answer).json();
      return res.obj;
    } catch (e) {
      return false;
    }
  
};