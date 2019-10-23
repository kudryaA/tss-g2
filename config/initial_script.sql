drop sequence recipeSeq;
drop table person;
drop table moderator;
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
  email TEXT
);

CREATE TABLE moderator (
  name TEXT,
  login TEXT PRIMARY KEY,
  password TEXT
);

create table type (
  name Text PRIMARY KEY ,
  description Text
);

create table recipe (
  recipe_id INT PRIMARY KEY ,
  name text ,
  recipeComposition text ,
  cookingSteps text ,
  publicationDate Date ,
  image text,
  creator text,
  rating INT
);

create table recipeType (
  recipe_id INT references recipe(recipe_id) ,
  type_name Text references type(name)
);
