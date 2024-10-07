package dados.apaches3;

public class Local {
    private Integer idLocal;
    private String nome;

    public Local() {}

    public Local(Integer idLocal, String nome) {
        this.idLocal = idLocal;
        this.nome = nome;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "\nLocal {" +
                "idLocal = " + idLocal +
                ", nome = '" + nome + '\'' +
                '}';
    }
}

