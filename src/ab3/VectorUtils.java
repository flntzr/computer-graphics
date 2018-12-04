package ab3;

public final class VectorUtils {
	public static float[] crossProductVec3(float[] a, float[] b) {
		return new float[] { a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] - b[0] };
	}

	public static float[] subtractVec3(float[] a, float[] b) {
		return new float[] { a[0] - b[0], a[1] - b[1], a[2] - b[2] };
	}
	
	public static float[] addVec3(float[] a, float[] b) {
		return new float[] { a[0] + b[0], a[1] + b[1], a[2] + b[2] };
	}
}
