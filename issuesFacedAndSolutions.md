# issues faced
## using @Enumerated(EnumType.String):
- while creating user object and saving to db, we used EnumType.String to strore the actual value in form of string(ADMIN or CUSTOMER). By default is is EnumType.Ordinal which stores numerical values (0, 1, ...).
## using @Query for customized db search:
- we can use this annotation and provide customized sql query for CRUD. Here I have used search products by productName using like keyword. The query written is in JPQL (which works with entities and objects). SQL query is generated based on the query passed.
