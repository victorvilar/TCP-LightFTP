package Backup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static void main(String[] args) throws Exception {

		System.out.println("Aguardando entrada do usuario.");
		ServerSocket sSocket = new ServerSocket(1818);
		Socket socket = null;
		BufferedReader bR;
		PrintWriter pW;
		String msgReceived;
		FileOutputStream fos;
		File file;
		BufferedInputStream bis;

		socket = sSocket.accept();
		System.out.println("Usuario conectado.");
		while (true) {
			bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			switch (bR.readLine()) {
			case "1":
				// Conectar
				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println("Voce esta conectado.");
				pW.flush();
				break;

			case "2":
				System.out.println("Usuario deseja enviar um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				msgReceived = bR.readLine();
				System.out.println(msgReceived);

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];
				InputStream is = socket.getInputStream();
				fos = new FileOutputStream("Arquivos Servidor/" + msgReceived);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
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

				socket = sSocket.accept();
				break;
			case "3":
				// Enviar arquivo
				System.out.println("Usuario deseja receber um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				msgReceived = bR.readLine();
				System.out.println(msgReceived);

				// envia o arquivo
				file = new File("Arquivos Servidor/" + msgReceived);
				mybytearray = new byte[(int) file.length()];
				bis = new BufferedInputStream(new FileInputStream(file));
				bis.read(mybytearray, 0, mybytearray.length);
				bos = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Enviando...");
				bos.write(mybytearray, 0, mybytearray.length);
				bos.flush();
				bos.close();
				bis.close();
				
				socket = sSocket.accept();
				break;
			case "4":
				// Listar arquivos
				System.out.println("Usuario solicitou a lista de arquivos.");
				// Enviar a quantidade de arquivos
				int quantity = new File("Arquivos Servidor/").listFiles().length;

				pW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pW.println(quantity);
				pW.flush();

				file = new File("Arquivos Servidor/");
				File[] listOfFiles = file.listFiles();

				for (int i = 0; i < quantity; i++) {
					// pW = new PrintWriter(new
					// OutputStreamWriter(socket.getOutputStream()));
					pW.println(listOfFiles[i].getName());
					pW.flush();
				}
				break;
			case "5":
				// Sair
				Thread.sleep(1000);
				socket.close();
				sSocket.close();
				break;
			}
		}
	}
}
/*
 * As funções do servidor: • Enviar lista de arquivos disponíveis; • Receber
 * arquivos enviados pelo cliente e armazena-los em disco; • Enviar arquivos
 * requisitados pelos clientes; • Após a desconexão do cliente voltar a aceitar
 * uma nova conexão.
 */