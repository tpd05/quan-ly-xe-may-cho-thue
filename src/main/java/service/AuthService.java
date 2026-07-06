package service;

import dao.TaiKhoanDAO;
import dto.TaiKhoanHienThiDTO;
import dto.TaiKhoanSuaDTO;
import model.TaiKhoan;
import session.Session;
import util.PasswordUtil;
import util.Result;
import util.Validator;

/**
 * Service xử lý nghiệp vụ xác thực: đăng nhập, đăng xuất, kiểm tra quyền
 * truy cập theo vai trò đối tác, và quản lý thông tin tài khoản của đối
 * tác hiện tại (xem, sửa).
 */
public class AuthService {

	private final TaiKhoanDAO dao;
	 
	public AuthService() {
		this(new TaiKhoanDAO());
	}
 
	/**
	 * Constructor phụ, chỉ dùng cho unit test: cho phép truyền vào 1
	 * {@link TaiKhoanDAO} trỏ tới file tạm, tránh test đụng vào file dữ
	 * liệu thật.
	 *
	 * @param dao DAO tài khoản dùng riêng cho test.
	 */
	public AuthService(TaiKhoanDAO dao) {
		this.dao = dao;
	}

	/**
	 * Đăng nhập vào hệ thống với vai trò đối tác.
	 *
	 * Mật khẩu được so sánh dưới dạng hash (SHA-256) qua
	 * {@link PasswordUtil#matches(String, String)}. Nếu tài khoản đang lưu
	 * mật khẩu ở dạng cũ (plaintext, từ dữ liệu trước khi áp dụng hashing),
	 * sau khi xác thực thành công mật khẩu sẽ tự động được băm lại và lưu
	 * đè xuống hệ thống, giúp dữ liệu cũ dần được chuyển hoàn toàn sang
	 * dạng hash mà không cần thao tác migrate thủ công.
	 *
	 * @param username Tên đăng nhập.
	 * @param password Mật khẩu.
	 * @return Tài khoản đã đăng nhập thành công.
	 * @throws IllegalArgumentException nếu sai tài khoản/mật khẩu, hoặc
	 *                                   không có quyền đối tác.
	 */
	public TaiKhoan dangNhap(String username, String password) {
		TaiKhoan taiKhoan = dao.findByUsername(username);

		// Gộp chung 1 thông báo cho cả 2 trường hợp "tài khoản không tồn tại"
		// và "sai mật khẩu", tránh lộ thông tin tài khoản nào tồn tại trong
		// hệ thống (user enumeration).
		if (taiKhoan == null || !PasswordUtil.matches(password, taiKhoan.getPassword())) {
			throw new IllegalArgumentException("Sai tài khoản hoặc mật khẩu.");
		}

		if (taiKhoan.getRole() != TaiKhoan.Role.DOI_TAC) {
			throw new IllegalArgumentException("Tài khoản không có quyền truy cập.");
		}

		// Tự động nâng cấp mật khẩu cũ (plaintext) sang dạng hash ngay sau
		// khi xác thực thành công, phục vụ migrate dần dữ liệu cũ.
		if (!PasswordUtil.isHashed(taiKhoan.getPassword())) {
			taiKhoan.setPassword(PasswordUtil.hash(password));
			dao.update(taiKhoan);
		}

		Session.setCurrentUser(taiKhoan);
		return taiKhoan;
	}

	/** Đăng xuất, xóa thông tin phiên đăng nhập hiện tại. */
	public void dangXuat() {
		Session.clear();
	}

	/**
	 * Kiểm tra người dùng hiện tại có đang đăng nhập với vai trò đối tác hay không.
	 * Dùng để bảo vệ các chức năng chỉ dành cho đối tác.
	 *
	 * @throws IllegalStateException nếu chưa đăng nhập hoặc không phải đối tác.
	 */
	public void requireDoiTac() {
		if (!Session.isLoggedIn() || !Session.isDoiTac()) {
			throw new IllegalStateException("Bạn cần đăng nhập với vai trò đối tác.");
		}
	}

	/**
	 * Lấy thông tin tài khoản của đối tác hiện tại để hiển thị.
	 * DTO trả về không chứa userID và password.
	 *
	 * @return Thông tin tài khoản hiện tại (dạng hiển thị).
	 * @throws IllegalStateException nếu chưa đăng nhập hoặc không phải đối tác.
	 */
	public TaiKhoanHienThiDTO layThongTinTaiKhoan() {
		requireDoiTac();
		return new TaiKhoanHienThiDTO(Session.getCurrentUser());
	}

	/**
	 * Sửa thông tin tài khoản của đối tác hiện tại.
	 *
	 * Không cho sửa username, userID, role (đây là các trường định danh/hệ
	 * thống, không phải thông tin cá nhân). Mật khẩu chỉ được đổi khi
	 * {@code input.getMatKhauMoi()} có giá trị (không rỗng) — bỏ trống nghĩa
	 * là giữ nguyên mật khẩu cũ.
	 *
	 * @param input Dữ liệu mới cần cập nhật.
	 * @return Kết quả thao tác (thành công/thất bại kèm thông báo).
	 * @throws IllegalStateException nếu chưa đăng nhập hoặc không phải đối tác.
	 */
	public Result suaThongTinTaiKhoan(TaiKhoanSuaDTO input) {
		requireDoiTac();

		if (!Validator.isHoTen(input.getHoTen())) {
			return Result.fail("Họ tên không hợp lệ.");
		}
		if (!Validator.isPhone(input.getSoDienThoai())) {
			return Result.fail("Số điện thoại không hợp lệ.");
		}
		if (!Validator.isEmail(input.getEmail())) {
			return Result.fail("Email không hợp lệ.");
		}
		if (!Validator.isCCCD(input.getSoCCCD())) {
			return Result.fail("Số CCCD không hợp lệ.");
		}
		if (!Validator.isNotBlank(input.getTenCuaHang())) {
			return Result.fail("Tên cửa hàng không hợp lệ.");
		}
		if (!Validator.isNotBlank(input.getDiaChiCuaHang())) {
			return Result.fail("Địa chỉ cửa hàng không hợp lệ.");
		}

		TaiKhoan current = Session.getCurrentUser();
		current.setHoTen(input.getHoTen());
		current.setSoDienThoai(input.getSoDienThoai());
		current.setEmail(input.getEmail());
		current.setSoCCCD(input.getSoCCCD());
		current.setTenCuaHang(input.getTenCuaHang());
		current.setDiaChiCuaHang(input.getDiaChiCuaHang());

		if (!dao.update(current)) {
			return Result.fail("Không thể cập nhật thông tin tài khoản. Vui lòng thử lại.");
		}

		return Result.ok("Cập nhật thông tin tài khoản thành công.");
	}
}