package slide.data;

import android.graphics.Path;

public class BtnDisplay {

	public DisGroup disGroup;
	public GroupDisplay NumberDisplay;
	public GroupDisplay LetterUpperDisplay;
	public GroupDisplay LetterLowerDisplay;

	public class GroupDisplay {
		public SetDisplay[] sets;

		public class SetDisplay {
			public Btn[] btns;

			public class Btn {
				int btnId;
				Path btnPath;
				String btnText;

				public Btn(int i, Path p, String s) {
					btnPath = p;
					btnId = i;
					btnText = s;
				}
			}

			public String getTextById(int i) {
				for (int j = 0; j < btns.length; j++) {
					if (btns[j].btnId == i) {
						return btns[j].btnText;
					}
				}
				return null;
			}
		}
	}

	public enum DisGroup {
		Number, LetterU, LetterL;
	}

}
