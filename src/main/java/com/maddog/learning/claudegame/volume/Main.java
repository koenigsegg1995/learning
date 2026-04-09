/**
 * 音量調節器
 * 你在寫一個音響的音量控制邏輯。使用者可以按「+」加大音量、按「-」降低音量。但有幾個規則要注意：
 * -
 * 音量範圍是 0 ~ 100
 * 加超過 100 就停在 100，減低於 0 就停在 0
 * 有一個「靜音」按鈕（用 M 表示），按下去音量會記住當前值並變成 0；再按一次會恢復成記住的值
 * 在靜音狀態下按 + 或 - 會自動取消靜音，然後才執行該操作
 * -
 * 給你一個初始音量和一串操作，回傳最終音量。
 * -
 * 範例：
 * 初始 | 操作     | 結果  | 說明
 * 50  | "+++"   |  53  | 50→51→52→53
 * 98  | "+++++" |  100 | 上限封頂
 * 30  | "M++"   |  32  | 30→靜音(記住30)→取消靜音回30→31→32
 * 40  | "MM"    |  40  | 靜音→取消靜音，回到原值
 * 60  | "M"     |  0   | 靜音中，最終音量是 0
 */
package com.maddog.learning.claudegame.volume;

public class Main {

    public static void main(String[] args) {
        System.out.println(adjust(50, "+++"));
        System.out.println(adjust(98, "+++++"));
        System.out.println(adjust(30, "M++"));
        System.out.println(adjust(40, "MM"));
        System.out.println(adjust(60, "M"));
    }

    /**
     * operations 是一串字元，每個字元代表一次操作
     * '+' 加 1, '-' 減 1, 'M' 切換靜音
     *
     * @param initial
     *          初始音量
     * @param operations
     *          操作
     * @return 最終音量
     */
    public static int adjust(int initial, String operations) {
        // 靜音
        boolean mute = false;
        // 音量
        int volume = initial;

        for(int i = 0; i < operations.length(); i++){
            switch(operations.charAt(i)){
                case '+' : {
                    volume = Math.min(volume + 1, 100);

                    mute = false;

                    break;
                }

                case '-' : {
                    volume = Math.max(volume - 1, 0);

                    mute = false;

                    break;
                }

                case 'M' : {
                    mute = !mute;

                    break;
                }
            }
        }

        return mute ? 0 : volume;
    }

}
