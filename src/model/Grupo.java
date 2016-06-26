package model;
import java.util.ArrayList;

public class Grupo {

    private ArrayList<Sintagma> listaSintagmas;

    public Grupo(Sintagma entidade) {
        listaSintagmas = new ArrayList<>();
        listaSintagmas.add(entidade);
    }
    
    public Grupo(ArrayList<Sintagma> lista) {
        listaSintagmas = new ArrayList<>();
        listaSintagmas.addAll(lista);
    }
    
    public Grupo(){
        listaSintagmas = new ArrayList<>();
    }

    public boolean addSintagma(Sintagma s) {
        return listaSintagmas.add(s);
    }

    public ArrayList<Sintagma> getListaSintagmas() {
        return listaSintagmas;
    }

    public boolean remove(Sintagma s) {
        for (Sintagma aux : listaSintagmas) {
            if (aux.snID == s.snID) {
                return listaSintagmas.remove(s);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String r="";
        for(Sintagma s: listaSintagmas){
            r+= s.sn + " ";
        }
        return r;
    }
    
}
