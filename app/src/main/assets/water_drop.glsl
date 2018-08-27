precision lowp float;
varying highp vec2 aCoord;
uniform sampler2D samplerFrom;
uniform sampler2D samplerTo;
uniform highp float progress;
    
vec4 getFromColor(vec2 uv){
    return texture2D(samplerFrom, uv);
}
    
vec4 getToColor(vec2 uv){
   return texture2D(samplerTo, uv);
}
    
const float amplitude = 20.0;
const float speed = 30.0;
    
vec4 transition(vec2 p) {
   vec2 dir = p - vec2(.5);
   float dist = length(dir);
    
   if (dist > progress) {
       return mix(getFromColor( p), getToColor( p), progress);
   } else {
       vec2 offset = dir * sin(dist * amplitude - progress * speed);
       return mix(getFromColor( p + offset), getToColor( p), progress);
   }
}
    
void main() {
   gl_FragColor = transition(aCoord);
}

