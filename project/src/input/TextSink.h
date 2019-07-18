#ifndef INPUT_TEXTSINK_H_
#define INPUT_TEXTSINK_H_

namespace Pixelverse {

class TextSink{
public:
	TextSink();
	virtual ~TextSink();

	void addCharacter(char c);
	void attach();
	void detach();
};

} /* namespace Pixelverse */

#endif /* INPUT_TEXTSINK_H_ */
