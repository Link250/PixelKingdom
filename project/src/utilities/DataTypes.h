#ifndef UTILITIES_DATATYPES_H_
#define UTILITIES_DATATYPES_H_

#include <cmath>

typedef unsigned short materialID_t;
typedef unsigned short biomeID_t;
typedef unsigned short entityID_t;
typedef unsigned int itemID_t;

static const int MAP_SCALE = 2;

struct vec2{
	double x, y;
	inline vec2 operator+(vec2 v){
		return vec2{x + v.x, y + v.y};
	}
	inline vec2& operator+=(vec2 v){
		x += v.x;
		y += v.y;
		return *this;
	}
	inline vec2 operator-(vec2 v){
		return vec2{x - v.x, y - v.y};
	}
	inline vec2& operator-=(vec2 v){
		x -= v.x;
		y -= v.y;
		return *this;
	}
	inline vec2 operator*(double f){
		return vec2{x * f, y * f};
	}
	inline vec2& operator*=(double f){
		x *= f;
		y *= f;
		return *this;
	}
	inline vec2 operator/(double f){
		return vec2{x / f, y / f};
	}
	inline vec2& operator/=(double f){
		x /= f;
		y /= f;
		return *this;
	}
	inline vec2 normalize(){
		return this->operator /(length());
	}
	inline vec2 rotate(double r){
		return {x*cos(r) - y*sin(r), x*sin(r) + y*cos(r)};
	}
	inline double dot(vec2 v){
		return x*v.x + y*v.y;
	}
	inline double scalarProjection(vec2 v){
		return dot(v)/v.length();
	}
	inline double length(){
		return sqrt(x*x+y*y);
	}
	inline double angle(){
		return atan2(x, y);
	}
};

struct int2{
	int x, y;
	inline int2 operator+(int2 i){
		return int2{x + i.x, y + i.y};
	}
	inline int2& operator+=(int2 i){
		x += i.x;
		y += i.y;
		return *this;
	}
	inline int2 operator-(int2 i){
		return int2{x - i.x, y - i.y};
	}
	inline int2& operator-=(int2 i){
		x -= i.x;
		y -= i.y;
		return *this;
	}
	inline int2 operator*(int m){
		return int2{x * m, y * m};
	}
	inline int2& operator*=(int m){
		x *= m;
		y *= m;
		return *this;
	}
	inline int2 operator/(int m){
		return int2{x / m, y / m};
	}
	inline int2& operator/=(int m){
		x /= m;
		y /= m;
		return *this;
	}
	bool inside(int x, int y, int w, int h){
		return this->x >= x && this->x < x + w && this->y >= y && this->y < y + h;
	}
	bool inside(int2 pos, int2 size){
		return this->x >= pos.x && this->x < pos.x + size.x && this->y >= pos.y && this->y < pos.y + size.y;
	}
};

struct pixel_area{
	int x, y, w, h;
	void setPosition(int x, int y){
		this->x = x;
		this->y = y;
	}
	void setPosition(int2 pos){
		this->x = pos.x;
		this->y = pos.y;
	}
	void setSize(int w, int h){
		this->w = w;
		this->h = h;
	}
	void setSize(int2 size){
		this->w = size.x;
		this->h = size.y;
	}
	void setBounds(int x, int y, int w, int h){
		this->x = x;
		this->y = y;
		this->w = w;
		this->h = h;
	}
	void setBounds(int2 pos, int2 size){
		this->x = pos.x;
		this->y = pos.y;
		this->w = size.x;
		this->h = size.y;
	}
	bool contains(int x2, int y2){
		return x2 >= x && x2 < x + w && y2 >= y && y2 < y + h;
	}
	bool contains(int2 pos){
		return pos.x >= x && pos.x < x + w && pos.y >= y && pos.y < y + h;
	}
};

union coordinate{
	struct{
		union{
			signed int x;
			struct{
				unsigned char p_x, c_x;
				signed short r_x;
			};
			unsigned char x_bytes[4];
		};
		union{
			signed int y;
			struct{
				unsigned char p_y, c_y;
				signed short r_y;
			};
			unsigned char y_bytes[4];
		};
	};
	inline coordinate operator+(coordinate c){
		return coordinate{x + c.x, y + c.y};
	}
	inline coordinate& operator+=(coordinate &c){
		x += c.x;
		y += c.y;
		return *this;
	}
	inline coordinate operator-(coordinate c){
		return coordinate{x - c.x, y - c.y};
	}
	inline coordinate& operator-=(coordinate &c){
		x -= c.x;
		y -= c.y;
		return *this;
	}
	inline int rc_x(){ return x>>8; }
	inline int rc_y(){ return y>>8; }
	inline coordinate regionCoordinate(){
		return coordinate(x & 0xffff0000, y & 0xffff0000);
	}
	inline coordinate chunkCoordinate(){
		return coordinate(x & 0xffffff00, y & 0xffffff00);
	}
	coordinate() {}
	coordinate(int x, int y) {
		this->x = x;
		this->y = y;
	}
	coordinate(vec2 vec) {
		this->x = floor(vec.x/MAP_SCALE);
		this->y = floor(vec.y/MAP_SCALE);
	}
};

union light{
	struct{
		unsigned char r, g, b;
	};
	unsigned char bytes[3];
};

#endif /* UTILITIES_DATATYPES_H_ */
