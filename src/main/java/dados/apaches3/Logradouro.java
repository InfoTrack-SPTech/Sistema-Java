package dados.apaches3;

public class Logradouro {
    private Integer idLogradouro;
    private String nome;
    private String cep;
    private String numero;
    private Integer fkBairro;

    public Logradouro() {}

    public Logradouro(Integer idLogradouro, String nome, String cep, String numero, Integer fkBairro) {
        this.idLogradouro = idLogradouro;
        this.nome = nome;
        this.cep = cep;
        this.numero = numero;
        this.fkBairro = fkBairro;
    }

    public Integer getIdLogradouro() {
        return idLogradouro;
    }

    public void setIdLogradouro(Integer idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getFkBairro() {
        return fkBairro;
    }

    public void setFkBairro(Integer fkBairro) {
        this.fkBairro = fkBairro;
    }

    @Override
    public String toString() {
        return "\nLogradouro {" +
                "idLogradouro = " + idLogradouro +
                ", nome = '" + nome + '\'' +
                ", cep = '" + cep + '\'' +
                ", numero = '" + numero + '\'' +
                ", fkBairro = " + fkBairro +
                '}';
    }
}

