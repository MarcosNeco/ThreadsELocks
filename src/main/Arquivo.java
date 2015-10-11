package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Arquivo {
	
	private int tamanho;
	private int id;
	private String diretorio;

	private ReentrantLock semaforoEscrita = new ReentrantLock();
	private ReentrantLock semaforoLeitura = new ReentrantLock();
	
	private List<Thread> objetosLockParaLeitura = new ArrayList<Thread>();
	private List<Thread> objetosLockParaEscrita = new ArrayList<Thread>();
	
	public Arquivo(int id, String diretorio, int tamanho) {
		this.id = id;
		this.diretorio = diretorio;
		this.tamanho = tamanho;
	}
	
    public void escreverArquivo(String nomeUsuario) throws Exception{
    	synchronized(this){
    		if(this.semaforoEscrita.isLocked() && !this.semaforoEscrita.isHeldByCurrentThread()){
    			Thread thread = Thread.currentThread();
    			objetosLockParaEscrita.add(thread);
    			thread.wait();
    		}
    	}
    	System.out.println("Usuário:"+nomeUsuario +"|escrevendo arquivo " + id + " | Tamanho arquivo: " + tamanho + "MB" + "|diretorio:" + diretorio);
    }
	
    public void lerArquivo(String nomeUsuario) throws Exception{
    	synchronized(this){
    		if(this.semaforoLeitura.isLocked() && !this.semaforoLeitura.isHeldByCurrentThread()){
    			Thread thread = Thread.currentThread();
    			objetosLockParaLeitura.add(thread);
    			thread.wait();
    		}
    	}
    	System.out.println("Usuário:" + nomeUsuario + "|lendo arquivo " + this.id +" | Tamanho arquivo:" + tamanho + "MB" + "|diretorio:" + diretorio);
	}
	
    public synchronized void pegaAcessoExclusivoParaEscrita(String nomeUsuario) throws Exception{
 	   this.semaforoEscrita.lock();
 	   System.out.println("Usuário:" + nomeUsuario + "|pegou acesso exclusivo para leitura | arquivo:" + this.id+ "|diretorio:" + diretorio);
 	}
	
	public synchronized void liberaAcessoExclusivoParaEscrita(String nomeUsuario){
		this.semaforoEscrita.unlock();
		for(Thread thread : objetosLockParaEscrita){
			thread.notify();
		}
		objetosLockParaEscrita.clear();
		System.out.println("Usuário:" + nomeUsuario + "|liberou acesso exclusivo para leitura | arquivo:" + this.id + "|diretorio:" + diretorio);
	}

    public synchronized void pegaAcessoExclusivoParaLeitura(String nomeUsuario) throws Exception{
	   this.semaforoLeitura.lock();
	   System.out.println("Usuário:" + nomeUsuario + "|pegou acesso exclusivo para leitura | arquivo:" + this.id + "|diretorio:" + diretorio);
	}
	
	public synchronized void liberaAcessoExclusivoParaLeitura(String nomeUsuario){
		this.semaforoLeitura.unlock();
		for(Thread thread : objetosLockParaLeitura){
			thread.notify();
		}
		objetosLockParaLeitura.clear();
		System.out.println("Usuário:" + nomeUsuario + "|liberou acesso exclusivo para leitura | arquivo:" + this.id + "|diretorio:" + diretorio);
	}

	
	public int getTamanho() {
		return tamanho;
	}
	
	public int getId() {
		return id;
	}
}
