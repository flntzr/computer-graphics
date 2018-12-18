#version 330

layout(location=0) in vec3 positions;
layout(location=1) in vec3 colors;
layout(location=3) in vec2 uvs;

out vec3 triangleColor;
out vec2 uv;

uniform mat4 matrix;
uniform mat4 projection_matrix;

void main() {
	uv = uvs;
	triangleColor = colors;// * alpha;
	gl_Position = projection_matrix * matrix * vec4(positions, 1.0);
}
