package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ThanhToan {
	private int maThanhToan;
	private int maDonThue;
	private BigDecimal soTien;
	private String phuongThuc;
	private LocalDateTime thoiGianTao;
	private TrangThai trangThai;

	public enum TrangThai {

		CHUA_THANH_TOAN,

		DA_THANH_TOAN

	}

	public ThanhToan(int maThanhToan, int maDonThue, BigDecimal soTien, String phuongThuc, LocalDateTime thoiGianTao,
			TrangThai trangThai) {
		super();
		this.maThanhToan = maThanhToan;
		this.maDonThue = maDonThue;
		this.soTien = soTien;
		this.phuongThuc = phuongThuc;
		this.thoiGianTao = thoiGianTao;
		this.trangThai = trangThai;
	}

	public ThanhToan() {
		super();
	}

	public ThanhToan(int maDonThue, BigDecimal soTien, String phuongThuc, LocalDateTime thoiGianTao,
			TrangThai trangThai) {
		super();
		this.maDonThue = maDonThue;
		this.soTien = soTien;
		this.phuongThuc = phuongThuc;
		this.thoiGianTao = thoiGianTao;
		this.trangThai = trangThai;
	}

	public int getMaThanhToan() {
		return maThanhToan;
	}

	public void setMaThanhToan(int maThanhToan) {
		this.maThanhToan = maThanhToan;
	}

	public int getMaDonThue() {
		return maDonThue;
	}

	public void setMaDonThue(int maDonThue) {
		this.maDonThue = maDonThue;
	}

	public BigDecimal getSoTien() {
		return soTien;
	}

	public void setSoTien(BigDecimal soTien) {
		this.soTien = soTien;
	}

	public String getPhuongThuc() {
		return phuongThuc;
	}

	public void setPhuongThuc(String phuongThuc) {
		this.phuongThuc = phuongThuc;
	}

	public LocalDateTime getThoiGianTao() {
		return thoiGianTao;
	}

	public void setThoiGianTao(LocalDateTime thoiGianTao) {
		this.thoiGianTao = thoiGianTao;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		this.trangThai = trangThai;
	}

}
