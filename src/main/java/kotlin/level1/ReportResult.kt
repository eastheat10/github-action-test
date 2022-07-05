//package kotlin.level1
//
//import java.util.*
//
////import java.util.StringTokenizer
//
///**
// * 신고 결과 받기
// * https://programmers.co.kr/learn/courses/30/lessons/92334?language=java#
// */
//class ReportResult {
//
//    fun solution(id_list: Array<String>, report: Array<String>, k: Int): IntArray {
//
//        val answer: IntArray = intArrayOf(id_list.size)
//
//        val map: MutableMap<String, MutableSet<String>> = mutableMapOf()
//        val result: MutableMap<String, Int> = mutableMapOf()
//        val reportList: MutableSet<String> = mutableSetOf()
//
//        for(id in id_list) {
//            map[id] = mutableSetOf()
//        }
//
//        for (s in report) {
//            val st = StringTokenizer(s)
//            map[st.nextToken()]?.add(st.nextToken())
//        }
//
//        for (key in map.keys) {
//
//            for (bad in map[key]!!) {
//                result[bad] = result.getOrDefault(bad, 0) + 1
//            }
//        }
//
//        for (i in id_list.indices) {
//
//            for (bad in map[id_list[i]]!!) {
//                if (result[bad]!! >= k) {
//                    answer[i]++
//                }
//            }
//        }
//
//        return answer
//    }
//
//    /*
//    public int[] solution(String[] id_list, String[] report, int k) {
//
//        int[] answer = new int[id_list.length];
//
//        HashMap<String, HashSet<String>> map = new HashMap<>();
//        HashMap<String, Integer> result = new HashMap<>();
//        HashSet<String> reportList = new HashSet<>();
//
//        for (String id : id_list) {
//            map.put(id, new HashSet<>());
//        }
//
//        for (String r : report) {
//            StringTokenizer st = new StringTokenizer(r);
//            map.get(st.nextToken()).add(st.nextToken());
//        }
//
//        for (String key : map.keySet()) {
//
//            for (String bad : map.get(key)) {
//                result.put(bad, result.getOrDefault(bad, 0) + 1);
//            }
//        }
//
//        for(int i = 0; i < id_list.length; i++) {
//
//            for (String bad : map.get(id_list[i])) {
//                if (result.get(bad) >= k) {
//                    answer[i]++;
//                }
//            }
//        }
//
//        return answer;
//    }
//     */
//
//}