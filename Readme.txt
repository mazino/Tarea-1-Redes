Integrantes:
	Nombre: Felipe Vásquez  Rol: 201104535-1
	Nombre: Cecilia Villarroel  Rol: 201104558-0

Consideraciones:
	Este es el cliente que se ejecuta en un pc externo al servidor, su puerto esta predefinido en el 8020, si se desea
	cambiar ir a webserver.java línea 9 y cambiar el puerto por el deseado.

	La IP del servidor se asume conocida, luego para que se pueda establecer el flujo de información se debe colocar la IP
	del servidor manualmente para esto, abrir el código peticionweb.java línea 12 y cambiar el String IPservidor por 
	el correspondiente.

	Para la apertura de los html se utilizó la librería commons-io-2.4 contenida en la carpeta lib.

	La página del chat (chat.html) se recarga cada 10 seg. para preguntar al servidor si existen archivos y mensajes
	nuevos.

Ejecución:
	Al momento de agregar un contacto utilizar IPv4 correspondiente al pc donde se ejecutara ese contacto.

	Para entrar a chatear con un contacto escribir el nombre del contacto en el input  y apretar botton chatear.

	Para envió de archivos, el archivo debe encontrarse en la carpeta de la aplicación, para efectos de esta tarea en la 
	carpeta Tarea1 (según repositorio github).
