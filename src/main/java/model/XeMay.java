package model;

import java.math.BigDecimal;

public class XeMay {
	private int maXe;
	private int userID;
	private String hangXe;
	private String dongXe;
	private int namSanXuat;
	private float dungTich;
	private String bienSo;
	private TrangThai trangThai;
	private BigDecimal giaNgay;
	private BigDecimal giaTuan;

	public enum TrangThai {
		SAN_SANG, BAO_TRI
	}

	public XeMay() {
		super();
	}

	public XeMay(int maXe, int userID, String hangXe, String dongXe, int namSanXuat, float dungTich, String bienSo,
			TrangThai trangThai, BigDecimal giaNgay, BigDecimal giaTuan) {
		super();
		this.maXe = maXe;
		this.userID = userID;
		this.hangXe = hangXe;
		this.dongXe = dongXe;
		this.namSanXuat = namSanXuat;
		this.dungTich = dungTich;
		this.bienSo = bienSo;
		this.trangThai = trangThai;
		this.giaNgay = giaNgay;
		this.giaTuan = giaTuan;
	}

	public XeMay(int userID, String hangXe, String dongXe, int namSanXuat, float dungTich, String bienSo,
			TrangThai trangThai, BigDecimal giaNgay, BigDecimal giaTuan) {
		super();
		this.userID = userID;
		this.hangXe = hangXe;
		this.dongXe = dongXe;
		this.namSanXuat = namSanXuat;
		this.dungTich = dungTich;
		this.bienSo = bienSo;
		this.trangThai = trangThai;
		this.giaNgay = giaNgay;
		this.giaTuan = giaTuan;
	}

	public int getMaXe() {
		return maXe;
	}

	public void setMaXe(int maXe) {
		this.maXe = maXe;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getHangXe() {
		return hangXe;
	}

	public void setHangXe(String hangXe) {
		this.hangXe = hangXe;
	}

	public String getDongXe() {
		return dongXe;
	}

	public void setDongXe(String dongXe) {
		this.dongXe = dongXe;
	}

	public int getNamSanXuat() {
		return namSanXuat;
	}

	public void setNamSanXuat(int namSanXuat) {
		this.namSanXuat = namSanXuat;
	}

	public float getDungTich() {
		return dungTich;
	}

	public void setDungTich(float dungTich) {
		this.dungTich = dungTich;
	}

	public String getBienSo() {
		return bienSo;
	}

	public void setBienSo(String bienSo) {
		this.bienSo = bienSo;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		this.trangThai = trangThai;
	}

	public BigDecimal getGiaNgay() {
		return giaNgay;
	}

	public void setGiaNgay(BigDecimal giaNgay) {
		this.giaNgay = giaNgay;
	}

	public BigDecimal getGiaTuan() {
		return giaTuan;
	}

	public void setGiaTuan(BigDecimal giaTuan) {
		this.giaTuan = giaTuan;
	}
	

}