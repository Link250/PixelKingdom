#ifndef GFX_SCREEN_H_
#define GFX_SCREEN_H_

#include "Texture.h"
#include "SpriteSheet.h"
#include "MaterialTexture.h"
#include "Model.h"
#include "Shader.h"
#include "../utilities/DataTypes.h"
#include "../gui/Font.h"
#include "../map/Planet.h"

namespace Pixelverse {

class Screen{
public:
	Screen();
	virtual ~Screen();
	static void initialize(int width, int height);
	static void unload();
	static void setCenter(vec2 center);
	static void changeTargetZoom(double factor);
	static void setTargetRotation(double rotation);
	static vec2 getCenter();
	static double getZoom();
	static double getRotation();
	static void renderGameTexture(shared_ptr<Texture> texture, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	static void renderGameSprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	static void renderGUITexture(shared_ptr<Texture> texture, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	static void renderGUISprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	static void renderPlanet(shared_ptr<Planet> planet);
	static void update();
	static GLFWwindow* window;
	static int width, height;
	static std::unique_ptr<Font> mainFont;
	static std::unique_ptr<Font> stackFont;
private:
	static double zoom;
	static double zoom_target;
	static double rotation;
	static double rotation_target;
	static vec2 center;
	static std::unique_ptr<Shader> baseShader, spriteShader, guiShader, terrainShader;
	static std::unique_ptr<Model> spriteModel;
	static std::unique_ptr<MaterialTexture> matTexture;
	static void framebufferSizeCallback(GLFWwindow* window, int width, int height);
};

} /* namespace Pixelverse */

#endif /* GFX_SCREEN_H_ */
