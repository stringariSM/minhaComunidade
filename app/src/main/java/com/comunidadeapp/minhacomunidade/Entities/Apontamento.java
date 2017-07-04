package com.comunidadeapp.minhacomunidade.Entities;

import java.util.Date;

/**
 * Created by egasp on 28/06/2017.
 */

public class Apontamento {
    public int ID;
    public String Descricao;
    public Date Data;
    public TipoApontamento Tipo;
    public Usuario Responsavel;
    public String UrlFoto;

    public Apontamento(){}

    public Apontamento(String Descricao, Date Data, TipoApontamento Tipo, Usuario Responsavel, String UrlFoto){
        this.Descricao = Descricao;
        this.Data = Data;
        this.Tipo = Tipo;
        this.Responsavel = Responsavel;
        this.UrlFoto = UrlFoto;
    }
}
