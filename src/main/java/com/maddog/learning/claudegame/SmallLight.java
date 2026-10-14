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
            // 最大筆數
            int limit;

            //////////// 1. 輸入原始檔檔案路徑 ////////////
            System.out.println("請輸入原始檔案路徑，或輸入 N 結束程式:");

            // 結束程式
            if ("N".equals(oriStr = sc.next())){
                break;
            }

            // 有輸入字串且為合法路徑
            if (sc.hasNext() && Files.isRegularFile(oriPath = Path.of(oriStr))) {
                log.info("原始檔案路徑: {}", oriStr);
            } else {
                System.out.println("輸入原始檔案路徑錯誤！");

                continue;
            }

            //////////// 2. 輸入轉換檔案路徑 ////////////
            System.out.println("請輸入轉換檔案路徑:");

            // 有輸入字串且為合法路徑
            if (sc.hasNext()) {
                destPath = Path.of(destStr = sc.next());

                log.info("轉換檔案路徑: {}", destStr);
            } else {
                System.out.println("輸入轉換檔案路徑錯誤！");

                continue;
            }

            //////////// 3. 輸入最大比數 ////////////
            System.out.println("請輸入最大筆數: ");

            if (sc.hasNextInt()) {
                limit = sc.nextInt();

                log.info("最大筆數: {}", limit);
            } else {
                System.out.println("輸入最大筆數錯誤！");

                continue;
            }

            //////////// 4. 開始轉換 ////////////
            try (BufferedReader oriData = Files.newBufferedReader(oriPath, StandardCharsets.UTF_16)){
                use(oriData, destPath, limit);

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

    public static void use(BufferedReader oriData,
                    Path destPath,
                    int limit) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(destPath, StandardCharsets.UTF_16LE)) {
            // 寫 BOM
            writer.write('\uFEFF');

            // 列數
            int rowCnt = 0;
            // 該列資料字串
            String row;

            // 讀檔案資料，最多讀取 limit 筆
            while ((row = oriData.readLine()) != null && rowCnt <= limit) {
                // 寫出
                writer.write(row);

                // 換行
                writer.newLine();

                // 計數
                rowCnt++;
            }
        }
    }

}
