precision lowp float;
varying highp vec2 aCoord;
uniform sampler2D samplerFrom;
uniform sampler2D samplerTo;
uniform highp float progress;
const float SQRT_2 = 1.414213562373;
const float dots = 20.0;
const vec2 center = vec2(0, 0);

vec4 getFromColor(vec2 uv) {
    return texture2D(samplerFrom, uv);
}

vec4 getToColor(vec2 uv) {
    return texture2D(samplerTo, uv);
}

vec4 transition(vec2 uv) {
    bool nextImage = distance(fract(uv * dots), vec2(0.5, 0.5)) < (progress / distance(uv, center));
    return nextImage ? getToColor(uv) : getFromColor(uv);
}
void main() {
    gl_FragColor = transition(aCoord);
}
