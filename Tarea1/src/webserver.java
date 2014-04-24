import java.io.*;
import java.net.*;


public class webserver
{
	int puerto = 8000;
	
	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;
	
		
	// funcion para centralizar los mensajes de depuracion

	void depura(String mensaje)
	{
		depura(mensaje,DEBUG);
	}	

	void depura(String mensaje, int gravedad)
	{
		System.out.println("Mensaje: " + mensaje);
	}	
		
	// punto de entrada a nuestro programa
	public static void main(String [] array) 	
	{
		webserver instancia = new webserver();	
		instancia.arranca();
		
		
	}
	
	boolean arranca()
	{
		depura("Arrancamos nuestro servidor",DEBUG);
		
		try
		{	
			ServerSocket s = new ServerSocket(puerto);
			
			depura("Quedamos a la espera de conexion");
			
			while(true)  
			{
				Socket entrante = s.accept();
				peticionWeb pCliente = new peticionWeb(entrante);
				pCliente.start();
			}
		
		}
		catch(Exception e)
		{
			depura("Error en servidor\n" + e.toString());
			
		}
		
		return true;
	}
	
}