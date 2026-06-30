package util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.TaiKhoan;

public final class Validator {

	private Validator() {
	}

	// VARLIDATOR CHUNG

	// Kiểm tra chuỗi không null và không rỗng.
	public static boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

	// Kiểm tra số nguyên dương (>0).
	public static boolean isPositive(int value) {
		return value > 0;
	}

	// Kiểm tra số thực dương (>0).
	public static boolean isPositive(float value) {
		return value > 0;
	}

	// Kiểm tra BigDecimal dương (>0).
	public static boolean isPositive(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0;
	}

	// Kiểm tra số nguyên không âm (>=0).
	public static boolean isPositiveOrZero(int value) {
		return value >= 0;
	}

	// Kiểm tra số thực không âm (>=0).
	public static boolean isPositiveOrZero(float value) {
		return value >= 0;
	}

	// Kiểm tra BigDecimal không âm (>=0).
	public static boolean isPositiveOrZero(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
	}

	// Kiểm tra giá trị nằm trong khoảng [min, max].
	public static boolean inRange(int value, int min, int max) {
		return value >= min && value <= max;
	}

	// Kiểm tra đối tượng null.
	public static boolean isNull(Object object) {
		return object == null;
	}

	// Kiểm tra đối tượng khác null.
	public static boolean isNotNull(Object object) {
		return object != null;
	}

	// Kiểm tra khoảng thời gian hợp lệ. start phải trước end.
	public static boolean isValidDateRange(LocalDateTime start, LocalDateTime end) {
		return start != null && end != null && start.isBefore(end);
	}

	// VALIDATOR THEO NGHIỆP VỤ

	// TÀI KHOẢN

	// Username: 3 - 20 ký tự, chỉ gồm chữ cái, số và dấu gạch dưới.
	public static boolean isUserName(String value) {
		return isNotBlank(value) && value.matches("^[a-zA-Z0-9_]{3,20}$");
	}

	// Password: tối thiểu 8 ký tự.
	public static boolean isPassword(String value) {
		return isNotBlank(value) && value.length() >= 8;
	}

	// Họ tên: 2 - 100 ký tự.
	public static boolean isHoTen(String value) {
		return isNotBlank(value) && value.length() >= 2 && value.length() <= 100;
	}

	// Số điện thoại Việt Nam.
	public static boolean isPhone(String value) {
		return isNotBlank(value) && value.matches("^0\\d{9}$");
	}

	// Email.
	public static boolean isEmail(String value) {
		return isNotBlank(value) && value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

	// CCCD gồm đúng 12 chữ số.
	public static boolean isCCCD(String value) {
		return isNotBlank(value) && value.matches("^\\d{12}$");
	}

	// Kiểm tra Role có tồn tại.
	public static boolean isRole(TaiKhoan.Role role) {
		return role != null;
	}

}