package level1;

import java.util.ArrayList;

/**
 * 신규 아이디 추천
 * https://programmers.co.kr/learn/courses/30/lessons/72410
 */

public class NewId {

    ArrayList<String> answer;
    int len;

    public String solution(String new_id) {

        answer = new ArrayList<>();
        len = new_id.length();

        new_id = new_id.toLowerCase();

        int begin = 0;
        for (int i = 0; i < len; i++) {

            char c = new_id.charAt(i);
            if (isWrong(c)) {
                answer.add(new_id.substring(begin, i));
                begin = i + 1;
            }
        }

        answer.add(new_id.substring(begin, len));

        new_id = getNewId();

        begin = 0;
        boolean isDot = false;
        for (int i = 0; i < len; i++) {

            char c = new_id.charAt(i);
            if (isDot && isDot(c)) {
                answer.add(new_id.substring(begin, i));
                begin = i + 1;
            }
            isDot = isDot(c);
        }
        answer.add(new_id.substring(begin, len));
        new_id = getNewId();

        begin = 0;
        for (int i = 0; i < len; i++) {

            char c = new_id.charAt(i);
            if (isDot(c)) {
                begin++;
            } else {
                break;
            }
        }

        new_id = new_id.substring(begin, len);
        len = new_id.length();

        int finish = len;

        for (int i = len - 1; i >= 0; i--) {

            char c = new_id.charAt(i);
            if (isDot(c)) {
                finish--;
            } else {
                break;
            }
        }

        new_id = new_id.substring(0, finish);
        len = new_id.length();

        if (len == 0) {
            new_id = "a";
            len++;
        }

        if (len >= 16) {
            new_id = new_id.substring(0, 15);
            if (new_id.charAt(14) == '.') {
                new_id = new_id.substring(0, 14);
            }
            len = new_id.length();
        }

        while (len < 3) {

            new_id += new_id.charAt(len - 1);
            len++;
        }

        return new_id;
    }

    public boolean isDot(char c) {
        return c == '.';
    }

    public String getNewId() {
        StringBuilder sb = new StringBuilder();
        for (String s : answer) {
            sb.append(s);
        }
        answer.clear();
        len = sb.toString().length();
        return sb.toString();
    }

    public boolean isWrong(char c) {
        return !(isAlpha(c) || isSpecialLetter(c) || isNumber(c));
    }

    public boolean isAlpha(char c) {
        return 97 <= c && c <= 122;
    }

    public boolean isSpecialLetter(char c) {
        return c == '-' || c == '_' || c == '.';
    }

    public boolean isNumber(char c) {
        return 48 <= c && c <= 57;
    }

    public static void main(String[] args) {

        NewId newId = new NewId();

        String a1 = newId.solution("...!@BaT#*..y.abcdefghijklm");
        String a2 = newId.solution("z-+.^.");
        String a3 = newId.solution("=.=");
        String a4 = newId.solution("123_.def");
        String a5 = newId.solution("abcdefghijklmn.p");

        System.out.println(a1 + " " + (a1.equals("bat.y.abcdefghi")));
        System.out.println(a2 + " " + (a2.equals("z--")));
        System.out.println(a3 + " " + (a3.equals("aaa")));
        System.out.println(a4 + " " + (a4.equals("123_.def")));
        System.out.println(a5 + " " + (a5.equals("abcdefghijklmn")));
    }
}
