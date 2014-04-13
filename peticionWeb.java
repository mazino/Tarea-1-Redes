import java.io.*;
import java.net.*;
import java.util.*;



class peticionWeb extends Thread
{
	int contador = 0;

	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;

	void depura(String mensaje)
	{
		depura(mensaje,DEBUG);
	}	

	void depura(String mensaje, int gravedad)
	{
		System.out.println(currentThread().toString() + " - " + mensaje);
	}	

	private Socket scliente 	= null;		// representa la petición de nuestro cliente
   	private PrintWriter out 	= null;		// representa el buffer donde escribimos la respuesta

   	peticionWeb(Socket ps)
   	{
		depura("El contador es " + contador);
		
		contador ++;
		
		
		
		scliente = ps;
		setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja
   	}

	public void run() // emplementamos el metodo run
	{
		depura("Procesamos conexion");

		try
		{
			BufferedReader in = new BufferedReader (new InputStreamReader(scliente.getInputStream()));
  			out = new PrintWriter(new OutputStreamWriter(scliente.getOutputStream(),"8859_1"),true) ;


			String cadena = "";		// cadena donde almacenamos las lineas que leemos
			int i=0;				// lo usaremos para que cierto codigo solo se ejecute una vez
	
			do			
			{
				cadena = in.readLine();

				if (cadena != null )
				{
					// sleep(500);
					depura("--" + cadena + "-");
				}


				if(i == 0) // la primera linea nos dice que fichero hay que descargar
				{
			        i++;
			        
			        StringTokenizer st = new StringTokenizer(cadena);
                    
                    if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) 
                    {
                    	retornaFichero(st.nextToken()) ;
                    }
                    else 
                    {
                    	out.println("400 Petición Incorrecta") ;
                    }
				}
				
			}
			while (cadena != null && cadena.length() != 0);

		}
		catch(Exception e)
		{
			depura("Error en servidor\n" + e.toString());
		}
			
		depura("Hemos terminado");
	}
	
	
	void retornaFichero(String sfichero)
	{	
		depura("Recuperamos el fichero " + sfichero);
		
		// comprobamos si tiene una barra al principio
		if (sfichero.startsWith("/"))
		{
			sfichero = sfichero.substring(1) ;
		}
        
        // si acaba en /, le retornamos el index.htm de ese directorio
        // si la cadena esta vacia, no retorna el index.htm principal
        if (sfichero.endsWith("/") || sfichero.equals(""))
        {
        	
        	sfichero = sfichero + "index.html" ;
        	
        }
        
        try
        {
        	String responseString = 
                    "<form action=\"http://127.0.0.1:5000\" enctype=\"multipart/form-data\"" +
                    "method=\"post\">" +
                    "Enter the name of the File <input name=\"file\" type=\"file\"><br>" +
                    "<input value=\"Upload\" type=\"submit\"></form>" +
                    "Upload only text files." 
                   ;
        	 
        	
		    // Ahora leemos el fichero y lo retornamos
        	String WebHtmlR ="<!DOCTYPE html> <html> <head> <title>Avioncito de Papel</title> </head>"+
		                    "<form name='Agregar Contacto' method='POST'><center><h2>Agregar Contacto</h2>";
        	out.println(WebHtmlR);  
        	
		    File mifichero = new File(sfichero);
		    try {                
		    	 
	            mifichero.createNewFile();
	 
	        } catch (Exception e) {
	 
	            System.out.println("No se ha podido crear el archivo");
	 
	        }
		    
		    if (mifichero.exists()) 
		    {
		    	System.out.println("holaa");
	      		out.println("HTTP/1.0 200 ok");
	      		out.println("Server: Roberto Server/1.0");
				out.println("Date: " + new Date());
				out.println("Content-Type: text/html");
				out.println("Content-Length: " + mifichero.length());
				out.println("\n");
 
				BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
				
				
				String linea = "";
				
				do			
				{
					linea = ficheroLocal.readLine();
	
					if (linea != null )
					{
						// sleep(500);
						out.println(linea);
					}
				}
				while (linea != null);
				
				depura("fin envio fichero");
				
				ficheroLocal.close();
				out.close();
				
			}  // fin de si el fiechero existe 
			else
			{
				depura("No encuentro el fichero " + mifichero.toString());	
	      		out.println("HTTP/1.0 4000000 ok");
	      		out.close();
			}
			
		}
		catch(Exception e)
		{
			depura("Error al retornar fichero");	
		}

	}
	
}