/**
 * 便利商店折扣計算機
 * 你在便利商店寫收銀系統。店裡有「買二送一」的活動——每 3 件商品中，最便宜的那件免費。
 * -
 * 規則：
 * 商品依價格從高到低排序後，每 3 件一組
 * 每組中最便宜的（第 3 件）免費
 * 不滿 3 件的那組正常收費
 * 回傳折扣後的總金額
 * -
 * 範例：
 * 商品價格：[100, 50, 80, 30, 60, 20]
 * 排序後：[100, 80, 60, 50, 30, 20]
 * 第1組：100, 80, 60 → 60 免費 → 收 180
 * 第2組：50, 30, 20  → 20 免費 → 收 80
 * -
 * 總金額 = 260
 */
package com.maddog.learning.claudegame.checkout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("總金額 = " + checkout(new int[]{100, 50, 80, 30, 60, 20}));
    }

    /**
     * 回傳折扣後的總金額
     *
     * @param prices
     *          價格陣列
     * @return 折扣後總金額
     */
    public static int checkout(int[] prices) {
        int total = 0;
        List<Integer> team = new ArrayList<>();

        // 重新排列價格
        Arrays.sort(prices);

        for(int i = prices.length - 1; i >= 0; i--){
            team.add(prices[i]);

            // 集滿一組
            if(team.size() == 3){
                int teamTotal = getTeamTotal(team);

                total += teamTotal - team.get(2);

                team.clear();
            } else if (i == 0){
                int teamTotal = getTeamTotal(team);

                total += teamTotal;
            }
        }

        return total;
    }

    private static int getTeamTotal(List<Integer> team){
        return team.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

}
