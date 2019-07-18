#ifndef GUI_GAMEFIELDS_ITEMFIELD_H_
#define GUI_GAMEFIELDS_ITEMFIELD_H_

#include "../../gfx/Texture.h"
#include "../../utilities/DataTypes.h"
#include "../../item/Item.h"

#include <memory>

namespace Pixelverse {

class ItemField{
public:
	ItemField(int2 position = {0, 0}, std::shared_ptr<Item> item = nullptr);
	virtual ~ItemField();
	void render();

	static constexpr int2 size = {36, 36};
	int2 position;

	std::shared_ptr<Item> item;
private:
	static std::shared_ptr<Texture> background;
};

} /* namespace Pixelverse */

#endif /* GUI_GAMEFIELDS_ITEMFIELD_H_ */
