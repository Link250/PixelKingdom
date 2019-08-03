#include "TemplateItem.h"

namespace Pixelverse {

itemID_t TemplateItem::id = Item::registerItem(std::make_shared<TemplateItem>(), &id);

TemplateItem::TemplateItem() : Item("template_item", "TemplateItem", "Tooltip of this Item", itemType, maxStackSize){}

itemID_t TemplateItem::getID(){return id;}

std::shared_ptr<Item> TemplateItem::newInstance(){
	return std::make_shared<TemplateItem>();
}

} /* namespace Pixelverse */
