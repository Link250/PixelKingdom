#include "Game.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "../config/KeyConfig.h"
#include "../input/InputHandler.h"
#include "../gfx/Screen.h"
#include <cmath>
#include "../utilities/FastNoise.h"
#include <vector>
#include "../utilities/DataTypes.h"
#include "../materials/Material.h"
#include "../map/Biome.h"
#include "../item/Item.h"

namespace Pixelverse {

std::unique_ptr<Screen> Game::screen;
std::unique_ptr<Map> Game::map;
std::unique_ptr<Mouse> Game::mouse;
long Game::updateCount;
int Game::currentFPS;
std::vector<std::function<void()>> Game::loaders;

void Game::errorCallback(int error, const char *description){
	cerr << "GLFW error " << error << ": " << description << endl;
}

void Game::initialize(){
	int width = 1024, height = 512;

	glfwSetErrorCallback(errorCallback);
	if (!glfwInit()) {
		exit(EXIT_FAILURE);
	}

	Material::loadList();
	Biome::loadList();

	screen = make_unique<Screen>(width, height);

	Item::loadList();

	mouse = make_unique<Mouse>();

	glfwSetKeyCallback(screen->window, InputHandler::keyCallback);
	glfwSetCharCallback(screen->window, InputHandler::charCallback);
	glfwSetMouseButtonCallback(screen->window, InputHandler::mouseButtonCallback);
	glfwSetScrollCallback(screen->window, InputHandler::mouseScrollCallback);
	glfwSetCursorPosCallback(screen->window, InputHandler::mousePositionCallback);

	map = make_unique<Map>();

	std::cout << "loading queued ressources" << std::endl;
	for(std::function<void()> loader : loaders){
		loader();
	}
}

void Game::mainLoop(){
	int frames = 0;
	double lastTime = 0;
	updateCount = 0;

	while (!glfwWindowShouldClose(screen->window)) {

		update();
		updateCount++;

		render();

		frames++;
		if((glfwGetTime() - lastTime) >= 1.0){
			currentFPS = frames;
			fflush(stdout);
			lastTime = glfwGetTime();
			frames = 0;
		}
	}

	exit(EXIT_SUCCESS);
}

void Game::update(){
	glfwPollEvents();

	map->update();

	if(InputHandler::scrollY != 0.0){
		if(InputHandler::scrollY > 0){
			if(screen->zoom_target < 8)
				screen->zoom_target *= 2;
		}else{
			if(screen->zoom_target > 0.125)
				screen->zoom_target /= 2;
		}
		InputHandler::scrollY = 0;
	}

	screen->update();

	mouse->update();

/*	if(InputHandler::buttonINFO){
		shared_ptr<Material> m = map->getMaterial(coordinate(mouse->mapPos), true);
		std::cout << m->name << std::endl;
	}*/
}

void Game::render(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	map->render();

	if(InputHandler::keyPressed(KeyConfig::INFO)){
		coordinate playerCoords(map->player->position);
		std::string infoText =
				"FPS:" + std::to_string(currentFPS) + "\n\n" +
				"x:" + std::to_string(map->player->position.x) + "\n" +
				"x_r:" + std::to_string(playerCoords.r_x) + " x_c:" + std::to_string(playerCoords.c_x) + " x_p:" + std::to_string(playerCoords.p_x) + "\n" +
				"y:" + std::to_string(map->player->position.y) + "\n" +
				"y_r:" + std::to_string(playerCoords.r_y) + " y_c:" + std::to_string(playerCoords.c_y) + " y_p:" + std::to_string(playerCoords.p_y) + "\n\n" +
				"m_f_name:" + map->getMaterial(coordinate(mouse->mapPos), true)->name + "\n"
				"m_b_name:" + map->getMaterial(coordinate(mouse->mapPos), false)->name;
		screen->mainFont->render(infoText, vec2{10, 10});
	}

	mouse->render();

	glfwSwapBuffers(screen->window);
}

} /* namespace Game */

/*struct UDS{
	virtual void load() = 0;
	virtual ~UDS(){}
};

struct HeatUDS: public virtual UDS{
	int heat;
	void load(){
		heat = 1337;
	}
};

struct ColdUDS: public virtual UDS{
	int cold;
	void load(){
		cold = 666;
	}
};

struct TempUDS: public HeatUDS, public ColdUDS{
	void load(){
		HeatUDS::load();
		ColdUDS::load();
	}
};*/

int main(int argc, char **argv) {
//	shared_ptr<UDS> uds = make_shared<TempUDS>();
//	shared_ptr<HeatUDS> heatUDS = dynamic_pointer_cast<HeatUDS>(uds);
//	uds->load();
//	printf("%i", heatUDS->heat);
	Pixelverse::Game::initialize();
	Pixelverse::Game::mainLoop();
}
