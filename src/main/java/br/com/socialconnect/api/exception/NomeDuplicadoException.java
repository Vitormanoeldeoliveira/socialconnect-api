package br.com.socialconnect.api.exception;

public class NomeDuplicadoException extends RuntimeException {

    private final String nome;

    public NomeDuplicadoException(String nome) {
        super("Já existe um produto cadastrado com o nome: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
