package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Lớp tiện ích băm (hash) mật khẩu bằng SHA-256 và so sánh mật khẩu người
 * dùng nhập với giá trị đã lưu.
 *
 * Hỗ trợ tương thích ngược với dữ liệu cũ chưa được băm (plaintext): nếu
 * giá trị lưu trong hệ thống không có định dạng của 1 hash SHA-256 (64 ký
 * tự hex), {@link #matches(String, String)} sẽ so sánh trực tiếp như
 * plaintext. Việc này cho phép các tài khoản cũ tiếp tục đăng nhập được;
 * tầng gọi (service) nên tự động băm lại và lưu đè mật khẩu ngay sau khi
 * xác thực thành công bằng đường plaintext, để dữ liệu dần được chuyển
 * hoàn toàn sang dạng hash mà không cần thao tác migrate thủ công.
 */
public final class PasswordUtil {

	private static final String HASH_ALGORITHM = "SHA-256";

	// Hash SHA-256 dạng hex luôn có đúng 64 ký tự [0-9a-fA-F]
	private static final Pattern SHA256_HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]{64}$");

	private PasswordUtil() {
	}

	/**
	 * Băm mật khẩu bằng SHA-256, trả về chuỗi hex 64 ký tự (chữ thường).
	 *
	 * @param plainPassword Mật khẩu gốc (plaintext) cần băm.
	 * @return Chuỗi hash dạng hex.
	 * @throws IllegalArgumentException nếu plainPassword là null.
	 */
	public static String hash(String plainPassword) {
		if (plainPassword == null) {
			throw new IllegalArgumentException("Mật khẩu cần băm không được null.");
		}

		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder(hashBytes.length * 2);
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// SHA-256 luôn có sẵn trong mọi JVM chuẩn, nhánh này thực tế không xảy ra
			throw new IllegalStateException("Thuật toán " + HASH_ALGORITHM + " không khả dụng trên máy này.", e);
		}
	}

	/**
	 * Kiểm tra 1 chuỗi có đúng định dạng của hash SHA-256 (64 ký tự hex)
	 * hay không. Dùng để phân biệt dữ liệu mật khẩu cũ (plaintext) với
	 * dữ liệu đã được băm.
	 *
	 * @param value Chuỗi cần kiểm tra.
	 * @return true nếu value có định dạng hash SHA-256 hợp lệ.
	 */
	public static boolean isHashed(String value) {
		return value != null && SHA256_HEX_PATTERN.matcher(value).matches();
	}

	/**
	 * So sánh mật khẩu người dùng nhập với giá trị đã lưu trong hệ thống.
	 *
	 * Nếu giá trị đã lưu có định dạng hash SHA-256, sẽ băm mật khẩu nhập vào
	 * rồi so sánh 2 hash. Nếu giá trị đã lưu chưa phải hash (dữ liệu cũ,
	 * plaintext), sẽ so sánh trực tiếp để đảm bảo tương thích ngược.
	 *
	 * @param plainPassword Mật khẩu người dùng vừa nhập (plaintext).
	 * @param storedValue   Giá trị mật khẩu đang lưu trong hệ thống (có thể
	 *                      là hash hoặc plaintext cũ).
	 * @return true nếu khớp.
	 */
	public static boolean matches(String plainPassword, String storedValue) {
		if (plainPassword == null || storedValue == null) {
			return false;
		}

		if (isHashed(storedValue)) {
			return hash(plainPassword).equals(storedValue);
		}

		// Dữ liệu cũ chưa được băm -> so sánh trực tiếp (tương thích ngược)
		return storedValue.equals(plainPassword);
	}
}