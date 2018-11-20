#version 330
layout(location=0) in vec2 positions;
layout(location=1) in vec3 colors;

out vec3 triangleColor;

vec2 rotatePoint(vec2 point, float angle) {
	float x = cos(angle) * point[0] - sin(angle) * point[1];
	float y = sin(angle) * point[0] + cos(angle) * point[1];
	return vec2(x, y);
}

void main() {
	triangleColor = colors;
	float angle = 0.2;
	gl_Position = vec4(rotatePoint(positions, angle), 0.0, 1.0);
}
