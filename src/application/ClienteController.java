package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.beans.binding.StringExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClienteController implements Runnable{
	
	@FXML
	private TextArea txtMensaje;
	@FXML
	private Button btnSeleccionarArchivo;
	@FXML
	private Button btnEnviarMensaje;
	
	public ClienteController () {
		Thread hilo = new Thread(this);
		hilo.start();
	}
	
	public void seleccionarArchivo() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			open(file);
		}	
	}
	
	public void open(File file) {
		String mensaje;
		try {
			mensaje = Fichero.buscarArchivo(file);
			txtMensaje.setText(mensaje);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void enviarSolicitud() {
		try {
			Socket cliente = new Socket("192.168.1.118", 52000);
			
			DataOutputStream flujo_salida = new DataOutputStream(cliente.getOutputStream());
			
			//String mensaje = "MSH|^~\\&|HIS|MedCenter|LIS|MedCenter|20060307110114||ORM^O01|MSGID20060307110114|P|2.3\rPID|||12001||Jones^John^^^Mr.||19670824|M|||123 West St.^^Denver^CO^80020^USA|||||||\rPV1||O|OP^PAREG^||||2342^Jones^Bob|||OP|||||||||2|||||||||||||||||||||||||20060307110111|\rORC|NW|20060307110114\rOBR|1|20060307110114||003038^Urinalysis^L|||20060307110114";
			String mensaje = "MSH|^~\\&|EPIC|EPIC|||20140418173314|1148|ORM^O01|497|D|2.3||\r" + 
					"PID|1||20891312^^^^EPI||APPLESEED^JOHN^A^^MR.^||19661201|M||AfrAm|505 S. HAMILTON AVE^^MADISON^WI^53505^US^^^DN |DN|(608)123-4567|(608)123-5678||S|| 11480003|123-45-7890||||^^^WI^^\r" + 
					"PD1|||FACILITY(EAST)^^12345|1173^MATTHEWS^JAMES^A^^^\r" + 
					"PV1|||^^^CARE HEALTH SYSTEMS^^^^^||| |1173^MATTHEWS^JAMES^A^^^||||||||||||610613||||||||||||||||||||||||||||||||V\r" + 
					"ORC|NW|987654^EPIC|76543^EPC||Final||^^^20140418170014^^^^||20140418173314|1148^PATTERSON^JAMES^^^^||1173^MATTHEWS^JAMES^A^^^|1133^^^222^^^^^|(618)222-1122||\r" + 
					"OBR|1|363463^EPC|1858^EPC|73610^X-RAY ANKLE 3+ VW^^^X-RAY ANKLE ||||||||||||1173^MATTHEWS^JAMES^A^^^|(608)258-8866||||||||Final||^^^20140418170014^^^^|||||6064^MANSFIELD^JEREMY^^^^||1148010^1A^EAST^X-RAY^^^|^|\r" + 
					"DG1||I10|S82^ANKLE FRACTURE^I10|ANKLE FRACTURE||";
			//String mensaje = txtMensaje.getText();
						
			flujo_salida.writeUTF(mensaje);
			
			flujo_salida.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		try {
			
			ServerSocket servidor = new ServerSocket(52000);

			while(true) {
				Socket socket = servidor.accept();

				DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());

				byte[] bytes = new byte[flujoEntrada.available()];
						
				for(int i = 0; i < bytes.length; i++) {
					bytes[i] = flujoEntrada.readByte();
				}
				
				String mensaje = new String(bytes);
				System.out.println(mensaje);
				
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
