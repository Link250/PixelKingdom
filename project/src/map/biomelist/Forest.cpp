#include "../Biome.h"

#include "../../materials/Material.h"

namespace Pixelverse {

Forest::Forest(materialID_t id) : Biome(id, "forest", "Forest"){}

void Forest::generate(/*const Planet &planet, */coordinate position,
		materialID_t (&data)[Chunk::SIZE*2]){
	coordinate pixelPos;
	for (pixelPos.y = position.y; pixelPos.c_y == position.c_y; pixelPos.y++)
	for (pixelPos.x = position.x; pixelPos.c_x == position.c_x; pixelPos.x++){
		int height, dist, cellHeight;
		vec2 cellPos, nPos;
		Planet::cellNoise.GetCellularCenter(pixelPos.x, pixelPos.y, cellPos.x, cellPos.y);
//		printf("%i %i %f %f; ", pixelPos.x, pixelPos.y, cellPos.x, cellPos.y);
//		noise.SetCellularReturnType(FastNoise::CellularReturnType::Distance);
		if(abs(pixelPos.x) > abs(pixelPos.y)){
			height = abs(pixelPos.x);
			cellHeight = abs(cellPos.x);
			dist = abs(pixelPos.y);
			nPos = {std::copysign(Planet::surfaceHeight, pixelPos.x), double(pixelPos.y)};
		}else{
			height = abs(pixelPos.y);
			cellHeight = abs(cellPos.y);
			dist = abs(pixelPos.x);
			nPos = {double(pixelPos.x), std::copysign(Planet::surfaceHeight, pixelPos.y)};
		}
		nPos /= double(Planet::surfaceHeight);
		double heightScale = std::min((1.0-double(dist)/Planet::surfaceHeight)*4, 1.0);
		int dirtHeight = Planet::mainNoise.GetValue(nPos.x, nPos.y)*Planet::surfaceVariation*heightScale + Planet::surfaceHeight;
		//if(pixelPos.x == 0)printf("a%i|", int(Planet::mainNoise.GetValue(nPos.x, nPos.y)*Planet::surfaceVariation*heightScale));
		//if(pixelPos.x == 0)printf("b%i|", int(Planet::mainNoise.GetValue(0, 250)*Planet::surfaceVariation));
		//if(pixelPos.x == 0)printf("c%f,%f|", nPos.x, nPos.y);
		bool stone = (Planet::mainNoise.GetCellular(cellPos.x, cellPos.y)+1.0) / 2 < double(dirtHeight-cellHeight)/Planet::dirtLayerHeight;
		if(height <= dirtHeight){
			if(height == dirtHeight){
				data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] =  Material::getID("grass");
			}else{
				if(stone){
					data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] =  Material::getID("stone");
				}else{
					data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] =  Material::getID("dirt");
				}
			}
		}else{
			data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] =  Material::getID("air");
		}
		if(height < Planet::surfaceHeight && data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] == Material::getID("air")){
			data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] =  Material::getID("water");
		}
		if(cellHeight < dirtHeight){
			double idStep = 1.0/Planet::mainNoise.GetFrequency();
			materialID_t id;
			double largest;
			double gravel = fabs(Planet::mainNoise.GetCellular(cellPos.x/5, cellPos.y/5, Material::getID("gravel")*idStep));
			double silt = fabs(Planet::mainNoise.GetCellular(cellPos.x/5, cellPos.y/5, Material::getID("silt")*idStep));
			double clay = fabs(Planet::mainNoise.GetCellular(cellPos.x/5, cellPos.y/5, Material::getID("clay")*idStep));
			double loam = fabs(Planet::mainNoise.GetCellular(cellPos.x/5, cellPos.y/5, Material::getID("loam")*idStep));
			double sand = fabs(Planet::mainNoise.GetCellular(cellPos.x/5, cellPos.y/5, Material::getID("sand")*idStep));
			if(gravel > silt){
				largest = gravel;
				id = Material::getID("gravel");
			}else{
				largest = silt;
				id = Material::getID("silt");
			}
			if(clay > largest){
				largest = clay;
				id = Material::getID("clay");
			}
			if(loam > largest){
				largest = loam;
				id = Material::getID("loam");
			}
			if(sand > largest){
				largest = sand;
				id = Material::getID("sand");
			}
			if(largest > 0.993){
				data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x] = id;
			}
		}
		data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x + Chunk::SIZE] = data[Chunk::WIDTH * pixelPos.p_y + pixelPos.p_x];
	}
}

} /* namespace Pixelverse */
