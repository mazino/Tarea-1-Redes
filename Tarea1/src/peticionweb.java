import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

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

	private Socket scliente = null;		// representa la petición de nuestro cliente
   	private PrintWriter out = null;		// representa el buffer donde escribimos la respuesta

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
  			out = new PrintWriter(new OutputStreamWriter(scliente.getOutputStream()),true) ;
  			    
           
  			List<String> list = new ArrayList<String>();
  			String line = "";
  			
            while((line = in.readLine()) != null){
            	list.add(line);
            	depura("--" + line + "-");
            	
            	
            	if(line.isEmpty()){
	            	cargarpagina(list);
	            	list = new ArrayList<String>();
	            	
            	}
            	
            	
            }
            
           
            list.clear();
  		
		}
		catch(Exception e)
		{
			depura("Error en servidor\n" + e.toString());
		}
		
		depura("Hemos terminado");
	}
	
	
	void cargarpagina(List<String> lista)
	{
			String html ="<html><body>  <center> <title>Avioncito de papel</title>"+
					
					"<form action='agregarcontacto.html' method='get'>"+
					"<input type='submit' value='Agregar contacto' /> "+
					"<a href='agregarcontacto.html'> Volver atras</a>"+
									 
					
					"</form></center> </body> </html>";
			 
			
		
			String[] req = lista.get(0).split(" ");
			
			if(req[0].equals("GET")){
				
				
				
				File archivo = null;
			    FileReader fr = null;
			    BufferedReader br = null;
			    
			    try {                
			    	archivo = new File("contactos.txt");
			    	if ( !archivo.exists())
			    	{
			    		archivo.createNewFile();
			    	}
			    	fr = new FileReader(archivo);
			    	br = new BufferedReader(fr);
			    	
			    	String linea;
			    	String[] aux;
			    	
			    	out.println("<table>");
			    	out.println("<tr><th scope='col'>Contactos</th></tr>");
	        		
			    	/*Lectura del archivo contactos.txt y escritura en el html*/
			        while((linea=br.readLine())!=null)
			        {	
			        	aux = linea.split("&");
			        	if(aux[0].split("=")[0].equals("Nombre"))
			        		
			        		out.println("<tr>");			    			        	
			        		out.println("<td>"+aux[0].split("=")[1]+"</td>");
			        		out.println("</tr>"); 
			       
			        } 
			     	
			        out.println("</table>");
			        
			        out.println(html);
		 
		        } catch (Exception e) {
		 
		        	e.printStackTrace();
		 
		        }finally{
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		      }
	    		
	    		
	     	   
	    	}
	    	else if(req[0].equals("POST")){
	    		try
	    		{	
	    			InputStream archivo = new FileInputStream ("agregarcontacto.html");
	                String home = IOUtils.toString(archivo, "UTF-8");
	                out.println(home); 
	               
	               
	    		}
	    		
	    		catch(Exception e)
	    		{
	    			depura("Error en servidor\n" + e.toString());
	    		}
	  
	    	}
		
	}
	
	
}