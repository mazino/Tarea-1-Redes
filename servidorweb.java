import java.io.*;
import java.net.*;
import java.util.*;

public class servidorweb
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
		servidorweb instancia = new servidorweb(array);	
		instancia.arranca();
	}
	
	// constructor que interpreta los parameros pasados
	servidorweb(String[] param)
	{
		procesaParametros();	
	}
	
	// parsearemos el fichero de entrada y estableceremos las variables de clase
	boolean procesaParametros()
	{
		return true;	
	}
	
	boolean arranca()
	{
		depura("Arrancamos nuestro servidor",DEBUG);
		
		try
		{
		
			
			ServerSocket s = new ServerSocket(8000);

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
