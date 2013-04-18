package slide.data;

public class CornerBtn {
	public TextPos textPos = new TextPos();;
	public String btnText;

	public class TextPos {
		public float x;
		public float y;
	}

	public CornerBtn(String s, float[] p) {
		btnText = s;
		textPos.x = p[0];
		textPos.y = p[1];
	}
}
