package week_4_tute;

public class StringTool {

    public String title(String s){
        String titled = "";
        String[] splitS = s.split(" ");

        for (int i = 0; i < splitS.length; i++){
           String[] wordSplit = splitS[i].split("");
           titled += wordSplit[0].toUpperCase() + splitS[i].substring(1) + " ";
        }
        return titled;
    }
}
