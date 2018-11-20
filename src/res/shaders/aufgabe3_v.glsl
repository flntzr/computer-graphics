#version 330

layout(location=0) in vec3 positions;
layout(location=1) in vec3 colors;
//layout(location=2) in vec3 normal_vectors;

out vec3 triangleColor;

uniform mat4 matrix;
uniform mat4 projection_matrix;

void main() {
//	mat4 inverseMatrix = inverse(transpose(matrix));
//	float alpha = dot(vec4(-1, 1, 1, 0), inverseMatrix * vec4(normal_vectors, 0)); // angle between light vector and camera vector
	triangleColor = colors;// * alpha;
	gl_Position = projection_matrix * matrix * vec4(positions, 1.0);
}
