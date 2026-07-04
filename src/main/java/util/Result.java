package util;

/**
 * Đối tượng kết quả chung cho các thao tác nghiệp vụ (thành công/thất bại
 * kèm thông báo). Dùng để thay thế việc trả về boolean đơn thuần, giúp
 * truyền tải thông báo lỗi cụ thể cho view.
 */
public final class Result {

	private final boolean success;
	private final String message;

	private Result(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	/**
	 * Tạo kết quả thành công.
	 *
	 * @param message Thông báo thành công.
	 * @return Result với success = true.
	 */
	public static Result ok(String message) {
		return new Result(true, message);
	}

	/**
	 * Tạo kết quả thất bại.
	 *
	 * @param message Thông báo lỗi.
	 * @return Result với success = false.
	 */
	public static Result fail(String message) {
		return new Result(false, message);
	}

	/** Kiểm tra thao tác có thành công hay không. */
	public boolean isSuccess() {
		return success;
	}

	/** Lấy thông báo kèm theo kết quả. */
	public String getMessage() {
		return message;
	}
}