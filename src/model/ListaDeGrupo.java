package model;

import java.util.ArrayList;

public class ListaDeGrupo {

    private final ArrayList<Grupo> grupos;
    private final Grupo solitarios;
    
    public ListaDeGrupo() {
        this.grupos = new ArrayList<>();
        solitarios = new Grupo();
    }

    public Grupo getSolitarios() {
        return solitarios;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

}
