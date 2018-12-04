#version 330
in vec3 triangle_color;
in vec3 normal_vector;
in vec3 position;

out vec3 color;


float n = 1; // hardness
float iL = 2.0; // light intensity
vec3 light_pos = vec3(0, 0, 500); // light position
float iA = .8;
float kA = .6;
float kS = .5;
float kD = .8;

void main() {
	vec3 l = normalize(light_pos - position); // direction to light
	vec3 v = normalize(-position); // direction to camera
	vec3 r = 2 * dot(l, normal_vector) * normal_vector - l;
//	float i = (dot(l, normal_vector));
//	float i = iL * pow(dot(r, v), n) * kS;
	float i = iA * kA + iL * (dot(l, normal_vector)* kD + pow(max(dot(r, v), 0 ), n) * kS);
	color = vec3(i, i, i);
}
