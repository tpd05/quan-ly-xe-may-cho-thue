package util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

/**
 * Lớp tiện ích kiểm tra tính hợp lệ (validation) cho các trường dữ liệu
 * dùng chung và theo từng nghiệp vụ cụ thể (tài khoản, xe máy).
 */
public final class Validator {

	private Validator() {
	}

	/* ==========================================================
	 * VALIDATOR CHUNG
	 * ==========================================================
	 */

	/** Kiểm tra chuỗi không null và không rỗng. */
	public static boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/** Kiểm tra số nguyên dương (&gt;0). */
	public static boolean isPositive(int value) {
		return value > 0;
	}

	/** Kiểm tra số thực dương (&gt;0). */
	public static boolean isPositive(float value) {
		return value > 0;
	}

	/** Kiểm tra BigDecimal dương (&gt;0). */
	public static boolean isPositive(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0;
	}

	/** Kiểm tra số nguyên không âm (&gt;=0). */
	public static boolean isPositiveOrZero(int value) {
		return value >= 0;
	}

	/** Kiểm tra số thực không âm (&gt;=0). */
	public static boolean isPositiveOrZero(float value) {
		return value >= 0;
	}

	/** Kiểm tra BigDecimal không âm (&gt;=0). */
	public static boolean isPositiveOrZero(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
	}

	/** Kiểm tra giá trị nằm trong khoảng [min, max]. */
	public static boolean inRange(int value, int min, int max) {
		return value >= min && value <= max;
	}

	/** Kiểm tra đối tượng null. */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/** Kiểm tra đối tượng khác null. */
	public static boolean isNotNull(Object object) {
		return object != null;
	}

	/** Kiểm tra khoảng thời gian hợp lệ (start phải trước end). */
	public static boolean isValidDateRange(LocalDateTime start, LocalDateTime end) {
		return start != null
				&& end != null
				&& start.isBefore(end);
	}

	/* ==========================================================
	 * VALIDATOR TÀI KHOẢN
	 * ==========================================================
	 */

	/** Username: 3 - 20 ký tự, chỉ gồm chữ cái, số và dấu gạch dưới. */
	public static boolean isUserName(String value) {
		return isNotBlank(value)
				&& value.matches("^[a-zA-Z0-9_]{3,20}$");
	}

	/** Password: tối thiểu 8 ký tự. */
	public static boolean isPassword(String value) {
		return isNotBlank(value)
				&& value.length() >= 8;
	}

	/** Họ tên: 2 - 100 ký tự. */
	public static boolean isHoTen(String value) {
		return isNotBlank(value)
				&& value.length() >= 2
				&& value.length() <= 100;
	}

	/** Số điện thoại Việt Nam (10 số, bắt đầu bằng 0). */
	public static boolean isPhone(String value) {
		return isNotBlank(value)
				&& value.matches("^0\\d{9}$");
	}

	/** Email hợp lệ theo định dạng chuẩn. */
	public static boolean isEmail(String value) {
		return isNotBlank(value)
				&& value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

	/** CCCD gồm đúng 12 chữ số. */
	public static boolean isCCCD(String value) {
		return isNotBlank(value)
				&& value.matches("^\\d{12}$");
	}

	/**
	 * Địa chỉ (dùng cho diaChiNhanXe, diaChiCuaHang, ...): 5 - 200 ký tự,
	 * KHÔNG được chứa dấu phẩy.
	 *
	 * Lý do cấm dấu phẩy: dữ liệu được lưu dạng CSV với dấu phẩy làm ký tự
	 * phân tách cột (xem {@code TaiKhoanDAO}, {@code DonThueDAO}). Nếu giá
	 * trị địa chỉ tự chứa dấu phẩy, dòng CSV khi ghi xuống sẽ bị lệch cột,
	 * gây lỗi khi đọc lại (xem thêm ghi chú tại {@code BaseDAO}). Người
	 * dùng nên thay dấu phẩy trong địa chỉ bằng dấu gạch ngang hoặc khoảng
	 * trắng, ví dụ: "123 Nguyễn Trãi - Thanh Xuân - Hà Nội".
	 *
	 * @param value Địa chỉ cần kiểm tra.
	 * @return true nếu hợp lệ (không rỗng, độ dài 5-200, không chứa dấu phẩy).
	 */
	public static boolean isDiaChi(String value) {
		return isNotBlank(value)
				&& value.length() >= 5
				&& value.length() <= 200
				&& !value.contains(",");
	}

	/* ==========================================================
	 * VALIDATOR XE MÁY
	 * ==========================================================
	 */

	/** Hãng xe: 2 - 50 ký tự. */
	public static boolean isHangXe(String value) {
		return isNotBlank(value)
				&& value.length() >= 2
				&& value.length() <= 50;
	}

	/** Dòng xe: 2 - 100 ký tự. */
	public static boolean isDongXe(String value) {
		return isNotBlank(value)
				&& value.length() >= 2
				&& value.length() <= 100;
	}

	/** Năm sản xuất: từ 1900 đến năm hiện tại. */
	public static boolean isNamSanXuat(int value) {
		return value >= 1900
				&& value <= Year.now().getValue();
	}

	/** Dung tích xi-lanh (cc): trong khoảng 50 - 2000. */
	public static boolean isDungTich(float value) {
		return value >= 50
				&& value <= 2000;
	}

	/** Biển số xe theo định dạng Việt Nam (ví dụ: 29A-12345 hoặc 29A-123.45). */
	public static boolean isBienSo(String value) {
		return isNotBlank(value)
				&& value.matches("^(?:\\d{2}[A-Z]-\\d{5}|\\d{2}[A-Z]-\\d{3}\\.\\d{2})$");
	}
}