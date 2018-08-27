precision lowp float;
uniform sampler2D samplerTexture;
varying vec2 aCoord;

void main() {
    gl_FragColor = texture2D(samplerTexture, aCoord);
}
