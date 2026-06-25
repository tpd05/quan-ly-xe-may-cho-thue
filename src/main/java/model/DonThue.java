package model;

import java.time.LocalDateTime;

public class DonThue {
	private int maDonThue;
	private int userID;
	private String diaChiNhanXe;
	private String trangThai;
	private LocalDateTime ngayDat;

	public DonThue(int maDonThue, int userID, String diaChiNhanXe, String trangThai, LocalDateTime ngayDat) {
		super();
		this.maDonThue = maDonThue;
		this.userID = userID;
		this.diaChiNhanXe = diaChiNhanXe;
		this.trangThai = trangThai;
		this.ngayDat = ngayDat;
	}

	public DonThue(int userID, String diaChiNhanXe, String trangThai, LocalDateTime ngayDat) {
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

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public LocalDateTime getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDateTime ngayDat) {
		this.ngayDat = ngayDat;
	}

}