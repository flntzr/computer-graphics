#version 330
out vec3 color;
in vec3 triangleColor;
in vec2 uv;

uniform sampler2D sampler;

bool isWithinGrid(float p) {
	int rounded = int(round(p * 100));
	return rounded % 10 == 0;
}

vec3 getProceduralTexture() {
	if (isWithinGrid(uv[0]) || isWithinGrid(uv[1])) {
		return vec3(1, 1, 0);
	}
	return vec3(0, 0, 0);
}

void main() {
	color = getProceduralTexture();
}
