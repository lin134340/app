package slide.data;

import android.graphics.Path;

public class BtnDisplay {
	public int btnId;
	public Path btnPath;
	public String btnText;
	public TextPos textPos = new TextPos();

	public class TextPos {
		public float x;
		public float y;
	}

	public BtnDisplay(int i, Path p, String s, float[] pos) {
		btnId = i;
		btnPath = new Path(p);
		btnText = s;
		textPos.x = pos[0];
		textPos.y = pos[1];
	}
}
