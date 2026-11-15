import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        test(new String[]{"he","she","his","hers"},"ahishers",4);
        test(new String[]{"a","aa","aaa"},"aaaa",9);
        test(new String[]{"abc","abc","bc"},"zabc",3);
        test(new String[]{"abc","xyz"},"dddd",0);
        System.out.println("Tests passed!!");
        System.out.println("patterns,text_length,aho_avg_ms,kmp_avg_ms,matches");

        int[] patternCounts={10,100,1000};
        int[] textLengths={1000,10000,100000};
        for(int count:patternCounts) {
            for(int length:textLengths) {
                benchmark(count,length);
            }
        }
    }
    private static void test(String[] patterns,String text,int expected) {
        List<Match> aho=new AhoCor(patterns).search(text);
        List<Match> kmp=KMP.search(patterns,text);
        if(aho.size()!=expected||!sameMatches(aho,kmp)) {
            throw new IllegalStateException("Test failed");
        }
    }
    private static boolean sameMatches(List<Match> first,List<Match> second) {
        if(first.size()!=second.size()) {
            return false;
        }
        Comparator<Match> order=Comparator.comparingInt((Match m)->m.patternId)
                .thenComparingInt(m->m.position);
        first.sort(order);
        second.sort(order);
        for(int i=0;i<first.size();i++) {
            if(first.get(i).patternId!=second.get(i).patternId
                    ||first.get(i).position!=second.get(i).position) {
                return false;
            }
        }
        return true;
    }
    private static void benchmark(int count,int length) {
        Random random=new Random(42+count*31+length);
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<length;i++) {
            builder.append((char)('a'+random.nextInt(26)));
        }
        String text=builder.toString();
        String[] patterns=new String[count];
        for(int i=0;i<count;i++) {
            int size=5+random.nextInt(6);
            int start=random.nextInt(length-size+1);
            patterns[i]=text.substring(start,start+size);
        }
        new AhoCor(patterns).search(text);
        KMP.search(patterns,text);
        long ahoTotal=0;
        long kmpTotal=0;
        int matches=0;
        for(int run=0;run<5;run++) {
            long start=System.nanoTime();
            List<Match> aho=new AhoCor(patterns).search(text);
            ahoTotal+=System.nanoTime()-start;

            start=System.nanoTime();
            List<Match> kmp=KMP.search(patterns,text);
            kmpTotal+=System.nanoTime()-start;
            if(!sameMatches(aho,kmp)) {
                throw new IllegalStateException("Returned different matches");
            }
            matches=aho.size();
        }
        System.out.printf(Locale.US,"%d,%d,%.3f,%.3f,%d%n",
                count,length,ahoTotal/5000000.0,kmpTotal/5000000.0,matches);
    }
}
