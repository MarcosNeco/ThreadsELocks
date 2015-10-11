package main;

import java.util.List;

public class Servidor extends Thread {
	
	private List<Arquivo> arquivos;
	private String nome;
	private Servidor servidorDestino;
	private int idArquivoAlterado;
	private boolean desligarServidor = false;
	
	public Servidor(List<Arquivo> arquivos, String nome) {
		this.arquivos = arquivos;
		this.nome = nome;
	}
	
	@Override
	public void run() {
		while(!desligarServidor){
			synchronized (this) {
				try {
					this.wait();
					Arquivo arquivoLocal = this.arquivos.get(this.idArquivoAlterado);
					sincronizaArquivo(arquivoLocal);
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
	}
	
	public synchronized void notificaAlteracaoArquivo(int idArquivo) throws Exception{
		this.idArquivoAlterado = idArquivo;
		notify();
	}
	
	private void sincronizaArquivo(Arquivo arquivo) throws Exception {
		arquivo.pegaAcessoExclusivoParaLeitura(this.nome);
		this.servidorDestino.copiaArquivo(arquivo);
		arquivo.liberaAcessoExclusivoParaLeitura(this.nome);
	}
	
	private void copiaArquivo(Arquivo arquivoRemoto) throws Exception {
		Arquivo arquivoLocal = this.arquivos.get(arquivoRemoto.getId());
		arquivoLocal.pegaAcessoExclusivoParaLeitura(this.nome);
		arquivoLocal.pegaAcessoExclusivoParaEscrita(this.nome);
		System.out.println("Servidor:"+ nome +"|sincronizando arquivo " + arquivoRemoto.getId() +"....|tamanho: " + arquivoRemoto.getTamanho() +" MB");
		for(int i = 0; i < 10; i++){
			arquivoRemoto.lerArquivo(this.nome);
			arquivoLocal.escreverArquivo(this.nome);
			Thread.sleep(10*arquivoLocal.getTamanho());
		}
		arquivoLocal.liberaAcessoExclusivoParaEscrita(this.nome);
		arquivoLocal.liberaAcessoExclusivoParaLeitura(this.nome);
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}
	
	public void setServidorDestino(Servidor servidoDestino) {
		this.servidorDestino = servidoDestino;
	}
	
	public void desligarServidor(){
		this.desligarServidor = true;
	}
}
