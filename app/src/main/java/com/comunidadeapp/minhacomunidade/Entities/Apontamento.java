package com.comunidadeapp.minhacomunidade.Entities;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by egasp on 28/06/2017.
 */

public class Apontamento {
    public String ID;
    public String Descricao;
    public Date Data;
    public TipoApontamento Tipo;
    public Usuario Responsavel;
    public String UrlFoto;
    public Bitmap Foto;
    public String resolvido = "false";
    public String Cidade;
    public double Latitude;
    public double Longitude;

    public Apontamento(){}

    public Apontamento(String Descricao, Date Data, TipoApontamento Tipo, Usuario Responsavel, String UrlFoto, String Cidade, double Latitude, double Longitude){
        this.Descricao = Descricao;
        this.Data = Data;
        this.Tipo = Tipo;
        this.Responsavel = Responsavel;
        this.UrlFoto = UrlFoto;
        this.Cidade = Cidade;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
}
