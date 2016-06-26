package controller;

import java.util.ArrayList;
import model.Grupo;
import model.ListaDeGrupo;
import model.Sintagma;

public class ListaDeGrupoController {

    private ListaDeGrupo listaGrupo;
    private GrupoController grupos;

    public ListaDeGrupoController() {
        listaGrupo = new ListaDeGrupo();
        grupos = new GrupoController();
    }

    public ArrayList<Grupo> getGrupos() {
        return listaGrupo.getGrupos();
    }

    public Grupo getSolitarios() {
        return listaGrupo.getSolitarios();
    }

    public boolean addGrupo(Grupo g) {
        return listaGrupo.getGrupos().add(g);
    }

    public boolean removeSintagmaDoGrupo(Sintagma s) {
        for (Grupo g : listaGrupo.getGrupos()) {
            for (Sintagma s2 : g.getListaSintagmas()) {
                if (s.snID == s2.snID) {
                    return g.remove(s);
                }
            }
        }
        return false;
    }

    public boolean addSintagmaNoGrupo(Sintagma s) {
        Grupo aux = null;

        for (Grupo g : listaGrupo.getGrupos()) {
            if (s.set == -1) {
                listaGrupo.getSolitarios().addSintagma(s);
                return true;
            } else if (g.getListaSintagmas().get(0).set == s.set) {
                aux = g;
                aux.addSintagma(s);
                return true;
            }
        }

        addGrupo(new Grupo(s));
        return false;

    }

    public void organizaGrupos() {
        ArrayList<Grupo> listaGrupos = new ArrayList<>();
        for (Grupo g : listaGrupo.getGrupos()) {
            if (g.getListaSintagmas().size() == 1) {
                g.getListaSintagmas().get(0).set = -1;
                listaGrupo.getSolitarios().addSintagma(g.getListaSintagmas().get(0));
                listaGrupos.add(g);
            }

        }

        for (Grupo grupo : listaGrupos) {
            listaGrupo.getGrupos().remove(grupo);
        }
    }

    public boolean remove(ArrayList<Sintagma> listaApagar) {
        ArrayList<Sintagma> auxApagar = new ArrayList<>();
        ArrayList<Grupo> auxGrupo = new ArrayList<>();
        auxApagar.addAll(listaApagar);
        auxGrupo.addAll(listaGrupo.getGrupos());
        for (Sintagma apagar : auxApagar) {
            for (Grupo g : auxGrupo) {
                ArrayList<Sintagma> aux = new ArrayList<>();
                aux.addAll(g.getListaSintagmas());
                for (Sintagma s : aux) {
                    if (s.equals(apagar)) {
                        g.getListaSintagmas().remove(s);
                        listaApagar.remove(s);
                        if (g.getListaSintagmas().size() < 1) {
                            listaGrupo.getGrupos().remove(g);
                        }
                    }
                }
            }
        }
        return listaApagar.isEmpty();
    }

    public void altera(Sintagma s, Grupo g) {
        for (Grupo paraGrupo : listaGrupo.getGrupos()) {
            if (paraGrupo.equals(g)) {
                s.set = g.getListaSintagmas().get(0).set;
                paraGrupo.getListaSintagmas().add(s);
                break;
            }
        }

        for (Grupo doGrupo : listaGrupo.getGrupos()) {
            if (doGrupo.getListaSintagmas().contains(s) && !doGrupo.equals(g)) {
                doGrupo.getListaSintagmas().remove(s);
                break;
            }
        }
    }

    public String getLog(ArrayList<Sintagma> l1, ArrayList<Sintagma> l2) {
        String log = "";

        for (Sintagma s1 : l1) {
            for (Sintagma s2 : l2) {
                if (s1.snID == s2.snID) {
                    if (s1.set != -1 && s2.set == -1) {
                        log += "remove sn: " + s1.snID + "set:" + s2.set + "\n";
                    } else if (((s1.set != s2.set) && s2.set != -1) || ((s1.set == -1 && s2.set != -1))) {
                        log += "update sn: " + s1.snID + "set:" + s2.set + "\n";
                    }
                }
            }
        }
        return log;
    }

    public String imprime_Correfs(ArrayList<Sintagma> sintagmas) {
        grupos.ordenaPorSet(sintagmas);
        String cadeias = "";
        ArrayList<Integer> n = new ArrayList<Integer>();

        for (int i = 1; i < sintagmas.size(); i++) {
            int x = sintagmas.get(i - 1).set;

            if (x == sintagmas.get(i).set) {
                n.add(sintagmas.get(i).set);
            }
        }

        ArrayList<Sintagma> aux = new ArrayList<Sintagma>();
        for (int i = 0; i < sintagmas.size(); i++) {
            boolean b = true;
            for (int j = 0; j < n.size(); j++) {
                if (sintagmas.get(i).set == n.get(j) && b) {
                    b = false;
                    aux.add(sintagmas.get(i));
                }
            }

        }

        int x = -10;
        int set = 0;
        for (int j = 0; j < aux.size(); j++) {

            if (aux.get(j).set != x) {
                cadeias += "\n[Cadeia " + aux.get(j).set + "]\n";
                set = aux.get(j).set;
            }
            String caso = "";
            if(aux.get(j).sn.contains("%")){
                caso = aux.get(j).sn.replaceAll("%", "%%");
                cadeias += "sn=\"" + caso + "\" sn_set=" + aux.get(j).set + "\n";
            } else 
                cadeias += "sn=\"" + aux.get(j).sn + "\" sn_set=" + aux.get(j).set + "\n";
            

            x = aux.get(j).set;
        }
        return cadeias;
    }
}
