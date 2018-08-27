precision lowp float;
varying highp vec2 aCoord;
uniform sampler2D samplerFrom;
uniform sampler2D samplerTo;
uniform highp float progress;
    
vec4 getFromColor(vec2 uv) {
    return texture2D(samplerFrom, uv);
}
    
vec4 getToColor(vec2 uv) {
    return texture2D(samplerTo, uv);
}
    
    
vec4 transition(vec2 p) {
    vec2 block = floor(p.xy / vec2(16));
    vec2 uv_noise = block / vec2(64);
    uv_noise += floor(vec2(progress) * vec2(1200.0, 3500.0)) / vec2(64);
    vec2 dist = progress > 0.0 ? (fract(uv_noise) - 0.5) * 0.3 * (1.0 -progress) : vec2(0.0);
    vec2 red = p + dist * 0.2;
    vec2 green = p + dist * .3;
    vec2 blue = p + dist * .5;
    return vec4(mix(getFromColor(red), getToColor(red), progress).r,mix(getFromColor(green),
        getToColor(green), progress).g,mix(getFromColor(blue), getToColor(blue), progress).b,1.0);
}
    
void main() {
   gl_FragColor = transition(aCoord);
}

