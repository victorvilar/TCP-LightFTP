package SourceDanilo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
	public static void main(String[] args) throws Exception {

		Socket socket = new Socket("localhost", 1818);
		BufferedReader brKboard = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader brApp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pW = new PrintWriter(socket.getOutputStream(),true);
		String msgReceived, msgString;
		FileOutputStream fos;
		File file;
		BufferedOutputStream bosFile;
		BufferedInputStream bisApp = new BufferedInputStream(socket.getInputStream());
		BufferedInputStream bisFile;

		while (true) {
			imprimeMenu();
			
			switch (brKboard.readLine()) {
			case "1":
				// Conectar
				pW.println("1");
				if(socket.isConnected()){
					System.out.println("Voce ja esta conectado.");
					break;
				}
				
				socket = new Socket("localhost", 1818);

				msgReceived = brApp.readLine();
				System.out.println(msgReceived);
				break;
			case "2": // Enviar arquivo
				// Envia a opcao escolhida
				pW.println("2");

				// Envia o caminho do arquivo
				System.out.println("Digite o caminho do arquivo que sera enviado: ");
				msgReceived = brKboard.readLine();
				pW.println(msgReceived);

				// envia o arquivo
				file = new File(msgReceived);
				byte[] mybytearray = new byte[(int) file.length()];
				bisFile = new BufferedInputStream(new FileInputStream(file));
				bisFile.read(mybytearray, 0, mybytearray.length);
				System.out.println("Enviando...");
				BufferedOutputStream bosApp = new BufferedOutputStream(socket.getOutputStream());
				bosApp.write(mybytearray, 0, mybytearray.length);
				bosApp.flush();
				bosApp.close();
				System.out.println("Envio concluido.");
				socket = new Socket("localhost", 1818);
				break;
			case "3":
				// Receber arquivo
				// Envia a opcao escolhida
				pW.println("3");

				// Envia o caminho do arquivo
				System.out.println("Digite o nome do arquivo que sera recebido: ");
				msgReceived = brKboard.readLine();
				pW.println(msgReceived);

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				mybytearray = new byte[filesize];
				fos = new FileOutputStream("Arquivos Cliente/" + msgReceived);
				bosFile = new BufferedOutputStream(fos);
				bytesRead = bisApp.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = bisApp.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bosFile.write(mybytearray, 0, current);
				bosFile.flush();
				bosFile.close();
				System.out.println("Recebimento concluido.");				
				socket = new Socket("localhost", 1818);
				break;
			case "4":
				// Listar arquivos
				// Envia a opcao escolhida
				pW.println("4");

				// Recebe numero de arquivos
				msgReceived = brApp.readLine();

				for (int i = 0; i < Integer.parseInt(msgReceived); i++) {
					msgString = brApp.readLine();
					System.out.println(msgString);
				}
				break;
			case "5":
				pW.println("5");

				socket.close();
				break;
			}
		}
	}

	private static void imprimeMenu() {
		System.out.println("Escolha uma das opcoes:\n" + "1 - Conectar.\n" + "2 - Enviar arquivo.\n"
				+ "3 - Receber arquivo.\n" + "4 - Listar arquivos.\n" + "5 - Desconectar.\n" + "Opcao: ");
	}
}