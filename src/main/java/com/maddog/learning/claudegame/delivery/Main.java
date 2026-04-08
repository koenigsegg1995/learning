/**
 * 快遞站包裹分類
 * 你在快遞站打工，負責把包裹依照重量分到不同貨車上，規則如下：
 * -
 * 每台貨車有最大載重限制（單位：公斤）
 * 包裹必須依照送來的順序裝車，不能調換順序
 * 當前這台車裝不下了（加上去會超重），就換下一台車
 * 回傳總共需要幾台貨車
 * -
 * **範例：**
 * ```
 * 包裹重量：[5, 3, 8, 2, 7, 1, 6]
 * 貨車載重上限：10
 * -
 * 第1台：5, 3 → 合計 8（再放 8 會超重，換車）
 * 第2台：8, 2 → 合計 10（再放 7 會超重，換車）
 * 第3台：7, 1 → 合計 8（再放 6 會超重，換車）
 * 第4台：6 → 合計 6
 * -
 * 回傳 4
 * -
 * 邊界情境也想一下：
 * 如果某個包裹單件就超過載重上限怎麼辦？那就是裝不下，回傳 -1
 */
package com.maddog.learning.claudegame.delivery;

public class Main {

    public static void main(String[] args) {
        System.out.println(countTrucks(new int[]{5, 3, 8, 2, 7, 1, 6}, 10));
    }

    /**
     * 回傳需要幾台貨車
     * @param packages
     *          包裹陣列
     * @param maxLoad
     *          最大載重
     * @return 趟數
     */
    public static int countTrucks(int[] packages, int maxLoad) {
        // 趟數
        int count = 0;
        // 載重
        int weight = 0;
        // 測試載重
        int testWeight = 0;

        for(int i = 0; i < packages.length; ){ // i 由內部遞增
            // 若單件包裹大於最大載重
            if(packages[i] > maxLoad){
                return -1;
            }

            // 裝看看
            testWeight = weight + packages[i];

            if(testWeight < maxLoad){
                if(i == packages.length - 1){ // 如果為最後一趟，直接出發
                    count++;

                    break;
                }

                // 裝貨
                weight = testWeight;

                i++;
            } else if(testWeight > maxLoad){
                // 維持原樣出發，下一趟車清空
                count++;
                weight = 0;
            } else { // 剛好等於上限
                // 裝貨出發
                count++;
                i++;
                weight = 0;
            }
        }

        return count;
    }

}
