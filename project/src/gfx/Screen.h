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
	GLFWwindow* window;
	Screen(int width, int height);
	virtual ~Screen();
	void setCenter(vec2 center);
	void drawGameTexture(shared_ptr<Texture> texture, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	void drawGameSprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	void drawGUITexture(shared_ptr<Texture> texture, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	void drawGUISprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale = {1, 1}, float rotation = 0.0f, bool centered = true);
	void drawPlanet(shared_ptr<Planet> planet);
	void update();
	double zoom;
	double zoom_target;
	double rotation;
	double rotation_target;
	vec2 center;
	int width, height;
	std::unique_ptr<Font> mainFont;
private:
	std::unique_ptr<Shader> baseShader, spriteShader, guiShader, terrainShader;
	std::unique_ptr<Model> spriteModel;
	std::unique_ptr<MaterialTexture> matTexture;
	static void framebufferSizeCallback(GLFWwindow* window, int width, int height);
};

} /* namespace Pixelverse */

#endif /* GFX_SCREEN_H_ */
