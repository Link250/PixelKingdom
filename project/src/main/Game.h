#ifndef MAIN_GAME_H_
#define MAIN_GAME_H_

#include "../utilities/glIncludes.h"
#include "../gfx/Screen.h"
#include "../map/Map.h"
#include "../entities/Player.h"
#include "../gui/Mouse.h"
#include "../gui/gamefields/GameField.h"
#include "../input/PlayerController.h"

#include <memory>
#include <functional>

namespace Pixelverse {

class Game {
public:
	static std::unique_ptr<Screen> screen;
	static std::unique_ptr<Map> map;
	static long updateCount;
	static int currentFPS;
	static void initialize();
	static void mainLoop();

	static void addGameField(shared_ptr<GameField> field);

	template <typename T> static T queueRessourceLoader(std::function<void()> loader){
		loaders.push_back(loader); return nullptr;
	}
private:
	static void errorCallback(int error, const char *description);
	static void update();
	static void render();

	static std::vector<shared_ptr<GameField>> gameFields;

	static std::vector<std::function<void()>> loaders;
};

} /* namespace Game */

#endif /* MAIN_GAME_H_ */
