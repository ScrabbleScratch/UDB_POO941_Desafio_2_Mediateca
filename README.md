# Mediateca
Para correr este programa, es necesario primero crear una base de datos local de MySQL y ejecutar el script que se encuentra en `Database/DB_Script.sql`. De esta manera se creara la base de datos con las tablas y usuario necesario para funcionar.

**Importante**
El programa está configurado para funcionar con una base de datos alojada en |`localhost`, por lo que si se usa una base de datos en otra ubicación, se tiene que modificar la string de conexión en el archivo `Mediateca/src/MediatecaDB/ConexionDB.java` con la string de conexión para la base de datos a utilizar.

En el programa se ha utilizado el conector para MySQL `mysql-connector-j-8.4.0.jar` descargado de la página oficial de MySQL. Esto debido a errores presentados por el conector incluído por defecto.