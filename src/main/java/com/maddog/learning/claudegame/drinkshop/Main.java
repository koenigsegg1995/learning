/**
 * 飲料店排隊計算機
 * 你在飲料店打工，每杯飲料製作時間不同。客人排成一排，你要算出每個客人從開始排隊到拿到飲料要等多久。
 * -
 * 規則很單純：
 * 一次只做一杯，做完才做下一杯
 * 每個客人的等待時間 = 前面所有人的製作時間 + 自己的製作時間
 * -
 * 範例：
 * 製作時間：[3, 5, 2]
 * 客人0：等 0 分鐘 + 做 3 分鐘 = 3
 * 客人1：等 3 分鐘 + 做 5 分鐘 = 8
 * 客人2：等 8 分鐘 + 做 2 分鐘 = 10
 * -
 * 回傳 [3, 8, 10]
 */
package com.maddog.learning.claudegame.drinkshop;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] result = waitTimes(new int[]{3, 5, 2});

        System.out.println(Arrays.toString(result));
    }

    /**
     * 回傳每個客人的總等待時間
     *
     * @param makeTimes
     *          製作時間
     * @return 等待時間陣列
     */
    public static int[] waitTimes(int[] makeTimes) {
        int total = 0;
        int[] result = new int[makeTimes.length];

        for(int i = 0; i < makeTimes.length; i++){
            total += makeTimes[i];
            result[i] = total;
        }

        return result;
    }

}
