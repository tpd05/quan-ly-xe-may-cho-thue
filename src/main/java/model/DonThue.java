package model;

import java.time.LocalDateTime;

public class DonThue {
	private int maDonThue;
	private int userID;
	private String diaChiNhanXe;
	private TrangThai trangThai;
	private LocalDateTime ngayDat;

	public enum TrangThai {
		CHO_XAC_NHAN, CHO_NHAN_XE, DANG_THUE, HOAN_THANH, DA_HUY
	}

	public DonThue(int maDonThue, int userID, String diaChiNhanXe, TrangThai trangThai, LocalDateTime ngayDat) {
		super();
		this.maDonThue = maDonThue;
		this.userID = userID;
		this.diaChiNhanXe = diaChiNhanXe;
		this.trangThai = trangThai;
		this.ngayDat = ngayDat;
	}

	public DonThue() {
		super();
	}

	public DonThue(int userID, String diaChiNhanXe, TrangThai trangThai, LocalDateTime ngayDat) {
		super();
		this.userID = userID;
		this.diaChiNhanXe = diaChiNhanXe;
		this.trangThai = trangThai;
		this.ngayDat = ngayDat;
	}

	public int getMaDonThue() {
		return maDonThue;
	}

	public void setMaDonThue(int maDonThue) {
		this.maDonThue = maDonThue;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getDiaChiNhanXe() {
		return diaChiNhanXe;
	}

	public void setDiaChiNhanXe(String diaChiNhanXe) {
		this.diaChiNhanXe = diaChiNhanXe;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		this.trangThai = trangThai;
	}

	public LocalDateTime getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDateTime ngayDat) {
		this.ngayDat = ngayDat;
	}

}