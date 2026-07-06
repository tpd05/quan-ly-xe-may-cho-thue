package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test cho {@link Result}.
 */
class ResultTest {

	@Test
	@DisplayName("Result.ok() -> isSuccess() = true, giữ đúng message")
	void ket_qua_thanh_cong() {
		Result result = Result.ok("Thêm xe thành công.");

		assertTrue(result.isSuccess());
		assertEquals("Thêm xe thành công.", result.getMessage());
	}

	@Test
	@DisplayName("Result.fail() -> isSuccess() = false, giữ đúng message lỗi")
	void ket_qua_that_bai() {
		Result result = Result.fail("Biển số đã tồn tại.");

		assertFalse(result.isSuccess());
		assertEquals("Biển số đã tồn tại.", result.getMessage());
	}

	@Test
	@DisplayName("ok() và fail() độc lập, không ảnh hưởng lẫn nhau")
	void hai_ket_qua_doc_lap() {
		Result ok = Result.ok("OK");
		Result fail = Result.fail("FAIL");

		assertTrue(ok.isSuccess());
		assertFalse(fail.isSuccess());
	}
}
