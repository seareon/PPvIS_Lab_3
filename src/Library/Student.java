package Library;

import java.io.Serializable;

public class Student implements Serializable {
	private String fio;
	private String numberGrup;
	private int truancyõ[] = {0, 0, 0};
	
	public Student(String fio, String numberGrup, int sickness, int otherCauses, int withoutGoodReason) {
		this.fio = fio;
		this.numberGrup = numberGrup;
		truancyõ[0] = sickness;
		truancyõ[1] = otherCauses;
		truancyõ[2] = withoutGoodReason;
	}
	
	public String getFIO() {
		return fio;
	}
	
	public String getGrup() {
		return numberGrup;
	}
	
	public int getNumbertSickness() {
		return truancyõ[0];
	}
	
	public int getNumberOtherCauses() {
		return truancyõ[1];
	}
	
	public int getNumberWithoutGoodReason() {
		return truancyõ[2];
	}
}
