DROP DATABASE IF EXISTS videoclub_pro;
/*CREATE DATABASE videoclub_pro;*/
CREATE DATABASE IF NOT EXISTS videoclub_pro;
USE videoclub_pro;
CREATE TABLE pelicula(
id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
titulo VARCHAR(100) NOT NULL,
descripcion TEXT NOT NULL,
anyo_publicacion INT UNSIGNED NOT NULL,
duracion VARCHAR(10) NOT NULL,
categoria ENUM("ACCION", "AVENTURAS", "CIENCIA_FICCION", "COMEDIA", "DOCUMENTAL", "DRAMA", "FANTASIA", "MUSICAL", "SUSPENSE", "TERROR", "BÉLICA") NOT NULL,
formato ENUM("XVID", "DIVX", "MP4", "H264", "FLV") NOT NULL,
valoracion ENUM("UNA","DOS","TRES","CUATRO","CINCO") NOT NULL,
caratula LONGBLOB,
url TEXT
);
CREATE TABLE equipo(
id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
nombre VARCHAR(50) NOT NULL,
apellidos VARCHAR(50) NOT NULL,
anyo_nacimiento INT UNSIGNED NOT NULL,
pais VARCHAR(50) NOT NULL, 
puesto ENUM("ACTOR", "DIRECTOR") NOT NULL
);
CREATE TABLE usuario(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL,
apellidos VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
contrasenya TEXT NOT NULL,
direccion VARCHAR(100) NOT NULL,
activo BOOLEAN NOT NULL,
fecha_registro DATE NOT NULL,
rol ENUM ("ADMIN","USER")
);
CREATE TABLE alquiler(
id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
fecha_alquiler DATE NOT NULL,
id_cliente INT NOT NULL,
id_pelicula INT NOT NULL,
fecha_retorno DATE NOT NULL
);
CREATE TABLE pago(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
id_cliente INT NOT NULL,
id_alquiler INT NOT NULL,
cantidad DOUBLE NOT NULL,
fecha_pago DATE NOT NULL,
completado BOOLEAN NOT NULL
);


INSERT INTO usuario (nombre, apellidos, email, direccion, activo, contrasenya, fecha_registro, rol)
VALUES ("admin", "admin", "admin@admin.com", "", 1, "$2y$10$/zY780DscVJryXd7Ij5VFu/rrfaDgsl7B32pMBVfWF9Z.AgfW5eHG", CURDATE(),"ADMIN");

INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("El Padrino","América, años 40. Don Vito Corleone (Marlon Brando) es el respetado y temido jefe de una de las cinco familias de la mafia de Nueva York. Tiene cuatro hijos: Connie (Talia Shire), el impulsivo Sonny (James Caan), el pusilánime Fredo (John Cazale) y Michael (Al Pacino), que no quiere saber nada de los negocios de su padre. Cuando Corleone, en contra de los consejos de Il consigliere Tom Hagen (Robert Duvall), se niega a participar en el negocio de las drogas, el jefe de otra banda ordena su asesinato. Empieza entonces una violenta y cruenta guerra entre las familias mafiosas",1972,175,'DRAMA','DIVX','CINCO',
 LOAD_FILE("/var/lib/mysql-files/samples/el-padrino-1.jpg"), "https://drive.google.com/u/0/uc?id=14_fyqRFbgTyg2MRRK_axI_rIV6hqAEdX");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url)
VALUES ("El Padrino 2","Continuación de la historia de los Corleone por medio de dos historias paralelas: la elección de Michael como jefe de los negocios familiares y los orígenes del patriarca, Don Vito Corleone, primero en su Sicilia natal y posteriormente en Estados Unidos, donde, empezando desde abajo, llegó a ser un poderosísimo jefe de la mafia de Nueva York",1974,200,'DRAMA','MP4','CINCO',
 LOAD_FILE("/var/lib/mysql-files/samples/el-padrino-2.jpg)", "https://drive.google.com/u/0/uc?id=1KmtCsAtE5ldDkjWQRye-GIo5nDjNME-I");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("Senderos de gloria","Primera Guerra Mundial (1914-1918). En 1916, en Francia, el general Boulard ordena la conquista de una inexpugnable posición alemana y encarga esa misión al ambicioso general Mireau. El encargado de dirigir el ataque será el coronel Dax. La toma de la colina resulta un infierno, y el regimiento emprende la retirada hacia las trincheras. El alto mando militar, irritado por la derrota, decide imponer al regimiento un terrible castigo que sirva de ejemplo a los demás soldados",1958,87,'BÉLICA','H264','TRES',
 LOAD_FILE("/var/lib/mysql-files/samples/senderos-gloria.jpg"), "https://drive.google.com/u/0/uc?id=1-K8QPeySBLiDTkmolS70C_1N_L7FZ4fS");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("Primera plana","Chicago, 1929. Earl Williams, convicto del asesinato de un policía, espera en la cárcel el momento de su ejecución. Mientras tanto, en la sala de prensa del Tribunal Supremo, un grupo de periodistas espera el indulto o la confirmación de la sentencia. Hildy Johnson, el cronista de sucesos del Chicago Examiner, que tendría que cubrir la información, está a punto de contraer matrimonio y abandonar su trabajo pero Walter Burns, el maquiavélico director del periódico, empeñado en retenerlo, tratará de impedir su boda por todos los medios",1974,105,'COMEDIA','MP4','CUATRO',
 LOAD_FILE("/var/lib/mysql-files/samples/primera_plana.jpg"), "https://drive.google.com/u/0/uc?id=16GHmCEqniRAFS5cohf_c1hX5p4aBsWk1");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("Resident Evil","En un centro clandestino de investigación genética, se produce un brote vírico. Para contener la fuga, se sella toda la instalación y, en principio, se cree que mueren todos los empleados, pero en realidad se convierten en feroces zombis.",2003,100,'ACCION','DIVX','CINCO', 
LOAD_FILE("/var/lib/mysql-files/samples/resident-evil.jpg"), "https://drive.google.com/u/0/uc?id=1GFLmU4Kx82BPO2HHojOr9xReQmyWcWoU");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("Resident Evil 2","Después del brote del virus-T en el laboratorio subterráneo La Colmena, un grupo de científicos abre la instalación para investigar lo que ocurrió luego de capturar a Alice Abernathy y Matt, pero en el trayecto son atacados por los zombis y mutantes del virus-T",2004,94,'ACCION','FLV','CINCO', 
LOAD_FILE("/var/lib/mysql-files/samples/resident-evil-2.jpg"), "https://drive.google.com/u/0/uc?id=1c4pl9tiEB5m7lrnO-Le6ztl3zNXn21W6");
INSERT INTO pelicula (titulo, descripcion, anyo_publicacion, duracion, categoria, formato, valoracion, caratula, url) 
VALUES ("Titanic","Relata la relación de Jack Dawson y Rose DeWitt Bukater, dos jóvenes que se conocen y se enamoran a bordo del transatlántico RMS Titanic en su viaje inaugural desde Southampton (Inglaterra) a Nueva York (Estados Unidos) en abril de 1912. Pertenecientes a diferentes clases sociales, intentan salir adelante pese a las adversidades que los separarían de forma definitiva, entre ellas el prometido de Rose, Caledon «Cal» Hockley (un adinerado del cual ella no está enamorada, pero su madre la ha obligado a permanecer con él para garantizar un futuro económico próspero) y el hundimiento del barco tras chocar con un iceberg. ",1997,100,'DRAMA','XVID','CUATRO',  
LOAD_FILE("/var/lib/mysql-files/samples/titanic.jpg"), "https://drive.google.com/u/0/uc?id=1adcHHHrr79JkIrPHojPcF4WWL_M31E51");

INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Francis","Ford Coppola",1939,"Estados Unidos", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Stanley","Kubrick",1928,"Estados Unidos", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Billy","Wilder",1906,"Polonia", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("John","Carpenter",1948,"Estados Unidos", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("James","Cameron",1954,"Canadá", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Pedro","Almodovar",1949,"España", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Martin","Scorsese",1942,"Estados Unidos", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Quentin","Tarantino",1963,"Estados Unidos", "DIRECTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Woody","Allen",1935,"Estados Unidos", "DIRECTOR");

INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Marlon","Brandon",1924,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Al","Pacino",1940,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Robert","Duvall",1931,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("James","Cann",1940,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Diane","Keaton",1946,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Robert","de Niro",1943,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Kirk","Douglas",1916,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Ralph","Meeker",1920,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Adolphe","Menjou",1890,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Jack","Lemmon",1925,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Walter","Matthau",1920,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Susan","Sarandon",1946,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Leonardo","DiCaprio",1974,"Estados Unidos", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Mila","Jovovich",1975,"Ucrania", "ACTOR");
INSERT INTO equipo (nombre, apellidos, anyo_nacimiento, pais, puesto) VALUES ("Kate","Winslet",1975,"Inglaterra", "ACTOR");



