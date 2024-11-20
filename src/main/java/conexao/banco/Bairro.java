package conexao.banco;

import java.util.ArrayList;
import java.util.List;

public class Bairro {
    private Integer idBairro;
    private String nome;
    private List<Logradouro> ruas;

    public Bairro() {}

    public Bairro(Integer idBairro, String nome) {
        this.idBairro = idBairro;
        this.nome = nome;
        this.ruas = new ArrayList<>();
    }

    public void addLogradouro(Logradouro rua){
        ruas.add(rua);
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

    public List<Logradouro> getRuas() {
        return ruas;
    }

    public void setRuas(List<Logradouro> ruas) {
        this.ruas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\nBairro {" +
                "idBairro = " + idBairro +
                ", nome = '" + nome + '\'' +
                '}';
    }
}

