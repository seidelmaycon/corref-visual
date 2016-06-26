import java.io.File;
import java.util.ArrayList;
import model.Sintagma;

public class Acesso {
    Leitor leitor = new Leitor();
    
    public ArrayList<Sintagma> getSintagmas(String s){
        return leitor.getSintagmas(s);
    }
    
    public void salvarSintagmas(ArrayList<Sintagma> lista, File file){
        leitor.saveSintagmas(lista, file);
    }
    
    public String getText(String s){
        return leitor.getText(s);
    }
    
    public ArrayList<String> lerSentencas(String s){
        return leitor.lerSentencas(s);
    }
}