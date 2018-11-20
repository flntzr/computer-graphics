#version 330
out vec3 color;
in vec3 triangleColor;

void main() {
	color = triangleColor;
}
