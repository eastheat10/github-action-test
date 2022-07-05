package level1;

import java.util.Map;

/**
 * 숫자 문자열과 영단어
 * https://programmers.co.kr/learn/courses/30/lessons/81301
 */

public class AlphaToNum {

    public int sol(String s) {

        Map<String, String> map = Map.of("zero", "0"
            , "one", "1"
            , "two", "2"
            , "three", "3"
            , "four", "4"
            , "five", "5"
            , "six", "6"
            , "seven", "7"
            , "eight", "8"
            , "ine", "9");

        int len = s.length();
        int begin = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {

            char c = s.charAt(i);

            if (isNum(c)) {
                begin++;
                sb.append(c);
            } else {
                if (map.containsKey(s.substring(begin, i + 1))) {
                    sb.append(map.get(s.substring(begin, i + 1)));
                    begin = i + 1;
                }
            }
        }

        sb.append(s, begin, len);

        return Integer.parseInt(sb.toString());
    }

    public boolean isNum(char c) {
        return 48 <= c && c <= 57;
    }

    public int solution(String s) {

        StringBuilder sb = new StringBuilder();

        String[] alpha =
            {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String[] digit = new String[10];
        for (int i = 0; i < 10; i++) {
            digit[i] = String.valueOf(i);
        }

        for (int i = 0; i < 10; i++) {
            s = s.replaceAll(alpha[i], digit[i]);
        }

        return Integer.parseInt(s);
    }

    public static void main(String[] args) {

        AlphaToNum a = new AlphaToNum();

        int a1 = a.solution("one4seveneight");
        int a2 = a.solution("23four5six7");
        int a3 = a.solution("2three45sixseven");
        int a4 = a.solution("123");

        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
        System.out.println(a4);
    }
}
