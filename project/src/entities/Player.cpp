#include "Player.h"

#include "../main/Game.h"
#include "../input/InputHandler.h"
#include "../config/KeyConfig.h"

#include <iostream>
#include <algorithm>

namespace Pixelverse {

Player::Player(vec2 position) : Mob(position, {0, 0}, 100, 100), beltBag(1), itemBags(2), selectedSlot(0), buildSize(5){
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
	Screen::setTargetRotation(rotation);
	//if(Game::updateCount%60 == 0)std::cout << rotation << std::endl;

	beltBag.update();
	itemBags.update();
}

void Player::render(){
	Screen::renderGameSprite(sprites, 0, position, {1, 1}, rotation);
}

void Player::applyGravity(){
	vec2 temp = Game::map->getGravity(coordinate(position));
	rotation = temp.angle();
	velocity += temp;
}

int Player::collectItem(std::shared_ptr<Item> item){
	int taken = 0;
	if(beltBag[0] != nullptr) taken += beltBag->inventory->collectItem(&item);
	if(itemBags[0] != nullptr) taken += itemBags[0]->inventory->collectItem(&item);
	if(itemBags[1] != nullptr) taken += itemBags[1]->inventory->collectItem(&item);
	return taken;
}

size_t Player::getSelectedSlot(){return selectedSlot;}

void Player::changeSelectedSlot(size_t slotChange){
	if(beltBag[0] != nullptr){
		selectedSlot = (selectedSlot+slotChange)%beltBag->inventory->size;
		if(selectedSlot < 0) selectedSlot += beltBag->inventory->size;
	}
}

void Player::setSelectedSlot(size_t slot){
	if(beltBag[0] != nullptr){
		selectedSlot = slot%beltBag->inventory->size;
		if(selectedSlot < 0) selectedSlot += beltBag->inventory->size;
	}
}

int Player::getBuildSize(){return buildSize;}

void Player::changeBuildSize(int sizeChange){
	buildSize += sizeChange;
	if(buildSize > MAX_BUILD_SIZE) buildSize = MAX_BUILD_SIZE;
	if(buildSize < 1) buildSize = 1;
}

void Player::setBuildSize(int size){
	buildSize = size;
	if(buildSize > MAX_BUILD_SIZE) buildSize = MAX_BUILD_SIZE;
	if(buildSize < 1) buildSize = 1;
}

void Player::load(){
	Mob::load();
	//TODO load beltBag, itemBags
	std::shared_ptr<BeltBag> newBeltBag = Item::createNew<BeltBag>("small_belt_bag");
	beltBag.collectItem(&newBeltBag);
	std::shared_ptr<Bag> newItemBag1 = Item::createNew<Bag>("small_item_pouch");
	itemBags.collectItem(&newItemBag1);
	std::shared_ptr<Bag> newItemBag2 = Item::createNew<Bag>("small_item_pouch");
	itemBags.collectItem(&newItemBag2);

	std::shared_ptr<Item> newItem;
	newItem = Item::createNew("stone", 100); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("dirt", 200); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("sand", 99999); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("small_item_pouch"); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("small_belt_bag"); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("fabric", 10); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("stone_pickaxe"); beltBag->inventory->collectItem(&newItem);
	newItem = Item::createNew("dev_pickaxe"); beltBag->inventory->collectItem(&newItem);
}

void Player::save(){
	Mob::save();
	//TODO save beltBag, itemBags
}

} /* namespace Pixelverse */
