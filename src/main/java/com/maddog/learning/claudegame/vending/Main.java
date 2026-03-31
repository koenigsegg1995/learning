/**
 * 自動販賣機找零
 * -
 * 你在寫一台自動販賣機的找零系統。客人投幣後選了飲料，機器要用最少的硬幣數量把零錢找回去。
 * 機器裡有四種硬幣：50、10、5、1 元。
 * -
 * 範例：
 * 找零金額 | 回傳 | 說明
 * 87 | [1, 3, 1, 2] | 50×1 + 10×3 + 5×1 + 1×2 = 87
 * 50 | [1, 0, 0, 0] | 剛好一枚 50
 * 3  | [0, 0, 0, 3] | 只能用 1 元
 * 0  | [0, 0, 0, 0] | 不用找零
 */
package com.maddog.learning.claudegame.vending;

public class Main {

    public static void main(String[] args) {
        System.out.println(printResult(87));
        System.out.println(printResult(50));
        System.out.println(printResult(3));
        System.out.println(printResult(0));
    }

    /**
     * 回傳一個長度 4 的陣列
     *
     * @param amount
     *          找零金額
     * @return [50元幾枚, 10元幾枚, 5元幾枚, 1元幾枚]
     */
    public static int[] getChange(int amount) {
        // 建立長度為 4 的陣列
        int[] result = {0, 0, 0, 0};
        // 建立找零硬幣種類陣列
        int[] changeKind = {50, 10, 5, 1};

        for(int i = 0; i < 4; i++) {
            if(amount == 0) { // 找完錢
                return result;
            }

            // 幾枚當前硬幣
            result[i] = amount / changeKind[i];
            // 換完當前硬幣剩餘零錢
            amount %= changeKind[i];

        }

        return result;
    }

    /**
     * 將找零陣列轉為字串
     *
     * @param amount
     *          找零金額
     * @return 輸出字串
     */
    private static String printResult(int amount){
        // 找零陣列
        int[] change = getChange(amount);

        // 建立結果字串
        String result = "%d | [%d, %d, %d, %d] | 50×%d + 10×%d + 5×%d + 1×%d = %d";

        // 50 元
        int fifty = change[0];
        // 10 元
        int ten = change[1];
        // 5 元
        int five = change[2];
        // 1 元
        int one = change[3];

        return result.formatted(amount, fifty, ten, five, one, fifty, ten, five, one, amount);
    }

}
