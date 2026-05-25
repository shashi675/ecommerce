# issues faced
## using @Enumerated(EnumType.String):
- while creating user object and saving to db, we used EnumType.String to strore the actual value in form of string(ADMIN or CUSTOMER). By default is is EnumType.Ordinal which stores numerical values (0, 1, ...).
## using @Query for customized db search:
- we can use this annotation and provide customized sql query for CRUD. Here I have used search products by productName using like keyword. The query written is in JPQL (which works with entities and objects). SQL query is generated based on the query passed.
## pushed the log file by mistake:
- without adding the log file to .gitignore file, I pushed the changes to Github.
- For correction, I added the file name to .gitignore and then pushed. But in the previous commit, the file was still present.
- I went back to previous commit and make the required changes in the .gitignore file and finally pushed it.
