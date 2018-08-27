precision lowp float;
varying highp vec2 aCoord;
uniform sampler2D samplerFrom;
uniform sampler2D samplerTo;
uniform highp float progress;
const float zoom_quickness = 0.8;
float nQuick = clamp(zoom_quickness,0.2,1.0);

vec4 getFromColor(vec2 uv) {
    return texture2D(samplerFrom, uv);
}

vec4 getToColor(vec2 uv) {
    return texture2D(samplerTo, uv);
}

vec2 zoom(vec2 uv, float amount) {
    return 0.5 + ((uv - 0.5) * (1.0 - amount));
}

vec4 transition(vec2 uv) {
    return mix(
        getFromColor(zoom(uv, smoothstep(0.0, nQuick, progress))),
        getToColor(uv),
        smoothstep(nQuick - 0.2, 1.0, progress)
    );
}
void main() {
    gl_FragColor = transition(aCoord);
}
