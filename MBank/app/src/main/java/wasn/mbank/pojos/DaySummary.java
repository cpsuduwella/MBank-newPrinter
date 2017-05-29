package wasn.mbank.pojos;

/**
 * class that contains attributes of day summary
 * @author eranga
 */
public class DaySummary {

	int depositCount;
	
	int withdrawCount;
	
	String depositAmount;
	
	String withdrawAmount;

	public DaySummary(int depositCount, int withdrawCount,
			String depositAmount, String withdrawAmount) {
		super();
		this.depositCount = depositCount;
		this.withdrawCount = withdrawCount;
		this.depositAmount = depositAmount;
		this.withdrawAmount = withdrawAmount;
	}

	public int getDepositCount() {
		return depositCount;
	}

	public void setDepositCount(int depositCount) {
		this.depositCount = depositCount;
	}

	public int getWithdrawCount() {
		return withdrawCount;
	}

	public void setWithdrawCount(int withdrawCount) {
		this.withdrawCount = withdrawCount;
	}

	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(String withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	
}
