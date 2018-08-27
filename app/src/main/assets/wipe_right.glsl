precision lowp float;
uniform sampler2D samplerFrom;
varying highp vec2 aCoord;
uniform sampler2D samplerTo;
uniform highp float progress;

vec4 getFromColor(vec2 uv) {
    return texture2D(samplerFrom, uv);
}

vec4 getToColor(vec2 uv) {
    return texture2D(samplerTo, uv);
}

vec4 transition(vec2 uv) {
    vec2 p = uv.xy / vec2(1.0).xy;
    vec4 a = getFromColor(p);
    vec4 b = getToColor(p);
    return mix(a, b, step(0.0 + p.x, progress));
}

void main() {
    gl_FragColor = transition(aCoord);
}
