package com.comunidadeapp.minhacomunidade.Entities;



public class Usuario {
    public String Id;
    public String nome;
    public String email;
    public String foto;
    public String tipo;

    public Usuario() {}

    public Usuario(String Id,String nome,String email,String foto, String tipo){
        this.Id = Id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.tipo = tipo;
    }
}
