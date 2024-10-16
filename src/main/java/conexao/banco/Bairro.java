package conexao.banco;

public class Bairro {
    private Integer idBairro;
    private String nome;

    public Bairro() {}

    public Bairro(Integer idBairro, String nome) {
        this.idBairro = idBairro;
        this.nome = nome;
    }

    public Integer getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Integer idBairro) {
        this.idBairro = idBairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "\nBairro {" +
                "idBairro = " + idBairro +
                ", nome = '" + nome + '\'' +
                '}';
    }
}

