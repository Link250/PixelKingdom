#include "Player.h"

#include "../main/Game.h"
#include "../input/InputHandler.h"
#include "../config/KeyConfig.h"

#include <iostream>
#include <algorithm>

namespace Pixelverse {

Player::Player(vec2 position) : Mob(position, {0, 0}, 100, 100), beltBag(1), itemBags(2){
	sprites = make_shared<SpriteSheet>("entities/player_sheet", 26, 28);
}

Player::~Player(){}

void Player::update(){
	vec2 gravity = Game::map->getGravity(coordinate(position));
	if (InputHandler::keyPressed(KeyConfig::JUMP) && canJump) {
		vec2 jump = gravity.normalize() * -jumpspeed;
		if(velocity.scalarProjection(jump)/jumpspeed < 1.0)
			velocity += jump;
		jumpLength++;
		if(jumpLength>jumpMaxLength) {
			canJump = false;
			jumpCooldown = jumpCooldownLength;
		}
	}else if(jumpLength>0){
		canJump = false;
		jumpCooldown = jumpCooldownLength;
		jumpLength = 0;
	}
	vec2 walkLeft = gravity.normalize().rotate(PI/2);
	double currentWalkLeft = velocity.dot(walkLeft);
	//printf("v = %f, %f; w = %f, %f; d = %f\n", velocity.x, velocity.y, (walkLeft*walkspeed).x, (walkLeft*walkspeed).y, velocity.dot(walkLeft*walkspeed)); fflush(stdout);
	//printf("%f\n", currentWalkLeft); fflush(stdout);
	if (InputHandler::keyPressed(KeyConfig::LEFT) ^ InputHandler::keyPressed(KeyConfig::RIGHT)) {
		if (InputHandler::keyPressed(KeyConfig::LEFT) && currentWalkLeft < walkspeed) {
			velocity += walkLeft * (currentWalkLeft/walkspeed < 0 ? (onGround ? slowdownGround : slowdownAir) : (onGround ? accelerationGround : accelerationAir));
			//TODO add limiter
		}
		if (InputHandler::keyPressed(KeyConfig::RIGHT) && currentWalkLeft > -walkspeed) {
			velocity -= walkLeft * (currentWalkLeft/walkspeed > 0 ? (onGround ? slowdownGround : slowdownAir) : (onGround ? accelerationGround : accelerationAir));
			//TODO add limiter
		}
	} else if(currentWalkLeft != 0){
			double length = (onGround ? slowdownGround : slowdownAir);
		if (currentWalkLeft < 0) {
			velocity += walkLeft * min(length, -currentWalkLeft);
		} else {
			velocity -= walkLeft * min(length, +currentWalkLeft);

		}
	}
	if (jumpCooldown > 0 && onGround)
		jumpCooldown--;
	if (jumpCooldown == 0)
		canJump = true;

	std::unique_ptr<Collision> collision = Collision::canMoveTo(hitbox, position, velocity, rotation, true);

	onGround = (collision->collide_y || collision->collide_x) && velocity.dot(gravity) > 0;

	if(collision->collide_x)velocity.x = 0;
	if(collision->collide_y)velocity.y = 0;

	position = collision->entityPos;

	Screen::setCenter(position);
	Screen::rotation_target = rotation;
	//if(Game::updateCount%60 == 0)std::cout << rotation << std::endl;
}

void Player::render(){
	Screen::renderGameSprite(sprites, 0, position, {1, 1}, rotation);
}

void Player::applyGravity(){
	vec2 temp = Game::map->getGravity(coordinate(position));
	rotation = temp.angle();
	velocity += temp;
}

void Player::load(){
	Mob::load();
	//TODO load beltBag, itemBags
	beltBag.collectItem(Item::createNew<BeltBag>("small_belt_bag"));
	itemBags.collectItem(Item::createNew<Bag>("small_item_pouch"));
	itemBags.collectItem(Item::createNew<Bag>("small_item_pouch"));
	beltBag->inventory->collectItem(Item::createNew("stone", 12345));
	beltBag->inventory->collectItem(Item::createNew("dirt", 67890));
	beltBag->inventory->collectItem(Item::createNew("grass", 300));
	beltBag->inventory->collectItem(Item::createNew("small_item_pouch"));
	beltBag->inventory->collectItem(Item::createNew("small_belt_bag"));
	beltBag->inventory->collectItem(Item::createNew("fabric", 10));
}

void Player::save(){
	Mob::save();
	//TODO save beltBag, itemBags
}

} /* namespace Pixelverse */
