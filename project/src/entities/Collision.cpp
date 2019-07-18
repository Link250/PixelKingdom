#include "Collision.h"

#include "../main/Game.h"

namespace Pixelverse {

Collision::Collision() : collide_x(false), collide_y(false), collisionStrength(0){}

Collision::~Collision(){}

std::unique_ptr<Collision> Collision::canMoveTo(Hitbox hitbox, vec2 position, vec2 movement, double rotation = 0, bool canSlip = false){
	std::unique_ptr<Collision> collision = std::make_unique<Collision>();

	double l = movement.length();
	rotation = fmod(rotation, M_PI*2);
	bool sideways = (rotation > M_PI/4 && rotation < M_PI/4*3) || (rotation < -M_PI/4 && rotation > -M_PI/4*3);
	if(sideways) hitbox = {hitbox.ymin, hitbox.ymax, hitbox.xmin, hitbox.xmax};
	vec2 newPos(position), lastPos(position);
	MainCheck:
	for (double sl = 0; sl <= l + stepsize + 0.1; sl += stepsize) {
		if(sl<=l) {
			newPos = position + movement * sl/l;
		}else {
			newPos = position + movement;
		}
		//gets the first Collision on the Y axis
		if(!collision->collide_y && movement.y != 0.0){
			for(double i = hitbox.xmin; i <= hitbox.xmax; i+=0.5){
				if(i>hitbox.xmax)i = hitbox.xmax;
				coordinate check = coordinate(vec2{lastPos.x+i,newPos.y+(movement.y<0 ? hitbox.ymin : hitbox.ymax)});
				if(Game::map->getMaterial(check, true)->solid) {
					if(canSlip && sideways) {
						if(!collision->collide_x && getCollision(hitbox, vec2{newPos.x + (movement.x < 0 ? -2 : 2), lastPos.y}, lastPos)) {
							lastPos.x = lastPos.x + (movement.x < 0 ? -2 : 2);
							position.x += (movement.x < 0 ? -2 : 2) - stepsize/l*movement.x;
							lastPos.y = newPos.y;
							goto MainCheck;
						}else if(getCollision(hitbox, vec2{newPos.x + (movement.x < 0 ? 2 : -2), lastPos.y}, lastPos)){
							lastPos.x = lastPos.x + (movement.x < 0 ? 2 : -2);
							position.x += (movement.x < 0 ? 2 : -2) - stepsize/l*movement.x;
							lastPos.y = newPos.y;
							goto MainCheck;
						}
					}
					collision->collisions.push_back(check);
					collision->collide_y = true;
				}
			}
		}

		//gets the first Collision on the X axis
		if(!collision->collide_x && movement.x != 0.0){
			for(double i = hitbox.ymin; i <= hitbox.ymax; i+=0.5){
				if(i>hitbox.ymax)i = hitbox.ymax;
				coordinate check = coordinate(vec2{newPos.x+(movement.x<0 ? hitbox.xmin : hitbox.xmax),lastPos.y+i});
				if(Game::map->getMaterial(check, true)->solid) {
					if(canSlip && !sideways) {
						if(!collision->collide_y && getCollision(hitbox, vec2{newPos.x, lastPos.y + (movement.y < 0 ? -2 : 2)}, lastPos)) {
							lastPos.y = lastPos.y + (movement.y < 0 ? -2 : 2);
							position.y += (movement.y < 0 ? -2 : 2) - stepsize/l*movement.y;
							lastPos.x = newPos.x;
							goto MainCheck;
						}else if(getCollision(hitbox, vec2{newPos.x, lastPos.y+(movement.y < 0 ? 2 : -2)}, lastPos)){
							lastPos.y = lastPos.y + (movement.y < 0 ? 2 : -2);
							position.y += (movement.y < 0 ? 2 : -2) - stepsize/l*movement.y;
							lastPos.x = newPos.x;
							goto MainCheck;
						}
					}
					collision->collisions.push_back(check);
					collision->collide_x = true;
				}
			}
		}
		if(collision->collide_x && collision->collide_y) {
			break;
		}else {
			if(!collision->collide_x)lastPos.x = newPos.x;
			if(!collision->collide_y)lastPos.y = newPos.y;
		}
	}
	collision->entityPos = lastPos;
	collision->collisionStrength = l;

	return collision;
}

bool Collision::getCollision(Hitbox hitbox, vec2 newPos, vec2 lastPos) {
	for(double i = hitbox.xmin; i <= hitbox.xmax; i+=0.5){
		if(i>hitbox.xmax)i = hitbox.xmax;
		if(Game::map->getMaterial(coordinate(vec2{newPos.x+i,newPos.y+(newPos.y < lastPos.y ? hitbox.ymin : hitbox.ymax)}), true)->solid) {
			return false;
		}
	}

	for(double i = hitbox.ymin; i <= hitbox.ymax; i+=0.5){
		if(i>hitbox.ymax)i = hitbox.ymax;
		if(Game::map->getMaterial(coordinate(vec2{newPos.x+(newPos.x < lastPos.x ? hitbox.xmin : hitbox.xmax),newPos.y+i}), true)->solid) {
			return false;
		}
	}
	return true;
}

} /* namespace Pixelverse */
