const fs = require('fs');
const FormData = require('form-data');

exports.addRecipe = async (fetch, configuration, recipeName = '2', cookingSteps = '22',
  recipeComposition = '222', selectedTypes = 'Category1',
  publicationDate = '05/10/2019 14:10:10', image = fs.createReadStream('unnamed.jpg')) => {
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
        body: formData,
        timeout: 0
      }));
      const res = await (answer).json();
      console.log(res);
      return res.obj;
    } catch (e) {
      return false;
    }
  
};