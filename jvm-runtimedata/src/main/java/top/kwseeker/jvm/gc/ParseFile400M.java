package top.kwseeker.jvm.gc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseFile400M {

    public static void main(String[] args) {
        List<Map<String, String>> lst = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Map<String, String> map = new HashMap<>(3); //TODO
            //map.put("Column1", "Content1");
            //map.put("Column2", "Content2");
            //map.put("Column3", "Content3");
            map.put("Column1", "Content1".intern());
            map.put("Column2", "Content2".intern());
            map.put("Column3", "Content3".intern());
            lst.add(map);
        }

        Map<String, List<Map<String, String>>> contentCache = new HashMap<>();
        //contentCache.put("contents", lst);
        contentCache.put("contents".intern(), lst);
    }
}