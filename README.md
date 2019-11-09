# Fyre
## Api
Сервер возвращает json. В json обязательно есть поле status которое показывает успешность выполнение операции.
Так же может появится поле obj. В нём будет находится дополнительная информация к ответу(к примеру список рецептов).
* /test - проверка подключения к серверу;
* /session - информация про сессию
* /logout - разлогинится
* /login?login={}&password={} - проверка авторизации пользователя. Результат авторизации будет сохранен как булевое значение в дополнительной информации к ответ;
* /registration?login={}&password={}&name={}&surname={}&email={} - регистрация пользователя. Результат регистрации будет сохранен как булевое значение в дополнительной информации к ответу.
* /add/recipe?recipeName={}&recipeComposition={}&cookingSteps={}&publicationDate={}&selectedTypes={}&image={} - добавление рецепта. Результат добавления будет сохранен как булевое значение в дополнительной информации к ответу.
* /add/type?typeName={}&description={}&image={} - добавление типа. Результат добавления будет сохранен как булевое значение в дополнительной информации к ответу.
* /change/status?userLogin={} - изменение статуса пользователя. Результат изменения будет сохранен как булевое значение в дополнительной информации к ответу.
* /delete/recipe?recipeId={} - удаление рецепта по имени. Результат удаления будет сохранен как булевое значение в дополнительной информации к ответу.
* /select/types - получение информации о типах рецептов. Результат будет сохранен как список объектов типа Type в дополнительной информации к ответу.
* /select/users - получение информации о пользователях. Результат будет сохранен как список объектов типа Person в дополнительной информации к ответу.
* /recipe?recipeId={} - получение информации про рецепт по айди
* /select/recipes?pageNumber={}&pageSize={}&recipeType={}&sortType={} - получение  информации про рецепты по заданым параметрам и количесво страниц которые можно получить из них.
* /image?id={} - получить картинку рецепта. get запрос. Пример  /image?id=42424shfh
* /search/recipe?ingredientName={} - поиск рецепта по названию ингредиента.
* /update/recipe?recipeId={}&recipeName={}&composition={}&cookingSteps - обновление рецепта по введенным параметрам.
* /select/unconfirmedRecipes - получить все рецепты что нуждаются в подтверждении.
* /recipeConfirmation?recipeId={} - подтвердить пользовательский рецепт по id. Результат подтверждения будет сохранен как булевое значение в дополнительной информации к ответу.
* /add/comment?recipeId={}&commentText={} - добавление комментария к рецепту. Результат добавления будет сохранен как булевое значение в дополнительной информации к ответу.
* /select/comments?recipeId={} - получение информации о комментариях относящихся к выбраному рецепту. Результат будет сохранен как список объектов типа Comment в дополнительной информации к ответу.
* /store/time/interactive?page={}&time={} - отправка времени загруки страницы. page в формате index.html и time в милисекундах.
## Технологии
* java
* javalin
* postgres
* slf4j
* guava
* gson
* thymeleaf
* nodejs
* maven
## Инструкция по установке
1. Нужно установить java, maven, postgres(запомнить конфигурацию), git.
2. Склонировать репозиторий.
3. Настроить файл config/configuration.yml и файл config/password(пароль к базе)
4. Выполнить в базе скрипт config/initial_script.sql
5. Собрать и запустить jar файл
```
mvn package
java -jar target/fyre-1.0-SNAPSHOT.jar
```
