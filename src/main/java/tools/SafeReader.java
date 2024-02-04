package tools;

import java.util.List;

public class SafeReader {
    public static String getValue(List<String> list, int index){
        return index < list.size() && index >= 0 ? list.get(index) : "";
    }
}
