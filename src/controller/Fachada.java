package controller;

import java.util.ArrayList;
import model.Grupo;
import model.Sintagma;

public class Fachada {

    private static Fachada instance = null;
    private final ListaDeGrupoController listaGrupoController;
    private final GrupoController grupoController;
    private final SintagmaController sintagmaController;

    public Fachada() {
        listaGrupoController = new ListaDeGrupoController();
        grupoController = new GrupoController();
        sintagmaController = new SintagmaController();
    }

    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    public ArrayList<Grupo> getGrupos() {
        return listaGrupoController.getGrupos();
    }

    public Grupo getGrupoSolitario() {
        return listaGrupoController.getSolitarios();
    }

    public boolean addSintagmaNoGrupo(Sintagma s) {
        return listaGrupoController.addSintagmaNoGrupo(s);
    }

    public void organizaGrupos() {
        listaGrupoController.organizaGrupos();
    }

    public void ordenaPorQtdFilhos() {
        grupoController.ordenaPorQtdFilhos(listaGrupoController.getGrupos());
    }

    public String getLog(ArrayList<Sintagma> original, ArrayList<Sintagma> atual) {
        return listaGrupoController.getLog(original, atual);
    }

    public String trataString(String s) {
        return sintagmaController.trataString(s);
    }
    
    public String imprimeCorref(ArrayList<Sintagma> sintagmas){
        return listaGrupoController.imprime_Correfs(sintagmas);
    }

}
