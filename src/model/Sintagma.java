package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Sintagma implements Serializable {

    public String textName;
    public String sn;
    public int sentenca;
    public ArrayList<Word> words;
    public int set;
    public int snID;
    public String nucleo;
    public String lemma;
    public boolean prop;
    public String genero;
    public String numero;
    public boolean nucleoPronome;
    public String groupedBy;
    public boolean shallow;
    public ArrayList<Integer> paiDe;
    public int filhoDe;

    public Sintagma(String textName, String sn, int sentenca, ArrayList<Word> words, int set, int snID, String nucleo, String lemma, boolean prop, String genero, String numero, boolean nucleoPronome, String groupedBy, boolean shallow, ArrayList<Integer> paiDe, int filhoDe) {
        this.textName = textName;
        this.sn = sn;
        this.sentenca = sentenca;
        this.words = words;
        this.set = set;
        this.snID = snID;
        this.nucleo = nucleo;
        this.lemma = lemma;
        this.prop = prop;
        this.genero = genero;
        this.numero = numero;
        this.nucleoPronome = nucleoPronome;
        this.groupedBy = groupedBy;
        this.shallow = shallow;
        this.paiDe = paiDe;
        this.filhoDe = filhoDe;
    }

    @Override
    public String toString() {
        return sn;
    }
    
    
}
