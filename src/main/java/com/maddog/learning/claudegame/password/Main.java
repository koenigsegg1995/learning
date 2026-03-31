/**
 * 密碼驗證器
 * 銀行的 ATM 系統需要驗證使用者設定的密碼，規則如下：
 * -
 * 長度必須在 8～16 之間
 * 必須包含至少 一個大寫字母
 * 必須包含至少 一個小寫字母
 * 必須包含至少 一個數字
 * 只能包含英文字母和數字（不允許特殊符號）
 * -
 * 回傳一個 List<String>，列出所有不符合的規則。如果密碼合法，回傳空 List。
 * -
 * 範例：
 * 輸入 | 回傳
 * "Abc12345" | [] （合法）
 * "abc"      | ["長度必須在 8~16 之間", "必須包含至少一個大寫字母", "必須包含至少一個數字"]
 * "ABCD1234" | ["必須包含至少一個小寫字母"]
 * "Abc@1234" | ["不允許特殊符號"]
 */
package com.maddog.learning.claudegame.password;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        System.out.println(printResult("Abc12345"));
        System.out.println(printResult("abc"));
        System.out.println(printResult("ABCD1234"));
        System.out.println(printResult("Abc@1234"));
    }

    /**
     * 回傳不符合的規則列表
     *
     * @param password
     *          密碼
     * @return 不符合規則 List
     */
    public static List<String> validate(String password) {
        // 建立不符合規則 List
        List<String> result = new ArrayList<>();

        // 1. 長度必須在 8～16 之間
        if(password.length() < 8 || password.length() > 16){
            result.add("長度必須在 8~16 之間");
        }

        // 轉換為 char 陣列
        char[] chars = password.toCharArray();

        // 至少一個大寫字母
        boolean uppercase = false;
        // 至少一個小寫字母
        boolean lowercase = false;
        // 至少一個數字
        boolean number = false;
        // 只能包含英文字母和數字（不允許特殊符號）
        boolean special = false;

        for(char c : chars){
            // 2. 必須包含至少一個大寫字母
            if(Character.isUpperCase(c)){
                uppercase = true;

                continue;
            }

            // 3. 必須包含至少一個小寫字母
            if(Character.isLowerCase(c)){
                lowercase = true;

                continue;
            }

            // 4. 必須包含至少一個數字
            if(Character.isDigit(c)){
                number = true;

                continue;
            }

            // 5. 只能包含英文字母和數字（不允許特殊符號）
            special = true;
        }

        if(!uppercase){
            result.add("必須包含至少一個大寫字母");
        }

        if (!lowercase){
            result.add("必須包含至少一個小寫字母");
        }

        if (!number){
            result.add("必須包含至少一個數字");
        }

        if(special){
            result.add("不允許特殊符號");
        }

        return result;
    }

    /**
     * 轉換輸出字串
     *
     * @param password
     *          密碼
     * @return 輸出字串
     */
    private static String printResult(String password){
        // 得到不符合規則 List
        List<String> result = validate(password);

        if(result.isEmpty()){ // 無不符合規則
            return "[] （合法）";
        }

        // 拼接結果字串
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        for(String s : result){
            joiner.add("\"" + s + "\"");
        }

        return joiner.toString();
    }

}
