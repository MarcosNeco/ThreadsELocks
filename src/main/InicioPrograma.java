package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class InicioPrograma {
	
	private static final int MAXIMO_RANDOMICO = 1000;
	private static List<Arquivo> arquivosJekyll = new ArrayList<Arquivo>();
	private static List<Arquivo> arquivosHyde = new ArrayList<Arquivo>();
	
	public static void main(String[] args) {
		Scanner leitorDoTeclado = new Scanner(System.in);
		try{
			System.out.println("Quantidade de arquivos: ");
			int quantidadeArquivos = leitorDoTeclado.nextInt();
			System.out.println("Alterações por cliente:");
			int quantidadeAlteracoesPorUsuario = leitorDoTeclado.nextInt();
			System.out.println("Quantidade de threads http:");
			int quantidadeWorkersHttp = leitorDoTeclado.nextInt();
			System.out.println("Numero requisições simuladas por cada thread http:");
			int quantidadeRequisicoesWorkersHttp = leitorDoTeclado.nextInt();
			
			gerarListaArquivos(quantidadeArquivos);
			
			Servidor servidorJekyll = new Servidor(arquivosJekyll, "Servidor Jekyll");
			Servidor servidorHyde = new Servidor(arquivosHyde, "Servidor Hyde");
			
			servidorJekyll.setServidorDestino(servidorHyde);
			servidorHyde.setServidorDestino(servidorJekyll);
			
			servidorJekyll.start();
			servidorHyde.start();
			
			iniciaUsuarios(quantidadeAlteracoesPorUsuario, servidorJekyll,servidorHyde);
			
			iniciaWorkersHttp(quantidadeWorkersHttp, quantidadeRequisicoesWorkersHttp);
			
		}catch(Exception e ){
			System.err.println(e);
		}finally{
			leitorDoTeclado.close();
		}

	}

	private static void iniciaUsuarios(int quantidadeAlteracoesPorUsuario,
			Servidor servidorJekyll, Servidor servidorHyde) {
		Usuario usuarioJekyll = new Usuario(servidorJekyll, "Usuario Jekyll", quantidadeAlteracoesPorUsuario);
		Usuario usuarioHyde = new Usuario(servidorHyde, "Usuario Hyde", quantidadeAlteracoesPorUsuario);
		
		usuarioJekyll.start();
		usuarioHyde.start();
	}

	private static void iniciaWorkersHttp(int quantidadeThreadsHttp, int quantidadeRequisicoesThreadsHttp) {
		for(int i = 0; i < quantidadeThreadsHttp ; i++){
			WorkerHttp workerHttp = new WorkerHttp(i, quantidadeRequisicoesThreadsHttp, arquivosJekyll, arquivosHyde);
			workerHttp.start();
		}
	}

	private static void gerarListaArquivos(int quantidadeArquivos) {
		Random randomico =  new Random();
		for(int i = 0; i < quantidadeArquivos; i++){
			int tamanhoArquivo = randomico.nextInt(MAXIMO_RANDOMICO);
			Arquivo arquivoJekyll = new Arquivo(i,"jekyll", tamanhoArquivo);
			arquivosJekyll.add(arquivoJekyll);
			
			Arquivo arquivoHyde = new Arquivo(i,"hyde", tamanhoArquivo);
			arquivosHyde.add(arquivoHyde);
		}
	}

}
