package Backup;

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

		Socket socket = null;
		BufferedReader bR;
		PrintWriter pW;
		String msgReceived, msgString;
		FileOutputStream fos;
		File file;
		BufferedOutputStream bos;
		BufferedInputStream bis;

		while (true) {
			imprimeMenu();
			bR = new BufferedReader(new InputStreamReader(System.in));
			switch (bR.readLine()) {
			case "1":
				// Conectar
				System.out.println("Informe o IP e porta para conexao.\nExemplo: localhost:1818.");

				bR = new BufferedReader(new InputStreamReader(System.in));
				String ipPort = bR.readLine();

				String ip = ipPort.split(":")[0];
				String port = ipPort.split(":")[1];

				socket = new Socket(ip, Integer.parseInt(port));

				// socket = new Socket("localhost", 1818);

				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("1");
				pW.flush();
				bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				msgReceived = bR.readLine();
				System.out.println(msgReceived);
				break;

			case "2": // Enviar arquivo
				// Envia a opcao escolhida
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("2");
				pW.flush();

				// Envia o caminho do arquivo
				System.out.println("Digite o caminho do arquivo que sera enviado: ");
				bR = new BufferedReader(new InputStreamReader(System.in));
				msgReceived = bR.readLine();
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println(msgReceived);
				pW.flush();

				// envia o arquivo
				file = new File(msgReceived);
				byte[] mybytearray = new byte[(int) file.length()];
				bis = new BufferedInputStream(new FileInputStream(file));
				bis.read(mybytearray, 0, mybytearray.length);
				bos = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Enviando...");
				bos.write(mybytearray, 0, mybytearray.length);
				bos.flush();
				bos.close();
				socket = new Socket("localhost", 1818);
				break;
			case "3":
				// Receber arquivo
				// Envia a opcao escolhida
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("3");
				pW.flush();

				// Envia o caminho do arquivo
				System.out.println("Digite o nome do arquivo que sera recebido: ");
				bR = new BufferedReader(new InputStreamReader(System.in));
				msgReceived = bR.readLine();
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println(msgReceived);
				pW.flush();

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				mybytearray = new byte[filesize];
				InputStream is = socket.getInputStream();
				fos = new FileOutputStream("Arquivos Cliente/" + msgReceived);
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				bos.close();
				socket = new Socket("localhost", 1818);
				break;
			case "4":
				// Listar arquivos
				// Envia a opcao escolhida
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("4");
				pW.flush();

				// Recebe numero de arquivos
				bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				msgReceived = bR.readLine();

				for (int i = 0; i < Integer.parseInt(msgReceived); i++) {
					// bR = new BufferedReader(new
					// InputStreamReader(socket.getInputStream()));
					msgString = bR.readLine();
					System.out.println(msgString);
				}
				break;
			case "5":
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("5");
				pW.flush();

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
/*
 * As funções do cliente são: • Se conectar a um servidor lightFTP no host e
 * porta especificados; • Requisitar e receber lista de arquivos disponíveis no
 * servidor; • Receber arquivos enviados pelo servidor e armazena-los em disco;
 * • Fazer o upload de arquivos locais para o servidor remoto; • Se desconectar
 * do servidor.
 */