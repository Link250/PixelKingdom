#include "Screen.h"
#include "../main/Game.h"
#include "../input/InputHandler.h"
#include "../config/KeyConfig.h"

namespace Pixelverse {

GLFWwindow* Screen::window;
double Screen::zoom;
double Screen::zoom_target;
double Screen::rotation;
double Screen::rotation_target;
vec2 Screen::center;
int Screen::width, Screen::height;
std::unique_ptr<Font> Screen::mainFont;
std::unique_ptr<Font> Screen::stackFont;
std::unique_ptr<Shader> Screen::baseShader, Screen::spriteShader, Screen::guiShader, Screen::terrainShader;
std::unique_ptr<Model> Screen::spriteModel;
std::unique_ptr<MaterialTexture> Screen::matTexture;


Screen::Screen(){}

Screen::~Screen(){}

void Screen::initialize(int width, int height){
	Screen::width = width;
	Screen::height = height;
	zoom = 1.0;
	zoom_target = 1.0;
	rotation = 0;
	rotation_target = 0;

	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

	window = glfwCreateWindow(width, height, "Pixelverse", NULL, NULL);
	if (!window) {
		glfwTerminate();
		exit(EXIT_FAILURE);
	}

	glfwMakeContextCurrent(window);
	gladLoadGLLoader((GLADloadproc) glfwGetProcAddress);
	glfwSwapInterval(1);

	glfwSetFramebufferSizeCallback(window, Pixelverse::Screen::framebufferSizeCallback);

	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

	Model::vertex vertices[] = {
		{{-1.0f,-1.0f}, {0.0f, 0.0f}},
		{{ 1.0f,-1.0f}, {1.0f, 0.0f}},
		{{-1.0f, 1.0f}, {0.0f, 1.0f}},
		{{ 1.0f, 1.0f}, {1.0f, 1.0f}}
	};

	static int indices[] = {
		0, 1, 2,
		1, 2, 3
	};

	mainFont = make_unique<Font>("PCSenior", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 !\"#$%&'()*+,-./:;<=>?@[\\]^_{}|°", 16, 16);
	stackFont = make_unique<Font>("stack_font", "1234567890", 6, 8);

	spriteModel = make_unique<Model>(vertices, 4, indices, 6);

	matTexture = make_unique<MaterialTexture>();

	baseShader = make_unique<Shader>("simple", vector<string>({"mainTex", "resolution", "translation", "size", "spriteRotation", "screenRotation"}));
	spriteShader = make_unique<Shader>("sprite_sheet", vector<string>({"mainTex", "resolution", "translation", "size", "spriteRotation", "screenRotation", "spriteResolution", "spriteOffset"}));
	guiShader = make_unique<Shader>("simple_gui", vector<string>({"mainTex", "resolution", "translation", "size", "spriteRotation", "spriteResolution", "spriteOffset"}));
	terrainShader = make_unique<Shader>("terrain", vector<string>({"materialID", "textures", "resolution", "translation", "size", "spriteRotation", "screenRotation", "layer"}));
}

void Screen::unload(){
	glfwDestroyWindow(window);
	glfwTerminate();
}

void Screen::update(){
	zoom += (zoom_target-zoom)*0.25;
	rotation += (rotation_target-rotation)*0.2;
}

void Screen::framebufferSizeCallback(GLFWwindow* window, int width, int height){
    glViewport(0, 0, width, height);
    Screen::width = width;
    Screen::height = height;
}

void Screen::setCenter(vec2 center){
	Screen::center = center;//vec2({double(int(center.x*zoom))/zoom, double(int(center.y*zoom))/zoom});
}

void Screen::renderGameTexture(shared_ptr<Texture> texture, vec2 position, vec2 scale, float rotation, bool centered){
	baseShader->bind();
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, texture->textureID);
	glUniform1i(baseShader->uniforms["mainTex"], 0);
	glUniform2f(baseShader->uniforms["resolution"], width/zoom, height/zoom);
	glUniform1f(baseShader->uniforms["screenRotation"], Screen::rotation);
	if(centered){
		glUniform2f(baseShader->uniforms["translation"], position.x - center.x, position.y - center.y);
	}else{
		glUniform2f(baseShader->uniforms["translation"], position.x + texture->width/2.0f - center.x, position.y + texture->height/2.0f - center.y);
	}
	glUniform2f(baseShader->uniforms["size"], texture->width * scale.x, texture->height * scale.y);
	glUniform1f(baseShader->uniforms["spriteRotation"], -rotation);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, spriteModel->indexBuffer);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
}

void Screen::renderGameSprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale, float rotation, bool centered){
	spriteShader->bind();
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, spriteSheet->texture.textureID);
	glUniform1i(spriteShader->uniforms["mainTex"], 0);
	glUniform2f(spriteShader->uniforms["resolution"], width/zoom, height/zoom);
	glUniform1f(spriteShader->uniforms["screenRotation"], Screen::rotation);
	if(centered){
		glUniform2f(spriteShader->uniforms["translation"], position.x - center.x, position.y - center.y);
	}else{
		glUniform2f(spriteShader->uniforms["translation"], position.x + spriteSheet->spriteWidth/2.0f - center.x, position.y + spriteSheet->spriteHeight/2.0f - center.y);
	}
	glUniform2f(spriteShader->uniforms["size"], spriteSheet->spriteWidth * scale.x, spriteSheet->spriteHeight * scale.y);
	glUniform1f(spriteShader->uniforms["spriteRotation"], -rotation);

	glUniform2f(spriteShader->uniforms["spriteResolution"], spriteSheet->spriteWidth / (double)spriteSheet->texture.width, spriteSheet->spriteHeight / (double)spriteSheet->texture.height);
	glUniform2f(spriteShader->uniforms["spriteOffset"], tile%spriteSheet->sheetWidth, tile/spriteSheet->sheetWidth);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, spriteModel->indexBuffer);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
}

void Screen::renderGUITexture(shared_ptr<Texture> texture, vec2 position, vec2 scale, float rotation, bool centered){
	guiShader->bind();
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, texture->textureID);
	glUniform1i(guiShader->uniforms["mainTex"], 0);
	glUniform2f(guiShader->uniforms["resolution"], width, height);
	if(centered){
		glUniform2f(guiShader->uniforms["translation"], position.x, position.y);
	}else{
		glUniform2f(guiShader->uniforms["translation"], position.x + texture->width/2.0f, position.y + texture->height/2.0f);
	}
	glUniform2f(guiShader->uniforms["size"], texture->width * scale.x, texture->height * scale.y);
	glUniform1f(guiShader->uniforms["spriteRotation"], -rotation);

	glUniform2f(guiShader->uniforms["spriteResolution"], 1.0f, 1.0f);
	glUniform2f(guiShader->uniforms["spriteOffset"], 0, 0);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, spriteModel->indexBuffer);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
}

void Screen::renderGUISprite(shared_ptr<SpriteSheet> spriteSheet, int tile, vec2 position, vec2 scale, float rotation, bool centered){
	guiShader->bind();
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, spriteSheet->texture.textureID);
	glUniform1i(guiShader->uniforms["mainTex"], 0);
	glUniform2f(guiShader->uniforms["resolution"], width, height);
	if(centered){
		glUniform2f(guiShader->uniforms["translation"], position.x, position.y);
	}else{
		glUniform2f(guiShader->uniforms["translation"], position.x + spriteSheet->spriteWidth/2.0f, position.y + spriteSheet->spriteHeight/2.0f);
	}
	glUniform2f(guiShader->uniforms["size"], spriteSheet->spriteWidth * scale.x, spriteSheet->spriteHeight * scale.y);
	glUniform1f(guiShader->uniforms["spriteRotation"], -rotation);

	glUniform2f(guiShader->uniforms["spriteResolution"], spriteSheet->spriteWidth / (double)spriteSheet->texture.width, spriteSheet->spriteHeight / (double)spriteSheet->texture.height);
	glUniform2f(guiShader->uniforms["spriteOffset"], tile%spriteSheet->sheetWidth, tile/spriteSheet->sheetWidth);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, spriteModel->indexBuffer);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
}

void Screen::renderPlanet(shared_ptr<Planet> planet){
	terrainShader->bind();
	glUniform1i(terrainShader->uniforms["materialID"], 0);
	glUniform1i(terrainShader->uniforms["textures"], 1);
	glUniform2f(terrainShader->uniforms["resolution"], width/zoom, height/zoom);
	glUniform1f(terrainShader->uniforms["screenRotation"], Screen::rotation);
	glUniform2f(terrainShader->uniforms["size"], Chunk::WIDTH * MAP_SCALE, Chunk::WIDTH * MAP_SCALE);
	glUniform1f(terrainShader->uniforms["spriteRotation"], 0);
	glUniform1i(terrainShader->uniforms["layer"], false);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, spriteModel->indexBuffer);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D_ARRAY, matTexture->textureID);
	glActiveTexture(GL_TEXTURE0);

	vec2 upLeft = center - vec2{-width/2/zoom, -height/2/zoom}.rotate(rotation),
		 upRight = center - vec2{width/2/zoom, -height/2/zoom}.rotate(rotation),
		 downLeft = center - vec2{-width/2/zoom, height/2/zoom}.rotate(rotation),
		 downRight= center - vec2{width/2/zoom, height/2/zoom}.rotate(rotation);
	coordinate minPos = coordinate(vec2{min(min(min(upLeft.x, upRight.x), downLeft.x), downRight.x),
									min(min(min(upLeft.y, upRight.y), downLeft.y), downRight.y)});
	coordinate maxPos = coordinate(vec2{max(max(max(upLeft.x, upRight.x), downLeft.x), downRight.x),
									max(max(max(upLeft.y, upRight.y), downLeft.y), downRight.y)});
	coordinate chunkPos;
	//printf("pos= %i, %i, %i, %i\n", minPos.x, minPos.y, maxPos.x, maxPos.y);
	//printf("cpos= %i, %i, %i, %i\n", minPos.rc_x(), minPos.rc_y(), maxPos.rc_x(), maxPos.rc_y());
	//TODO optimieren (nur sichtbare anzeigen)
	int chunksDrawn;
	for(chunkPos.y = minPos.y; chunkPos.rc_y() <= maxPos.rc_y(); chunkPos.y += Chunk::WIDTH){
		for(chunkPos.x = minPos.x; chunkPos.rc_x() <= maxPos.rc_x(); chunkPos.x += Chunk::WIDTH){
			shared_ptr<Chunk> chunk = planet->getChunk(chunkPos);
			if(chunk == nullptr) {
				planet->loadChunk(chunkPos);
				continue;
			}
			chunk->bindTexture(false);
			glUniform2f(terrainShader->uniforms["translation"], chunk->getPosition().x*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.x, chunk->getPosition().y*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.y);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
			chunksDrawn++;
		}
	}

	glUniform1i(terrainShader->uniforms["layer"], true);
	for(chunkPos.y = minPos.y; chunkPos.rc_y() <= maxPos.rc_y(); chunkPos.y += Chunk::WIDTH){
		for(chunkPos.x = minPos.x; chunkPos.rc_x() <= maxPos.rc_x(); chunkPos.x += Chunk::WIDTH){
			shared_ptr<Chunk> chunk = planet->getChunk(chunkPos);
			chunk->bindTexture(true);
			glUniform2f(terrainShader->uniforms["translation"], chunk->getPosition().x*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.x, chunk->getPosition().y*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.y);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
			chunksDrawn++;
		}
	}

	if(InputHandler::keyPressed(KeyConfig::INFO)){
		baseShader->bind();
		glUniform1i(baseShader->uniforms["mainTex"], 0);
		glUniform2f(baseShader->uniforms["resolution"], width/zoom, height/zoom);
		glUniform1f(baseShader->uniforms["screenRotation"], Screen::rotation);
		glUniform2f(baseShader->uniforms["size"], Chunk::WIDTH * MAP_SCALE, Chunk::WIDTH * MAP_SCALE);
		glUniform1f(baseShader->uniforms["spriteRotation"], 0);

		for(chunkPos.y = minPos.y; chunkPos.rc_y() <= maxPos.rc_y(); chunkPos.y += Chunk::WIDTH){
			for(chunkPos.x = minPos.x; chunkPos.rc_x() <= maxPos.rc_x(); chunkPos.x += Chunk::WIDTH){
				shared_ptr<Chunk> chunk = planet->getChunk(chunkPos);
				chunk->bindOverlay();
				glUniform2f(baseShader->uniforms["translation"], chunk->getPosition().x*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.x, chunk->getPosition().y*MAP_SCALE + Chunk::WIDTH/2*MAP_SCALE - center.y);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);
				chunksDrawn++;
			}
		}
		//printf("chunksDrawn: %i\n", chunksDrawn);
	}
}

} /* namespace Pixelverse */
