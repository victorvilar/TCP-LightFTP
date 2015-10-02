package SourceDanilo;

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
		PrintWriter pW = new PrintWriter(socket.getOutputStream(),true);
		String msgReceived;
		File file;
		BufferedInputStream bisApp = new BufferedInputStream(socket.getInputStream());
		BufferedInputStream bisFile;

		System.out.println("Usuario conectado.");
		while (socket.isConnected()) {
			BufferedReader brApp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			switch (brApp.readLine()) {
			case "1":
				System.out.println("Usuario conectado.");
				// Conectar
				pW.println("Voce esta conectado.");
				break;
			case "2":
				System.out.println("Usuario deseja enviar um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				msgReceived = brApp.readLine();
				System.out.println("Arquivo: " + msgReceived);

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Arquivos Servidor/" + msgReceived));

				do {
					bytesRead = bisApp.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				bos.close();
				System.out.println("Envio concluido.");
				socket = sSocket.accept();
				System.out.println("Chegou aqui");
				break;
			case "3":
				// Enviar arquivo
				System.out.println("Usuario deseja receber um arquivo.");
				// Receber arquivo
				// Recebe caminho do arquivo
				msgReceived = brApp.readLine();
				System.out.println("Arquivo: " + msgReceived);

				// envia o arquivo
				file = new File("Arquivos Servidor/" + msgReceived);
				mybytearray = new byte[(int) file.length()];
				bisFile = new BufferedInputStream(new FileInputStream(file));
				bisFile.read(mybytearray, 0, mybytearray.length);
				bos = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Enviando...");
				bos.write(mybytearray, 0, mybytearray.length);
				bos.flush();
				bos.close();
				bisFile.close();
				System.out.println("Recebimento concluido.");				
				socket = sSocket.accept();
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
				System.out.println("Usuario desconectado.");
				socket = sSocket.accept();
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