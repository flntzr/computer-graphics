#version 330
in vec3 triangle_color;
in vec3 normal_vector;
in vec3 position;

out vec3 color;


float n = .5; // hardness
float iL = 1.5; // light intensity
vec3 light_pos = vec3(0, 0, 5); // light position
float iA = .9;
float kA = .5;
float kS = .3;
float kD = .5;

void main() {
	vec3 l = normalize(light_pos - position); // direction to light
	vec3 v = normalize(-position); // direction to camera
	vec3 r = 2 * dot(l, normal_vector) * normal_vector - l;
//	float i = (dot(l, normal_vector));
//	float i = pow(max(dot(r, v), 0), n);
	float i = iA * kA + iL * (dot(l, normal_vector)* kD + pow(max(dot(r, v), 0 ), n) * kS);
	color = i * triangle_color;
}
