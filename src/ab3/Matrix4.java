package ab3;

import java.text.DecimalFormat;

//Alle Operationen �ndern das Matrixobjekt selbst und geben das eigene Matrixobjekt zur�ck
//Dadurch kann man Aufrufe verketten, z.B.
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4 {
	private float[] m = new float[16]; // columnwise, then row-wise

	public Matrix4() {
		m[0] = 1;
		m[5] = 1;
		m[10] = 1;
		m[15] = 1;
	}

	public Matrix4(Matrix4 copy) {
		this.m = copy.getValuesAsArray();
	}

	public Matrix4(float[] m) {
		this.m = m;
	}

	public Matrix4(float near, float far, float width, float height) {
		float[] m0 = new Matrix4().getValuesAsArray();
		m0[10] = (-far - near) / (far - near);
		m0[14] = (-2 * near * far) / (far - near);
		m0[15] = 0;
		m0[11] = -1;
//		float[] m1 = new Matrix4().getValuesAsArray();
		m0[0] = (2 * near) / width;
		m0[5] = (2 * near) / height;
		this.m = new Matrix4(m0).getValuesAsArray();
	}

	public Matrix4 multiply(Matrix4 other) {
		final float[] m0 = other.getValuesAsArray();
		float[] result = new float[16];
		result[0] = m0[0] * m[0] + m0[4] * m[1] + m0[8] * m[2] + m0[12] * m[3];
		result[1] = m0[1] * m[0] + m0[5] * m[1] + m0[9] * m[2] + m0[13] * m[3];
		result[2] = m0[2] * m[0] + m0[6] * m[1] + m0[10] * m[2] + m0[14] * m[3];
		result[3] = m0[3] * m[0] + m0[7] * m[1] + m0[11] * m[2] + m0[15] * m[3];

		result[4] = m0[0] * m[4] + m0[4] * m[5] + m0[8] * m[6] + m0[12] * m[7];
		result[5] = m0[1] * m[4] + m0[5] * m[5] + m0[9] * m[6] + m0[13] * m[7];
		result[6] = m0[2] * m[4] + m0[6] * m[5] + m0[10] * m[6] + m0[14] * m[7];
		result[7] = m0[3] * m[4] + m0[7] * m[5] + m0[11] * m[6] + m0[15] * m[7];

		result[8] = m0[0] * m[8] + m0[4] * m[9] + m0[8] * m[10] + m0[12] * m[11];
		result[9] = m0[1] * m[8] + m0[5] * m[9] + m0[9] * m[10] + m0[13] * m[11];
		result[10] = m0[2] * m[8] + m0[6] * m[9] + m0[10] * m[10] + m0[14] * m[11];
		result[11] = m0[3] * m[8] + m0[7] * m[9] + m0[11] * m[10] + m0[15] * m[11];

		result[12] = m0[0] * m[12] + m0[4] * m[13] + m0[8] * m[14] + m0[12] * m[15];
		result[13] = m0[1] * m[12] + m0[5] * m[13] + m0[9] * m[14] + m0[13] * m[15];
		result[14] = m0[2] * m[12] + m0[6] * m[13] + m0[10] * m[14] + m0[14] * m[15];
		result[15] = m0[3] * m[12] + m0[7] * m[13] + m0[11] * m[14] + m0[15] * m[15];
		this.m = result;
		return this;
	}

	public Matrix4 translate(float x, float y, float z) {
		float[] transM = new float[16];
		transM[0] = 1;
		transM[5] = 1;
		transM[10] = 1;
		transM[15] = 1;
		transM[12] = x;
		transM[13] = y;
		transM[14] = z;
		this.m = this.multiply(new Matrix4(transM)).getValuesAsArray();
		return this;
	}

	public Matrix4 scale(float uniformFactor) {
		float[] scaleM = new float[16];
		scaleM[0] = uniformFactor;
		scaleM[5] = uniformFactor;
		scaleM[10] = uniformFactor;
		scaleM[15] = 1;
		this.m = this.multiply(new Matrix4(scaleM)).getValuesAsArray();
		return this;
	}

	public Matrix4 scale(float sx, float sy, float sz) {
		float[] scaleM = new float[16];
		scaleM[0] = sx;
		scaleM[5] = sy;
		scaleM[10] = sz;
		scaleM[15] = 1;
		this.m = this.multiply(new Matrix4(scaleM)).getValuesAsArray();
		return this;
	}

	public Matrix4 rotateX(float angle) {
		float[] rotationM = new float[16];
		float cosA = (float) Math.cos(angle);
		float sinA = (float) Math.sin(angle);
		rotationM[0] = 1;
		rotationM[15] = 1;
		rotationM[5] = cosA;
		rotationM[6] = sinA;
		rotationM[9] = -sinA;
		rotationM[10] = cosA;
		return this.multiply(new Matrix4(rotationM));
	}

	public Matrix4 rotateY(float angle) {
		float[] rotationM = new float[16];
		float cosA = (float) Math.cos(angle);
		float sinA = (float) Math.sin(angle);
		rotationM[5] = 1;
		rotationM[15] = 1;
		rotationM[0] = cosA;
		rotationM[2] = sinA;
		rotationM[8] = -sinA;
		rotationM[10] = cosA;
		return this.multiply(new Matrix4(rotationM));
	}

	public Matrix4 rotateZ(float angle) {
		float[] rotationM = new float[16];
		float cosA = (float) Math.cos(angle);
		float sinA = (float) Math.sin(angle);
		rotationM[10] = 1;
		rotationM[15] = 1;
		rotationM[0] = cosA;
		rotationM[1] = sinA;
		rotationM[4] = -sinA;
		rotationM[5] = cosA;
		return this.multiply(new Matrix4(rotationM));
	}

	public float[] getValuesAsArray() {
		return this.m;
	}

	@Override
	public String toString() {
		DecimalFormat f = new DecimalFormat("##.00");
		return f.format(m[0]) + ", " + f.format(m[4]) + ", " + f.format(m[8]) + ", " + f.format(m[12]) + "\n"
				+ f.format(m[1]) + ", " + f.format(m[5]) + ", " + f.format(m[9]) + ", " + f.format(m[13]) + "\n"
				+ f.format(m[2]) + ", " + f.format(m[6]) + ", " + f.format(m[10]) + ", " + f.format(m[14]) + "\n"
				+ f.format(m[3]) + ", " + f.format(m[7]) + ", " + f.format(m[11]) + ", " + f.format(m[15]) + "\n";
	}

}
