#include "Planet.h"

#include "../utilities/PhysicConstants.h"

#include <ctime>

namespace Pixelverse {

FastNoise Planet::mainNoise, Planet::cellNoise, Planet::heatNoise;

Planet::Planet(){
	std::time_t time = std::time(0);
	mainNoise.SetSeed(time);
	mainNoise.SetFrequency(10);
	for (int i = 0; i < 256; ++i) {
		mainNoise.VAL_LUT[i] = mainNoise.VAL_LUT[i]*fabs(mainNoise.VAL_LUT[i])*fabs(mainNoise.VAL_LUT[i]);
		if(mainNoise.VAL_LUT[i] > 0){
			mainNoise.VAL_LUT[i] = mainNoise.VAL_LUT[i]*0.9+0.1;
		}else{
			mainNoise.VAL_LUT[i] = mainNoise.VAL_LUT[i]*1.1+0.1;
		}
	}

	cellNoise.SetSeed(time+1);
	cellNoise.SetFrequency(0.05);

	heatNoise.SetSeed(time+2);
	heatNoise.SetFrequency(5);
	for (int i = 0; i < 256; ++i) {
		heatNoise.VAL_LUT[i] = heatNoise.VAL_LUT[i]*fabs(heatNoise.VAL_LUT[i]);
	}
	/*int res = 50;
	int list[res*2+1] = {0};
	for(int i = 0; i < 256; i ++){
		list[int(round(mainNoise.VAL_LUT[i]*res))+res]++;
	}
	for(int i = 0; i < res*2+1; i ++){
		printf("%3i|", i-res);
		for (int j = 0; j < list[i]; ++j) {
			printf("-");
		}
		printf("|%i\n", list[i]);
	}*/
}

Planet::~Planet(){}

void Planet::update(){
	regions.forEach([](std::shared_ptr<Region> region){
		region->update();
	});
}

bool Planet::setMaterialUpdating(coordinate position, bool layer){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->setMaterialUpdating(position, layer);
	}return false;
}

std::shared_ptr<Region> Planet::getRegion(coordinate position){
	return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2);
}

std::shared_ptr<Chunk> Planet::getChunk(coordinate position){
	//std::shared_ptr<Region> region = regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH];
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->getChunk(position);
	}
	return nullptr;
}

void Planet::loadRegion(coordinate position){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) == nullptr){
		regions.setValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2, std::make_shared<Region>());
	}
}

void Planet::loadChunk(coordinate position){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) == nullptr){
		regions.setValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2, std::make_shared<Region>());
	}
	regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->loadChunk(position);
}

materialID_t Planet::getMaterialID(coordinate position, bool layer){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->getMaterialID(position, layer);
	}return 0;
}

void Planet::setMaterialID(coordinate position, bool layer, materialID_t id){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->setMaterialID(position, layer, id);
	}
}

bool Planet::placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->placeMaterial(position, layer, id, player);
	}return false;
}

bool Planet::breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	if(regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2) != nullptr){
		return regions.getValue(position.r_x+WIDTH/2, position.r_y+WIDTH/2)->breakMaterial(position, layer, player, miningTool);
	}return false;
}

vec2 Planet::getGravity(coordinate position){
	if(abs(position.x) > abs(position.y)){
//		double dist = double(abs(pixelPos.y))/std::max(abs(pixelPos.x), 1);
//		if(dist < 0.9){
			return {std::copysign(g_earth, -position.x), 0.0};
//		}else{
//			return vec2{std::copysign(g_earth, -pixelPos.x), 0.0}.rotate(std::copysign(M_PI/4, pixelPos.y));
//		}
	}else{
//		double dist = double(abs(pixelPos.x))/std::max(abs(pixelPos.y), 1);
//		if(dist < 0.9){
			return {0.0, std::copysign(g_earth, -position.y)};
//		}else{
//			return vec2{0.0, std::copysign(g_earth, -pixelPos.y)}.rotate(std::copysign(M_PI/4, pixelPos.x));
//		}
	}
	//circular gravity
	//return vec2({-pixelPos.x, -pixelPos.y}).normalize() * g_earth;
}

int2 Planet::getMaterialGravity(coordinate position){
	if(abs(position.x) > abs(position.y)){
		return {(position.x < 0)-(0 < position.x), 0};
	}else{
		return {0, (position.y < 0)-(0 < position.y)};
	}
}

} /* namespace Pixelverse */
