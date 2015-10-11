package main;

import java.util.List;
import java.util.Random;

public class WorkerHttp extends Thread {
	
	private String nome;
	private int quantidadeRequisicoes;
	private List<Arquivo> arquivosJekyl;
	private List<Arquivo> arquivosHyde;
	
	
	public WorkerHttp(int id, int quantidadeRequisicoes, List<Arquivo> arquivosJekyl, List<Arquivo> arquivosHyde) {
		this.nome = "WorkerHTTP " + id;
		this.quantidadeRequisicoes = quantidadeRequisicoes;
		this.arquivosJekyl = arquivosJekyl;
		this.arquivosHyde = arquivosHyde;
	}
	
	@Override
	public void run() {
		Random random = new Random();
		while(quantidadeRequisicoes > 0){
			Arquivo arquivo = null;
			if(quantidadeRequisicoes %2 ==0){
				arquivo = arquivosJekyl.get(random.nextInt(arquivosJekyl.size()));
			}
			else{
				arquivo = arquivosHyde.get(random.nextInt(arquivosHyde.size()));
			}
			try {
				this.lerArquivo(arquivo);
			} catch (Exception e) {
				System.err.println(e);
			}
			quantidadeRequisicoes--;
		}
	}

	private void lerArquivo(Arquivo arquivo) throws Exception {
		arquivo.pegaAcessoExclusivoParaLeitura(this.nome);
		arquivo.lerArquivo(this.nome);
		Thread.sleep(arquivo.getTamanho());
		arquivo.liberaAcessoExclusivoParaLeitura(this.nome);
	}
}
