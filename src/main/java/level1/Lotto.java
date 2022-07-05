package level1;

import java.util.HashSet;
import java.util.Set;

/**
 * 로또 최고 순위와 최저 순위
 * https://programmers.co.kr/learn/courses/30/lessons/77484
 */
public class Lotto {

    public int[] solution(int[] lottos, int[] win_nums) {

        int right = 0;
        int unknown = 0;

        Set<Integer> win = new HashSet<>();
        for (int win_num : win_nums) {
            win.add(win_num);
        }

        for (int lotto : lottos) {
            if (win.contains(lotto)) {
                right++;
            }

            if (lotto == 0) {
                unknown++;
            }
        }

        int minRank = right < 2 ? 6 : 7 - right;
        int maxRank = minRank - unknown == 0 ? 1 : minRank - unknown;

        return new int[] {maxRank, minRank};
    }

    public static void main(String[] args) {

        Lotto lotto = new Lotto();
        int[] a1 = lotto.solution(new int[] {44, 1, 0, 0, 31, 25}, new int[] {31, 10, 45, 1, 6, 19});
        for (int i : a1) {
            System.out.print(i + " ");
        }
        System.out.println();


        int[] a2 = lotto.solution(new int[] {0, 0, 0, 0, 0, 0}, new int[] {38, 19, 20, 40, 15, 25});
        for (int i : a2) {
            System.out.print(i + " ");
        }
        System.out.println();

        int[] a3 = lotto.solution(new int[] {45, 4, 35, 20, 3, 9}, new int[] {20, 9, 3, 45, 4, 35});
        for (int i : a3) {
            System.out.print(i + " ");
        }
        System.out.println();

    }
}
