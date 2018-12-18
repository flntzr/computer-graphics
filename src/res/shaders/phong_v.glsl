#version 330

layout(location=0) in vec3 positions;
//layout(location=1) in vec3 colors;
layout(location=1) in vec3 normal_vectors;
layout(location=2) in vec2 uvs;

//out vec3 triangle_color;
out vec3 normal_vector;
out vec3 position;
out vec2 uv;

uniform mat4 matrix;
uniform mat4 projection_matrix;

void main() {
	uv = uvs;
//	triangle_color = colors;
	mat3 inverse_matrix = inverse(transpose(mat3(matrix)));
	normal_vector = normalize(inverse_matrix * normal_vectors);
	position = mat3(matrix) * positions;
	gl_Position = projection_matrix * matrix * vec4(positions, 1.0);
}
