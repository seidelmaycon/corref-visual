package controller;

import java.util.ArrayList;
import java.util.Collections;
import model.Grupo;
import model.Sintagma;

public class GrupoController {
    
    public void ordenaPorQtdFilhos(ArrayList<Grupo> grupos) {

        Collections.sort(grupos, (Grupo g1, Grupo g2) -> Integer.compare(g2.getListaSintagmas().size(), g1.getListaSintagmas().size()));
    }

    public void ordenaPorSet(Grupo g) {
        Collections.sort(g.getListaSintagmas(), (Sintagma sn1, Sintagma sn2) -> Integer.compare(sn2.set, sn1.set));
    }
    
    public void ordenaPorSet(ArrayList<Sintagma> lista) {
        Collections.sort(lista, (Sintagma sn1, Sintagma sn2) -> Integer.compare(sn2.set, sn1.set));
    }
    
    public boolean containsSet(int set, ArrayList<Grupo> grupos){
        for(Grupo g: grupos){
            for(Sintagma s: g.getListaSintagmas()){
                if(s.set == set)
                    return true;
            }
        }
        return false;
    }

    public ArrayList<Sintagma> getFilhos(String sn, ArrayList<Grupo> grupos) {
        for (Grupo g : grupos) {
            if (g.getListaSintagmas().get(0).sn.equals(sn)) {
                return g.getListaSintagmas();
            }
        }
        ArrayList<Sintagma> nada = new ArrayList<>();
        return nada;
    }
    
}
