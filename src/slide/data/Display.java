package slide.data;

public class Display {

	public DisGroup disGroup;
	public GroupDisplay numberDisplay = new GroupDisplay();
	public GroupDisplay letterUpperDisplay = new GroupDisplay();
	public GroupDisplay letterLowerDisplay = new GroupDisplay();
	public CornerBtn cornerBtnRD;
	public CornerBtn cornerBtnLD;
	public CornerBtn cornerBtnRU;
	
	public static enum DisGroup {
		Number, LetterU, LetterL;
	}

}
