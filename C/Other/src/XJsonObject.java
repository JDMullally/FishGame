import java.util.ArrayList;

public class XJsonObject {

    private int count;
    private ArrayList<String> seq;

    XJsonObject(int count, ArrayList<String> seq) {
        this.count = count;
        this.seq = new ArrayList<>(seq);
    }
}
