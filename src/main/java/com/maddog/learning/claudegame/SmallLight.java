package com.maddog.learning.claudegame;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

@Slf4j
public class SmallLight {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 主流程
        while (true) {
            // 原始檔路徑字串
            String oriStr;
            // 原始檔路徑
            Path oriPath;
            // 轉換檔路徑字串
            String destStr;
            // 轉換檔路徑
            Path destPath;
            // 最大筆數字串
            String limitStr;
            // 最大筆數
            int limit;

            //////////// 1. 輸入原始檔檔案路徑 ////////////
            System.out.println("請輸入原始檔案路徑，或於任意階段輸入 N 結束程式:");

            // 結束程式
            oriStr = formatInputStr(sc.nextLine());
            if (isOver(oriStr)){
                break;
            }

            // 有輸入字串且為合法路徑
            if (Files.isRegularFile(oriPath = Path.of(oriStr))) {
                log.info("原始檔案路徑: {}", oriStr);
            } else {
                System.out.println("輸入原始檔案路徑錯誤！");

                continue;
            }

            //////////// 2. 輸入轉換檔案路徑 ////////////
            System.out.println("請輸入轉換檔案路徑:");

            destStr = formatInputStr(sc.nextLine());
            // 是否結束
            if(isOver(destStr)){
                break;
            }

            destPath = Path.of(destStr);

            log.info("轉換檔案路徑: {}", destStr);

            //////////// 3. 輸入最大比數 ////////////
            System.out.println("請輸入最大筆數: ");

            // 結束程式
            limitStr = formatInputStr(sc.nextLine());
            if (isOver(limitStr)){
                break;
            }

            try {
                limit = Integer.parseInt(limitStr);

                // 上限低於 1 筆
                if (limit < 1) {
                    System.out.println("輸入最大筆數不能低於 1 筆！");

                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("輸入最大筆數錯誤！");

                continue;
            }

            log.info("最大筆數: {}", limit);

            //////////// 4. 開始轉換 ////////////
            try {
                use(oriPath, destPath, limit);

                // 轉換成功
                System.out.println("轉換成功！");
            } catch (Exception e) {
                System.out.println("轉換失敗！");

                log.error("轉換失敗！", e);
            }
        }

        System.out.println("程式結束！");

        sc.close();
    }

    /**
     * 使用縮小燈
     *
     * @param oriPath
     *          原始檔路徑
     * @param destPath
     *          轉換檔路徑
     * @param limit
     *          最大筆數上限
     * @throws IOException
     *          輸出入例外
     */
    private static void use(Path oriPath,
                            Path destPath,
                            int limit) throws IOException {
        try (
                BufferedReader reader = Files.newBufferedReader(oriPath, StandardCharsets.UTF_16);
                BufferedWriter writer = Files.newBufferedWriter(destPath, StandardCharsets.UTF_16LE)
        ) {
            // 寫 BOM
            writer.write('\uFEFF');

            // 列數
            int rowCnt = 0;
            // 該列資料字串
            String row;

            // 讀檔案資料，最多讀取 limit 筆
            while ((row = reader.readLine()) != null && rowCnt <= limit) {
                // 寫出
                writer.write(row);

                // 換行
                writer.newLine();

                // 計數
                rowCnt++;
            }
        }
    }

    /**
     * 輸入字串格式化
     *
     * @param input
     *          輸入字串
     * @return 格式化字串
     */
    private static String formatInputStr(String input) {
        return input.isBlank() ? "" : input.trim();
    }

    /**
     * 是否結束程式
     *
     * @param input
     *          輸入字串
     * @return 是/否
     */
    private static boolean isOver(String input) {
        return "N".equalsIgnoreCase(input.trim());
    }

}
