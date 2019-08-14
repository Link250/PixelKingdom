#ifndef UTILITIES_QUADTREE_H_
#define UTILITIES_QUADTREE_H_

#include <functional>

template<int depth, typename Type>
class quad_tree{
	static_assert(depth <= 8, "QuadTree depth cannot be greater than 8");
	static_assert(depth > 0, "QuadTree depth must be greater than 0");
private:
	quad_tree<depth-1, Type> *subtrees[4] = {nullptr};
public:
	virtual ~quad_tree(){
		for(size_t i = 0; i < 4; i++){
			if(subtrees[i]) delete subtrees[i];
		}
	}

	void setValue(char x, char y, Type i){
		size_t index = ((y >> (depth-2)) & 0b10) | ((x >> (depth-1))  & 0b1);
		if(subtrees[index] == nullptr)subtrees[index] = new quad_tree<depth-1, Type>();
		subtrees[index]->setValue(x, y, i);
	}

	Type getValue(char x, char y){
		size_t index = ((y >> (depth-2)) & 0b10) | ((x >> (depth-1))  & 0b1);
		if(subtrees[index] == nullptr)subtrees[index] = new quad_tree<depth-1, Type>();
		return subtrees[index]->getValue(x, y);
	}

	void forEach(std::function<void(Type)> func){
		for(auto tree : subtrees){
			if(tree != nullptr) tree->forEach(func);
		}
	}
};

template<typename Type>
class quad_tree<1, Type>{
	Type values[4];
	bool hasValue[4];
public:
	void setValue(char x, char y, Type i){
		size_t index = ((y << 1) & 0b10) | (x & 0b1);
		hasValue[index] = true;
		values[index] = i;
	}

	Type getValue(char x, char y){
		size_t index = ((y << 1) & 0b10) | (x & 0b1);
		if(hasValue[index])	return values[index];
		return 0;
	}

	void forEach(std::function<void(Type)> func){
		for(size_t i = 0; i < 4; i++){
			if(hasValue[i])func(values[i]);
		}
	}
};


#endif /* UTILITIES_QUADTREE_H_ */
