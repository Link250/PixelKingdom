#include "Pickaxe.h"
#include "../main/Game.h"
#include "../input/InputHandler.h"
#include "../gui/MaterialMinerMouse.h"

namespace Pixelverse {

Pickaxe::Pickaxe(std::string name,
		std::string displayName,
		std::string tooltip,
		int miningSize, int miningRange,
		float miningStrength, float miningTier,
		MiningType miningType):
				MiningTool(name, displayName, tooltip, miningSize, miningRange, miningStrength, miningTier, miningType){}

Pickaxe::~Pickaxe(){}

std::shared_ptr<Mouse> Pickaxe::holdItem(const std::shared_ptr<mouse_input> input, std::shared_ptr<Player> player){
	if(input->clicking && (input->button == GLFW_MOUSE_BUTTON_1 || input->button == GLFW_MOUSE_BUTTON_2)){
		coordinate mousePos = coordinate(InputHandler::getMouseMapPos() - (miningSize/2.0f - 0.5f)*MAP_SCALE);
		for(int x = 0; x < miningSize; ++x) {
			for(int y = 0; y < miningSize; ++y) {
				double r = miningSize*0.5;
				vec2 p = vec2(x, y) - r + 0.5;
				if((p.x*p.x)/(r*r) + (p.y*p.y)/(r*r) < 1){
					Game::map->breakMaterial(mousePos + coordinate(x, y), input->button == GLFW_MOUSE_BUTTON_1, player, this);
				}
			}
		}
	}
	return std::make_shared<MaterialMinerMouse>(miningSize);
}

} /* namespace Pixelverse */
