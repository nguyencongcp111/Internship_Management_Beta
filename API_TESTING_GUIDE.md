# InternshipManager – API & Kiểm thử thủ công

> Tài liệu được tổng hợp từ các Controller, DTO và SecurityConfig hiện có trong source code. Đây là hướng dẫn kiểm thử thủ công bằng Postman/Insomnia; không phải kết quả kiểm thử từng API. Một số endpoint chỉ được bảo vệ ở mức yêu cầu đăng nhập, trong khi quyền ADMIN/MENTOR/STUDENT được áp dụng theo annotation trong code.

## 1. Chuẩn bị môi trường

1. Khởi động PostgreSQL và Redis theo cấu hình local của project.
2. Tại thư mục gốc project, chạy:
   ```powershell
   .\\run-local.ps1
   ```
   Script này nạp biến môi trường từ `.env` rồi chạy Spring Boot. Nếu bạn khởi động bằng IntelliJ, cần khai báo biến môi trường tương ứng trong Run Configuration.
3. Mặc định Spring Boot dùng cổng `8080`, trừ khi cấu hình `server.port` được thay đổi.
4. Tạo một Postman Environment:
   - `baseUrl` = `http://localhost:8080`
   - `token` = để trống lúc đầu
   - `adminToken`, `mentorToken`, `studentToken` = token của từng vai trò sau khi đăng nhập.

Tất cả request JSON dùng header:

```http
Content-Type: application/json
Accept: application/json
```

Với endpoint cần đăng nhập, thêm:

```http
Authorization: Bearer <JWT_TOKEN>
```

## 2. Xác thực và phân quyền

SecurityConfig hiện tại quy định:

- `/api/auth/**` được permit ở tầng URL; riêng `/api/auth/me` cần JWT hợp lệ để lấy principal người dùng.
- `/api/admin/**` yêu cầu role ADMIN.
- Các endpoint còn lại yêu cầu đăng nhập; một số method có thêm `@PreAuthorize` theo ADMIN, MENTOR hoặc STUDENT.
- Session stateless; JWT được gửi trong header Authorization.
- Logout đưa token vào Redis blacklist. Sau logout, token đó không được tiếp tục sử dụng.

### 2.1 Đăng nhập

**POST** `/api/auth/login`

Body:

```json
{
  "username": "<username>",
  "password": "<password>"
}
```

Kỳ vọng: HTTP 200 và response chứa JWT trong object `data` (xem tên field thực tế trong response `JWTResponse`). Sao chép token để dùng cho các request tiếp theo.

Đăng nhập riêng từng tài khoản ADMIN, MENTOR và STUDENT để kiểm tra đúng quyền. Tài khoản không hoạt động hoặc thông tin đăng nhập sai phải bị từ chối.

### 2.2 Lấy thông tin tài khoản hiện tại

**GET** `/api/auth/me`

Header: `Authorization: Bearer <token>`

Kỳ vọng: HTTP 200 với thông tin user tương ứng token.

### 2.3 Đăng xuất

**POST** `/api/auth/logout`

Header: `Authorization: Bearer <token>`

Kỳ vọng: HTTP 200, message `Đã đăng xuất`.

**Kiểm tra blacklist:** ngay sau logout, gọi lại `GET /api/auth/me` bằng chính token vừa logout. Request phải bị từ chối (thông thường HTTP 401). Không dùng token đó cho các test tiếp theo; đăng nhập lại để lấy token mới.

## 3. Danh mục API

Các path bên dưới là path đầy đủ, không bao gồm `baseUrl`.

### 3.1 User / Admin

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/admin` | ADMIN | Lấy danh sách user |
| GET | `/api/admin/{id}` | ADMIN | Lấy user theo ID (controller trả HTTP 302 FOUND) |
| POST | `/api/admin` | ADMIN | Tạo user |
| PUT | `/api/admin/{id}` | ADMIN | Cập nhật user |
| PUT | `/api/admin/{id}/role` | ADMIN | Đổi role |
| PUT | `/api/admin/{id}/status` | ADMIN | Đổi trạng thái hoạt động |
| DELETE | `/api/admin/{id}` | ADMIN | Xóa user |

Body tạo user (`CreateUserRequest`):

```json
{
  "username": "test.student",
  "password": "ChangeMe123!",
  "fullName": "Test Student",
  "email": "test.student@example.com",
  "phoneNumber": "0900000000",
  "role": "STUDENT",
  "active": true
}
```

Lưu ý: role là enum; dùng đúng giá trị enum trong project (ví dụ ADMIN, MENTOR, STUDENT). DTO khai báo boolean `isActive`; nếu Jackson binding không nhận `active`, kiểm tra response lỗi và dùng đúng property hiển thị trong schema thực tế.

### 3.2 Student

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/student` | ADMIN | Danh sách sinh viên |
| GET | `/api/student/{id}` | Đã đăng nhập | Sinh viên theo ID |
| GET | `/api/student/my-profile` | STUDENT | Hồ sơ sinh viên đang đăng nhập |
| POST | `/api/student` | Đã đăng nhập | Tạo hồ sơ sinh viên |
| PUT | `/api/student/{id}` | ADMIN | Admin sửa hồ sơ sinh viên |
| PUT | `/api/student/my-profile` | STUDENT | Sinh viên tự sửa hồ sơ |

Body tạo hồ sơ (`CreateStudentRequest`):

```json
{
  "studentCode": "SV_TEST_001",
  "major": "Information Technology",
  "className": "IT01",
  "dateOfBirth": "2003-01-15",
  "address": "Ha Noi",
  "userId": 1
}
```

### 3.3 Mentor

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/mentor` | ADMIN | Danh sách mentor |
| GET | `/api/mentor/{id}` | Đã đăng nhập | Mentor theo ID |
| GET | `/api/mentor/my-profile` | MENTOR | Hồ sơ mentor đang đăng nhập |
| POST | `/api/mentor` | Đã đăng nhập | Tạo hồ sơ mentor |
| PUT | `/api/mentor/{id}` | ADMIN | Admin sửa hồ sơ mentor |
| PUT | `/api/mentor/my-profile` | MENTOR | Mentor tự sửa hồ sơ |

Body tạo hồ sơ (`CreateMentorRequest`):

```json
{
  "department": "Software Engineering",
  "academicRank": "Lecturer",
  "userId": 2
}
```

### 3.4 Internship Assignment

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/internship_assignment` | Đã đăng nhập | Danh sách assignment theo user hiện tại |
| GET | `/api/internship_assignment/{id}` | Đã đăng nhập | Chi tiết assignment theo user hiện tại |
| POST | `/api/internship_assignment` | ADMIN | Tạo assignment |
| PUT | `/api/internship_assignment/{id}` | ADMIN | Cập nhật assignment |

Body tạo assignment (`CreateAssignmentRequest`):

```json
{
  "assignmentTime": "15-11-2026 09:00",
  "status": "PENDING",
  "studentId": 1,
  "mentorId": 2,
  "phaseId": 1
}
```

`assignmentTime` dùng format `dd-MM-yyyy HH:mm`. Status là enum Status trong source code.

### 3.5 Internship Phases

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/internship_phases` | Đã đăng nhập | Danh sách giai đoạn |
| GET | `/api/internship_phases/{id}` | Đã đăng nhập | Chi tiết giai đoạn |
| POST | `/api/internship_phases` | ADMIN | Tạo giai đoạn |
| PUT | `/api/internship_phases/{id}` | ADMIN | Sửa giai đoạn |
| DELETE | `/api/internship_phases/{id}` | ADMIN | Xóa giai đoạn (controller trả HTTP 204) |

Body tạo giai đoạn (`CreatePhaseRequest`):

```json
{
  "phaseName": "Internship Phase 1",
  "startDate": "01-11-2026",
  "endDate": "30-11-2026",
  "description": "First internship phase"
}
```

Ngày dùng format `dd-MM-yyyy`.

### 3.6 Assessment Rounds

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/assessment_rounds` | Đã đăng nhập | Danh sách đợt đánh giá |
| GET | `/api/assessment_rounds/{id}` | Đã đăng nhập | Chi tiết đợt đánh giá |
| POST | `/api/assessment_rounds` | ADMIN | Tạo đợt đánh giá |
| PUT | `/api/assessment_rounds/{id}` | ADMIN | Sửa đợt đánh giá |
| DELETE | `/api/assessment_rounds/{id}` | ADMIN | Xóa đợt đánh giá |

Body tạo round (`CreateRoundRequest`):

```json
{
  "roundName": "Mid-term Evaluation",
  "phaseId": 1,
  "startDate": "2026-11-05",
  "endDate": "2026-11-15",
  "description": "Mid-term review"
}
```

### 3.7 Evaluation Criteria

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/evaluation_criteria` | Đã đăng nhập | Danh sách tiêu chí |
| GET | `/api/evaluation_criteria/{id}` | Đã đăng nhập | Chi tiết tiêu chí |
| POST | `/api/evaluation_criteria` | ADMIN | Tạo tiêu chí |
| PUT | `/api/evaluation_criteria/{id}` | ADMIN | Sửa tiêu chí |
| DELETE | `/api/evaluation_criteria/{id}` | ADMIN | Xóa tiêu chí |

Body tạo criteria (`CreateCriteriaRequest`):

```json
{
  "criterionName": "Technical Skills",
  "description": "Evaluate technical knowledge and implementation",
  "maxScore": 10
}
```

### 3.8 Round Criteria

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/round_criteria` | Đã đăng nhập | Danh sách tiêu chí của các round |
| GET | `/api/round_criteria/{id}` | Đã đăng nhập | Chi tiết liên kết round-criteria |
| POST | `/api/round_criteria` | ADMIN | Tạo liên kết round-criteria |
| PUT | `/api/round_criteria/{id}` | ADMIN | Sửa liên kết |
| DELETE | `/api/round_criteria/{id}` | ADMIN | Xóa liên kết |

Body tạo round criteria (`CreateRoundCriteriaRequest`):

```json
{
  "weight": 1,
  "roundId": 1,
  "criteriaId": 1
}
```

### 3.9 Assessment Results

| Method | Endpoint | Quyền theo code | Mục đích |
|---|---|---|---|
| GET | `/api/assessment_results` | Đã đăng nhập; kết quả còn phụ thuộc user hiện tại | Lấy kết quả; query tùy chọn `assignmentId`, `userId` |
| GET | `/api/assessment_results/{id}` | Đã đăng nhập; service kiểm tra user hiện tại | Chi tiết kết quả |
| POST | `/api/assessment_results` | MENTOR | Tạo kết quả |
| PUT | `/api/assessment_results/{id}` | MENTOR | Cập nhật kết quả |

Ví dụ query:

```http
GET {{baseUrl}}/api/assessment_results?assignmentId=1
GET {{baseUrl}}/api/assessment_results?userId=1
```

Body tạo kết quả (`ResultRequest`):

```json
{
  "assignmentId": 1,
  "roundId": 1,
  "criterionId": 1,
  "score": 8.5,
  "comments": "Good progress",
  "evaluatedBy": 2,
  "evaluationDate": "11/11/2026 15:30"
}
```

Ngày đánh giá dùng format `dd/MM/yyyy HH:mm`.

## 4. Quy trình kiểm thử đề xuất

Thực hiện theo thứ tự dưới đây để tránh lỗi khóa ngoại và dữ liệu thiếu:

1. **Auth:** đăng nhập ADMIN, MENTOR, STUDENT; kiểm tra `/api/auth/me`.
2. **User:** dùng ADMIN tạo các user test với role tương ứng. Ghi lại ID.
3. **Profile:** tạo hồ sơ Student/Mentor gắn với user ID; kiểm tra endpoint `my-profile`.
4. **Phase:** ADMIN tạo phase; ghi lại phase ID.
5. **Round:** ADMIN tạo round thuộc phase vừa tạo; ghi lại round ID.
6. **Criteria:** ADMIN tạo tiêu chí; ghi lại criteria ID.
7. **Round Criteria:** ADMIN liên kết round và criteria.
8. **Assignment:** ADMIN tạo assignment với student ID, mentor ID, phase ID hợp lệ.
9. **Results:** MENTOR tạo kết quả cho assignment/round/criteria tương ứng.
10. **Update & delete:** thử cập nhật từng tài nguyên; chỉ xóa dữ liệu test sau cùng vì có thể bị ràng buộc khóa ngoại.
11. **Logout:** logout từng token và xác nhận token đã bị blacklist.

Dùng dữ liệu test riêng, không chạy POST/PUT/DELETE trên dữ liệu thật nếu chưa có backup. Với DELETE, kiểm tra trước các quan hệ phụ thuộc.

## 5. Ma trận kiểm thử bảo mật

Dùng cùng một endpoint để thử nhiều token:

| Tình huống | Request | Kết quả mong đợi |
|---|---|---|
| Không có token | Gọi endpoint yêu cầu đăng nhập | 401 Unauthorized |
| Token sai/hết hạn/đã logout | Gọi endpoint yêu cầu đăng nhập | 401 Unauthorized |
| STUDENT gọi API chỉ ADMIN | Ví dụ `GET /api/student` | 403 Forbidden |
| MENTOR gọi API chỉ ADMIN | Ví dụ `POST /api/internship_phases` | 403 Forbidden |
| STUDENT gọi API chỉ MENTOR | Ví dụ `POST /api/assessment_results` | 403 Forbidden |
| ADMIN gọi API chỉ MENTOR | Ví dụ `POST /api/assessment_results` | 403 Forbidden |
| Đúng role | Gọi endpoint đúng quyền | 2xx nếu dữ liệu hợp lệ |

Lưu ý: các endpoint như `GET /api/student/{id}`, `GET /api/mentor/{id}`, `POST /api/student` và `POST /api/mentor` hiện không có `@PreAuthorize` riêng; SecurityConfig chỉ yêu cầu đăng nhập cho nhóm endpoint này. Hãy kiểm thử và đánh giá xem mức phân quyền đó có đúng yêu cầu nghiệp vụ không.

## 6. Kiểm tra validation và lỗi

Với endpoint POST/PUT, thử các trường hợp:

- Bỏ trường bắt buộc hoặc gửi chuỗi rỗng.
- Gửi email sai định dạng ở API tạo user.
- Gửi ID không tồn tại.
- Gửi enum không hợp lệ.
- Gửi ngày/giờ sai format.
- Gửi điểm âm hoặc giá trị vượt giới hạn nghiệp vụ.
- Gửi dữ liệu gây trùng khóa hoặc vi phạm khóa ngoại.

Ghi lại HTTP status, response body và log server. Không mặc định mọi lỗi đều phải trả cùng một status; đối chiếu với `GlobalExceptionHandler` và service tương ứng.

## 7. Checklist ghi nhận kết quả

| # | Nhóm | Đã kiểm tra | Kết quả | Ghi chú |
|---|---|---|---|---|
| 1 | Login hợp lệ | ☐ | | |
| 2 | Login sai / user inactive | ☐ | | |
| 3 | GET /auth/me với token hợp lệ | ☐ | | |
| 4 | Logout và JWT blacklist | ☐ | | |
| 5 | Phân quyền ADMIN | ☐ | | |
| 6 | Phân quyền MENTOR | ☐ | | |
| 7 | Phân quyền STUDENT | ☐ | | |
| 8 | CRUD User | ☐ | | |
| 9 | Student / Mentor profile | ☐ | | |
| 10 | Phase / Round / Criteria | ☐ | | |
| 11 | Round Criteria / Assignment | ☐ | | |
| 12 | Assessment Results | ☐ | | |
| 13 | Validation và exception | ☐ | | |

## 8. Ghi chú về độ chính xác

- Danh mục endpoint và quyền ở trên được đối chiếu từ các Controller và `SecurityConfig` hiện tại.
- Body mẫu được lấy từ các request DTO đã đọc; với các request update, xem class `Edit...Request` tương ứng để biết chính xác field được phép cập nhật.
- Một số Controller trả status khác thông lệ REST (ví dụ `GET /api/admin/{id}` trả 302 FOUND; `DELETE /api/internship_phases/{id}` trả 204 NO_CONTENT dù có tạo response body). Khi kiểm thử, ghi nhận hành vi thực tế thay vì tự giả định status.
- Tài liệu này không thay thế kiểm thử tích hợp tự động. `BUILD SUCCESSFUL` ở Gradle xác nhận task build/test chạy thành công, nhưng cần xem báo cáo test để biết số lượng và phạm vi test.
