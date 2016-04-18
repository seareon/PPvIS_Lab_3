package Library;

import java.io.Serializable;

public class Student implements Serializable {
	private String fio;
	private String numberGrup;
	private int truancy�[] = {0, 0, 0};
	
	public Student(String fio, String numberGrup, int sickness, int otherCauses, int withoutGoodReason) {
		this.fio = fio;
		this.numberGrup = numberGrup;
		truancy�[0] = sickness;
		truancy�[1] = otherCauses;
		truancy�[2] = withoutGoodReason;
	}
	
	public String getFIO() {
		return fio;
	}
	
	public String getGrup() {
		return numberGrup;
	}
	
	public int getNumbertSickness() {
		return truancy�[0];
	}
	
	public int getNumberOtherCauses() {
		return truancy�[1];
	}
	
	public int getNumberWithoutGoodReason() {
		return truancy�[2];
	}
}
