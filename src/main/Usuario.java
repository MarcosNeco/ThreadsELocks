package main;

import java.util.Random;


public class Usuario extends Thread{
	
	private static final long DOIS_SEGUNDOS = 2000;
	private String name;
	private Servidor servidor;
	private int numeroAlteracoes;
	
	public Usuario(Servidor servidor, String name, int numeroAlteracoes) {
		this.name = name;
		this.servidor = servidor;
		this.numeroAlteracoes = numeroAlteracoes;
	}
	
	@Override
	public void run() {
		while(numeroAlteracoes > 0){
			try{
				Random randomico = new Random();
				int idAleatorio = randomico.nextInt(servidor.getArquivos().size());
				alterarArquivo(idAleatorio);
				numeroAlteracoes --;
				esperaParaAlterarOutroArquivo();
			}catch(Exception e){
				System.err.println(e);
			}
		}
		servidor.desligarServidor();
	}
	
	private void alterarArquivo(int idArquivo) throws Exception{
		Arquivo arquivo = servidor.getArquivos().get(idArquivo);
		arquivo.escreverArquivo(name);
		servidor.notificaAlteracaoArquivo(idArquivo);
	}
	
	private void esperaParaAlterarOutroArquivo() throws InterruptedException{
		Thread.sleep(DOIS_SEGUNDOS);
	}
}
