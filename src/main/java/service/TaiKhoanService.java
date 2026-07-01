package service;

import java.util.List;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import util.FilePath;
import util.IdGenerator;
import util.Validator;

public class TaiKhoanService {

	private final TaiKhoanDAO dao = new TaiKhoanDAO();
	
    /**
     * Tìm tài khoản theo mã.
     */
	public TaiKhoan timTheoId(int id) {
		if(!Validator.isPositive(id)) {
			return null;
		}
		return dao.findById(id);
	}
	
    /**
     * Lấy toàn bộ tài khoản.
     */
	public List<TaiKhoan> timTatCa(){
		return dao.findAll();
	}
	
    /**
     * Tìm tài khoản theo username.
     */
	public TaiKhoan timTheoUsername(String username) {
		if(!Validator.isUserName(username)) {
			return null;
		}
		
		for(TaiKhoan tk : timTatCa()) {
			if(tk.getUsername().equals(username)) {
				return tk;
			}
		}
		return null;
	}
	
    /**
     * Kiểm tra username đã tồn tại chưa.
     */
	public boolean tonTaiUsername(String username) {
	    return timTheoUsername(username) != null;
	}
	
    /**
     * Kiểm tra soCCCD đã tồn tại chưa.
     */
	public boolean tonTaiSoCCCD(String soCCCD) {
		if(!Validator.isCCCD(soCCCD)) {
			return false;
		}
		
		for(TaiKhoan tk : timTatCa()) {
			if(tk.getSoCCCD().equals(soCCCD)) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * Kiểm tra email đã tồn tại chưa.
     */
	public boolean tonTaiEmail(String email) {
		if(!Validator.isEmail(email)) {
			return false;
		}
		
		for(TaiKhoan tk : timTatCa()) {
			if(tk.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * Thêm tài khoản.
     */
	public boolean them(TaiKhoan tk) {
		
		if(Validator.isNull(tk)) {
			return false;
		}
		
		if (!Validator.isEmail(tk.getEmail())) {
		    return false;
		}

		if (!Validator.isCCCD(tk.getSoCCCD())) {
		    return false;
		}
		
		if (!Validator.isPassword(tk.getPassword())) {
		    return false;
		}
		
		if (!Validator.isHoTen(tk.getHoTen())) {
			return false;
		}
		
		if (!Validator.isPhone(tk.getSoDienThoai())) {
			return false;
		}
		
		if(tonTaiUsername(tk.getUsername())) {
			return false;
		}
		
		if(tonTaiEmail(tk.getEmail())) {
			return false;
		}
		
		if(tonTaiSoCCCD(tk.getSoCCCD())) {
			return false;
		}
		
		tk.setUserID(IdGenerator.nextID(FilePath.TAI_KHOAN));
		
		return dao.insert(tk);
	}
	
    /**
     * Cập nhật thông tin tài khoản.
     */
	public boolean sua(TaiKhoan tk) {
		if (Validator.isNull(tk)) {
		    return false;
		}
		
		TaiKhoan old = timTheoId(tk.getUserID());

		if (Validator.isNull(old)) {
		    return false;
		}

		if (!Validator.isPassword(tk.getPassword())) {
		    return false;
		}

		if (!Validator.isHoTen(tk.getHoTen())) {
		    return false;
		}

		if (!Validator.isPhone(tk.getSoDienThoai())) {
		    return false;
		}

		if (!Validator.isEmail(tk.getEmail())) {
		    return false;
		}

		if (!Validator.isCCCD(tk.getSoCCCD())) {
		    return false;
		}
		
		if(!old.getEmail().equals(tk.getEmail())) {

		    if (tonTaiEmail(tk.getEmail())) {
		        return false;
		    }
		}
		
		if(!old.getSoCCCD().equals(tk.getSoCCCD())) {
			if(tonTaiSoCCCD(tk.getSoCCCD())) {
				return false;
			}
		}
		
		if (!old.getUsername().equals(tk.getUsername())) {
		    return false;
		}
		return dao.update(tk);
	}
	
    /**
     * Xóa tài khoản.
     */
	public boolean xoa(int id) {
		if(Validator.isNull(timTheoId(id))) {
			return false;
		}
		return dao.delete(id);
	}
}
