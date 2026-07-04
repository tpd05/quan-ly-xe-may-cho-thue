package service;

import java.util.ArrayList;
import java.util.List;

import dao.XeMayDAO;
import model.XeMay;
import session.Session;
import util.FilePath;
import util.IdGenerator;
import util.Result;
import util.Validator;

/**
 * Service xử lý nghiệp vụ quản lý xe máy: tìm kiếm, thêm, sửa, xóa.
 * Mọi thao tác đều chỉ áp dụng trên các xe thuộc về người dùng hiện tại
 * (theo Session), đảm bảo mỗi đối tác chỉ thấy và quản lý xe của mình.
 */
public class XeMayService {

	private final XeMayDAO dao = new XeMayDAO();

	/**
	 * Tìm xe theo ID, chỉ trả về nếu xe thuộc về người dùng hiện tại.
	 *
	 * @param maXe Mã xe cần tìm.
	 * @return Xe tương ứng, hoặc null nếu không hợp lệ / không tồn tại /
	 *         không thuộc về người dùng hiện tại.
	 */
	public XeMay timTheoId(int maXe) {
		if (!Validator.isPositive(maXe)) {
			return null;
		}

		XeMay xe = dao.findById(maXe);
		if (xe == null || xe.getUserID() != Session.getCurrentUserID()) {
			return null;
		}

		return xe;
	}

	/**
	 * Lấy toàn bộ danh sách xe thuộc về người dùng hiện tại.
	 *
	 * @return Danh sách xe của người dùng hiện tại.
	 */
	public List<XeMay> timTatCa() {
		List<XeMay> res = new ArrayList<>();
		int currentUserID = Session.getCurrentUserID();

		for (XeMay xe : dao.findAll()) {
			if (xe.getUserID() == currentUserID) {
				res.add(xe);
			}
		}

		return res;
	}

	/**
	 * Tìm kiếm xe theo từ khóa (biển số, hãng xe, hoặc dòng xe).
	 * Nếu từ khóa rỗng thì trả về toàn bộ danh sách xe.
	 *
	 * @param tuKhoa Từ khóa tìm kiếm.
	 * @return Danh sách xe phù hợp.
	 */
	public List<XeMay> timKiem(String tuKhoa) {
		if (!Validator.isNotBlank(tuKhoa)) {
			return timTatCa();
		}

		String keyword = tuKhoa.toLowerCase();
		List<XeMay> res = new ArrayList<>();

		for (XeMay xe : timTatCa()) {
			if (xe.getBienSo().toLowerCase().contains(keyword)
					|| xe.getHangXe().toLowerCase().contains(keyword)
					|| xe.getDongXe().toLowerCase().contains(keyword)) {
				res.add(xe);
			}
		}

		return res;
	}

	/**
	 * Tìm xe theo biển số (không phân biệt hoa thường).
	 *
	 * @param bienSo Biển số cần tìm.
	 * @return Xe tương ứng, hoặc null nếu không hợp lệ / không tìm thấy.
	 */
	public XeMay timTheoBienSo(String bienSo) {
		if (!Validator.isBienSo(bienSo)) {
			return null;
		}

		for (XeMay xe : timTatCa()) {
			if (xe.getBienSo().equalsIgnoreCase(bienSo)) {
				return xe;
			}
		}

		return null;
	}

	/**
	 * Kiểm tra biển số đã tồn tại trong danh sách xe của người dùng hiện tại
	 * hay chưa.
	 *
	 * @param bienSo Biển số cần kiểm tra.
	 * @return true nếu biển số đã tồn tại.
	 */
	public boolean tonTaiBienSo(String bienSo) {
		return timTheoBienSo(bienSo) != null;
	}

	/**
	 * Thêm xe mới. Tự động gán userID theo người dùng hiện tại và sinh
	 * mã xe mới.
	 *
	 * @param xe Thông tin xe cần thêm.
	 * @return Kết quả thao tác (thành công/thất bại kèm thông báo).
	 */
	public Result them(XeMay xe) {
		if (Validator.isNull(xe)) {
			return Result.fail("Dữ liệu xe không hợp lệ.");
		}

		Result validation = validateXeResult(xe);
		if (!validation.isSuccess()) {
			return validation;
		}

		if (tonTaiBienSo(xe.getBienSo())) {
			return Result.fail("Biển số đã tồn tại.");
		}

		// Gán chủ sở hữu là người dùng hiện tại và sinh mã xe mới
		xe.setUserID(Session.getCurrentUserID());
		xe.setMaXe(IdGenerator.nextID(FilePath.XE_MAY));

		if (!dao.insert(xe)) {
			return Result.fail("Không thể thêm xe. Vui lòng thử lại.");
		}

		return Result.ok("Thêm xe thành công.");
	}

	/**
	 * Sửa thông tin xe. Giữ nguyên userID gốc để tránh đổi quyền sở hữu xe.
	 *
	 * @param xe Thông tin xe mới (phải có maXe hợp lệ đã tồn tại).
	 * @return Kết quả thao tác (thành công/thất bại kèm thông báo).
	 */
	public Result sua(XeMay xe) {
		if (Validator.isNull(xe)) {
			return Result.fail("Dữ liệu xe không hợp lệ.");
		}

		XeMay old = timTheoId(xe.getMaXe());
		if (Validator.isNull(old)) {
			return Result.fail("Không tìm thấy xe.");
		}

		Result validation = validateXeResult(xe);
		if (!validation.isSuccess()) {
			return validation;
		}

		// Chỉ kiểm tra trùng biển số nếu biển số bị thay đổi
		if (!old.getBienSo().equalsIgnoreCase(xe.getBienSo()) && tonTaiBienSo(xe.getBienSo())) {
			return Result.fail("Biển số đã tồn tại.");
		}

		xe.setUserID(old.getUserID());
		if (!dao.update(xe)) {
			return Result.fail("Không thể cập nhật xe. Vui lòng thử lại.");
		}

		return Result.ok("Cập nhật xe thành công.");
	}

	/**
	 * Xóa xe theo mã xe.
	 *
	 * @param maXe Mã xe cần xóa.
	 * @return Kết quả thao tác (thành công/thất bại kèm thông báo).
	 */
	public Result xoa(int maXe) {
		if (Validator.isNull(timTheoId(maXe))) {
			return Result.fail("Không tìm thấy xe.");
		}

		if (!dao.delete(maXe)) {
			return Result.fail("Không thể xóa xe. Vui lòng thử lại.");
		}

		return Result.ok("Xóa xe thành công.");
	}

	/**
	 * Kiểm tra tính hợp lệ của dữ liệu xe (hãng, dòng, năm sản xuất,
	 * dung tích, biển số, giá thuê).
	 *
	 * @param xe Xe cần kiểm tra.
	 * @return Result.ok nếu hợp lệ, ngược lại Result.fail kèm thông báo lỗi
	 *         tương ứng với trường đầu tiên không hợp lệ.
	 */
	private Result validateXeResult(XeMay xe) {
		if (!Validator.isHangXe(xe.getHangXe())) {
			return Result.fail("Hãng xe không hợp lệ.");
		}
		if (!Validator.isDongXe(xe.getDongXe())) {
			return Result.fail("Dòng xe không hợp lệ.");
		}
		if (!Validator.isNamSanXuat(xe.getNamSanXuat())) {
			return Result.fail("Năm sản xuất không hợp lệ.");
		}
		if (!Validator.isDungTich(xe.getDungTich())) {
			return Result.fail("Dung tích không hợp lệ.");
		}
		if (!Validator.isBienSo(xe.getBienSo())) {
			return Result.fail("Biển số không hợp lệ.");
		}
		if (!Validator.isPositive(xe.getGiaNgay())) {
			return Result.fail("Giá thuê/ngày phải lớn hơn 0.");
		}
		if (!Validator.isPositive(xe.getGiaTuan())) {
			return Result.fail("Giá thuê/tuần phải lớn hơn 0.");
		}
		return Result.ok("");
	}
}