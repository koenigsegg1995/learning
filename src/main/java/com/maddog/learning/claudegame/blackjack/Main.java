/**
 * 二十一點對決
 * 你跟莊家各從牌堆裡輪流抽牌（玩家先抽），規則如下：
 * -
 * 牌堆是一個 int[]，值 1～13，依序從頂部抽
 * 點數計算：1～10 照面值，11、12、13 都算 10 點
 * 玩家策略：手上總點數 16 以下必須抽，17 以上停手
 * 莊家策略：一樣，16 以下必須抽，17 以上停手
 * 超過 21 點就爆掉了
 * 雙方都停手後比大小，點數高的贏
 * -
 * **範例：**
 * 牌堆：[10, 8, 5, 9, 7]
 * -
 * 玩家抽 10 → 總計 10（≤16，繼續抽）
 * 莊家抽 8  → 總計 8（≤16，繼續抽）
 * 玩家抽 5  → 總計 15（≤16，繼續抽）
 * 莊家抽 9  → 總計 17（≥17，停手）
 * 玩家抽 7  → 總計 22（爆了）
 * -
 * 回傳 "DEALER"
 */
package com.maddog.learning.claudegame.blackjack;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n贏家: " + blackjack(new int[]{10, 8, 5, 9, 7}));
    }

    /**
     * 回傳 "PLAYER", "DEALER", 或 "DRAW"
     *
     * @param deck
     *          牌組
     * @return 贏家
     */
    public static String blackjack(int[] deck) {
        // 玩家繼續抽
        boolean playerContinue = true;
        // 莊家繼續抽
        boolean dealerContinue = true;

        // 玩家點數
        int playerPoint = 0;
        // 莊家點數
        int dealerPoint = 0;

        for(int i = 0; i < deck.length; i++){
            // 這次的分數，超過 10 為 10
            int currentPoint = Math.min(deck[i], 10);

            if( (i % 2 == 0 || !dealerContinue) && playerContinue ) { // 玩家抽
                playerPoint += currentPoint;

                if(playerPoint <= 16) { // 小於等於 16 點
                    System.out.printf("玩家抽 %d → 總計 %d（≤16，繼續抽）\n", deck[i],  playerPoint);
                } else if (playerPoint <= 21){ // 超過 16 點但小於 21 點
                    playerContinue = false;

                    System.out.printf("玩家抽 %d → 總計 %d（≥17，停手）\n", deck[i],  playerPoint);
                } else { // 超過 21 點
                    System.out.printf("玩家抽 %d → 總計 %d（爆了）\n", deck[i],  playerPoint);

                    return "DEALER";
                }
            } else { // 莊家抽
                dealerPoint += currentPoint;

                if(dealerPoint <= 16) { // 小於等於 16 點
                    System.out.printf("莊家抽 %d → 總計 %d（≤16，繼續抽）\n", deck[i],  dealerPoint);
                } else if(dealerPoint <= 21) { // 超過 16 點但小於 21 點
                    dealerContinue = false;

                    System.out.printf("莊家抽 %d → 總計 %d（≥17，停手）\n", deck[i],  dealerPoint);
                } else {
                    System.out.printf("莊家抽 %d → 總計 %d（爆了）\n", deck[i],  dealerPoint);

                    return "PLAYER";
                }
            }

            if(!playerContinue && !dealerContinue) {
                break;
            }
        }

        if(playerPoint > dealerPoint){ // 玩家勝
            return "PLAYER";
        } else if(dealerPoint > playerPoint){ // 莊家勝
            return "DEALER";
        } else { // 平手
            return "DRAW";
        }
    }

}
