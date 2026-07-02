package service.business;

import session.Session;

public class DangXuatService {

    public boolean dangXuat() {

        if (!Session.isLoggedIn()) {
            return false;
        }

        Session.clear();
        return true;
    }
}