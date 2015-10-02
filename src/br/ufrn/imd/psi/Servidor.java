package br.ufrn.imd.psi;

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
		Socket socket = sSocket.accept();
		System.out.println("Usuario conectado.");
		BufferedReader bRApp = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
		PrintWriter pW = new PrintWriter(socket.getOutputStream(),true);
		String msgReceived;
		BufferedReader bRAppInside = new BufferedReader(new InputStreamReader(socket.getInputStream()));;

		File file;
		BufferedInputStream bis;
		BufferedOutputStream bosApp = new BufferedOutputStream(socket.getOutputStream()); 
		BufferedOutputStream bosFile;
		InputStream is = socket.getInputStream();

	
		while (true) {
			switch (bRApp.readLine()) {
			case "1":
				System.out.println("Usuario esta carente.");
				break;

			case "2":
				System.out.println("Usuario deseja enviar um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				msgReceived = bRApp.readLine();

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];
				bosFile = new BufferedOutputStream(new FileOutputStream("Arquivos Servidor/" + msgReceived));
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bosFile.write(mybytearray, 0, current);
				bosFile.flush();
				bosFile.close();
				
				socket = sSocket.accept();
				System.out.println("Recebimento de arquivo concluido.");
				break;
			case "3":
				// Enviar arquivo
				System.out.println("Usuario deseja receber um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				msgReceived = bRApp.readLine();
				System.out.println(msgReceived);

				// envia o arquivo
				file = new File("Arquivos Servidor/" + msgReceived);
				mybytearray = new byte[(int) file.length()];
				bis = new BufferedInputStream(new FileInputStream(file));
				bis.read(mybytearray, 0, mybytearray.length);
				System.out.println("Enviando...");
				bosApp.write(mybytearray, 0, mybytearray.length);
				bosApp.flush();
				bosApp.close();
				//bis.close();
				
				//socket = sSocket.accept();
				break;
			case "4":
				// Listar arquivos
				System.out.println("Usuario solicitou a lista de arquivos.");
				// Enviar a quantidade de arquivos
				int quantity = new File("Arquivos Servidor/").listFiles().length;

				pW.println(quantity);

				file = new File("Arquivos Servidor/");
				File[] listOfFiles = file.listFiles();

				for (int i = 0; i < quantity; i++) {
					pW.println(listOfFiles[i].getName());
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