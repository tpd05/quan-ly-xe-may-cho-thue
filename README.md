# Hệ Thống Quản Lý Cho Thuê Xe Máy - Phân Hệ Đối Tác (Java SE Console)

Tài liệu này hướng dẫn sử dụng chi tiết cho code base phục vụ thực hành Lập trình Hướng đối tượng nâng cao với Java Core (không dùng Spring/Hibernate), lưu trữ dữ liệu bằng file CSV.

## 1. Mục tiêu dự án

- Xây dựng phân hệ dành cho **đối tác** (chủ xe cho thuê) trong hệ thống quản lý thuê xe máy.
- Áp dụng kiến trúc phân tầng (Layered Architecture): Controller - Service - DAO - Model, tách biệt rõ với tầng View (Console UI).
- Rèn luyện OOP nâng cao: kế thừa, đa hình, generic (`DAO<T>`, `Mapper<T>`, `BaseDAO<T>`), enum, exception, tách lớp trách nhiệm.
- Lưu trữ dữ liệu bền vững bằng file CSV, không dùng database.

## 2. Yêu cầu môi trường

- JDK 17 trở lên.
- Terminal (zsh/bash/cmd) để biên dịch và chạy thủ công.
- IDE khuyến nghị: VS Code hoặc IntelliJ / Eclipse.

Kiểm tra Java:

    java -version
    javac -version

## 3. Cấu trúc thư mục

    src
    └── main
        └── java
            ├── controller
            │   ├── AuthController.java
            │   ├── DoiTacDonThueController.java
            │   ├── DoiTacMenuController.java
            │   ├── DoiTacXeController.java
            │   ├── MainMenuController.java
            │   └── TaiKhoanController.java
            ├── dao
            │   ├── BaseDAO.java
            │   ├── ChiTietDonThueDAO.java
            │   ├── DAO.java
            │   ├── DonThueDAO.java
            │   ├── Mapper.java
            │   ├── TaiKhoanDAO.java
            │   └── XeMayDAO.java
            ├── dto
            │   ├── ChiTietHienThiDTO.java
            │   ├── DonThueHienThiDTO.java
            │   ├── TaiKhoanHienThiDTO.java
            │   └── TaiKhoanSuaDTO.java
            ├── main
            │   └── Main.java
            ├── model
            │   ├── ChiTietDonThue.java
            │   ├── DonThue.java
            │   ├── TaiKhoan.java
            │   └── XeMay.java
            ├── service
            │   ├── AuthService.java
            │   ├── ChiTietDonThueService.java
            │   ├── DonThueService.java
            │   └── XeMayService.java
            ├── session
            │   └── Session.java
            ├── util
            │   ├── FilePath.java
            │   ├── FileUtil.java
            │   ├── IdGenerator.java
            │   ├── Input.java
            │   ├── PasswordUtil.java
            │   ├── Result.java
            │   └── Validator.java
            └── view
                ├── DoiTacDonThueView.java
                ├── DoiTacMenuView.java
                ├── DoiTacXeView.java
                ├── LoginView.java
                ├── MainMenuView.java
                └── TaiKhoanView.java

    data
    ├── chitietdonthue.csv
    ├── donthue.csv
    ├── taikhoan.csv
    └── xemay.csv

## 4. Vai trò từng tầng

### 4.1 Tầng Model (Domain)

- Chứa các đối tượng nghiệp vụ cốt lõi: `TaiKhoan`, `XeMay`, `DonThue`, `ChiTietDonThue`.
- `DonThue` và `XeMay` đều có enum trạng thái riêng (`TrangThai`) để quản lý vòng đời nghiệp vụ.
- Mỗi entity có 2 constructor: có ID (dùng khi đọc từ CSV) và không có ID (dùng khi tạo mới, ID do `IdGenerator` sinh tự động).

### 4.2 Tầng DAO (Data Access Object)

- Chuẩn hóa CRUD qua interface `DAO<T>` và mapping CSV qua interface `Mapper<T>`.
- `BaseDAO<T>` triển khai CRUD dùng chung (đọc/ghi toàn bộ file, tìm theo ID...), các DAO con chỉ cần định nghĩa `parse()`, `format()` và `getId()`.
- Dữ liệu được lưu trữ dạng CSV thuần (không dùng database), đọc/ghi qua `util.FileUtil`.
- Có thể thay thế bằng DAO đọc/ghi database trong tương lai mà không đổi logic ở tầng Service (miễn giữ nguyên interface `DAO<T>`).

### 4.3 Tầng DTO (Data Transfer Object)

- `DonThueHienThiDTO`, `ChiTietHienThiDTO`: gộp sẵn dữ liệu từ nhiều entity (đơn thuê + chi tiết + thông tin xe) thành 1 object để tầng View hiển thị mà không cần tự join dữ liệu.

### 4.4 Tầng Service (Business)

- Chứa toàn bộ luật nghiệp vụ:
  - Xác thực tài khoản, băm mật khẩu (`AuthService`).
  - Validate và quản lý xe, chỉ cho phép đối tác thao tác trên xe của chính mình (`XeMayService`).
  - Tổng hợp đơn thuê liên quan đến xe của đối tác hiện tại (`DonThueService`).
  - Tìm kiếm/xóa theo lô chi tiết đơn thuê (`ChiTietDonThueService`).
- Là tầng trung gian duy nhất được phép gọi xuống DAO; Controller/View không được gọi DAO trực tiếp.

### 4.5 Tầng Session

- `Session` lưu trạng thái tài khoản đang đăng nhập (static, dùng chung toàn ứng dụng).
- Chỉ phù hợp với ứng dụng console 1 người dùng/1 lần chạy; không dùng được nếu mở rộng sang mô hình đa người dùng đồng thời (web/multi-thread) mà không tái cấu trúc.

### 4.6 Tầng Util (Hỗ trợ kỹ thuật)

- `FileUtil`: đọc/ghi/append file, tự tạo thư mục/file nếu chưa tồn tại.
- `IdGenerator`: sinh ID tự tăng dựa trên giá trị lớn nhất hiện có trong file.
- `Validator`: tập trung toàn bộ rule kiểm tra hợp lệ (tài khoản, xe máy, địa chỉ...).
- `PasswordUtil`: băm mật khẩu bằng SHA-256, hỗ trợ tương thích ngược với dữ liệu cũ chưa băm.
- `Result`: đóng gói kết quả thao tác (thành công/thất bại + thông báo), dùng để Service trả lời Controller thay vì boolean đơn thuần.

### 4.7 Tầng View + Controller (Presentation)

- View: hiển thị menu, đọc input, validate định dạng cơ bản, không chứa logic nghiệp vụ.
- Controller: điều phối luồng, gọi Service xử lý nghiệp vụ, gọi View để nhập/hiển thị. Không viết logic nghiệp vụ ở Controller.

## 5. Luồng chạy tổng quát

1. `Main` khởi tạo `MainMenuController` và gọi `show()`.
2. `MainMenuController` hiển thị menu chính, xử lý đăng nhập qua `AuthController`.
3. Đăng nhập thành công với vai trò đối tác -> chuyển vào `DoiTacMenuController`.
4. `DoiTacMenuController` hiển thị menu đối tác với 4 chức năng: Quản lý xe, Quản lý đơn thuê, Thông tin tài khoản, Đăng xuất.
5. Mỗi chức năng con có Controller/View riêng, gọi xuống Service tương ứng để xử lý nghiệp vụ và Service gọi DAO để đọc/ghi dữ liệu CSV.

## 6. Cách biên dịch và chạy

Từ thư mục gốc dự án:

Biên dịch:

    javac -d out $(find src -name "*.java")

Chạy ứng dụng:

    java -cp out main.Main

Gợi ý dọn file class sau khi thử:

    find out -name "*.class" -delete

## 7. Hướng dẫn sử dụng menu Console

Menu chính:

- 1. Đăng nhập đối tác
- 0. Thoát

Menu đối tác (sau khi đăng nhập thành công):

- 1. Quản lý xe
- 2. Quản lý đơn thuê
- 3. Thông tin tài khoản
- 4. Đăng xuất

Menu Quản lý xe:

- 1. Xem danh sách xe
- 2. Thêm xe mới
- 3. Sửa thông tin xe
- 4. Xóa xe
- 5. Tìm kiếm xe
- 0. Quay lại menu đối tác

Menu Quản lý đơn thuê:

- 1. Xem danh sách đơn thuê
- 2. Xóa toàn bộ lịch sử đơn thuê
- 0. Quay lại

Menu Thông tin tài khoản:

- 1. Sửa thông tin
- 0. Quay lại

Lưu ý: chức năng **tạo mới đơn thuê** (đặt xe) không thuộc phạm vi phân hệ đối tác này — đối tác chỉ xem và quản lý lịch sử đơn thuê liên quan đến xe của mình. Việc tạo đơn thuê thuộc về phân hệ phía khách hàng (nằm ngoài phạm vi code hiện tại).

## 8. Tài liệu tính năng Quản lý xe

### 8.1 Mục tiêu tính năng

- Cho phép đối tác quản lý danh sách xe máy của chính mình: xem, thêm, sửa, xóa, tìm kiếm.
- Đảm bảo mỗi đối tác chỉ thấy và thao tác được trên xe thuộc sở hữu của mình (lọc theo `userID` trong `Session`).
- Đảm bảo dữ liệu đầu vào hợp lệ trước khi lưu, không cho crash chương trình khi người dùng nhập sai định dạng.
- Tuân thủ đúng luồng Layered Architecture: View -> Controller -> Service -> DAO.

### 8.2 Các lớp tham gia

- View: `DoiTacXeView` (`nhapThongTinXeMoi`, `nhapThongTinXeSua`, `hienThiDanhSachXe`...)
- Controller: `DoiTacXeController` (`quanLyXe`, `themXe`, `suaXe`, `xoaXe`, `timKiemXe`)
- Service: `XeMayService` (`them`, `sua`, `xoa`, `timKiem`, `timTheoBienSo`, `tonTaiBienSo`)
- DAO: `XeMayDAO` (`parse`, `format`, `getId`)
- Util: `Validator` (kiểm tra hãng xe, dòng xe, năm sản xuất, dung tích, biển số, giá thuê), `IdGenerator` (sinh mã xe mới)

### 8.3 Luồng xử lý chi tiết (chức năng Thêm xe mới)

1. Đối tác chọn menu 2 (Thêm xe mới) trong Quản lý xe.
2. `DoiTacXeController.themXe()` yêu cầu `requireDoiTac()` đã được kiểm tra từ trước khi vào menu.
3. `DoiTacXeView.nhapThongTinXeMoi()` đọc lần lượt: hãng xe, dòng xe, năm sản xuất, dung tích, biển số, giá/ngày, giá/tuần. Mỗi trường được validate và yêu cầu nhập lại ngay tại View nếu sai định dạng.
4. `XeMayService.them(xe)` validate lại nghiệp vụ (phòng trường hợp gọi trực tiếp service, không qua View) và kiểm tra trùng biển số trong phạm vi xe của đối tác hiện tại.
5. Service gán `userID` theo `Session.getCurrentUserID()` và sinh `maXe` mới qua `IdGenerator.nextID(FilePath.XE_MAY)`.
6. Service gọi `XeMayDAO.insert(xe)` để append dòng mới vào `data/xemay.csv`.
7. Controller nhận `Result` từ Service và gọi View hiển thị thông báo thành công/thất bại.

### 8.4 Quy tắc validate hiện tại

- Hãng xe, dòng xe: không rỗng, giới hạn độ dài.
- Năm sản xuất: từ 1900 đến năm hiện tại.
- Dung tích: 50 - 2000 cc.
- Biển số: đúng định dạng biển số xe máy Việt Nam, không trùng với xe khác của cùng đối tác.
- Giá thuê/ngày, giá thuê/tuần: phải lớn hơn 0.

### 8.5 Trường hợp có thể phát sinh khi thêm/sửa xe

- Biển số không hợp lệ hoặc bị trùng -> `Result.fail(...)`, không insert/update.
- Chưa đăng nhập hoặc không phải đối tác -> `IllegalStateException` từ `AuthService.requireDoiTac()`, bị chặn ngay từ đầu menu.
- Nhập sai kiểu dữ liệu (chữ thay vì số) khi sửa xe -> được bắt bằng vòng lặp nhập lại tại View, không làm crash chương trình.

### 8.6 Cơ chế lưu file CSV

- File đích: `data/xemay.csv`.
- Thêm mới: append 1 dòng vào cuối file (`FileUtil.appendLine`).
- Sửa/xóa: đọc toàn bộ danh sách, cập nhật trong bộ nhớ, ghi đè lại toàn bộ file (`FileUtil.writeAllLines`).
- Định dạng mỗi dòng CSV (`xemay.csv`):

      maXe,userID,hangXe,dongXe,namSanXuat,dungTich,bienSo,trangThai,giaNgay,giaTuan

  Ví dụ:

      3,1,Honda,Air Blade,2022,125.0,30A-123.45,SAN_SANG,120000,700000

### 8.7 Kiểm tra nhanh dữ liệu file sau khi thêm xe

Chạy lệnh tại thư mục gốc dự án:

    cat data/xemay.csv

### 8.8 Kịch bản kiểm thử thủ công (manual test)

Ca 1 - Thêm xe hợp lệ:

- Input: hangXe=Honda, dongXe=Vision, namSanXuat=2023, dungTich=110, bienSo=30A-111.11, giaNgay=100000, giaTuan=600000
- Expected: Thêm xe thành công.

Ca 2 - Biển số trùng:

- Thêm lại xe với biển số đã tồn tại của cùng đối tác.
- Expected: "Biển số đã tồn tại."

Ca 3 - Năm sản xuất tương lai:

- Input: namSanXuat = năm hiện tại + 1
- Expected: bị chặn tại View, yêu cầu nhập lại.

Ca 4 - Sửa xe với năm sản xuất không phải số:

- Input: nhập "abc" thay vì số khi sửa năm sản xuất.
- Expected: yêu cầu nhập lại, không crash chương trình.

Ca 5 - Xóa xe không tồn tại:

- Input: mã xe không có trong hệ thống.
- Expected: "Không tìm thấy xe."

## 9. Tài liệu tính năng Quản lý đơn thuê

### 9.1 Mục tiêu tính năng

- Cho phép đối tác xem toàn bộ đơn thuê liên quan đến các xe của mình.
- Cho phép đối tác xóa toàn bộ lịch sử đơn thuê và chi tiết tương ứng.
- Bảo toàn đơn thuê chỉ khi liên quan tới xe của đối tác hiện tại.

### 9.2 Các lớp tham gia

- View: `DoiTacDonThueView`
- Controller: `DoiTacDonThueController`
- Service: `DonThueService`
- DAO: `DonThueDAO`, `ChiTietDonThueDAO`
- DTO: `DonThueHienThiDTO`, `ChiTietHienThiDTO`

### 9.3 Luồng xử lý chi tiết

1. Đối tác chọn menu Quản lý đơn thuê từ `DoiTacMenuController`.
2. `DoiTacDonThueController.quanLyDonThue()` gọi `AuthService.requireDoiTac()` để đảm bảo chỉ đối tác mới truy cập được.
3. Người dùng chọn 1 để xem danh sách đơn thuê hoặc 2 để xóa toàn bộ lịch sử.
4. Với lựa chọn xem danh sách, controller gọi `donThueService.layDanhSachHienThi()` và `DoiTacDonThueView.hienThiDanhSachDonThue()` để hiển thị.
5. Với lựa chọn xóa, view yêu cầu xác nhận, sau đó controller gọi `donThueService.xoaToanBo()`.
6. `DonThueService.layDanhSachHienThi()` thu thập danh sách xe của đối tác, tìm chi tiết đơn thuê liên quan, rồi xây dựng DTO để hiển thị thông tin đơn và chi tiết từng xe.
7. `DonThueService.xoaToanBo()` xóa chi tiết đơn thuê trước, sau đó xóa các đơn thuê tương ứng.

### 9.4 Quy tắc nghiệp vụ hiện tại

- Chỉ hiển thị đơn thuê liên quan đến xe của đối tác hiện tại.
- Nếu một xe đã bị xóa khỏi hệ thống, đơn thuê vẫn được hiển thị nhưng thông tin xe sẽ được thay thế bằng thông báo "Không tìm thấy thông tin xe".
- Xóa lịch sử đơn thuê phải xóa cả chi tiết liên quan để tránh dữ liệu mồ côi.
- Đối tác không được tạo hoặc chỉnh sửa đơn thuê trong phân hệ này; phân hệ chỉ là quản lý và xóa lịch sử.

### 9.5 Kịch bản kiểm thử thủ công

Ca 1 - Xem danh sách đơn thuê:

- Chọn 1 từ menu Quản lý đơn thuê.
- Expected: hiển thị danh sách các đơn thuê liên quan đến xe của đối tác và chi tiết từng xe.

Ca 2 - Xóa toàn bộ lịch sử đơn thuê:

- Chọn 2, xác nhận 1.
- Expected: toàn bộ đơn thuê và chi tiết liên quan bị xóa, thông báo thành công.

Ca 3 - Hủy xóa:

- Chọn 2, nhập 0 khi được hỏi xác nhận.
- Expected: thao tác bị hủy, dữ liệu không thay đổi.

## 10. Tài liệu tính năng Thông tin tài khoản

### 10.1 Mục tiêu tính năng

- Cho phép đối tác xem thông tin cá nhân và cửa hàng hiện tại.
- Cho phép đối tác sửa các trường thông tin cá nhân mà không thay đổi username, password, userID hay role.
- Giữ nguyên các giá trị cũ khi người dùng bỏ trống ô nhập.

### 10.2 Các lớp tham gia

- View: `TaiKhoanView`
- Controller: `TaiKhoanController`
- Service: `AuthService`
- DTO: `TaiKhoanHienThiDTO`, `TaiKhoanSuaDTO`

### 10.3 Luồng xử lý chi tiết

1. Đối tác chọn menu Thông tin tài khoản từ `DoiTacMenuController`.
2. `TaiKhoanController.quanLyThongTin()` gọi `AuthService.requireDoiTac()` để bảo đảm quyền truy cập.
3. Controller hiển thị thông tin hiện tại từ `AuthService.layThongTinTaiKhoan()`.
4. Người dùng chọn 1 để sửa thông tin, `TaiKhoanView.nhapThongTinSua()` đọc các trường mới.
5. View cho phép bỏ trống mỗi trường để giữ nguyên giá trị cũ.
6. Controller gọi `AuthService.suaThongTinTaiKhoan(input)` và view hiển thị kết quả `Result`.

### 10.4 Quy tắc validate hiện tại

- Họ tên, số điện thoại, email, số CCCD, tên cửa hàng và địa chỉ cửa hàng phải hợp lệ theo `Validator`.
- Bỏ trống trường nghĩa là giữ nguyên giá trị hiện tại.
- Username, mật khẩu, userID và role không được sửa trong phân hệ này.

### 10.5 Kịch bản kiểm thử thủ công

Ca 1 - Xem thông tin tài khoản:

- Chọn 3 từ menu đối tác.
- Expected: hiển thị đầy đủ họ tên, số điện thoại, email, CCCD, tên cửa hàng, địa chỉ cửa hàng.

Ca 2 - Sửa thông tin thành công:

- Chọn 1, nhập giá trị mới hợp lệ cho email hoặc tên cửa hàng.
- Expected: thông báo cập nhật thành công.

Ca 3 - Nhập dữ liệu không hợp lệ:

- Chọn 1, nhập số điện thoại hoặc email sai định dạng.
- Expected: service trả về `Result.fail(...)` với thông báo lỗi.

## 11. Danh sách TODO / hạn chế đã biết cần lưu ý

### 11.1 Model

- Chưa có field lưu tổng tiền cho `DonThue`; tổng tiền/thành tiền hiện phải tự tính từ `ChiTietDonThue` (chưa có hàm tiện ích tính sẵn ở Service).

### 11.2 DAO

- `parse()`/`format()` dùng `split(",")` / `String.join(",", ...)` thuần, chưa hỗ trợ escaping CSV chuẩn (RFC 4180). Rủi ro với các trường địa chỉ tự do (`diaChiNhanXe`, `diaChiCuaHang`) nếu dữ liệu chứa dấu phẩy — hiện đang giảm thiểu bằng cách chặn dấu phẩy khi validate đầu vào (`Validator.isDiaChi`), chưa xử lý triệt để ở tầng DAO.
- Không có index/cache, mỗi thao tác `findById`/`update`/`delete` đều đọc lại toàn bộ file.

### 11.3 Service

- `DonThueService.xoaToanBo()` xóa theo từng bước tuần tự, chưa đảm bảo tính toàn vẹn giao dịch (transactional) nếu xóa giữa chừng thất bại.
- Chưa có hàm tính thành tiền/tổng tiền đơn thuê dựa trên đơn giá và thời gian thuê.
- Chưa có ràng buộc unique cho email/CCCD giữa các tài khoản khi sửa thông tin tài khoản.

### 11.4 Session

- Lưu trạng thái đăng nhập bằng static field, chỉ phù hợp 1 người dùng/1 tiến trình JVM.

### 11.5 UI

- `LoginView` chưa có lối thoát khi người dùng nhập sai định dạng liên tục (không có tùy chọn hủy quay lại menu chính).
- Chưa có bước xác nhận lại trước khi lưu khi sửa thông tin xe/tài khoản (khác với xóa, đã có `xacNhanXoa`).

## 12. Quy ước kiến trúc bắt buộc khi mở rộng

- Không viết logic nghiệp vụ ở View/Controller.
- Không cho View/Controller gọi DAO trực tiếp, bắt buộc qua Service.
- DAO chỉ làm CRUD và mapping CSV, không cài đặt business rule.
- Validate định dạng cơ bản có thể đặt ở View (để nhập lại ngay), nhưng validate nghiệp vụ bắt buộc phải lặp lại ở Service (phòng trường hợp Service được gọi từ nơi khác không qua View).
- Mọi thao tác thêm/sửa/xóa phải trả về `Result` để Controller/View biết rõ nguyên nhân thất bại, tránh trả `boolean` đơn thuần.

## 13. Lỗi thường gặp và cách xử lý

- Lỗi `NumberFormatException` khi nhập sai định dạng số:
  - Bọc parse bằng try-catch và yêu cầu nhập lại (xem mẫu tại `DoiTacXeView.nhapThongTinXeSua`).

- Lỗi `IllegalArgumentException` khi parse dòng CSV bị lệch cột:
  - Thường do dữ liệu 1 trường tự do (địa chỉ) chứa dấu phẩy làm lệch số cột. Kiểm tra lại dữ liệu trong file CSV tương ứng.

- Lỗi `IllegalStateException: Chưa đăng nhập.`:
  - Xảy ra khi gọi các hàm Service yêu cầu `Session` (ví dụ `XeMayService`) mà chưa qua bước `AuthService.requireDoiTac()`. Kiểm tra lại Controller đã gọi guard này ở đầu luồng chưa.

- Sai tài khoản/mật khẩu hiển thị nhầm thành lỗi phân quyền:
  - Đảm bảo `AuthController.dangNhap()` trả về đúng kết quả thành công/thất bại để `MainMenuController` không hiển thị chồng thông báo.

## 14. Định hướng nâng cấp sau template

- Tách CSV read/write thành 1 lớp `CsvUtil` dùng chung, hỗ trợ escaping chuẩn.
- Thêm salt khi băm mật khẩu (hiện tại dùng SHA-256 không salt để đơn giản hóa phạm vi đồ án).
- Tách `Session` khỏi static field nếu mở rộng sang mô hình đa người dùng đồng thời.
- Bổ sung phân hệ phía khách hàng (tạo đơn thuê, thanh toán) để hoàn thiện toàn bộ nghiệp vụ cho thuê xe.
- Bổ sung logging chuẩn (java.util.logging) cho các thao tác quan trọng (đăng nhập, thêm/sửa/xóa xe, xóa lịch sử đơn thuê).

---