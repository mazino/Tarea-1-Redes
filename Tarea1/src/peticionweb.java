import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

class peticionWeb extends Thread
{
	
	final int DEBUG = 2;

	
	private Socket scliente = null;		
   	private PrintWriter out = null;		
	private BufferedReader in = null;

   	peticionWeb(Socket ps)
   	{
		scliente = ps;
   	}

	public void run() 
	{
		System.out.println("Procesamos conexion");

		try
		{	
			in = new BufferedReader (new InputStreamReader(scliente.getInputStream()));
  			out = new PrintWriter(new OutputStreamWriter(scliente.getOutputStream()),true) ;
  			        
           		
  			List<String> list = new ArrayList<String>();
  			String line = "";
  			
  			while ((line = in.readLine()) != null ){
  				list.add(line);
  				System.out.println("--" + line + "-");
            	
            	if(line.split("=")[0].equals("Nombre"))
            	{
            		agregarcontacto(line);
            	}
            	if(line.isEmpty()){
	            	cargarpagina(list); 
	            }
            	
            }
  			out.close();
            list.clear();
            
		}
		catch(Exception e)
		{
			System.out.println("Error en servidor\n" + e.toString());
		}
		
		
		System.out.println("Hemos terminado");
	}
	
	
	void cargarpagina(List<String> lista)
	{
		
			String[] req = lista.get(0).split(" ");
			
			
			String url = req[1];

			if(url.equals("/inicio.html") || url.equals("/")){
				
				File archivo = null;
			    FileReader fr = null;
			    BufferedReader br = null;
			    
			    try {                
			    	archivo = new File("contactos.txt");
			    	if ( !archivo.exists())
			    	{	
			    		//Si el archivo contacto no existe se crea vacio
			    		archivo.createNewFile(); 
			    	}
			    	fr = new FileReader(archivo);
			    	br = new BufferedReader(fr);
			    	
			    	String linea;
			    	String[] aux;
			    	String nombre;
			    	
			    	out.println("<table>");
			    	out.println("<tr><th scope='col'>Contactos</th>");
			    	out.println("<th scope='col'>IP</th>");
			    	out.println("<th scope='col'>Puerto</th></tr>");
			    				    	
			    	/*Lectura del archivo contactos.txt y escritura en el html*/
			        while((linea=br.readLine())!=null)
			        {	
			        	aux = linea.split("&");
			        	nombre = aux[0].split("=")[1].replace("+"," ");
			        	
			        	out.println("<tr>");			    			        	
		        		out.println("<td>"+nombre+"</td>");
		        		out.write("<td>"+ aux[1].split("=")[1] +"</td>\n");
		                out.write("<td>"+ aux[2].split("=")[1] +"</td>\n");
		        		out.println("</tr>");
			        
			        		
			        } 
			     	
			        out.println("</table>");
			     

	    			InputStream a = new FileInputStream ("inicio.html");
	                String html = IOUtils.toString(a, "UTF-8");
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
						
			else if(req[1].equals("/agregarcontacto.html?")){
				try
	    		{	
					
					InputStream archivo = new FileInputStream ("agregarcontacto.html");
	                String html = IOUtils.toString(archivo, "UTF-8");
	                out.println(html);    
	                
	    		}
	    		
	    		catch(Exception e)
	    		{
	    			System.out.println("Error en servidor\n" + e.toString());
	    		}
				
			}
			
	      if(req[0].equals("POST")){
	    		/**Aqui se supone que deberia estar la funcion agregarcontacto, la cual guarda el 
	    		 * nuevo contacto en el .txt pero, por alguna razon guado se da click en el 
	    		 * boton guardar contacto los datos no se leen y se queda como en espera... >:*/
	  
	    	}
	}
	
	void agregarcontacto(String contacto){
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("contactos.txt",true);
            pw = new PrintWriter(fichero);
            
            
            /**Nos aseguramos que los 3 campos sean rellenados */
            if (contacto.split("&")[0].split("=").length==2 &&
            		contacto.split("&")[1].split("=").length==2 &&
            			contacto.split("&")[2].split("=").length==2)
            				
            				pw.println(contacto);
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
              if (null != fichero)
            	  fichero.close();
           } catch (Exception e2) {
        	   e2.printStackTrace();
           }
        }
	}
	
	
	
}