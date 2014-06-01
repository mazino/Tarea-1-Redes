
import java.awt.Desktop;
import java.io.IOException;
import java.net.*;


public class webserver
{	
	int puerto = 8000;
	private ServerSocket s;

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
			s = new ServerSocket(0);
			System.out.println( "El sistema nos ha dado el puerto "+ s.getLocalPort());
			System.out.println("Quedamos a la espera de conexion");
			String url = "http://localhost:"+ s.getLocalPort();
			System.out.println(url);
			if(Desktop.isDesktopSupported()){
	            Desktop desktop = Desktop.getDesktop();
	            try {
	                desktop.browse(new URI(url));
	            } catch (IOException | URISyntaxException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }else{
	            Runtime runtime = Runtime.getRuntime();
	            try {
	                runtime.exec("xdg-open " + url);
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
			while(true)  
			{
				Socket entrante = s.accept();		
				
				peticionWeb pCliente = new peticionWeb(entrante);
				pCliente.start();

			}

		}
		catch(Exception e)
		{
			System.out.println(e.toString());

		}

		return true;
	}

}