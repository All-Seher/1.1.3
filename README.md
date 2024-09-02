1. Добавил проперти файл SQL.properties, куда вынес все SQL-запросы для удобства 
2. Добавил в пакете Util класс SqlQuery, для загрузки пропертей с запросами из SQL.properties. И с помощью класса Property получаю в нужном месте строку с запросом при CRUD-операциях
3. Изменил в классе UserServiceImpl тип приватного поля на UserDao. Раньше использовал более конкретный тип(UserDaoJDBCImpl)
4. Изменил класс Util. Теперь в зависимости от того, ссылку какого типа передал переменной UserDao в классе UserServiceImpl. С помощью метода getDBConnection() теперь можно получить Connection, если реализация CRUD-операций через класс UserDaoJDBCImpl или получаем Session, если используем UserDaoHibernateImpl.      
