package main;

import static org.lwjgl.glfw.GLFW.*;
import java.util.HashMap;
import java.util.Map;

import main.ConfigFile.NoConfigFoundException;

public class KeyConfig {

	public static Map<Integer, Keys> keyMapping = new HashMap<>();
	private static ConfigFile configFile = new ConfigFile(Game.GAME_PATH+"Keys.optn");
	
	public static void setDefaultValues() {
		keyMapping.put(GLFW_KEY_W,Keys.UP);
		keyMapping.put(GLFW_KEY_S,Keys.DOWN);
		keyMapping.put(GLFW_KEY_A,Keys.LEFT);
		keyMapping.put(GLFW_KEY_D,Keys.RIGHT);
		keyMapping.put(GLFW_KEY_SPACE,Keys.JUMP);
		keyMapping.put(GLFW_KEY_LEFT_SHIFT,Keys.CROUCH);
		keyMapping.put(GLFW_KEY_ESCAPE,Keys.MENU);
		keyMapping.put(GLFW_KEY_H,Keys.HOTBAR);
		keyMapping.put(GLFW_KEY_I,Keys.INV);
		keyMapping.put(GLFW_KEY_C,Keys.CRAFT);
		keyMapping.put(GLFW_KEY_E,Keys.EQUIP);
		keyMapping.put(GLFW_KEY_F3,Keys.DEBUGINFO);
		keyMapping.put(GLFW_KEY_F5,Keys.DEBUGMODE);
		keyMapping.put(GLFW_KEY_X,Keys.DEBUGPXL);
		//TODO don't override already existing Configs (override only the broken ones)
		save();
	}
	
	public static void load(){
		configFile.load();
		try{
			for (Keys key : Keys.values()) {
				keyMapping.put(configFile.getConfig(key.toString()), key);
			}
		}catch(ClassCastException | NoConfigFoundException e){
			Game.logInfo("New Key Configs created ");
			setDefaultValues();
		}
	}
	
	public static void save(){
		keyMapping.forEach((i, key) -> configFile.setConfig(key.toString(), i));
		configFile.save();
	}
	
	public static String getKeyName(int keyCode) {
		switch(keyCode) {
		case GLFW_KEY_SPACE         :return "SPACE";
		case GLFW_KEY_APOSTROPHE    :return "APOSTROPHE";
		case GLFW_KEY_COMMA         :return "COMMA";
		case GLFW_KEY_MINUS         :return "MINUS";
		case GLFW_KEY_PERIOD        :return "PERIOD";
		case GLFW_KEY_SLASH         :return "SLASH";
		case GLFW_KEY_0             :return "0";
		case GLFW_KEY_1             :return "1";
		case GLFW_KEY_2             :return "2";
		case GLFW_KEY_3             :return "3";
		case GLFW_KEY_4             :return "4";
		case GLFW_KEY_5             :return "5";
		case GLFW_KEY_6             :return "6";
		case GLFW_KEY_7             :return "7";
		case GLFW_KEY_8             :return "8";
		case GLFW_KEY_9             :return "9";
		case GLFW_KEY_SEMICOLON     :return "SEMICOLON";
		case GLFW_KEY_EQUAL         :return "EQUAL";
		case GLFW_KEY_A             :return "A";
		case GLFW_KEY_B             :return "B";
		case GLFW_KEY_C             :return "C";
		case GLFW_KEY_D             :return "D";
		case GLFW_KEY_E             :return "E";
		case GLFW_KEY_F             :return "F";
		case GLFW_KEY_G             :return "G";
		case GLFW_KEY_H             :return "H";
		case GLFW_KEY_I             :return "I";
		case GLFW_KEY_J             :return "J";
		case GLFW_KEY_K             :return "K";
		case GLFW_KEY_L             :return "L";
		case GLFW_KEY_M             :return "M";
		case GLFW_KEY_N             :return "N";
		case GLFW_KEY_O             :return "O";
		case GLFW_KEY_P             :return "P";
		case GLFW_KEY_Q             :return "Q";
		case GLFW_KEY_R             :return "R";
		case GLFW_KEY_S             :return "S";
		case GLFW_KEY_T             :return "T";
		case GLFW_KEY_U             :return "U";
		case GLFW_KEY_V             :return "V";
		case GLFW_KEY_W             :return "W";
		case GLFW_KEY_X             :return "X";
		case GLFW_KEY_Y             :return "Y";
		case GLFW_KEY_Z             :return "Z";
		case GLFW_KEY_LEFT_BRACKET  :return "LEFT BRACKET";
		case GLFW_KEY_BACKSLASH     :return "BACKSLASH";
		case GLFW_KEY_RIGHT_BRACKET :return "RIGHT BRACKET";
		case GLFW_KEY_GRAVE_ACCENT  :return "GRAVE ACCENT";
		case GLFW_KEY_WORLD_1       :return "WORLD 1";
		case GLFW_KEY_WORLD_2       :return "WORLD 2";
		case GLFW_KEY_ESCAPE        :return "ESCAPE";
		case GLFW_KEY_ENTER         :return "ENTER";
		case GLFW_KEY_TAB           :return "TAB";
		case GLFW_KEY_BACKSPACE     :return "BACKSPACE";
		case GLFW_KEY_INSERT        :return "INSERT";
		case GLFW_KEY_DELETE        :return "DELETE";
		case GLFW_KEY_RIGHT         :return "RIGHT";
		case GLFW_KEY_LEFT          :return "LEFT";
		case GLFW_KEY_DOWN          :return "DOWN";
		case GLFW_KEY_UP            :return "UP";
		case GLFW_KEY_PAGE_UP       :return "PAGE UP";
		case GLFW_KEY_PAGE_DOWN     :return "PAGE DOWN";
		case GLFW_KEY_HOME          :return "HOME";
		case GLFW_KEY_END           :return "END";
		case GLFW_KEY_CAPS_LOCK     :return "CAPS LOCK";
		case GLFW_KEY_SCROLL_LOCK   :return "SCROLL LOCK";
		case GLFW_KEY_NUM_LOCK      :return "NUM LOCK";
		case GLFW_KEY_PRINT_SCREEN  :return "PRINT SCREEN";
		case GLFW_KEY_PAUSE         :return "PAUSE";
		case GLFW_KEY_F1            :return "F1";
		case GLFW_KEY_F2            :return "F2";
		case GLFW_KEY_F3            :return "F3";
		case GLFW_KEY_F4            :return "F4";
		case GLFW_KEY_F5            :return "F5";
		case GLFW_KEY_F6            :return "F6";
		case GLFW_KEY_F7            :return "F7";
		case GLFW_KEY_F8            :return "F8";
		case GLFW_KEY_F9            :return "F9";
		case GLFW_KEY_F10           :return "F10";
		case GLFW_KEY_F11           :return "F11";
		case GLFW_KEY_F12           :return "F12";
		case GLFW_KEY_F13           :return "F13";
		case GLFW_KEY_F14           :return "F14";
		case GLFW_KEY_F15           :return "F15";
		case GLFW_KEY_F16           :return "F16";
		case GLFW_KEY_F17           :return "F17";
		case GLFW_KEY_F18           :return "F18";
		case GLFW_KEY_F19           :return "F19";
		case GLFW_KEY_F20           :return "F20";
		case GLFW_KEY_F21           :return "F21";
		case GLFW_KEY_F22           :return "F22";
		case GLFW_KEY_F23           :return "F23";
		case GLFW_KEY_F24           :return "F24";
		case GLFW_KEY_F25           :return "F25";
		case GLFW_KEY_KP_0          :return "NUM 0";
		case GLFW_KEY_KP_1          :return "NUM 1";
		case GLFW_KEY_KP_2          :return "NUM 2";
		case GLFW_KEY_KP_3          :return "NUM 3";
		case GLFW_KEY_KP_4          :return "NUM 4";
		case GLFW_KEY_KP_5          :return "NUM 5";
		case GLFW_KEY_KP_6          :return "NUM 6";
		case GLFW_KEY_KP_7          :return "NUM 7";
		case GLFW_KEY_KP_8          :return "NUM 8";
		case GLFW_KEY_KP_9          :return "NUM 9";
		case GLFW_KEY_KP_DECIMAL    :return "NUM DECIMAL";
		case GLFW_KEY_KP_DIVIDE     :return "NUM DIVIDE";
		case GLFW_KEY_KP_MULTIPLY   :return "NUM MULTIPLY";
		case GLFW_KEY_KP_SUBTRACT   :return "NUM SUBTRACT";
		case GLFW_KEY_KP_ADD        :return "NUM ADD";
		case GLFW_KEY_KP_ENTER      :return "NUM ENTER";
		case GLFW_KEY_KP_EQUAL      :return "NUM EQUAL";
		case GLFW_KEY_LEFT_SHIFT    :return "LEFT SHIFT";
		case GLFW_KEY_LEFT_CONTROL  :return "LEFT CONTROL";
		case GLFW_KEY_LEFT_ALT      :return "LEFT ALT";
		case GLFW_KEY_LEFT_SUPER    :return "LEFT SUPER";
		case GLFW_KEY_RIGHT_SHIFT   :return "RIGHT SHIFT";
		case GLFW_KEY_RIGHT_CONTROL :return "RIGHT CONTROL";
		case GLFW_KEY_RIGHT_ALT     :return "RIGHT ALT";
		case GLFW_KEY_RIGHT_SUPER   :return "RIGHT SUPER";
		case GLFW_KEY_MENU          :return "MENU";
		default : return null;
		}
	}
}
