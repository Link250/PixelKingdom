#include "Chunk.h"
#include "DummyChunk.h"
#include "Planet.h"
#include "../materials/Material.h"
#include "Biome.h"
#include "../main/Game.h"

#include <iostream>
#include <cmath>
#include <thread>

namespace Pixelverse {

Chunk::Chunk(): position(0x7fffffff, 0x7fffffff), localOffset(0), textureOverlay(0), lightValues(0), lightSources(0), loading(true){
	texture[0] = 0;
	texture[1] = 0;
	lightReduction[0] = 0;
	lightReduction[1] = 0;
	textureUpdate[0] = false;
	textureUpdate[1] = false;
}

Chunk::Chunk(coordinate position) : position(position.chunkCoordinate()), localOffset(position.rc_x() + position.rc_y()*3 - 4),
		textureOverlay(0), lightValues(0), lightSources(0), loading(true){
	texture[0] = 0;
	texture[1] = 0;
	lightReduction[0] = 0;
	lightReduction[1] = 0;
	textureUpdate[0] = false;
	textureUpdate[1] = false;
	for(int i = 0; i < 9; i++)local[i] = DummyChunk::instance;
	std::thread loader(&Chunk::load, this);
	loader.detach();
}

Chunk::~Chunk(){
	glDeleteTextures(2, &texture[0]);
	glDeleteTextures(1, &textureOverlay);
	glDeleteTextures(1, &lightValues);
	glDeleteTextures(1, &lightSources);
	glDeleteTextures(2, &lightReduction[0]);
}

void Chunk::update(){
	updates.reset();
}

bool Chunk::setMaterialUpdating(coordinate position, bool layer){
	bool temp = !updates[position.p_x + position.p_y*WIDTH + layer*SIZE];
	updates.set(position.p_x + position.p_y*WIDTH + layer*SIZE);
	return temp;
}

void Chunk::setLocalChunk(coordinate position, std::shared_ptr<Chunk> chunk){
	local[position.rc_y()*3+position.rc_x()-localOffset] = chunk;
}

materialID_t Chunk::getMaterialID(coordinate position, bool layer){
	return data[position.p_x + position.p_y*WIDTH + layer*SIZE];
}

void Chunk::setMaterialID(coordinate position, bool layer, materialID_t id){
	data[position.p_x + position.p_y*WIDTH + layer*SIZE] = id;
	textureUpdate[layer] = true;
	if(setMaterialUpdating(position, !layer))Game::map->addMaterialUpdate(position, !layer);
	if(setMaterialUpdating(position, layer))Game::map->addMaterialUpdate(position, layer);
	if(local[4 + 1*(position.p_x==255)]->setMaterialUpdating(position+coordinate(1,0), layer))Game::map->addMaterialUpdate(position+coordinate(1,0), layer);
	if(local[4 - 1*(position.p_x==0)]->setMaterialUpdating(position+coordinate(-1,0), layer))Game::map->addMaterialUpdate(position+coordinate(-1,0), layer);
	if(local[4 + 3*(position.p_y==255)]->setMaterialUpdating(position+coordinate(0,1), layer))Game::map->addMaterialUpdate(position+coordinate(0,1), layer);
	if(local[4 - 3*(position.p_y==0)]->setMaterialUpdating(position+coordinate(0,-1), layer))Game::map->addMaterialUpdate(position+coordinate(0,-1), layer);
}

std::shared_ptr<Chunk> Chunk::getLocalChunk(coordinate position){
	return local[position.rc_y()*3+position.rc_x()-localOffset];
}

bool Chunk::placeMaterial(coordinate position, bool layer, materialID_t id, std::shared_ptr<Player> player){
	//material.replacble for water etc.
	std::shared_ptr<Material> newMaterial = Material::get(id);
	if(getMaterialID(position, layer) == 0 && newMaterial->canPlaceAt(this, position, layer, player)){
		setMaterialID(position, layer, id);
		newMaterial->placeAt(this, position, layer, player);
		return true;
	}return false;
}

bool Chunk::breakMaterial(coordinate position, bool layer, std::shared_ptr<Player> player, const MiningTool *miningTool){
	std::shared_ptr<Material> currentMaterial = Material::get(getMaterialID(position, layer));
	if(currentMaterial->canBreakAt(this, position, layer, player, miningTool)){
		setMaterialID(position, layer, 0);
		currentMaterial->breakAt(this, position, layer, player, miningTool);
		return true;
	}return false;
}

coordinate Chunk::getPosition(){
	return position;
}

//#define WATCH_LOADING_CHUNKS
#ifdef WATCH_LOADING_CHUNKS
void Chunk::bindTexture(bool layer){
	if(texture[layer] == 0)genTexture(layer);
	if(textureUpdate[layer] || loading)updateTexture(layer);
	glBindTexture(GL_TEXTURE_2D, texture[layer]);
}
#else
void Chunk::bindTexture(bool layer){
	if(!loading)genTexture(layer);
	if(textureUpdate[layer])updateTexture(layer);
	glBindTexture(GL_TEXTURE_2D, texture[layer]);
}
#endif

void Chunk::bindLightTexture(){
	if(!loading)genLightTextures();
	glBindTexture(GL_TEXTURE_2D, lightValues);
}

void Chunk::genTexture(bool layer){
	if(texture[layer] == 0)glGenTextures(1, &texture[layer]);

	glBindTexture(GL_TEXTURE_2D, texture[layer]);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_R16UI, WIDTH, WIDTH, 0, GL_RED_INTEGER, GL_UNSIGNED_SHORT, &data[SIZE*layer]);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
}

void Chunk::updateTexture(bool layer){
	glBindTexture(GL_TEXTURE_2D, texture[layer]);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_R16UI, WIDTH, WIDTH, 0, GL_RED_INTEGER, GL_UNSIGNED_SHORT, &data[SIZE*layer]);
	textureUpdate[layer] = false;
}

void Chunk::genLightTextures(){
	if(lightValues == 0)glGenTextures(1, &lightValues);

	glBindTexture(GL_TEXTURE_2D, lightValues);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_R16UI, WIDTH, WIDTH, 0, GL_RED_INTEGER, GL_UNSIGNED_SHORT, &data[0]);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
}

void Chunk::load(){
	printf("chunk loading started at x = %i_%i_%i, y = %i_%i_%i\n", position.r_x, position.c_x, position.p_x, position.r_y, position.c_y, position.p_y);
	Biome::get("forest")->generate(position, data);
	printf("chunk loaded at x = %i_%i_%i, y = %i_%i_%i\n", position.r_x, position.c_x, position.p_x, position.r_y, position.c_y, position.p_y);
	loading = false;
	textureUpdate[0] = true;
	textureUpdate[1] = true;
}

} /* namespace Pixelverse */
