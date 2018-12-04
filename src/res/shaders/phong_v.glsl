#version 330

layout(location=0) in vec3 positions;
layout(location=1) in vec3 colors;
layout(location=2) in vec3 normal_vectors;

out vec3 triangleColor;

uniform mat4 matrix;
uniform mat4 projection_matrix;

float n = 1; // hardness
float iL = 2.0; // light intensity
vec3 l = normalize(vec3(3, 0, 2)); // light source
float iA = .3;
float kA = .4;
float kS = .2;
float kD = .4;

void main() {
	mat4 inverseMatrix = inverse(transpose(matrix));
	vec3 normal_vector = vec3(inverseMatrix) * normal_vectors;
	normal_vector = normalize(normal_vector);
	vec3 r = 2 * dot(l, normal_vectors) * normal_vectors - l;
	vec3 v = normalize(vec3(-1, 1, 1)); // direction to camera
	float i = iA * kA + iL * (dot(l, normal_vector)* kD + pow(dot(r, v), n) * kS);
	triangleColor = vec3(i, i, i);
	gl_Position = projection_matrix * matrix * vec4(positions, 1.0);
}
