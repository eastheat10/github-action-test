package level1;

import java.util.*;

/**
 * 신고 결과 받기
 * https://programmers.co.kr/learn/courses/30/lessons/92334?language=java
 */

public class ReportResult {

    public int[] solution(String[] id_list, String[] report, int k) {

        int[] answer = new int[id_list.length];

        HashMap<String, HashSet<String>> map = new HashMap<>();
        HashMap<String, Integer> result = new HashMap<>();

        for (String id : id_list) {
            map.put(id, new HashSet<>());
        }

        for (String r : report) {
            StringTokenizer st = new StringTokenizer(r);
            map.get(st.nextToken()).add(st.nextToken());
        }

        for (String key : map.keySet()) {

            for (String bad : map.get(key)) {
                result.put(bad, result.getOrDefault(bad, 0) + 1);
            }
        }

        for(int i = 0; i < id_list.length; i++) {

            for (String bad : map.get(id_list[i])) {
                if (result.get(bad) >= k) {
                    answer[i]++;
                }
            }
        }

        return answer;
    }
}