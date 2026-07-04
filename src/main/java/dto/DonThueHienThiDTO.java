package dto;

import java.util.List;

import model.DonThue;

/**
 * DTO chứa thông tin đơn thuê dùng để hiển thị ra view.
 * Gộp 1 đơn thuê ({@link DonThue}) cùng danh sách các chi tiết đơn thuê
 * ({@link ChiTietHienThiDTO}) liên quan, giúp view hiển thị đầy đủ
 * thông tin đơn thuê mà không cần truy vấn thêm.
 */
public class DonThueHienThiDTO {

	private DonThue don;
	private List<ChiTietHienThiDTO> chiTiet;

	/**
	 * Khởi tạo DTO hiển thị đơn thuê.
	 *
	 * @param don     Thông tin đơn thuê.
	 * @param chiTiet Danh sách chi tiết đơn thuê tương ứng.
	 */
	public DonThueHienThiDTO(DonThue don, List<ChiTietHienThiDTO> chiTiet) {
		this.don = don;
		this.chiTiet = chiTiet;
	}

	public DonThue getDon() {
		return don;
	}

	public List<ChiTietHienThiDTO> getChiTiet() {
		return chiTiet;
	}
}