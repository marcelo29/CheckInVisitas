package br.com.android.check.domain;

public class Visita {

    public static final int EM_ANDAMENTO = 0, REALIZADA = 1, FINALIZADA = 2;

    private int id, situacao;
    private String cliente, endereco, telefone;
    private String data;
    private String hora;
    private Vendedor vendedor;
    private Boolean chkMarcado = false;

    public Visita(int situacao, String cliente, String endereco, String telefone, String hora) {
        this.situacao = situacao;
        this.cliente = cliente;
        this.endereco = endereco;
        this.telefone = telefone;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public Boolean getChkMarcado() {
        return chkMarcado;
    }

    public void setChkMarcado(Boolean chkMarcado) {
        this.chkMarcado = chkMarcado;
    }
}