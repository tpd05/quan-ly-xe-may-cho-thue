package service.business;

import model.TaiKhoan;
import service.entity.TaiKhoanService;
import session.Session;
import util.Validator;

public class DangNhapService {

    private final TaiKhoanService service = new TaiKhoanService();

    /**
     * Đăng nhập.
     * @param username,password
     * @return Tài khoản nếu thành công, ngược lại trả về null.
     */
    public TaiKhoan dangNhap(String username, String password) {

        if (!Validator.isUserName(username)) {
            return null;
        }

        if (!Validator.isPassword(password)) {
            return null;
        }

        TaiKhoan tk = service.timTheoUsername(username);

        if (Validator.isNull(tk)) {
            return null;
        }

        if (!tk.getPassword().equals(password)) {
            return null;
        }
        
        Session.setCurrentUser(tk);
        
        return tk;
    }

}