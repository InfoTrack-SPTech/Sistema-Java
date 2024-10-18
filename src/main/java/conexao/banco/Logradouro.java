package conexao.banco;

public class Logradouro {
    private Integer idLogradouro;
    private String nome;
    private String numero;
    private Integer fkBairro;
    private Double latitude;
    private Double longitude;

    public Logradouro() {}

    public Logradouro(Integer idLogradouro, String nome, String numero, Integer fkBairro) {
        this.idLogradouro = idLogradouro;
        this.nome = nome;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "\nLogradouro {" +
                "idLogradouro = " + idLogradouro +
                ", nome = '" + nome + '\'' +
                ", numero = '" + numero + '\'' +
                ", fkBairro = " + fkBairro +
                '}';
    }
}

