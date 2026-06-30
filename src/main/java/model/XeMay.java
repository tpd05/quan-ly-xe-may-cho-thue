package model;

import java.math.BigDecimal;

public class XeMay {
	private int maXe;
	private int maChiNhanh;
	private String hangXe;
	private String dongXe;
	private int doiXe;
	private float dungTich;
	private String urlHinhAnh;
	private String bienSo;
	private String trangThai;
	private BigDecimal giaNgay;
	private BigDecimal giaTuan;

	public XeMay(int maXe, int maChiNhanh, String hangXe, String dongXe, int doiXe, float dungTich, String urlHinhAnh,
			String bienSo, String trangThai, BigDecimal giaNgay, BigDecimal giaTuan) {
		super();
		this.maXe = maXe;
		this.maChiNhanh = maChiNhanh;
		this.hangXe = hangXe;
		this.dongXe = dongXe;
		this.doiXe = doiXe;
		this.dungTich = dungTich;
		this.urlHinhAnh = urlHinhAnh;
		this.bienSo = bienSo;
		this.trangThai = trangThai;
		this.giaNgay = giaNgay;
		this.giaTuan = giaTuan;
	}

	public XeMay(int maChiNhanh, String hangXe, String dongXe, int doiXe, float dungTich, String urlHinhAnh,
			String bienSo, String trangThai, BigDecimal giaNgay, BigDecimal giaTuan) {
		super();
		this.maChiNhanh = maChiNhanh;
		this.hangXe = hangXe;
		this.dongXe = dongXe;
		this.doiXe = doiXe;
		this.dungTich = dungTich;
		this.urlHinhAnh = urlHinhAnh;
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

	public int getMaChiNhanh() {
		return maChiNhanh;
	}

	public void setMaChiNhanh(int maChiNhanh) {
		this.maChiNhanh = maChiNhanh;
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

	public int getDoiXe() {
		return doiXe;
	}

	public void setDoiXe(int doiXe) {
		this.doiXe = doiXe;
	}

	public float getDungTich() {
		return dungTich;
	}

	public void setDungTich(float dungTich) {
		this.dungTich = dungTich;
	}

	public String getUrlHinhAnh() {
		return urlHinhAnh;
	}

	public void setUrlHinhAnh(String urlHinhAnh) {
		this.urlHinhAnh = urlHinhAnh;
	}

	public String getBienSo() {
		return bienSo;
	}

	public void setBienSo(String bienSo) {
		this.bienSo = bienSo;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
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