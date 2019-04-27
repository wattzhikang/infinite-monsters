package server_core;

/**
 * Dummy class encapsulating the information in
 * the client's JSON message
 * @author Zachariah Watt
 *
 */
public class StrategyModificationMSInfo {
	private String requestType;
	private int subscriptionID;
	private int xL;
	private int xR;
	private int yU;
	private int yL;
	private int oldPlayerX;
	private int oldPlayerY;
	private int newPlayerX;
	private int newPlayerY;
	
	public StrategyModificationMSInfo(String requestType, int xL, int xR, int yU, int yL, int oldPlayerX,
			int oldPlayerY, int newPlayerX, int newPlayerY) {
		super();
		this.requestType = requestType;
		this.xL = xL;
		this.xR = xR;
		this.yU = yU;
		this.yL = yL;
		this.oldPlayerX = oldPlayerX;
		this.oldPlayerY = oldPlayerY;
		this.newPlayerX = newPlayerX;
		this.newPlayerY = newPlayerY;
	}
	
	public int getSubscriptionID() {
		return subscriptionID;
	}

	public String getRequestType() {
		return requestType;
	}

	public int getxL() {
		return xL;
	}

	public int getxR() {
		return xR;
	}

	public int getyU() {
		return yU;
	}

	public int getyL() {
		return yL;
	}

	public int getOldPlayerX() {
		return oldPlayerX;
	}

	public int getOldPlayerY() {
		return oldPlayerY;
	}

	public int getNewPlayerX() {
		return newPlayerX;
	}

	public int getNewPlayerY() {
		return newPlayerY;
	}
}
