import java.io.*;
import java.net.*;


public class webserver
{
	int puerto = 8000;
	
	public static void main(String [] array) 	
	{
		webserver instancia = new webserver();	
		instancia.arranca();
		
		
	}
	
	boolean arranca()
	{
		System.out.println("Arrancamos nuestro servidor");
		try
		{	
			ServerSocket s = new ServerSocket(puerto);
			
			System.out.println("Quedamos a la espera de conexion");
						
			while(true)  
			{
				Socket entrante = s.accept();
				peticionWeb pCliente = new peticionWeb(entrante);
				pCliente.start();
				
			}
		
		}
		catch(Exception e)
		{
			System.out.println("Error en el servidor\n" + e.toString());
					
		}
		
		return true;
	}
	
}