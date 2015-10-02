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
import java.net.Socket;

public class Cliente {
	public static void main(String[] args) throws Exception {

		Socket socket = new Socket("localhost", 1818);
		BufferedReader bRKboard = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bRApp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pW = new PrintWriter(socket.getOutputStream(),true);
		String msgReceived, msgString;
		File file;
		BufferedOutputStream bosFile;
		BufferedOutputStream bosApp = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream bis;
		InputStream is = socket.getInputStream();

		while (true) {
			imprimeMenu();
			switch (bRKboard.readLine()) {
			case "1":
				// Envia a opcao escolhida
				pW.println("1");
				// Conectar
				System.out.println("Bem vindo");
				break;
			case "2": // Enviar arquivo
				// Envia a opcao escolhida
				pW.println("2");

				// Envia o caminho do arquivo
				System.out.println("Digite o caminho do arquivo que sera enviado: ");
				msgReceived = bRKboard.readLine();
				pW.println(msgReceived);

				// Envia o arquivo
				file = new File(msgReceived);
				byte[] mybytearray = new byte[(int) file.length()];
				bis = new BufferedInputStream(new FileInputStream(file));
				bis.read(mybytearray, 0, mybytearray.length);
				System.out.println("Enviando...");
				bosApp.write(mybytearray, 0, mybytearray.length);
				bosApp.flush();
				//bosApp.close();
				// socket = new Socket("localhost", 1818);
				break;
			case "3":
				// Receber arquivo
				// Envia a opcao escolhida
				pW.println("3");

				// Envia o caminho do arquivo
				System.out.println("Digite o nome do arquivo que sera recebido: ");
				msgReceived = bRKboard.readLine();
				pW.println(msgReceived);

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				mybytearray = new byte[filesize];
				bosFile = new BufferedOutputStream(new FileOutputStream("Arquivos Cliente/" + msgReceived));
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bosFile.write(mybytearray, 0, current);
				//bosFile.flush();
				// socket = new Socket("localhost", 1818);
				break;
			case "4":
				// Listar arquivos
				// Envia a opcao escolhida
				pW.println("4");

				// Recebe numero de arquivos
				msgReceived = bRApp.readLine();

				for (int i = 0; i < Integer.parseInt(msgReceived); i++) {
					msgString = bRApp.readLine();
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
/*
 * As funções do cliente são: • Se conectar a um servidor lightFTP no host e
 * porta especificados; • Requisitar e receber lista de arquivos disponíveis no
 * servidor; • Receber arquivos enviados pelo servidor e armazena-los em disco;
 * • Fazer o upload de arquivos locais para o servidor remoto; • Se desconectar
 * do servidor.
 */