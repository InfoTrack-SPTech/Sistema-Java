package dados.apaches3;

public class Zona {
    private Integer idZona;
    private String nome;

    public Zona() {}

    public Zona(Integer idZona, String nome) {
        this.idZona = idZona;
        this.nome = nome;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "\nZona {" +
                "idZona = " + idZona +
                ", nome = '" + nome + '\'' +
                '}';
    }
}

