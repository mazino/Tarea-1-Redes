import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

class peticionWeb extends Thread
{
	private Socket s=null;
	
	private  int port;
	private  Socket scliente = null;		// representa la petici�n de nuestro cliente
   	private  PrintWriter out = null;		// representa el buffer donde escribimos la respuesta
	private  BufferedReader in = null;
	

   	peticionWeb(Socket ps, int puerto)
   	{
		scliente = ps;
		port = puerto;
   	}

	public void run() // implementamos el metodo run
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
            	if(line.isEmpty()){
	            	cargarpagina(list); 
	            }
            	
            }
  			list.clear();
  			out.close();
                      
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}


		System.out.println("Hemos terminado");
	}


	void cargarpagina(List<String> lista) throws IOException
	{		

			String[] req = lista.get(0).split(" ");
			String url = req[1];

			if(req[0].equals("POST")){	
				
		    	  String datos;
	              char[] aux = new char[100];
	              in.read(aux);
	              for (int i=0;i<100;i++)
	                  if(aux[i]==(char)0)
	                      aux[i]='*';
	              datos=new String(aux);
	              datos = datos.replace("*","");
	              System.out.println("Datos: " + datos);
	              
	              if(url.equals("/inicio.html")){
	            	  agregarcontacto(datos);
	              }
	              
	              else if(url.equals("/chat.html"))
				  {		
	            	  
	            	  Socket s;
	            	  s = new Socket("localhost",8080);
	            	  PrintWriter enviar = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
	            	  BufferedReader b = new BufferedReader (new InputStreamReader(s.getInputStream()));

	  				  String[] mensaje = datos.split("&")[0].split("="); //Verificar si el destinatario esta en la lista de contactos
	            	  String ver = verificarcontacto(mensaje[1]);
	  
	            	  if(ver != null){
	            		  //Guardar los mensajes enviados en un txt para mostrarlos   
	            		  String men = procesarmensaje(ver+"&"+datos); //este es el mensaje a enviar al servidor 
	            		  enviar.println(men);
	            		  historial(men); //se agrega el mensaje que se quiere enviar en el historial
		            	  String respuesta = b.readLine();
		            	  System.out.println(respuesta);
	            	  }
	            	  
	            	  enviar.close();
		  			  b.close();
		  			  s.close();
	            	  
				  }
	             
	              
		      }
	
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
	                out.close();


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
	                out.close();

	    		}

	    		catch(Exception e)
	    		{
	    			System.out.println(e.toString());
	    		}

			}
			if(url.equals("/chat.html")){
		/**Hay que acomodar esto para que lo haga cada "cierto tiempo"*/		
          	  s = new Socket("localhost",8080);
          	  PrintWriter enviar = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
          	  BufferedReader b = new BufferedReader (new InputStreamReader(s.getInputStream()));
          	  enviar.println("mensaje=msjnuevos&Puerto="+port);   

        	  String respuesta=""; 
        	  List<String> list = new ArrayList<String>();
        	  while(!(respuesta = b.readLine()).equals("fin###"))
        	  {
        		  System.out.println(respuesta);
        		  list.add(respuesta);
        	  }
        	  
        	  for(int i = 0;i< list.size();i++){  
        		  respuesta(list.get(i));
        	  }
          	  
           	 	
				 File archivo = null;
         	     FileReader fr = null;
					BufferedReader br = null;
					try {                
						archivo = new File("historial.txt");
						if (!archivo.exists())
							archivo.createNewFile(); 
						
						fr = new FileReader(archivo);
						br = new BufferedReader(fr);
					
						String linea;  			    	
						out.println("<table>");
					    while((linea=br.readLine())!=null)/*Lectura del archivo historial.txt y escritura en el html*/
					    {	
					    	out.println("<tr>");			    			        	
							out.write("<td>"+ linea +"</td>\n");
							out.println("</tr>");
					    } 
					
					    out.println("</table>");
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
				InputStream arch = new FileInputStream ("chat.html");
                String html = IOUtils.toString(arch, "UTF-8");
                out.println(html); 
                out.close();
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
	
	
	String verificarcontacto(String contacto){ //revisar cierre del fichero
        
        File fichero = new File("contactos.txt" );
        BufferedReader entrada;
        String usuario = null;
        try {
	        entrada = new BufferedReader(new FileReader(fichero));
	        String linea;
	        while(entrada.ready()){
	        	linea = entrada.readLine();
	        	if(linea.split("&")[0].split("=")[1].equals(contacto)){
	        		System.out.println("Contacto reconocido");
	        		usuario = linea;
	        		entrada.close();
	        		break;
	
	        	}
	        }
	    }catch (IOException e) {
        	e.printStackTrace();
        }
		return usuario;
		
	}
	
	//Funcion para unir el mensaje con la direccion IP y puerto de Destinario
	String procesarmensaje(String msj){ 
		String [] aux;
		aux = msj.split("&");
		String mensaje = aux[1]+"&"+aux[2]+"&"+aux[3]+"&"+aux[4];
		return mensaje;
	}
	void historial(String msj){ 
		FileWriter fichero = null;
        PrintWriter pw = null;

       String  mensaje ="mensaje:"+ msj.split("&")[3].split("=")[1].replace("+", " ")+" to:"
    		   			+ msj.split("&")[2].split("=")[1].replace("+", " ");
        try
        {	
            fichero = new FileWriter("historial.txt",true);
            pw = new PrintWriter(fichero);				
            pw.println(mensaje);
 
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
	void respuesta(String msj){ 
		FileWriter fichero = null;
        PrintWriter pw = null;

       String  mensaje= msj.split("&")[2].split("=")[1].replace("+", " ")+" Dice:  " 
    		   	+ msj.split("&")[3].split("=")[1].replace("+", " ");
        try
        {	
            fichero = new FileWriter("historial.txt",true);
            pw = new PrintWriter(fichero);				
            pw.println(mensaje);
 
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
