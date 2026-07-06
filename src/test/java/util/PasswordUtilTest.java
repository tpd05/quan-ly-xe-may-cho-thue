package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit test cho {@link PasswordUtil}.
 */
class PasswordUtilTest {

	@Nested
	@DisplayName("hash()")
	class HashTest {

		@Test
		@DisplayName("Trả về chuỗi hex 64 ký tự")
		void tra_ve_64_ky_tu_hex() {
			String hash = PasswordUtil.hash("password123");

			assertEquals(64, hash.length());
			assertTrue(hash.matches("^[0-9a-f]{64}$"));
		}

		@Test
		@DisplayName("Cùng đầu vào -> luôn ra cùng 1 kết quả (deterministic)")
		void deterministic() {
			String hash1 = PasswordUtil.hash("password123");
			String hash2 = PasswordUtil.hash("password123");

			assertEquals(hash1, hash2);
		}

		@Test
		@DisplayName("Đầu vào khác nhau -> ra kết quả khác nhau")
		void dau_vao_khac_nhau_ra_ket_qua_khac_nhau() {
			String hash1 = PasswordUtil.hash("password123");
			String hash2 = PasswordUtil.hash("password124");

			assertNotEquals(hash1, hash2);
		}

		@Test
		@DisplayName("Ném IllegalArgumentException khi input null")
		void nem_exception_khi_null() {
			assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hash(null));
		}

		@Test
		@DisplayName("Chuỗi rỗng vẫn băm được (không ném lỗi)")
		void chuoi_rong_van_bam_duoc() {
			String hash = PasswordUtil.hash("");
			assertEquals(64, hash.length());
		}
	}

	@Nested
	@DisplayName("isHashed()")
	class IsHashedTest {

		@Test
		@DisplayName("true với chuỗi hash SHA-256 hợp lệ (64 hex, chữ thường)")
		void true_voi_hash_hop_le() {
			String hash = PasswordUtil.hash("password123");
			assertTrue(PasswordUtil.isHashed(hash));
		}

		@Test
		@DisplayName("true với chuỗi hex 64 ký tự viết hoa (pattern không phân biệt hoa/thường)")
		void true_voi_hex_viet_hoa() {
			String hashHoa = PasswordUtil.hash("password123").toUpperCase();
			assertTrue(PasswordUtil.isHashed(hashHoa));
		}

		@Test
		@DisplayName("false với mật khẩu plaintext thông thường")
		void false_voi_plaintext() {
			assertFalse(PasswordUtil.isHashed("password123"));
		}

		@Test
		@DisplayName("false với null")
		void false_voi_null() {
			assertFalse(PasswordUtil.isHashed(null));
		}

		@Test
		@DisplayName("false với chuỗi hex nhưng sai độ dài (không phải 64 ký tự)")
		void false_voi_do_dai_sai() {
			assertFalse(PasswordUtil.isHashed("abc123"));
			assertFalse(PasswordUtil.isHashed("a".repeat(63)));
			assertFalse(PasswordUtil.isHashed("a".repeat(65)));
		}

		@Test
		@DisplayName("false với chuỗi 64 ký tự nhưng chứa ký tự không phải hex")
		void false_voi_ky_tu_khong_phai_hex() {
			// 63 ký tự hex hợp lệ + 1 ký tự 'g' không phải hex = 64 ký tự nhưng không khớp
			String gan_giong_hash = "g".repeat(64);
			assertFalse(PasswordUtil.isHashed(gan_giong_hash));
		}
	}

	@Nested
	@DisplayName("matches()")
	class MatchesTest {

		@Test
		@DisplayName("true khi storedValue là hash đúng của plainPassword")
		void true_khi_stored_la_hash_dung() {
			String stored = PasswordUtil.hash("password123");
			assertTrue(PasswordUtil.matches("password123", stored));
		}

		@Test
		@DisplayName("false khi storedValue là hash nhưng plainPassword sai")
		void false_khi_stored_la_hash_nhung_sai_password() {
			String stored = PasswordUtil.hash("password123");
			assertFalse(PasswordUtil.matches("saipassword", stored));
		}

		@Test
		@DisplayName("Tương thích ngược: true khi storedValue là plaintext khớp trực tiếp")
		void true_khi_stored_la_plaintext_khop() {
			assertTrue(PasswordUtil.matches("password123", "password123"));
		}

		@Test
		@DisplayName("Tương thích ngược: false khi storedValue là plaintext không khớp")
		void false_khi_stored_la_plaintext_khong_khop() {
			assertFalse(PasswordUtil.matches("password123", "khacmatkhau"));
		}

		@Test
		@DisplayName("false khi plainPassword là null")
		void false_khi_plainPassword_null() {
			assertFalse(PasswordUtil.matches(null, PasswordUtil.hash("password123")));
		}

		@Test
		@DisplayName("false khi storedValue là null")
		void false_khi_storedValue_null() {
			assertFalse(PasswordUtil.matches("password123", null));
		}

		@Test
		@DisplayName("false khi cả 2 tham số đều null")
		void false_khi_ca_hai_null() {
			assertFalse(PasswordUtil.matches(null, null));
		}
	}
}