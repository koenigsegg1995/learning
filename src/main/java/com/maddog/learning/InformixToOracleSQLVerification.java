package com.maddog.learning;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@Slf4j
public class InformixToOracleSQLVerification {

    /**
     * 資料表名稱預設
     */
    private static final String DEFAULT_SYSTEM_NAME = "資料表名稱無系統名";

    /**
     * yml table-name 列固定文字 (縮排 4 空白)
     */
    private static final String TABLE_NAME_ROW = "    - table-name: ";

    /**
     * yml informix 語法列固定文字 (縮排 6 空白)
     */
    private static final String IFX_SQL_ROW = "      ifx-sql: ";

    /**
     * yml oracle SQL 列固定文字 (縮排 6 空白)
     */
    private static final String ORA_SQL_ROW = "      ora-sql: ";

    /**
     * txt Log row-num 列固定文字
     */
    private static final String LOG_ROW_NUM_ROW = "- row-num: ";

    /**
     * txt Log table-name 列固定文字 (縮排 4 空白)
     */
    private static final String LOG_TABLE_NAME_ROW = "    table-name: ";

    /**
     * txt Log description 列固定文字 (縮排 4 空白)
     */
    private static final String LOG_DESCRIPTION_ROW = "    description: ";

    /**
     * txt Log failed 列固定文字
     */
    private static final String LOG_FAILED_ROW = "failed: ";

    /**
     * txt Log success 列固定文字
     */
    private static final String LOG_SUCCESS_ROW = "success: ";

    /**
     * txt Log total 列固定文字
     */
    private static final String LOG_TOTAL_ROW = "total: ";

    /**
     * txt Log description "空白列"
     */
    private static final String LOST_ROW = "空白列";

    /**
     * txt Log description "資料表名稱為空"
     */
    private static final String LOST_TABLE_NAME = "資料表名稱為空";

    /**
     * txt Log description "欄位為空"
     */
    private static final String LOST_CELL = "欄位為空";

    /**
     * txt Log description "SQL 為空"
     */
    private static final String LOST_SQL = "SQL 為空";

    public static void main(String[] args) {
        // 使用者輸入 excel 檔所在資料夾 與 yml 輸出資料夾 路徑
        Scanner scanner = new Scanner(System.in);
        String sourceFileDir;
        Path targetDir;

        // 去除頭尾空格與引號
        System.out.println("請輸入 excel 檔所在資料夾路徑:");
        sourceFileDir = scanner.nextLine()
                .trim()
                .replaceAll("^\"|\"$", "");

        // 去除頭尾空格與引號
        System.out.println("請輸入 yml 輸出資料夾路徑:");
        targetDir = Path.of(
                scanner.nextLine()
                        .trim()
                        .replaceAll("^\"|\"$", "")
        );

        try (Stream<Path> sourcePathsStream = Files.list(Path.of(sourceFileDir))) {
            // 取得該資料夾內所有目標檔案 (非 ~$ 開頭的 .xlsx 檔)
            List<Path> sourcePathList = sourcePathsStream
                    .filter(file -> {
                        String fileName = file.getFileName().toString();

                        return fileName.endsWith(".xlsx") && !fileName.startsWith("~$");
                    })
                    .toList();

            // 將每個檔案進行轉換且驗證
            for (Path source : sourcePathList) {
                // 讀取 excel 轉換成 yml
                Path resultPath = transform(source, targetDir);

                if (resultPath != null) {
                    // 驗證 yml 檔是否合法
                    verifyYml(resultPath);
                } else {
                    log.warn("transform 失敗回傳 null ，跳過驗證步驟！");
                }
            }
        } catch (Exception e) {
            log.error("發生錯誤: ", e);
        }
    }

    /**
     * 核心方法，讀取 excel 轉換成 yml
     *
     * @param source
     *          excel 路徑
     * @param targetDir
     *          yml 輸出資料夾 路徑
     * @return resultPath
     *          yml 檔路徑
     */
    private static Path transform(Path source, Path targetDir) {
        // yml 檔路徑
        Path resultPath = null;
        // txt 檔路徑
        Path resultLogPath;

        // 系統名
        String systemName = DEFAULT_SYSTEM_NAME;

        // 創建 StringBuilder 儲存結果
        StringBuilder result = new StringBuilder();
        result.append("compare-sql-setting:\n").append("  tables:\n");

        // 創建 StringBuilder 儲存結果記錄
        StringBuilder resultLog = new StringBuilder();

        // 記錄成功、失敗和總和筆數
        int success = 0;
        int failed = 0;
        int total;

        // 取得 DataFormatter ，用於取 Cell 值
        DataFormatter formatter = new DataFormatter();

        // 取得 excel 檔案
        File excelFile = source.toFile();

        // 開始處理資訊
        try (Workbook workbook = WorkbookFactory.create(excelFile, null, true)) {
            // 取得頁籤 (該案例基本上只有一個)
            Sheet sheet = workbook.getSheetAt(0);

            // 取得最後一列 index
            int lastRowNum = sheet.getLastRowNum();

            // 遍歷每一列
            row:
            for (int i = 1; i <= lastRowNum; i++) {
                // 資料表名稱, oracle SQL語法, informix SQL語法
                String tableName = "";
                String ifxSql = "";
                String oraSql = "";

                // 取得該 index Row
                Row row = sheet.getRow(i);

                if (row == null) { // 空白列
                    // 記錄錯誤
                    buildResultLog(resultLog, i + 1, tableName, LOST_ROW);
                    // 失敗記數
                    failed++;

                    // 跳下一列
                    continue;
                }

                // 遍歷三個欄位 (0: 資料表名稱, 1: oracle SQL語法, 2: informix SQL語法
                for (int j = 0; j < 3; j++) {
                    // 取得該 index Cell
                    Cell cell = row.getCell(j);

                    if (cell == null) { // 空白欄
                        // 記錄錯誤
                        buildResultLog(resultLog, i + 1, tableName, LOST_CELL);
                        // 失敗記數
                        failed++;

                        // 跳下一列
                        continue row;
                    }

                    // 取得關鍵字串
                    switch (j) {
                        case 0 -> { // 資料表名稱
                            tableName = clearStr(formatter.formatCellValue(cell));

                            if (StringUtils.isEmpty(tableName)) { // 資料表名稱 為空
                                // 記錄錯誤
                                buildResultLog(resultLog, i + 1, tableName, LOST_TABLE_NAME);
                                // 失敗記數
                                failed++;

                                // 跳下一列
                                continue row;
                            }

                            // 取得系統名，只賦值一次，依照 資料表名稱 習慣，第一個底線前應為系統名
                            systemName = DEFAULT_SYSTEM_NAME.equals(systemName) ? tableName.split("_")[0] : systemName;
                        }

                        case 1 -> { // oracle SQL語法 (移除換行符與非法空格)
                            oraSql = clearStr(formatter.formatCellValue(cell));

                            if (StringUtils.isEmpty(oraSql)) { // oracle SQL語法 為空
                                // 記錄錯誤
                                buildResultLog(resultLog, i + 1, tableName, LOST_SQL);
                                // 失敗記數
                                failed++;

                                // 跳下一列
                                continue row;
                            }
                        }

                        case 2 -> { // informix SQL語法 (移除換行符與非法空格)
                            ifxSql = clearStr(formatter.formatCellValue(cell));

                            if (StringUtils.isEmpty(ifxSql)) { // informix SQL語法 為空
                                // 記錄錯誤
                                buildResultLog(resultLog, i + 1, tableName, LOST_SQL);
                                // 失敗記數
                                failed++;

                                // 跳下一列
                                continue row;
                            }
                        }
                    }
                }

                // 組裝結果字串
                buildResult(result, tableName, oraSql, ifxSql);
                // 成功記數
                success++;
            }

            // 取得 yml 檔路徑
            resultPath = targetDir.resolve(systemName + ".yml");
            // 輸出結果 yml 檔
            Files.writeString(resultPath, result.toString(), StandardCharsets.UTF_8);
            log.info("============================> {} yml 輸出完成！", systemName);

            // 統計成功失敗與總數
            total = success + failed;

            // resultLog 有內容
            if (!resultLog.isEmpty()) {
                // 加上統計結果
                addCountToLog(resultLog, failed, success, total);

                // 取得 txt 檔路徑
                resultLogPath = targetDir.resolve(systemName + "_exception.txt");
                // 輸出錯誤結果 txt 檔
                Files.writeString(resultLogPath, resultLog.toString(), StandardCharsets.UTF_8);
                log.info("============================> {} txt 輸出完成！",  systemName);
            }
        } catch (Exception e) {
            log.error("轉換失敗: ", e);
        }

        return resultPath;
    }

    /**
     * 驗證 yml 檔是否合法，若格式錯誤拋出例外
     *
     * @param ymlPath
     *          yml 檔路徑
     */
    private static void verifyYml(Path ymlPath) {
        // 取得系統名
        String systemName = ymlPath.getFileName().toString().split("\\.")[0];

        try (InputStream input = Files.newInputStream(ymlPath)) {
            Yaml yml = new Yaml();

            // 讀取成功為格式正確
            yml.load(input);

            log.info("============================> {} yml 格式正確！", systemName);
            throw new Exception();
        } catch (Exception e) {
            log.error("驗證 {} yml 發生錯誤: ", systemName, e);
        }
    }

    /**
     * 組裝 table 資訊
     *
     * @param result
     *          當前結果字串
     * @param tableName
     *          資料表名稱
     * @param oraSql
     *          oracle SQL語法
     * @param ifxSql
     *          informix SQL語法
     */
    private static void buildResult(StringBuilder result,
                                    String tableName,
                                    String oraSql,
                                    String ifxSql) {
        // 資料表名稱
        result.append(TABLE_NAME_ROW);
        result.append(tableName).append("\n");

        // informix SQL語法
        result.append(IFX_SQL_ROW);
        result.append(ifxSql).append("\n");

        // oracle SQL語法
        result.append(ORA_SQL_ROW);
        result.append(oraSql).append("\n");
    }

    /**
     * 清洗原始字串，移除換行與非法空格，再去除頭尾空格
     *
     * @param oriStr
     *          原始字串
     * @return cleanStr
     *          合法字串 (基於合法 tableName 不會換行，僅用於去除頭尾空格)
     */
    private static String clearStr(String oriStr) {
        return oriStr
                .replaceAll("[\\r\\n\\t\\u00A0]+", " ")
                .trim();
    }

    /**
     * 錯誤記錄
     *
     * @param resultLog
     *          當前錯誤記錄字串
     * @param rowNum
     *          excel 列號
     * @param tableName
     *          資料表名稱
     * @param description
     *          錯誤內容描述
     */
    private static void buildResultLog(StringBuilder resultLog,
                                       int rowNum,
                                       String tableName,
                                       String description) {
        // excel 列號
        resultLog.append(LOG_ROW_NUM_ROW);
        resultLog.append(rowNum).append("\n");

        // 資料表名稱
        resultLog.append(LOG_TABLE_NAME_ROW);
        resultLog.append(tableName).append("\n");

        // 錯誤內容描述
        resultLog.append(LOG_DESCRIPTION_ROW);
        resultLog.append(description).append("\n");
    }

    /**
     * 加上統計結果
     *
     * @param resultLog
     *          當前錯誤記錄字串
     * @param failed
     *          失敗數
     * @param success
     *          成功數
     * @param total
     *          總數
     */
    private static void addCountToLog(StringBuilder resultLog,
                                      int failed,
                                      int success,
                                      int total) {
        // 失敗數
        resultLog.append(LOG_FAILED_ROW);
        resultLog.append(failed).append("\n");

        // 成功數
        resultLog.append(LOG_SUCCESS_ROW);
        resultLog.append(success).append("\n");

        // 總數
        resultLog.append(LOG_TOTAL_ROW);
        resultLog.append(total).append("\n");
    }

}
