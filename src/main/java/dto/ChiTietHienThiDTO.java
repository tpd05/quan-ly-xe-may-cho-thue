package dto;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) chứa thông tin chi tiết đơn thuê dùng để
 * hiển thị ra view. Gộp dữ liệu từ chi tiết đơn thuê và thông tin xe
 * liên quan (nếu có) thành 1 object duy nhất, tránh view phải tự join dữ liệu.
 */
public class ChiTietHienThiDTO {

	private LocalDateTime thoiGianBatDau;
	private LocalDateTime thoiGianKetThuc;
	private int donGia;
	private String hangXe;
	private String dongXe;
	private String bienSo;

	/**
	 * Khởi tạo DTO hiển thị chi tiết đơn thuê.
	 *
	 * @param thoiGianBatDau  Thời gian bắt đầu thuê.
	 * @param thoiGianKetThuc Thời gian kết thúc thuê.
	 * @param donGia          Đơn giá thuê.
	 * @param hangXe          Hãng xe (có thể null nếu không có thông tin xe).
	 * @param dongXe          Dòng xe (có thể null nếu không có thông tin xe).
	 * @param bienSo          Biển số xe (có thể null nếu không có thông tin xe).
	 */
	public ChiTietHienThiDTO(LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc, int donGia,
			String hangXe, String dongXe, String bienSo) {
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.donGia = donGia;
		this.hangXe = hangXe;
		this.dongXe = dongXe;
		this.bienSo = bienSo;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public int getDonGia() {
		return donGia;
	}

	public String getHangXe() {
		return hangXe;
	}

	public String getDongXe() {
		return dongXe;
	}

	public String getBienSo() {
		return bienSo;
	}

	/**
	 * Kiểm tra xem chi tiết này có đầy đủ thông tin xe hay không.
	 * Dùng để view quyết định có hiển thị thông tin xe hay không
	 * (ví dụ trường hợp xe đã bị xóa khỏi hệ thống).
	 *
	 * @return true nếu có đủ cả 3 trường hangXe, dongXe, bienSo.
	 */
	public boolean coThongTinXe() {
		return hangXe != null && dongXe != null && bienSo != null;
	}
}