package model;

public class DoiTac {
	private int maDoiTac;
	private int userID;

	public DoiTac(int maDoiTac, int userID) {
		super();
		this.maDoiTac = maDoiTac;
		this.userID = userID;
	}

	public int getMaDoiTac() {
		return maDoiTac;
	}

	public void setMaDoiTac(int maDoiTac) {
		this.maDoiTac = maDoiTac;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}