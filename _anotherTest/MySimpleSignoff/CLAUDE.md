# CLAUDE.md - 簡單簽核系統 (Approval System)

## 專案概述
本專案為一個基於 Java Spring Boot 的輕量級線上簽核系統。
主要功能包含：填寫申請單、設定簽核流程、主管審批（同意/駁回）以及查詢簽核狀態。

## 詳細功能需求
- **簽核單列表**: 使用者登入後, 第一個頁面為所有簽核單列表. 詳列該使用者有權限檢視的簽核單
- **新增簽核單**: 使用者點選 "新增簽核單" 後顯示介面提供使用者輸入簽核單內容
- **簽核單欄位**: 簽核單欄位由 XML 設定檔讀入, 目前欄位包含:
  1. Title: 單行純文字欄位
  2. Create Date: 日期欄位, 精確到秒, 開單時自動設定為開單時間
  3. Update Date: 日期欄位, 精確到秒, 該簽核單有變動時自動設定為變動時間
  4. Signoff Person: 單行純文字欄位, 包含多個使用者 account, 由逗號分隔, 簽核單送出後依本欄位順序送給各使用者依序簽核
  5. Main Category: 下拉選單, 單選, 內容由設定檔讀入
  6. Sub Category: 下拉選單, 單選, 內容由設定檔讀入, 並連動到 Main Category
  7. Content: Rich Edit 欄位, 可輸入文字, 貼圖, 調整文字格式.
  8. Attachment: 附檔欄位, 可附加多個檔案
  9. Assignee: 目前簽核者
  10. Creater: 開單者
  11. Comment: 單行純文字欄位
- **簽核流程**: 簽核流程由 XML 設定檔讀入, 目前流程的各 Step 及 Action 說明如下:
  - **Step**:
    - **Open**: Assignee 設定為 Creater. Assignee 可以修改簽核單內容.
  	- **Approving**: Assignee 依序設定為 Signoff Person 欄位記錄的內容. Assignee 除了 Comment 外, 不可修改簽核單內容
  	- **Accept**: 簽核完成且所有簽核者都同意, Assignee 設定回 Creater. Assignee 除了 Comment 外, 不可修改簽核單內容
  	- **Reject**: 簽核過程中有任一簽核者不同意, Assignee 設定回 Creater. Assignee 除了 Comment 外, 不可修改簽核單內容
  	- **Close**: 本簽核單已完成, Assignee 設定回 Creater. 所有檢核內容都不能修改
  - **Actions**:
    - **Send For Approval**: Step 在 Open 時出現, 點選後 Step 進入 Approving, 且 Assignee 設定為 Signoff Person 欄位記錄的第一個人
  	- **Accept**: Step 在 Approving 時出現, 點選後的動作如下:
  	  - 如果 Signoff Person 欄位中還有其他簽核者, Step 停在 Approving, Assignee 設定為 Signoff Person 欄位記錄的下一個人
  	  - 如果 Signoff Person 欄位中已經沒有其他簽核者, Step 跳到 Accept, Assignee 設定為 Creater
  	- **Reject**: Step 在 Approving 時出現, 點選後 Step 跳到 Reject, Assignee 設定為 Creater
  	- **Re-Open**: Step 在 Accept 或 Reject 時出現, 點選後 Step 跳到 Open, Assignee 設定為 Creater
  	- **Done**: Step 在 Accept 或 Reject 時出現, 點選後 Step 跳到 Close, Assignee 設定為 Creater
- **簽核記錄**:
  - 所有透過介面進行的簽核單變動皆要保留記錄
  - 記錄內容包含: 變動人, 變動時間, 變動欄位在變動前的內容
  - 簽核記錄可在簽核單中查詢

## 技術堆疊 (Tech Stack)
- **後端框架**: Java 17 / Spring Boot 3.x
- **前端框架**: 使用 Spring Boot Thymeleaf 引撉, 並整合前後端在同一專案中 
- **資料庫**: H2 Database (開發環境) / MariaDB (生產環境)
- **持久層**: Spring Data JPA
- **安全架構**: Spring Security (基於 JWT 的身份驗證)
- **建置工具**: Maven
- **開發工具**: VS Code

## 程式碼風格與規範 (Code Style & Guidelines)
### 1. 架構分層 (Layered Architecture)
嚴格遵守標準的 Spring Boot 三層架構：
- `controller/`: 負責 REST API 路由、請求驗證（使用 `@Valid`）。
- `service/`: 包含所有商業邏輯。事務處理請務必加上 `@Transactional`。
- `repository/`: 繼承 `JpaRepository`，專職資料庫存取。
- `dto/`: 負責請求（Request）與回應（Response）的資料傳輸物件，嚴禁將 Entity 直接暴露給前端。

### 2. 命名與編碼規範
- **類別命名**: 必須明確反映其職責（例如：`ApprovalService`、`ApprovalController`）。
- **例外處理**: 統一由 `global/exception/GlobalExceptionHandler` 攔截，回傳標準 JSON 錯誤格式。
- **回應格式**: 所有 API 統一回傳 `ApiResponse<T>` 包裝物件（包含 code, message, data）。
- **註解**: 使用 Lombok 簡化程式碼（`@Data`, `@Slf4j`, `@RequiredArgsConstructor`）。

### 3. 開發限制
- 撰寫 Service 邏輯時，必須同時在 `src/test/java` 建立對應的單元測試（Unit Test）。
- 修改資料庫 Schema 時，請同步更新 `src/main/resources/schema.sql`。
