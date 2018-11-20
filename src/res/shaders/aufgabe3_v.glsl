#version 330

layout(location=0) in vec3 positions;
layout(location=1) in vec3 colors;
layout(location=2) in vec3 normal_vectors;

out vec3 triangleColor;

uniform mat4 matrix;

void main() {
//	float alpha = dot(vec3(0,0,1), normal_vectors); // angle between light vector and camera vector
	triangleColor = colors;// * alpha;
	gl_Position = matrix * vec4(positions, 1.0);
}
