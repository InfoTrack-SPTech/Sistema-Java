package dados.apaches3;

public class Bairro {
    private Integer idBairro;
    private String nome;
    private Integer fkZona;

    public Bairro() {}

    public Bairro(Integer idBairro, String nome, Integer fkZona) {
        this.idBairro = idBairro;
        this.nome = nome;
        this.fkZona = fkZona;
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

    public Integer getFkZona() {
        return fkZona;
    }

    public void setFkZona(Integer fkZona) {
        this.fkZona = fkZona;
    }

    @Override
    public String toString() {
        return "\nBairro {" +
                "idBairro = " + idBairro +
                ", nome = '" + nome + '\'' +
                ", fkZona = " + fkZona +
                '}';
    }
}

