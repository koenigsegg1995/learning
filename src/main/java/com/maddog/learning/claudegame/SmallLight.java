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

        // 原始檔路徑字串
        String oriStr;
        // 原始檔路徑
        Path oriPath;
        // 最大筆數
        int limit;
        // 輸入流
        BufferedReader oriData;

        while (true) {
            System.out.println("請輸入檔案路徑: ");

            // 有需入字串且為合法路徑
            if (sc.hasNext() && Files.isRegularFile(oriPath = Path.of(oriStr = sc.next()))) {

                log.info("檔案路徑: {}", oriStr);
            } else {
                System.out.println("輸入檔案錯誤！");

                continue;
            }

            System.out.println("請輸入最大筆數: ");

            if (sc.hasNextInt()) {
                limit = sc.nextInt();

                log.info("最大筆數: {}", limit);

                break;
            } else {
                System.out.println("輸入最大筆數錯誤！");
            }
        }
    }

    public void use(BufferedReader oriData,
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
