package slide.data;

import java.util.Vector;

import slide.data.BtnDisplay;

public class SetDisplay {
	public int btnN;
	public Vector<BtnDisplay> btns = new Vector<BtnDisplay>();
	
	public SetDisplay(Vector<BtnDisplay> btns){
		this.btns = btns;
		btnN = this.btns.size();
	}

	public String getTextById(int i) {
		for (int j = 0; j < btns.size(); j++) {
			if (btns.elementAt(j).btnId == i) {
				return btns.elementAt(j).btnText;
			}
		}
		return null;
	}
}
