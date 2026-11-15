import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AhoCor {
    static class Node {
        Node[] next=new Node[26];
        Node fail;
        Node outputLink;
        List<Integer> endid =new ArrayList<>();
    }
    private final Node root=new Node();
    private final List<Integer> lengths=new ArrayList<>();

    public AhoCor(String[] patterns) {
        for(String pattern:patterns) {
            add(pattern);
        }
        build();
    }
    private void add(String pattern) {
        Node current=root;
        int id=lengths.size();
        lengths.add(pattern.length());
        for(int i=0;i<pattern.length();i++) {
            int c=pattern.charAt(i)-'a';
            if(current.next[c]==null) {
                current.next[c]=new Node();
            }
            current=current.next[c];
        }
        current.endid.add(id);
    }

    private void build() {
        Queue<Node> queue=new LinkedList<>();
        root.fail=root;
        for(int c=0;c<26;c++) {
            if(root.next[c]==null) {
                root.next[c]=root;
            } else {
                root.next[c].fail=root;
                queue.add(root.next[c]);
            }
        }
        while(!queue.isEmpty()) {
            Node current=queue.remove();
            for(int c=0;c<26;c++) {
                Node child=current.next[c];
                if(child==null) {
                    current.next[c]=current.fail.next[c];
                } else {
                    child.fail=current.fail.next[c];
                    if(!child.fail.endid.isEmpty()) {
                        child.outputLink=child.fail;
                    } else {
                        child.outputLink=child.fail.outputLink;
                    }
                    queue.add(child);
                }
            }
        }
    }
    public List<Match> search(String text) {
        List<Match> matches=new ArrayList<>();
        Node current=root;
        for(int i=0;i<text.length();i++) {
            current=current.next[text.charAt(i)-'a'];
            Node node=current;
            while(node!=null) {
                for(int id:node.endid) {
                    matches.add(new Match(id,i-lengths.get(id)+1));
                }
                node=node.outputLink;
            }
        }
        return matches;
    }
}
