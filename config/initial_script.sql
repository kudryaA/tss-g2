drop sequence recipeSeq;
drop table person;
drop table recipeType;
drop table type;
drop table recipe;

create sequence recipeSeq;

create table person (
  login TEXT PRIMARY KEY ,
  password TEXT ,
  name TEXT ,
  surname TEXT ,
  bannedStatus boolean ,
  email TEXT ,
  role TEXT
);

create table type (
  name Text PRIMARY KEY ,
  description Text ,
  image Text
);

create table recipe (
  recipe_id INT PRIMARY KEY ,
  name text ,
  recipeComposition text ,
  cookingSteps text ,
  publicationDate TIMESTAMP ,
  image text,
  creator text,
  rating BIGINT,
  isConfirmed boolean
);

create table recipeType (
  recipe_id INT references recipe(recipe_id) ,
  type_name Text references type(name)
);
