#include "Game.h"
#include "../config/KeyConfig.h"
#include "../input/InputHandler.h"
#include "../gfx/Screen.h"
#include "../utilities/DataTypes.h"
#include "../materials/Material.h"
#include "../map/Biome.h"
#include "../item/Item.h"

#include <cmath>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <algorithm>

namespace Pixelverse {

std::unique_ptr<Map> Game::map;
std::vector<std::function<void()>> Game::loaders;
std::vector<shared_ptr<GameField>> Game::gameFields;
long Game::updateCount;
int Game::randTickNumber;
int Game::currentFPS;

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
	Screen::initialize(width, height);
	Item::loadList();
	Item::loadAllRessources();

	glfwSetKeyCallback(Screen::window, InputHandler::keyCallback);
	glfwSetCharCallback(Screen::window, InputHandler::charCallback);
	glfwSetMouseButtonCallback(Screen::window, InputHandler::mouseButtonCallback);
	glfwSetScrollCallback(Screen::window, InputHandler::mouseScrollCallback);
	glfwSetCursorPosCallback(Screen::window, InputHandler::mousePositionCallback);

	map = make_unique<Map>();
	int heightOffset = Planet::mainNoise.GetValue(0, 1)*Planet::surfaceVariation;
	printf("Player Height Offset: %i\n", heightOffset);
	std::shared_ptr<Player> player = make_shared<Player>(vec2({0.0, double((heightOffset + Planet::surfaceHeight + 20) * MAP_SCALE)}));
	player->load();
	map->entities.push_back(player);
	InputHandler::playerController = std::make_unique<PlayerController>(player);


	std::cout << "loading queued ressources" << std::endl;
	for(std::function<void()> loader : loaders){
		loader();
	}
}

void Game::mainLoop(){
	int frames = 0;
	double lastTime = 0;
	updateCount = 0;

	while (!glfwWindowShouldClose(Screen::window)) {

		randTickNumber = rand();
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

	Screen::unload();
	exit(EXIT_SUCCESS);
}

void Game::update(){
	glfwPollEvents();

	//TODO std::rotate
	for(auto itt = gameFields.begin(); itt != gameFields.end(); itt++){
		if((*itt)->active){
			(*itt)->update();
		}else{
			gameFields.erase(itt--);
		}
	}

	if(InputHandler::isMouseInputAvailable()){
		auto itt = gameFields.begin();
		for(; itt != gameFields.end(); itt++){
			if((*itt)->getArea().contains(int2(InputHandler::getMousePos()))){
				InputHandler::setMouseInputUser(std::static_pointer_cast<MouseInputUser>(*itt));
				break;
			}
		}
		if(itt != gameFields.end())
			std::rotate(gameFields.begin(), itt, itt+1);
	}

	map->update();

	InputHandler::playerController->update();

	InputHandler::useMouseInput();

	Screen::update();
}

void Game::render(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	map->render();

	if(InputHandler::keyPressed(KeyConfig::INFO)){
		const std::shared_ptr<Player> player = InputHandler::playerController->getPlayer();
		coordinate playerCoords(player->position);
		std::string infoText =
				"FPS:" + std::to_string(currentFPS) + "\n\n" +
				"x:" + std::to_string(player->position.x) + "\n" +
				"x_r:" + std::to_string(playerCoords.r_x) + " x_c:" + std::to_string(playerCoords.c_x) + " x_p:" + std::to_string(playerCoords.p_x) + "\n" +
				"y:" + std::to_string(player->position.y) + "\n" +
				"y_r:" + std::to_string(playerCoords.r_y) + " y_c:" + std::to_string(playerCoords.c_y) + " y_p:" + std::to_string(playerCoords.p_y) + "\n\n" +
				"m_f_name:" + map->getMaterial(coordinate(InputHandler::getMouseMapPos()), true)->name + "\n"
				"m_b_name:" + map->getMaterial(coordinate(InputHandler::getMouseMapPos()), false)->name;
		Screen::mainFont->render(infoText, vec2{10, 10});
	}

	for(auto itt = gameFields.rbegin(); itt != gameFields.rend(); itt++){
		(*itt)->render();
	}

	InputHandler::renderMouse();

	glfwSwapBuffers(Screen::window);
}

void Game::addGameField(shared_ptr<GameField> field){
	field->active = true;
	field->focus = true;
	gameFields.push_back(field);
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
