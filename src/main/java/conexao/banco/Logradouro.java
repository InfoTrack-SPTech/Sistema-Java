package conexao.banco;

import java.util.List;

public class Logradouro {

    private Integer idLogradouro;
    private String nome;
    private String numero;
    private Integer fkBairro;
    private String latitude;
    private String longitude;
    private List<Crime> crimes;

    public void setIdLogradouro(Integer idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public Integer getIdLogradouro() {
        return idLogradouro;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Logradouro{" +
                "idLogradouro=" + idLogradouro +
                ", nome='" + nome + '\'' +
                ", numero='" + numero + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", fkBairro=" + fkBairro +
                '}';
    }
}

