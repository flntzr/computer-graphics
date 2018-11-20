#version 330

layout(location=0) in vec3 positions;
layout(location=1) in vec3 colors;
layout(location=2) in vec3 normal_vectors;

out vec3 triangleColor;

uniform mat4 matrix;
uniform mat4 projection_matrix;

float n = 1; // hardness
float iL = 2.0; // light intensity
vec3 l = vec3(3, 0, 0); // light source
float iA = .3;
float kA = .4;
float kS = .2;
float kD = .4;

void main() {
	mat4 inverseMatrix = inverse(transpose(matrix));
//	float alpha = dot(vec4(-1, 1, 1, 0), inverseMatrix * vec4(normal_vectors, 0)); // angle between light vector and camera vector
	vec3 normal_vector = vec3(inverseMatrix) * normal_vectors;
	vec3 r = 2 * (l * normal_vectors) * normal_vectors - l;
	vec3 v = vec3(-1, 1, 1); // direction to camera
	float i = iA * kA + iL * (dot(l, normal_vector)* kD + pow(dot(r, v), n) * kS);
	triangleColor = colors * i;
	gl_Position = projection_matrix * matrix * vec4(positions, 1.0);
}
