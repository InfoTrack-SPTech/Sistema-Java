package conexao.banco;

public abstract class Crime{

    private Integer idCrime;
    private String dataOcorrencia;
    private Integer fkLogradouro;
    private Integer fkLocal;

    public Crime() {}

    public Crime(Integer idCrime, String dataOcorrencia, Integer fkLogradouro, Integer fkLocal) {
        this.idCrime = idCrime;
        this.dataOcorrencia = dataOcorrencia;
        this.fkLogradouro = fkLogradouro;
        this.fkLocal = fkLocal;
    }

    public Integer getIdCrime() {
        return idCrime;
    }

    public void setIdCrime(Integer idCrime) {
        this.idCrime = idCrime;
    }

    public String getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(String dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public Integer getFkLogradouro() {
        return fkLogradouro;
    }

    public void setFkLogradouro(Integer fkLogradouro) {
        this.fkLogradouro = fkLogradouro;
    }

    public Integer getFkLocal() {
        return fkLocal;
    }

    public void setFkLocal(Integer fkLocal) {
        this.fkLocal = fkLocal;
    }
}

