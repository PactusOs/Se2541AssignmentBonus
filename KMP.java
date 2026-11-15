import java.util.ArrayList;
import java.util.List;

public class KMP {
    public static List<Match> search(String[] patterns,String text) {
        List<Match> matches=new ArrayList<>();
        for(int id=0;id<patterns.length;id++) {
            String pattern=patterns[id];
            int[] pi=new int[pattern.length()];
            for(int i=1,j=0;i<pattern.length();i++) {
                while(j>0&&pattern.charAt(i)!=pattern.charAt(j)) {
                    j=pi[j-1];
                }
                if(pattern.charAt(i)==pattern.charAt(j)) {
                    j++;
                }
                pi[i]=j;
            }
            int j=0;
            for(int i=0;i<text.length();i++) {
                while(j>0&&text.charAt(i)!=pattern.charAt(j)) {
                    j=pi[j-1];
                }
                if(text.charAt(i)==pattern.charAt(j)) {
                    j++;
                }
                if(j==pattern.length()) {
                    matches.add(new Match(id,i-pattern.length()+1));
                    j=pi[j-1];
                }
            }
        }
        return matches;
    }
}
