drop table comment;
drop table person;
drop table recipeType;
drop table type;
drop table recipe;
drop table api_time;

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
  recipe_id Text PRIMARY KEY ,
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
  recipe_id Text references recipe(recipe_id) ,
  type_name Text references type(name)
);

create table comment (
  user_login Text references person(login) ,
  recipe_id Text references recipe(recipe_id) ,
  comment_text Text
);

create table api_time(
    api TEXT,
    time_execution BIGINT
);