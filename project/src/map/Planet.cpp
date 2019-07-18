#include "Planet.h"

#include "../utilities/PhysicConstants.h"

#include <cmath>
#include <ctime>

namespace Pixelverse {

FastNoise Planet::mainNoise, Planet::cellNoise, Planet::heatNoise;

Planet::Planet(){
	std::time_t time = std::time(0);
	mainNoise.SetSeed(time);
	mainNoise.SetFrequency(10);
	for (int i = 0; i < 256; ++i) {
		mainNoise.VAL_LUT[i] = mainNoise.VAL_LUT[i]*abs(mainNoise.VAL_LUT[i])*abs(mainNoise.VAL_LUT[i]);
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
		heatNoise.VAL_LUT[i] = heatNoise.VAL_LUT[i]*abs(heatNoise.VAL_LUT[i]);
	}
/*	int res = 50;
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

std::shared_ptr<Region> Planet::getRegion(coordinate position){
	return regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH];
}

std::shared_ptr<Chunk> Planet::getChunk(coordinate position){
	std::shared_ptr<Region> region = regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH];
	if(region != nullptr){
		return region->getChunk(position);
	}
	return nullptr;
}

void Planet::loadRegion(coordinate position){
	if(regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH] == nullptr){
		regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH] = std::make_shared<Region>();
	}
}

void Planet::loadChunk(coordinate position){
	if(regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH] == nullptr){
		regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH] = std::make_shared<Region>();
	}
	regions[(position.r_x+WIDTH/2) + (position.r_y+WIDTH/2)*WIDTH]->loadChunk(position);
}

materialID_t Planet::getMaterialID(coordinate pixelPos, bool layer){
	if(regions[(pixelPos.r_x+WIDTH/2) + (pixelPos.r_y+WIDTH/2)*WIDTH] != nullptr){
		return regions[(pixelPos.r_x+WIDTH/2) + (pixelPos.r_y+WIDTH/2)*WIDTH]->getMaterialID(pixelPos, layer);
	}return 0;
}

void Planet::setMaterialID(coordinate pixelPos, bool layer, materialID_t id){
	if(regions[(pixelPos.r_x+WIDTH/2) + (pixelPos.r_y+WIDTH/2)*WIDTH] != nullptr){
		regions[(pixelPos.r_x+WIDTH/2) + (pixelPos.r_y+WIDTH/2)*WIDTH]->setMaterialID(pixelPos, layer, id);
	}
}

vec2 Planet::getGravity(coordinate pixelPos){
	if(abs(pixelPos.x) > abs(pixelPos.y)){
//		double dist = double(abs(pixelPos.y))/std::max(abs(pixelPos.x), 1);
//		if(dist < 0.9){
			return {std::copysign(g_earth, -pixelPos.x), 0.0};
//		}else{
//			return vec2{std::copysign(g_earth, -pixelPos.x), 0.0}.rotate(std::copysign(M_PI/4, pixelPos.y));
//		}
	}else{
//		double dist = double(abs(pixelPos.x))/std::max(abs(pixelPos.y), 1);
//		if(dist < 0.9){
			return {0.0, std::copysign(g_earth, -pixelPos.y)};
//		}else{
//			return vec2{0.0, std::copysign(g_earth, -pixelPos.y)}.rotate(std::copysign(M_PI/4, pixelPos.x));
//		}
	}
	//circular gravity
	//return vec2({-pixelPos.x, -pixelPos.y}).normalize() * g_earth;
}

} /* namespace Pixelverse */
