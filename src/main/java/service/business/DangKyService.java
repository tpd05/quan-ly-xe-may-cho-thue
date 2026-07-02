package service.business;

import model.TaiKhoan;
import service.entity.TaiKhoanService;
import util.Validator;

public class DangKyService {
	private final TaiKhoanService service = new TaiKhoanService();

	/**
	 * Đăng ký tài khoản default role khách hàng
	 * @param taiKhoan
	 * @return true nếu đăng ký thành công
	 */
	public boolean dangKy(TaiKhoan taiKhoan) {
		if(Validator.isNull(taiKhoan)) {
			return false;
		}
		
		taiKhoan.setRole(TaiKhoan.Role.KHACH_HANG);
		
		return service.them(taiKhoan);
	}
}
