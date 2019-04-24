package Observer;

public enum AppStrings {
	NOEVENTS;

	public String toString() {
		switch (this) {
		case NOEVENTS:
			return "Nothing New Today.";
		default:
			return "";
		}
	}
}
