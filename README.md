# InternshipManager

## 1. Giới thiệu sơ bộ

**InternshipManager** là ứng dụng quản lý hoạt động thực tập, hỗ trợ nhà trường hoặc đơn vị đào tạo quản lý người dùng, hồ sơ sinh viên, mentor, quá trình phân công thực tập và kết quả đánh giá.

Dự án được xây dựng theo mô hình frontend – backend tách biệt:

- **Backend:** Java 17, Spring Boot 4.1, Spring Security, Spring Data JPA, PostgreSQL và Redis.
- **Frontend:** React 19, Vite 8, React Router và Axios.
- **Xác thực:** JWT kết hợp Redis blacklist để vô hiệu hóa token khi đăng xuất.
- **Phân quyền:** ADMIN, MENTOR và STUDENT.

Backend chạy mặc định tại `http://localhost:8386`; frontend chạy tại `http://localhost:3000` trong môi trường phát triển.

## 2. Tính năng

### Quản lý tài khoản và xác thực

- Đăng nhập, đăng xuất và lấy thông tin tài khoản hiện tại.
- Phân quyền theo ba vai trò ADMIN, MENTOR và STUDENT.
- Quản lý tài khoản: tạo, xem, cập nhật, thay đổi vai trò và trạng thái hoạt động.
- Mã hóa mật khẩu và xác thực request bằng JWT.
- Đưa JWT đã đăng xuất vào Redis blacklist để ngăn token tiếp tục được sử dụng.

### Quản lý thực tập

- Quản lý hồ sơ sinh viên và mentor.
- Cho phép sinh viên và mentor xem, cập nhật hồ sơ cá nhân.
- Quản lý các giai đoạn thực tập (Internship Phases).
- Quản lý các đợt đánh giá (Assessment Rounds).
- Quản lý tiêu chí đánh giá (Evaluation Criteria) và liên kết tiêu chí với từng đợt đánh giá.
- Phân công sinh viên với mentor và giai đoạn thực tập.
- Tạo, xem và cập nhật kết quả đánh giá.

### Giao diện người dùng

- Đăng nhập và điều hướng theo trạng thái xác thực.
- Giao diện Dashboard.
- Các màn hình quản lý người dùng, sinh viên, mentor, phân công, giai đoạn, đợt đánh giá, tiêu chí và kết quả.
- Màn hình hồ sơ cá nhân dành cho sinh viên và mentor.
- Sidebar hiển thị chức năng theo vai trò tài khoản.

## 3. Cấu trúc

Cấu trúc chính của repository:

```text
InternshipManager/
├── src/
│   ├── main/
│   │   ├── java/com/example/internshipmanager/
│   │   │   ├── controller/       # REST API controllers
│   │   │   ├── exception/        # Exception và xử lý lỗi tập trung
│   │   │   ├── models/
│   │   │   │   ├── dto/          # Request/response DTO
│   │   │   │   └── entity/       # JPA entities
│   │   │   ├── repository/       # Spring Data repositories
│   │   │   ├── security/         # Spring Security, JWT, principal
│   │   │   └── service/          # Business logic và service implementations
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/                     # Backend tests
├── frontend/
│   ├── src/
│   │   ├── api/                  # Axios client
│   │   ├── components/           # Layout, sidebar, protected route
│   │   ├── context/              # Authentication context
│   │   └── pages/                # Các trang giao diện
│   ├── package.json
│   └── vite.config.js
├── postman/
│   ├── collections/
│   │   ├── InternshipManager - Complete API/
│   │   └── InternshipManager - Regression Tests (Business Rules)/
│   └── environments/
├── API_TESTING_GUIDE.md          # Hướng dẫn kiểm thử API chi tiết
├── .env.example                  # Mẫu biến môi trường
├── run-local.ps1                 # Khởi chạy backend trên Windows
├── run-local.sh                  # Khởi chạy backend trên Linux/macOS
├── build.gradle
├── gradlew
└── gradlew.bat
```

## 4. Cài và chạy ứng dụng

### Yêu cầu môi trường

Cài đặt các thành phần sau trước khi chạy dự án:

- JDK 17.
- PostgreSQL.
- Redis.
- Node.js và npm.
- Git (nếu clone repository từ Git).

### Chuẩn bị cơ sở dữ liệu và biến môi trường

1. Khởi động PostgreSQL và Redis.
2. Tạo database PostgreSQL tên `InternshipManager`.
3. Cấu hình tài khoản PostgreSQL tương ứng với cấu hình trong `src/main/resources/application.properties`. Mặc định, project sử dụng user `postgres` và database tại `localhost:5432/InternshipManager`.
4. Tại thư mục gốc, sao chép file mẫu:

   **Windows PowerShell**
   ```powershell
   Copy-Item .env.example .env
   ```

   **Linux/macOS**
   ```bash
   cp .env.example .env
   ```

5. Mở `.env` và cập nhật các giá trị theo môi trường local:

   ```dotenv
   DB_PASSWORD=your_postgres_password
   REDIS_HOST=localhost
   REDIS_PORT=6379
   ```

Không commit file `.env` hoặc đưa mật khẩu thật lên repository.

### Chạy backend

Mở terminal tại thư mục gốc dự án.

**Windows PowerShell:**

```powershell
.\run-local.ps1
```

**Linux/macOS:**

```bash
chmod +x ./run-local.sh
./run-local.sh
```

Các script sẽ nạp biến môi trường từ `.env` và chạy Spring Boot bằng Gradle Wrapper. Backend mặc định hoạt động tại:

```text
http://localhost:8386
```

Nếu chạy trực tiếp bằng IntelliJ IDEA hoặc Gradle, hãy khai báo các biến môi trường cần thiết trong Run Configuration trước khi khởi động.

### Chạy frontend

Mở một terminal khác:

```bash
cd frontend
npm install
npm run dev
```

Truy cập frontend tại:

```text
http://localhost:3000
```

Vite đã được cấu hình proxy các request `/api` và `/actuator` về backend tại `http://localhost:8386`, do đó frontend development server và backend cần được chạy đồng thời.

### Build và kiểm tra nhanh

Backend:

```powershell
.\gradlew.bat build
```

Frontend (chạy trong thư mục `frontend`):

```bash
npm run lint
npm run build
```

## 5. Kiểm thử

Dự án sử dụng hai hướng kiểm thử chính: kiểm thử backend/frontend bằng các lệnh build và lint, cùng với kiểm thử API theo từng vai trò bằng Postman.

### Kiểm thử tự động và kiểm tra build

Tại thư mục gốc, chạy:

```powershell
.\gradlew.bat test
```

Để kiểm tra khả năng đóng gói backend:

```powershell
.\gradlew.bat build
```

Tại thư mục `frontend`, chạy:

```bash
npm run lint
npm run build
```

### Kiểm thử API bằng Postman

Các request được tổ chức tại:

- `postman/collections/InternshipManager - Complete API/`: 61 request thuộc 11 nhóm, bao gồm Authentication, Users, Student Profiles, Mentor Profiles, Assignments, Phases, Assessment Rounds, Evaluation Criteria, Round Criteria, Assessment Results và Security/Negative Tests.
- `postman/collections/InternshipManager - Regression Tests (Business Rules)/`: các request kiểm tra hồi quy quy tắc nghiệp vụ.
- `postman/environments/InternshipManager - Local.environment.yaml`: cấu hình môi trường local.

Trước khi chạy request, hãy bảo đảm backend, PostgreSQL và Redis đang hoạt động; đồng thời kiểm tra base URL, thông tin đăng nhập và các ID dữ liệu test trong Postman Environment.

Nên thực hiện kiểm thử theo trình tự: đăng nhập từng vai trò → quản lý tài khoản → tạo hồ sơ sinh viên/mentor → tạo phase → tạo round → tạo criteria và round criteria → tạo assignment → tạo kết quả đánh giá → kiểm tra cập nhật, xóa và các trường hợp phân quyền.

Các nhóm kiểm thử bao gồm:

- Xác thực: đăng nhập hợp lệ/không hợp lệ, tài khoản không hoạt động, lấy thông tin người dùng và đăng xuất.
- JWT và Redis: xác nhận token sau khi logout bị từ chối.
- Phân quyền: kiểm tra quyền truy cập của ADMIN, MENTOR và STUDENT; request không có token hoặc sai vai trò.
- Nghiệp vụ: tạo, đọc, cập nhật và xóa các dữ liệu thuộc phạm vi được hỗ trợ.
- Validation và regression: dữ liệu thiếu/sai định dạng, ID không hợp lệ, dữ liệu trùng và các quy tắc nghiệp vụ liên quan.

**Kết quả kiểm thử hiện tại:** theo quá trình kiểm thử của dự án, toàn bộ test case đã được hoàn thành (100%) nếu không tính việc đánh giá nội dung văn bản của trường `message` trong response. Phần nội dung message trả về chưa nằm trong phạm vi xác nhận này.

Để biết thêm endpoint, request body, quyền truy cập và quy trình kiểm thử chi tiết, xem [API_TESTING_GUIDE.md](./API_TESTING_GUIDE.md).
