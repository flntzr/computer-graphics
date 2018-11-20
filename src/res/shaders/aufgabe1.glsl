#version 330
out vec3 color;

struct Circle {
	vec2 center;
	float radius;
};

struct Rect {
	vec2 p1;
	vec2 p2;
	float angle;
};

struct Line {
	vec2 p1;
	vec2 p2;
};

bool isInCircle(float x, float y, Circle c) {
	vec2 p = vec2(x, y);
	return distance(p, c.center) <= c.radius;
}

bool isInRect(float x, float y, Rect r) {
	vec2 p = vec2(cos(-r.angle) * x - sin(-r.angle) * y, sin(-r.angle) * x + cos(-r.angle) * y);
	return p.x > r.p1.x && p.y > r.p1.y && p.x < r.p2.x && p.y < r.p2.y;
}

bool isInLine(float x, float y, Line l) {
	vec2 v = l.p2 - l.p1;
	float lineLength = distance(l.p1, l.p2);
	float angle = atan(v.y / v.x);
	vec2 p = vec2(cos(-angle) * x - sin(-angle) * y, sin(-angle) * x + cos(-angle) * y);
	return p.x > l.p1.x &&
			p.y > l.p1.y &&
			p.x < l.p1.x + lineLength &&
			p.y < l.p1.y + 5;
//	vec2 v = l.p2 - l.p1;
//	float angle = atan(v.y / v.x);
//	float lineLength = distance(l.p1, l.p2);
//	Rect r = Rect(vec2(l.p1.x, 0.0), vec2(l.p1.x + lineLength, 20.0), angle);
//	return isInRect(x, y, r);
}

void main() {
	float x = gl_FragCoord.x;
	float y = gl_FragCoord.y;
	Circle c = Circle(vec2(500.0, 500.0), 60.0);
	Rect r = Rect(vec2(100.0, 250.0), vec2(150.0, 400.0), 0.1);
	Line l = Line(vec2(200, 400), vec2(300, 500));
	if (isInCircle(x, y, c)) {
		color = vec3(1.0, 1.0, 1.0);
	} else if (isInRect(x, y, r)) {
		color = vec3(0.0, 1.0, 0.0);
	} else if (isInLine(x, y, l)) {
		color = vec3(0.0, 0.0, 0.0);
	} else if (gl_FragCoord.x <= 400) {
		color = vec3(1.0, 0.0, 1.0);
	} else if (gl_FragCoord.x > 400) {
		color = vec3(1.0, 0.0, 0.5);
	}
}
