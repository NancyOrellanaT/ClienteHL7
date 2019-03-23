package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Fichero {
	
	public static String buscarArchivo(File archivo) throws FileNotFoundException{
		
		String mensaje = "";
		String cadena = "";
		
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        
        try {
			while((cadena = b.readLine())!=null) {
			    mensaje += cadena;
			}
			b.close();
			
			return mensaje;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
