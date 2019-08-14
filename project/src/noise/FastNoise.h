// FastNoise.h
//
// MIT License
//
// Copyright(c) 2017 Jordan Peck
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// The developer's email is jorzixdan.me2@gzixmail.com (for great email, take
// off every 'zix'.)
//

// VERSION: 0.4.1

#ifndef FASTNOISE_H
#define FASTNOISE_H

// Uncomment the line below to use doubles throughout FastNoise instead of floats
#define FN_USE_DOUBLES

#define FN_CELLULAR_INDEX_MAX 3

#ifdef FN_USE_DOUBLES
typedef double FN_DECIMAL;
#else
typedef float FN_DECIMAL;
#endif

class FastNoise
{
public:
	explicit FastNoise(int seed = 1337) { SetSeed(seed); CalculateFractalBounding(); }

	enum NoiseType { Value, ValueFractal, Perlin, PerlinFractal, Simplex, SimplexFractal, Cellular, WhiteNoise, Cubic, CubicFractal };
	enum Interp { Linear, Hermite, Quintic };
	enum FractalType { FBM, Billow, RigidMulti };
	enum CellularDistanceFunction { Euclidean, Manhattan, Natural };
	enum CellularReturnType { CellValue, NoiseLookup, Distance, Distance2, Distance2Add, Distance2Sub, Distance2Mul, Distance2Div };

	// Sets seed used for all noise types
	// Default: 1337
	void SetSeed(int seed);

	// Returns seed used for all noise types
	int GetSeed() const { return m_seed; }

	// Sets frequency for all noise types
	// Default: 0.01
	void SetFrequency(FN_DECIMAL frequency) { m_frequency = frequency; }

	// Returns frequency used for all noise types
	FN_DECIMAL GetFrequency() const { return m_frequency; }

	// Changes the interpolation method used to smooth between noise values
	// Possible interpolation methods (lowest to highest quality) :
	// - Linear
	// - Hermite
	// - Quintic
	// Used in Value, Perlin Noise and Position Warping
	// Default: Quintic
	void SetInterp(Interp interp) { m_interp = interp; }

	// Returns interpolation method used for supported noise types
	Interp GetInterp() const { return m_interp; }

	// Sets noise return type of GetNoise(...)
	// Default: Simplex
	void SetNoiseType(NoiseType noiseType) { m_noiseType = noiseType; }

	// Returns the noise type used by GetNoise
	NoiseType GetNoiseType() const { return m_noiseType; }

	// Sets octave count for all fractal noise types
	// Default: 3
	void SetFractalOctaves(int octaves) { m_octaves = octaves; CalculateFractalBounding(); }

	// Returns octave count for all fractal noise types
	int GetFractalOctaves() const { return m_octaves; }
	
	// Sets octave lacunarity for all fractal noise types
	// Default: 2.0
	void SetFractalLacunarity(FN_DECIMAL lacunarity) { m_lacunarity = lacunarity; }

	// Returns octave lacunarity for all fractal noise types
	FN_DECIMAL GetFractalLacunarity() const { return m_lacunarity; }

	// Sets octave gain for all fractal noise types
	// Default: 0.5
	void SetFractalGain(FN_DECIMAL gain) { m_gain = gain; CalculateFractalBounding(); }

	// Returns octave gain for all fractal noise types
	FN_DECIMAL GetFractalGain() const { return m_gain; }

	// Sets method for combining octaves in all fractal noise types
	// Default: FBM
	void SetFractalType(FractalType fractalType) { m_fractalType = fractalType; }

	// Returns method for combining octaves in all fractal noise types
	FractalType GetFractalType() const { return m_fractalType; }


	// Sets distance function used in cellular noise calculations
	// Default: Euclidean
	void SetCellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction) { m_cellularDistanceFunction = cellularDistanceFunction; }

	// Returns the distance function used in cellular noise calculations
	CellularDistanceFunction GetCellularDistanceFunction() const { return m_cellularDistanceFunction; }

	// Sets return type from cellular noise calculations
	// Note: NoiseLookup requires another FastNoise object be set with SetCellularNoiseLookup() to function
	// Default: CellValue
	void SetCellularReturnType(CellularReturnType cellularReturnType) { m_cellularReturnType = cellularReturnType; }

	// Returns the return type from cellular noise calculations
	CellularReturnType GetCellularReturnType() const { return m_cellularReturnType; }

	// Noise used to calculate a cell value if cellular return type is NoiseLookup
	// The lookup value is acquired through GetNoise() so ensure you SetNoiseType() on the noise lookup, value, Perlin or simplex is recommended
	void SetCellularNoiseLookup(FastNoise* noise) { m_cellularNoiseLookup = noise; }

	// Returns the noise used to calculate a cell value if the cellular return type is NoiseLookup
	FastNoise* GetCellularNoiseLookup() const { return m_cellularNoiseLookup; }

	// Sets the 2 distance indices used for distance2 return types
	// Default: 0, 1
	// Note: index0 should be lower than index1
	// Both indices must be >= 0, index1 must be < 4
	void SetCellularDistance2Indices(int cellularDistanceIndex0, int cellularDistanceIndex1);

	// Returns the 2 distance indices used for distance2 return types
	void GetCellularDistance2Indices(int& cellularDistanceIndex0, int& cellularDistanceIndex1) const;

	// Sets the maximum distance a cellular point can move from its grid position
	// Setting this high will make artifacts more common
	// Default: 0.45
	void SetCellularJitter(FN_DECIMAL cellularJitter) { m_cellularJitter = cellularJitter; }

	// Returns the maximum distance a cellular point can move from its grid position
	FN_DECIMAL GetCellularJitter() const { return m_cellularJitter; }

	// Sets the maximum warp distance from original location when using GradientPerturb{Fractal}(...)
	// Default: 1.0
	void SetGradientPerturbAmp(FN_DECIMAL gradientPerturbAmp) { m_gradientPerturbAmp = gradientPerturbAmp; }

	// Returns the maximum warp distance from original location when using GradientPerturb{Fractal}(...)
	FN_DECIMAL GetGradientPerturbAmp() const { return m_gradientPerturbAmp; }

	//2D
	FN_DECIMAL GetValue(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL GetValueFractal(FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL GetPerlin(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL GetPerlinFractal(FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL GetSimplex(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL GetSimplexFractal(FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL GetCellular(FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL GetWhiteNoise(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL GetWhiteNoiseInt(int x, int y) const;

	FN_DECIMAL GetCubic(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL GetCubicFractal(FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL GetNoise(FN_DECIMAL x, FN_DECIMAL y) const;

	void GradientPerturb(FN_DECIMAL& x, FN_DECIMAL& y) const;
	void GradientPerturbFractal(FN_DECIMAL& x, FN_DECIMAL& y) const;

	//3D
	FN_DECIMAL GetValue(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL GetValueFractal(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL GetPerlin(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL GetPerlinFractal(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL GetSimplex(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL GetSimplexFractal(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL GetCellular(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	void GetCellularCenter(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL& xc, FN_DECIMAL& yc) const;

	FN_DECIMAL GetWhiteNoise(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL GetWhiteNoiseInt(int x, int y, int z) const;

	FN_DECIMAL GetCubic(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL GetCubicFractal(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL GetNoise(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	void GradientPerturb(FN_DECIMAL& x, FN_DECIMAL& y, FN_DECIMAL& z) const;
	void GradientPerturbFractal(FN_DECIMAL& x, FN_DECIMAL& y, FN_DECIMAL& z) const;

	//4D
	FN_DECIMAL GetSimplex(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z, FN_DECIMAL w) const;

	FN_DECIMAL GetWhiteNoise(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z, FN_DECIMAL w) const;
	FN_DECIMAL GetWhiteNoiseInt(int x, int y, int z, int w) const;

	FN_DECIMAL VAL_LUT[256] =
	{
		FN_DECIMAL(0.3490196078), FN_DECIMAL(0.4352941176), FN_DECIMAL(-0.4509803922), FN_DECIMAL(0.6392156863), FN_DECIMAL(0.5843137255), FN_DECIMAL(-0.1215686275), FN_DECIMAL(0.7176470588), FN_DECIMAL(-0.1058823529), FN_DECIMAL(0.3960784314), FN_DECIMAL(0.0431372549), FN_DECIMAL(-0.03529411765), FN_DECIMAL(0.3176470588), FN_DECIMAL(0.7254901961), FN_DECIMAL(0.137254902), FN_DECIMAL(0.8588235294), FN_DECIMAL(-0.8196078431),
		FN_DECIMAL(-0.7960784314), FN_DECIMAL(-0.3333333333), FN_DECIMAL(-0.6705882353), FN_DECIMAL(-0.3882352941), FN_DECIMAL(0.262745098), FN_DECIMAL(0.3254901961), FN_DECIMAL(-0.6470588235), FN_DECIMAL(-0.9215686275), FN_DECIMAL(-0.5294117647), FN_DECIMAL(0.5294117647), FN_DECIMAL(-0.4666666667), FN_DECIMAL(0.8117647059), FN_DECIMAL(0.3803921569), FN_DECIMAL(0.662745098), FN_DECIMAL(0.03529411765), FN_DECIMAL(-0.6156862745),
		FN_DECIMAL(-0.01960784314), FN_DECIMAL(-0.3568627451), FN_DECIMAL(-0.09019607843), FN_DECIMAL(0.7490196078), FN_DECIMAL(0.8352941176), FN_DECIMAL(-0.4039215686), FN_DECIMAL(-0.7490196078), FN_DECIMAL(0.9529411765), FN_DECIMAL(-0.0431372549), FN_DECIMAL(-0.9294117647), FN_DECIMAL(-0.6549019608), FN_DECIMAL(0.9215686275), FN_DECIMAL(-0.06666666667), FN_DECIMAL(-0.4431372549), FN_DECIMAL(0.4117647059), FN_DECIMAL(-0.4196078431),
		FN_DECIMAL(-0.7176470588), FN_DECIMAL(-0.8117647059), FN_DECIMAL(-0.2549019608), FN_DECIMAL(0.4901960784), FN_DECIMAL(0.9137254902), FN_DECIMAL(0.7882352941), FN_DECIMAL(-1.0), FN_DECIMAL(-0.4745098039), FN_DECIMAL(0.7960784314), FN_DECIMAL(0.8509803922), FN_DECIMAL(-0.6784313725), FN_DECIMAL(0.4588235294), FN_DECIMAL(1.0), FN_DECIMAL(-0.1843137255), FN_DECIMAL(0.4509803922), FN_DECIMAL(0.1450980392),
		FN_DECIMAL(-0.231372549), FN_DECIMAL(-0.968627451), FN_DECIMAL(-0.8588235294), FN_DECIMAL(0.4274509804), FN_DECIMAL(0.003921568627), FN_DECIMAL(-0.003921568627), FN_DECIMAL(0.2156862745), FN_DECIMAL(0.5058823529), FN_DECIMAL(0.7647058824), FN_DECIMAL(0.2078431373), FN_DECIMAL(-0.5921568627), FN_DECIMAL(0.5764705882), FN_DECIMAL(-0.1921568627), FN_DECIMAL(-0.937254902), FN_DECIMAL(0.08235294118), FN_DECIMAL(-0.08235294118),
		FN_DECIMAL(0.9058823529), FN_DECIMAL(0.8274509804), FN_DECIMAL(0.02745098039), FN_DECIMAL(-0.168627451), FN_DECIMAL(-0.7803921569), FN_DECIMAL(0.1137254902), FN_DECIMAL(-0.9450980392), FN_DECIMAL(0.2), FN_DECIMAL(0.01960784314), FN_DECIMAL(0.5607843137), FN_DECIMAL(0.2705882353), FN_DECIMAL(0.4431372549), FN_DECIMAL(-0.9607843137), FN_DECIMAL(0.6156862745), FN_DECIMAL(0.9294117647), FN_DECIMAL(-0.07450980392),
		FN_DECIMAL(0.3098039216), FN_DECIMAL(0.9921568627), FN_DECIMAL(-0.9137254902), FN_DECIMAL(-0.2941176471), FN_DECIMAL(-0.3411764706), FN_DECIMAL(-0.6235294118), FN_DECIMAL(-0.7647058824), FN_DECIMAL(-0.8901960784), FN_DECIMAL(0.05882352941), FN_DECIMAL(0.2392156863), FN_DECIMAL(0.7333333333), FN_DECIMAL(0.6549019608), FN_DECIMAL(0.2470588235), FN_DECIMAL(0.231372549), FN_DECIMAL(-0.3960784314), FN_DECIMAL(-0.05098039216),
		FN_DECIMAL(-0.2235294118), FN_DECIMAL(-0.3725490196), FN_DECIMAL(0.6235294118), FN_DECIMAL(0.7019607843), FN_DECIMAL(-0.8274509804), FN_DECIMAL(0.4196078431), FN_DECIMAL(0.07450980392), FN_DECIMAL(0.8666666667), FN_DECIMAL(-0.537254902), FN_DECIMAL(-0.5058823529), FN_DECIMAL(-0.8039215686), FN_DECIMAL(0.09019607843), FN_DECIMAL(-0.4823529412), FN_DECIMAL(0.6705882353), FN_DECIMAL(-0.7882352941), FN_DECIMAL(0.09803921569),
		FN_DECIMAL(-0.6078431373), FN_DECIMAL(0.8039215686), FN_DECIMAL(-0.6), FN_DECIMAL(-0.3254901961), FN_DECIMAL(-0.4117647059), FN_DECIMAL(-0.01176470588), FN_DECIMAL(0.4823529412), FN_DECIMAL(0.168627451), FN_DECIMAL(0.8745098039), FN_DECIMAL(-0.3647058824), FN_DECIMAL(-0.1607843137), FN_DECIMAL(0.568627451), FN_DECIMAL(-0.9921568627), FN_DECIMAL(0.9450980392), FN_DECIMAL(0.5137254902), FN_DECIMAL(0.01176470588),
		FN_DECIMAL(-0.1450980392), FN_DECIMAL(-0.5529411765), FN_DECIMAL(-0.5764705882), FN_DECIMAL(-0.1137254902), FN_DECIMAL(0.5215686275), FN_DECIMAL(0.1607843137), FN_DECIMAL(0.3725490196), FN_DECIMAL(-0.2), FN_DECIMAL(-0.7254901961), FN_DECIMAL(0.631372549), FN_DECIMAL(0.7098039216), FN_DECIMAL(-0.568627451), FN_DECIMAL(0.1294117647), FN_DECIMAL(-0.3098039216), FN_DECIMAL(0.7411764706), FN_DECIMAL(-0.8509803922),
		FN_DECIMAL(0.2549019608), FN_DECIMAL(-0.6392156863), FN_DECIMAL(-0.5607843137), FN_DECIMAL(-0.3176470588), FN_DECIMAL(0.937254902), FN_DECIMAL(0.9843137255), FN_DECIMAL(0.5921568627), FN_DECIMAL(0.6941176471), FN_DECIMAL(0.2862745098), FN_DECIMAL(-0.5215686275), FN_DECIMAL(0.1764705882), FN_DECIMAL(0.537254902), FN_DECIMAL(-0.4901960784), FN_DECIMAL(-0.4588235294), FN_DECIMAL(-0.2078431373), FN_DECIMAL(-0.2156862745),
		FN_DECIMAL(0.7725490196), FN_DECIMAL(0.3647058824), FN_DECIMAL(-0.2392156863), FN_DECIMAL(0.2784313725), FN_DECIMAL(-0.8823529412), FN_DECIMAL(0.8980392157), FN_DECIMAL(0.1215686275), FN_DECIMAL(0.1058823529), FN_DECIMAL(-0.8745098039), FN_DECIMAL(-0.9843137255), FN_DECIMAL(-0.7019607843), FN_DECIMAL(0.9607843137), FN_DECIMAL(0.2941176471), FN_DECIMAL(0.3411764706), FN_DECIMAL(0.1529411765), FN_DECIMAL(0.06666666667),
		FN_DECIMAL(-0.9764705882), FN_DECIMAL(0.3019607843), FN_DECIMAL(0.6470588235), FN_DECIMAL(-0.5843137255), FN_DECIMAL(0.05098039216), FN_DECIMAL(-0.5137254902), FN_DECIMAL(-0.137254902), FN_DECIMAL(0.3882352941), FN_DECIMAL(-0.262745098), FN_DECIMAL(-0.3019607843), FN_DECIMAL(-0.1764705882), FN_DECIMAL(-0.7568627451), FN_DECIMAL(0.1843137255), FN_DECIMAL(-0.5450980392), FN_DECIMAL(-0.4980392157), FN_DECIMAL(-0.2784313725),
		FN_DECIMAL(-0.9529411765), FN_DECIMAL(-0.09803921569), FN_DECIMAL(0.8901960784), FN_DECIMAL(-0.2862745098), FN_DECIMAL(-0.3803921569), FN_DECIMAL(0.5529411765), FN_DECIMAL(0.7803921569), FN_DECIMAL(-0.8352941176), FN_DECIMAL(0.6862745098), FN_DECIMAL(0.7568627451), FN_DECIMAL(0.4980392157), FN_DECIMAL(-0.6862745098), FN_DECIMAL(-0.8980392157), FN_DECIMAL(-0.7725490196), FN_DECIMAL(-0.7098039216), FN_DECIMAL(-0.2470588235),
		FN_DECIMAL(-0.9058823529), FN_DECIMAL(0.9764705882), FN_DECIMAL(0.1921568627), FN_DECIMAL(0.8431372549), FN_DECIMAL(-0.05882352941), FN_DECIMAL(0.3568627451), FN_DECIMAL(0.6078431373), FN_DECIMAL(0.5450980392), FN_DECIMAL(0.4039215686), FN_DECIMAL(-0.7333333333), FN_DECIMAL(-0.4274509804), FN_DECIMAL(0.6), FN_DECIMAL(0.6784313725), FN_DECIMAL(-0.631372549), FN_DECIMAL(-0.02745098039), FN_DECIMAL(-0.1294117647),
		FN_DECIMAL(0.3333333333), FN_DECIMAL(-0.8431372549), FN_DECIMAL(0.2235294118), FN_DECIMAL(-0.3490196078), FN_DECIMAL(-0.6941176471), FN_DECIMAL(0.8823529412), FN_DECIMAL(0.4745098039), FN_DECIMAL(0.4666666667), FN_DECIMAL(-0.7411764706), FN_DECIMAL(-0.2705882353), FN_DECIMAL(0.968627451), FN_DECIMAL(0.8196078431), FN_DECIMAL(-0.662745098), FN_DECIMAL(-0.4352941176), FN_DECIMAL(-0.8666666667), FN_DECIMAL(-0.1529411765),
	};
private:
	unsigned char m_perm[512];
	unsigned char m_perm12[512];

	int m_seed = 1337;
	FN_DECIMAL m_frequency = FN_DECIMAL(0.01);
	Interp m_interp = Quintic;
	NoiseType m_noiseType = Simplex;

	int m_octaves = 3;
	FN_DECIMAL m_lacunarity = FN_DECIMAL(2);
	FN_DECIMAL m_gain = FN_DECIMAL(0.5);
	FractalType m_fractalType = FBM;
	FN_DECIMAL m_fractalBounding;

	CellularDistanceFunction m_cellularDistanceFunction = Euclidean;
	CellularReturnType m_cellularReturnType = CellValue;
	FastNoise* m_cellularNoiseLookup = nullptr;
	int m_cellularDistanceIndex0 = 0;
	int m_cellularDistanceIndex1 = 1;
	FN_DECIMAL m_cellularJitter = FN_DECIMAL(0.45);

	FN_DECIMAL m_gradientPerturbAmp = FN_DECIMAL(1);

	void CalculateFractalBounding();

	//2D
	FN_DECIMAL SingleValueFractalFBM(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleValueFractalBillow(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleValueFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleValue(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL SinglePerlinFractalFBM(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SinglePerlinFractalBillow(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SinglePerlinFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SinglePerlin(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL SingleSimplexFractalFBM(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleSimplexFractalBillow(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleSimplexFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleSimplexFractalBlend(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleSimplex(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL SingleCubicFractalFBM(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleCubicFractalBillow(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleCubicFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleCubic(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y) const;

	FN_DECIMAL SingleCellular(FN_DECIMAL x, FN_DECIMAL y) const;
	FN_DECIMAL SingleCellular2Edge(FN_DECIMAL x, FN_DECIMAL y) const;

	void SingleGradientPerturb(unsigned char offset, FN_DECIMAL warpAmp, FN_DECIMAL frequency, FN_DECIMAL& x, FN_DECIMAL& y) const;

	//3D
	FN_DECIMAL SingleValueFractalFBM(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleValueFractalBillow(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleValueFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleValue(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL SinglePerlinFractalFBM(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SinglePerlinFractalBillow(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SinglePerlinFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SinglePerlin(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL SingleSimplexFractalFBM(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleSimplexFractalBillow(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleSimplexFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleSimplex(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL SingleCubicFractalFBM(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleCubicFractalBillow(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleCubicFractalRigidMulti(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleCubic(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	FN_DECIMAL SingleCellular(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;
	FN_DECIMAL SingleCellular2Edge(FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z) const;

	void SingleGradientPerturb(unsigned char offset, FN_DECIMAL warpAmp, FN_DECIMAL frequency, FN_DECIMAL& x, FN_DECIMAL& y, FN_DECIMAL& z) const;

	//4D
	FN_DECIMAL SingleSimplex(unsigned char offset, FN_DECIMAL x, FN_DECIMAL y, FN_DECIMAL z, FN_DECIMAL w) const;

	inline unsigned char Index2D_12(unsigned char offset, int x, int y) const;
	inline unsigned char Index3D_12(unsigned char offset, int x, int y, int z) const;
	inline unsigned char Index4D_32(unsigned char offset, int x, int y, int z, int w) const;
	inline unsigned char Index2D_256(unsigned char offset, int x, int y) const;
	inline unsigned char Index3D_256(unsigned char offset, int x, int y, int z) const;
	inline unsigned char Index4D_256(unsigned char offset, int x, int y, int z, int w) const;

	inline FN_DECIMAL ValCoord2DFast(unsigned char offset, int x, int y) const;
	inline FN_DECIMAL ValCoord3DFast(unsigned char offset, int x, int y, int z) const;
	inline FN_DECIMAL GradCoord2D(unsigned char offset, int x, int y, FN_DECIMAL xd, FN_DECIMAL yd) const;
	inline FN_DECIMAL GradCoord3D(unsigned char offset, int x, int y, int z, FN_DECIMAL xd, FN_DECIMAL yd, FN_DECIMAL zd) const;
	inline FN_DECIMAL GradCoord4D(unsigned char offset, int x, int y, int z, int w, FN_DECIMAL xd, FN_DECIMAL yd, FN_DECIMAL zd, FN_DECIMAL wd) const;
};
#endif
