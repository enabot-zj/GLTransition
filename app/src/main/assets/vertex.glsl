attribute vec4 vPosition;
attribute highp vec2 vCoord;
varying highp vec2 aCoord;
uniform mat4 vMatrix;

void main() {
    gl_Position = vMatrix * vPosition;
    aCoord = vCoord;
}
