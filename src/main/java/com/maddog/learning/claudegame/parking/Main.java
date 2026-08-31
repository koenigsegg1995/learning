/**
 * 停車場計費系統
 * -
 * 你在寫停車場的計費程式。費率規則如下：
 * -
 * 前 1 小時：30 元（不足 1 小時也算 30）
 * 第 2～4 小時：每小時 20 元
 * 第 5 小時起：每小時 10 元
 * 不足一小時的部分無條件進位算一小時（例如停 2 小時 10 分鐘 → 算 3 小時）
 * 每日上限 200 元（超過就只收 200）
 * -
 * 給你進場和出場的分鐘數（從午夜 00:00 起算），回傳停車費。
 * -
 * 範例：
 *-
 * 進場	出場	        停了多久	                     計算	                 費用
 * 60	90	   30 分鐘 → 算 1 小時	 前 1 小時 30 元	                     30
 * 60	240	   180 分鐘 = 3 小時       30 + 20×2	                         70
 * 0	610	   610 分鐘 → 算 11 小時   30 + 20×3 + 10×7 = 160              160
 * 0	1440   1440 分鐘 = 24 小時	 30 + 20×3 + 10×20 = 290 → 超過上限   200
 */
package com.maddog.learning.claudegame.parking;

public class Main {

    public static void main(String[] args) {

    }

    /**
     * 回傳停車費
     *
     * @param entryMinute
     *          進場時間（分鐘）
     * @param exitMinute
     *          出場時間（分鐘）
     * @return 停車費
     */
    public static int calculate(int entryMinute, int exitMinute) {

    }

}
