package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit test cho {@link Validator}.
 *
 * Validator là lớp thuần logic (pure function), không phụ thuộc I/O hay
 * static state bên ngoài -> dễ test cách ly, không cần mock.
 */
class ValidatorTest {

	@Nested
	@DisplayName("isNotBlank")
	class IsNotBlankTest {

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = { "   ", "\t", "\n" })
		@DisplayName("Trả về false với null, rỗng, hoặc chỉ chứa khoảng trắng")
		void tra_ve_false_khi_gia_tri_khong_hop_le(String value) {
			assertFalse(Validator.isNotBlank(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "a", "abc", " abc ", "123" })
		@DisplayName("Trả về true với chuỗi có nội dung")
		void tra_ve_true_khi_co_noi_dung(String value) {
			assertTrue(Validator.isNotBlank(value));
		}
	}

	@Nested
	@DisplayName("isPositive")
	class IsPositiveTest {

		@Test
		@DisplayName("int dương -> true")
		void int_duong() {
			assertTrue(Validator.isPositive(1));
			assertTrue(Validator.isPositive(100));
		}

		@Test
		@DisplayName("int âm hoặc bằng 0 -> false")
		void int_am_hoac_bang_khong() {
			assertFalse(Validator.isPositive(0));
			assertFalse(Validator.isPositive(-5));
		}

		@Test
		@DisplayName("float dương -> true, float âm hoặc 0 -> false")
		void float_duong_va_am() {
			assertTrue(Validator.isPositive(0.1f));
			assertFalse(Validator.isPositive(0f));
			assertFalse(Validator.isPositive(-0.1f));
		}

		@Test
		@DisplayName("BigDecimal dương -> true")
		void bigdecimal_duong() {
			assertTrue(Validator.isPositive(BigDecimal.valueOf(1)));
		}

		@Test
		@DisplayName("BigDecimal null, âm, hoặc 0 -> false")
		void bigdecimal_khong_hop_le() {
			assertFalse(Validator.isPositive((BigDecimal) null));
			assertFalse(Validator.isPositive(BigDecimal.ZERO));
			assertFalse(Validator.isPositive(BigDecimal.valueOf(-1)));
		}
	}

	@Nested
	@DisplayName("isPositiveOrZero")
	class IsPositiveOrZeroTest {

		@Test
		@DisplayName("int/float/BigDecimal = 0 vẫn hợp lệ (khác isPositive)")
		void bang_khong_van_hop_le() {
			assertTrue(Validator.isPositiveOrZero(0));
			assertTrue(Validator.isPositiveOrZero(0f));
			assertTrue(Validator.isPositiveOrZero(BigDecimal.ZERO));
		}

		@Test
		@DisplayName("Giá trị âm -> false")
		void gia_tri_am_khong_hop_le() {
			assertFalse(Validator.isPositiveOrZero(-1));
			assertFalse(Validator.isPositiveOrZero(-0.1f));
			assertFalse(Validator.isPositiveOrZero(BigDecimal.valueOf(-1)));
		}

		@Test
		@DisplayName("BigDecimal null -> false")
		void bigdecimal_null_khong_hop_le() {
			assertFalse(Validator.isPositiveOrZero((BigDecimal) null));
		}
	}

	@Nested
	@DisplayName("inRange")
	class InRangeTest {

		@ParameterizedTest
		@CsvSource({ "5, 1, 10", "1, 1, 10", "10, 1, 10" })
		@DisplayName("Giá trị nằm trong [min, max] (bao gồm biên) -> true")
		void trong_khoang_bao_gom_bien(int value, int min, int max) {
			assertTrue(Validator.inRange(value, min, max));
		}

		@ParameterizedTest
		@CsvSource({ "0, 1, 10", "11, 1, 10" })
		@DisplayName("Giá trị ngoài khoảng -> false")
		void ngoai_khoang(int value, int min, int max) {
			assertFalse(Validator.inRange(value, min, max));
		}
	}

	@Nested
	@DisplayName("isNull / isNotNull")
	class NullCheckTest {

		@Test
		void isNull_dung_voi_null() {
			assertTrue(Validator.isNull(null));
			assertFalse(Validator.isNull(new Object()));
		}

		@Test
		void isNotNull_dung_voi_object_hop_le() {
			assertTrue(Validator.isNotNull(new Object()));
			assertFalse(Validator.isNotNull(null));
		}
	}

	@Nested
	@DisplayName("isValidDateRange")
	class DateRangeTest {

		@Test
		@DisplayName("start trước end -> true")
		void start_truoc_end() {
			LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
			LocalDateTime end = LocalDateTime.of(2026, 1, 2, 0, 0);
			assertTrue(Validator.isValidDateRange(start, end));
		}

		@Test
		@DisplayName("start sau hoặc bằng end -> false")
		void start_sau_hoac_bang_end() {
			LocalDateTime a = LocalDateTime.of(2026, 1, 2, 0, 0);
			LocalDateTime b = LocalDateTime.of(2026, 1, 1, 0, 0);

			// start sau end
			assertFalse(Validator.isValidDateRange(a, b));
			// start bằng end
			assertFalse(Validator.isValidDateRange(a, a));
		}

		@Test
		@DisplayName("start hoặc end null -> false")
		void start_hoac_end_null() {
			LocalDateTime now = LocalDateTime.now();
			assertFalse(Validator.isValidDateRange(null, now));
			assertFalse(Validator.isValidDateRange(now, null));
			assertFalse(Validator.isValidDateRange(null, null));
		}
	}

	@Nested
	@DisplayName("Validator tài khoản")
	class TaiKhoanValidatorTest {

		@ParameterizedTest
		@ValueSource(strings = { "abc", "user_123", "a1_", "abcdefghij1234567890" }) // 20 ký tự
		@DisplayName("Username hợp lệ: 3-20 ký tự, chữ/số/gạch dưới")
		void username_hop_le(String value) {
			assertTrue(Validator.isUserName(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "ab", "a", "user name", "user@123", "abcdefghij12345678901" }) // 21 ký tự
		@DisplayName("Username không hợp lệ: quá ngắn/dài hoặc chứa ký tự đặc biệt")
		void username_khong_hop_le(String value) {
			assertFalse(Validator.isUserName(value));
		}

		@Test
		@DisplayName("Password tối thiểu 8 ký tự")
		void password_hop_le() {
			assertTrue(Validator.isPassword("12345678"));
			assertFalse(Validator.isPassword("1234567"));
		}

		@ParameterizedTest
		@CsvSource({ "0912345678, true", "091234567, false", "1912345678, false", "09123456789, false" })
		@DisplayName("Số điện thoại VN: đúng 10 số, bắt đầu bằng 0")
		void so_dien_thoai(String value, boolean expected) {
			assertEquals(expected, Validator.isPhone(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "abc@gmail.com", "a.b_c-d@sub.domain.vn" })
		@DisplayName("Email hợp lệ")
		void email_hop_le(String value) {
			assertTrue(Validator.isEmail(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "abc", "abc@", "abc@domain", "@domain.com" })
		@DisplayName("Email không hợp lệ")
		void email_khong_hop_le(String value) {
			assertFalse(Validator.isEmail(value));
		}

		@Test
		@DisplayName("CCCD phải đúng 12 chữ số")
		void cccd() {
			assertTrue(Validator.isCCCD("123456789012"));
			assertFalse(Validator.isCCCD("12345678901")); // 11 số
			assertFalse(Validator.isCCCD("1234567890123")); // 13 số
			assertFalse(Validator.isCCCD("12345678901a")); // có chữ
		}
	}

	@Nested
	@DisplayName("Validator xe máy")
	class XeMayValidatorTest {

		@Test
		@DisplayName("Hãng xe: 2-50 ký tự")
		void hang_xe() {
			assertTrue(Validator.isHangXe("Honda"));
			assertFalse(Validator.isHangXe("H")); // 1 ký tự
			assertFalse(Validator.isHangXe(""));
		}

		@Test
		@DisplayName("Dòng xe: 2-100 ký tự")
		void dong_xe() {
			assertTrue(Validator.isDongXe("Wave Alpha"));
			assertFalse(Validator.isDongXe("A"));
		}

		@Test
		@DisplayName("Năm sản xuất: từ 1900 đến năm hiện tại")
		void nam_san_xuat() {
			int currentYear = Year.now().getValue();

			assertTrue(Validator.isNamSanXuat(1900));
			assertTrue(Validator.isNamSanXuat(currentYear));
			assertFalse(Validator.isNamSanXuat(1899));
			assertFalse(Validator.isNamSanXuat(currentYear + 1)); // năm tương lai
		}

		@Test
		@DisplayName("Dung tích: 50-2000 cc")
		void dung_tich() {
			assertTrue(Validator.isDungTich(50f));
			assertTrue(Validator.isDungTich(2000f));
			assertFalse(Validator.isDungTich(49.9f));
			assertFalse(Validator.isDungTich(2000.1f));
		}

		@ParameterizedTest
		@ValueSource(strings = { "30A-12345", "30A-123.45", "99Z-99999" })
		@DisplayName("Biển số hợp lệ theo 2 định dạng: 5 số liền hoặc 3.2 số")
		void bien_so_hop_le(String value) {
			assertTrue(Validator.isBienSo(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "30A12345", "30-12345", "3A-12345", "30A-1234", "30a-12345" })
		@DisplayName("Biển số không hợp lệ: sai định dạng hoặc sai chữ hoa")
		void bien_so_khong_hop_le(String value) {
			assertFalse(Validator.isBienSo(value));
		}
	}
}
