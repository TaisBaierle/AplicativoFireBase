package br.feevale.appameacasambientaisfirebase;

import java.io.Serializable;

public class AmeacasAmbientais implements Serializable {

    private String endereco;
    private String data;
    private String descricao;
    private String imagem;

    public AmeacasAmbientais(){

    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return endereco + " " + data + " " + descricao;
    }
}
