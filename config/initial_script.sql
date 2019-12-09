drop table api_time;
drop table mailConfirmation;
drop table keys;
drop table comment;
drop table userSubscribe;
drop table likes;
drop table users_rating;
drop table person;
drop table recipeType;
drop table type;
drop table recipe;


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


create table userSubscribe (
  user_login Text references person(login),
  sub_login Text references person(login)
);

create table mailConfirmation (
  login Text references person(login),
  confirmationKey Text unique
);

create table likes (
  user_login Text references person(login),
  recipe_id Text references recipe(recipe_id)
);

create table keys (
  login Text references person(login),
  key Text unique,
  numberOfDownload int default 0
);

create table api_time(
    api TEXT,
    time_execution BIGINT
);

create table users_rating(
     user_login Text references person(login),
     rating BIGINT
);