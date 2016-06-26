package controller;

public class SintagmaController {
    
    public String trataString(String s) {
        s = s.replace("--", " ");
        s = s.replace(" )", ")");
        s = s.replace("( ", "(");
        s = s.replace(" %", "%");
        s = s.replace(" , ", ", ");
        s = s.replace(" .", ".");
        s = s.replace("_", " ");
        s = s.replace(" de a", " da ");
        s = s.replace(" do o ", " do ");
        s = s.replace(" de o ", " do ");
        s = s.replace(" de as ", " das ");
        s = s.replace(" de os ", " dos ");
        s = s.replace(" por o ", " pelo ");
        s = s.replace(" por a ", " pela ");
        s = s.replace(" por os ", " pelos ");
        s = s.replace(" por as ", " pelas ");
        s = s.replace(" em as ", " nas ");
        s = s.replace(" em os ", " nos ");
        s = s.replace(" em a ", " na ");
        s = s.replace(" em este ", " neste ");
        s = s.replace(" em esta ", " nesta ");
        s = s.replace(" em estes ", " nestes ");
        s = s.replace(" em estas ", " nestas ");
        s = s.replace(" em esse ", " nesse ");
        s = s.replace(" em essa ", " nessa ");
        s = s.replace(" em esses ", " nesses ");
        s = s.replace(" em essas ", " nessas ");
        s = s.replace(" em o ", " no ");
        s = s.replace(" aa ", " à ");
        s = s.replace(" a as ", " às ");

        return s;
    }
    
}
