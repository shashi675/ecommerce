# issues faced
## using @Enumerated(EnumType.String):
- while creating user object and saving to db, we used EnumType.String to strore the actual value in form of string(ADMIN or CUSTOMER). By default is is EnumType.Ordinal which stores numerical values (0, 1, ...).
- 
