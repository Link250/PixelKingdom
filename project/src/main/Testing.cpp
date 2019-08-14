#include <iostream>
#include <fstream>
#include <sstream>
#include <glad/glad.h>
#include <GLFW/glfw3.h>
using namespace std;


/*struct vertex{
	struct {float x, y, z;}position;
	struct {float r, g, b;}color;
	struct {float u, v;}texture;
};

vertex vertices_cube[] = {
	{{-0.5f,-0.5f,-0.5f}, {0.0f, 0.0f, 0.0f}, {0.0f, 0.0f}},
	{{-0.5f,-0.5f, 0.5f}, {0.0f, 0.0f, 1.0f}, {1.0f, 0.0f}},
	{{-0.5f, 0.5f,-0.5f}, {0.0f, 1.0f, 0.0f}, {0.0f, 1.0f}},
	{{-0.5f, 0.5f, 0.5f}, {0.0f, 1.0f, 1.0f}, {1.0f, 1.0f}},
	{{ 0.5f,-0.5f,-0.5f}, {1.0f, 0.0f, 0.0f}, {1.0f, 0.0f}},
	{{ 0.5f,-0.5f, 0.5f}, {1.0f, 0.0f, 1.0f}, {0.0f, 0.0f}},
	{{ 0.5f, 0.5f,-0.5f}, {1.0f, 1.0f, 0.0f}, {1.0f, 1.0f}},
	{{ 0.5f, 0.5f, 0.5f}, {1.0f, 1.0f, 1.0f}, {0.0f, 1.0f}}
};

static const int indices_cube_lines[] = {
	0, 1,
	0, 2,
	0, 4,
	1, 3,
	1, 5,
	2, 3,
	2, 6,
	4, 5,
	4, 6,
	3, 7,
	5, 7,
	6, 7
};

static const int indices_cube_tris[] = {
	0, 1, 2,
	1, 2, 3,
	0, 1, 4,
	1, 4, 5,
	0, 2, 4,
	2, 4, 6,
	4, 5, 6,
	5, 6, 7,
	2, 3, 6,
	3, 6, 7,
	1, 3, 5,
	3, 5, 7
};

static bool button_UP = false,
			button_DOWN = false,
			button_LEFT = false,
			button_RIGHT = false;

static float zoom = 2.0f;

static float mouseX = 0.0f, mouseY = 0.0f;
static float pressX = 0.0f, pressY = 0.0f;

static bool press = false;

static float rotationX = 0.0f, rotationY = 0.0f;

void errorCallback(int error, const char *description)
{
	std::cerr << "GLFW error " << error << ": " << description << std::endl;
}

static void key_callback(GLFWwindow* window, int key, int scancode, int action, int mods) {
	if(action == GLFW_PRESS){
		if(key == GLFW_KEY_ESCAPE)
			glfwSetWindowShouldClose(window, GLFW_TRUE);
		if(key == GLFW_KEY_KP_SUBTRACT)
			zoom *= 15.0f/16.0f;
		if(key == GLFW_KEY_KP_ADD)
			zoom *= 16.0f/15.0f;
	}
	if(key == GLFW_KEY_W)
		button_UP = action > 0;
	if(key == GLFW_KEY_S)
		button_DOWN = action > 0;
	if(key == GLFW_KEY_A)
		button_LEFT = action > 0;
	if(key == GLFW_KEY_D)
		button_RIGHT = action > 0;
	if(key == GLFW_KEY_V && action == GLFW_PRESS && mods == GLFW_MOD_CONTROL)
		cout << glfwGetClipboardString(window) << "\n";
}

static void mouse_button_callback(GLFWwindow* window, int button, int action, int mods) {
	pressX = mouseX;
	pressY = mouseY;
	press = action;
}

static void mouse_scroll_callback(GLFWwindow* window, double xoffset, double yoffset){
	if(yoffset != 0.0){
		if(yoffset > 0.0){
			zoom *= 16.0f/15.0f;
		}else{
			zoom *= 15.0f/16.0f;
		}
	}
	std::cout << zoom << std::endl;
}

static void mouse_position_callback(GLFWwindow* window, double xpos, double ypos){
	mouseX = float(xpos);
	mouseY = float(ypos);
	if(press){
		rotationX += (mouseX-pressX)/512.0f;
		rotationY -= (mouseY-pressY)/512.0f;
		pressX = mouseX;
		pressY = mouseY;
	}
}

static GLuint loadTextureFile(string path) {
	std::vector<unsigned char> image;
	unsigned width, height;
	unsigned error = lodepng::decode(image, width, height, path);

	// If there's an error, display it.
	if (error != 0) {
		std::cout << "error " << error << ": " << lodepng_error_text(error)
				<< std::endl;
	}

	std::vector<unsigned char> image2(width * height * 4);
	for (size_t y = 0; y < height; y++)
	for (size_t x = 0; x < width; x++)
	for (size_t c = 0; c < 4; c++) {
		image2[4 * width * y + 4 * x + c] = image[4 * width * y + 4 * x + c];
	}

	// Create one OpenGL texture
	GLuint textureID;
	glGenTextures(1, &textureID);

	// "Bind" the newly created texture : all future texture functions will modify this texture
	glBindTexture(GL_TEXTURE_2D, textureID);

	// Give the image to OpenGL
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, &image2[0]);
	glGenerateMipmap(GL_TEXTURE_2D);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	return textureID;
}

int main(int argc, char **argv) {
	GLFWwindow* window;
	GLuint vertex_buffer, index_buffer, vertexArray, vertex_shader, fragment_shader, program, texture;
	GLint vpos_location, vcol_location, vtex_location, camPosLocation, rotationXLocation, rotationYLocation, textureLocation, projectionLocation;
	int width = 1024, height = 1024;

	glfwSetErrorCallback(errorCallback);
	if (!glfwInit()) {
		exit(EXIT_FAILURE);
	}
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

	window = glfwCreateWindow(width, height, "Simple example", NULL, NULL);
	if (!window) {
		glfwTerminate();
		exit(EXIT_FAILURE);
	}

	glfwSetKeyCallback(window, key_callback);
	glfwSetMouseButtonCallback(window, mouse_button_callback);
	glfwSetScrollCallback(window, mouse_scroll_callback);
	glfwSetCursorPosCallback(window, mouse_position_callback);

	glfwMakeContextCurrent(window);
	gladLoadGLLoader((GLADloadproc) glfwGetProcAddress);
	glfwSwapInterval(1);

	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LESS);

	glGenBuffers(1, &vertex_buffer);
	glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices_cube), vertices_cube, GL_STATIC_DRAW);

	glGenBuffers(1, &index_buffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index_buffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices_cube_tris), indices_cube_tris, GL_STATIC_DRAW);

	std::ifstream in_vert("./res/shader/main.vert");
	std::string contents_vert((std::istreambuf_iterator<char>(in_vert)), std::istreambuf_iterator<char>());
	const GLchar *source_vert = contents_vert.c_str();
	in_vert.close();

	vertex_shader = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vertex_shader, 1, &source_vert, NULL);
	glCompileShader(vertex_shader);

	GLint isCompiled = 0;
	glGetShaderiv(vertex_shader, GL_COMPILE_STATUS, &isCompiled);
	if(isCompiled == GL_FALSE)
	{
		GLint maxLength = 0;
		glGetShaderiv(vertex_shader, GL_INFO_LOG_LENGTH, &maxLength);

		// The maxLength includes the NULL character
		std::vector<GLchar> errorLog(maxLength);
		glGetShaderInfoLog(vertex_shader, maxLength, &maxLength, &errorLog[0]);

		// Provide the infolog in whatever manor you deem best.
		// Exit with failure.
		glDeleteShader(vertex_shader); // Don't leak the shader.
		cout << errorLog.data();
	}

	std::ifstream in_frag("./res/shader/main.frag");
	std::string contents_frag((std::istreambuf_iterator<char>(in_frag)), std::istreambuf_iterator<char>());
	const GLchar *source_frag = contents_frag.c_str();
	in_frag.close();

	fragment_shader = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fragment_shader, 1, &source_frag, NULL);
	glCompileShader(fragment_shader);

	isCompiled = 0;
	glGetShaderiv(fragment_shader, GL_COMPILE_STATUS, &isCompiled);
	if(isCompiled == GL_FALSE)
	{
		GLint maxLength = 0;
		glGetShaderiv(fragment_shader, GL_INFO_LOG_LENGTH, &maxLength);

		// The maxLength includes the NULL character
		std::vector<GLchar> errorLog(maxLength);
		glGetShaderInfoLog(fragment_shader, maxLength, &maxLength, &errorLog[0]);

		// Provide the infolog in whatever manor you deem best.
		// Exit with failure.
		glDeleteShader(fragment_shader); // Don't leak the shader.
		cout << errorLog.data();
	}

	program = glCreateProgram();
	glAttachShader(program, vertex_shader);
	glAttachShader(program, fragment_shader);
	glLinkProgram(program);

    glGenVertexArrays(1, &vertexArray);
    glBindVertexArray(vertexArray);
	vpos_location = glGetAttribLocation(program, "vPos");
	glEnableVertexAttribArray(vpos_location);
	glVertexAttribPointer(vpos_location, 3, GL_FLOAT, GL_FALSE, sizeof(vertex), (void*) 0);
	vcol_location = glGetAttribLocation(program, "vCol");
	glEnableVertexAttribArray(vcol_location);
	glVertexAttribPointer(vcol_location, 3, GL_FLOAT, GL_FALSE, sizeof(vertex), (void*) sizeof(vertex::position));
	vtex_location = glGetAttribLocation(program, "vTex");
	glEnableVertexAttribArray(vtex_location);
	glVertexAttribPointer(vtex_location, 2, GL_FLOAT, GL_FALSE, sizeof(vertex), (void*) (sizeof(vertex::position)+sizeof(vertex::color)));

	texture = loadTextureFile("./res/textures/cube.png");

	camPosLocation = glGetUniformLocation(program, "camPos");
	rotationXLocation = glGetUniformLocation(program, "rotationX");
	rotationYLocation = glGetUniformLocation(program, "rotationY");
	textureLocation = glGetUniformLocation(program, "mainTex");
	projectionLocation = glGetUniformLocation(program, "projection");

	float nearPlane = 0.001f, farPlane = 100.0f;
	float projection[] = {	1.0f/0.7f, 0.0f, 0.0f, 0.0f,
							0.0f, 1.0f/0.7f, 0.0f, 0.0f,
							0.0f, 0.0f, farPlane/(farPlane-nearPlane), 1.0f,
							0.0f, 0.0f, -(farPlane*nearPlane)/(farPlane-nearPlane), 0.0f};

	int frames = 0;
	double lastTime = 0;
	while (!glfwWindowShouldClose(window)) {
		glfwPollEvents();

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glUseProgram(program);
		glUniform3f(camPosLocation, -(mouseX/512.0f-1.0f)*2.0f, (mouseY/512.0f-1.0f)*2.0f, -zoom);
		glUniform1f(rotationXLocation, rotationX);
		glUniform1f(rotationYLocation, rotationY);
		glUniform1f(textureLocation, texture);
		glUniformMatrix4fv(projectionLocation, 1, GL_FALSE, (GLfloat*)projection);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index_buffer);
//		glDrawElements(GL_LINES, 24, GL_UNSIGNED_INT, (void*) 0);
		glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, (void*) 0);

		glfwSwapBuffers(window);

		frames++;
		if((glfwGetTime() - lastTime) >= 1.0){
			printf("FPS: %i\n", frames);
			fflush(stdout);
			lastTime = glfwGetTime();
			frames = 0;
		}
	}

	glfwDestroyWindow(window);
	glfwTerminate();
	exit(EXIT_SUCCESS);
}*/
