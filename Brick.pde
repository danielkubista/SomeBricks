abstract class Brick {
    boolean underControl = true;
    Body body;
    final int SPACE = 2;
    final static int UNIT = 50;
    float desiredX;
    float pseudoX;
    Brick(float x){
        desiredX = x;
        pseudoX = x;
    }
    abstract void makeBody(Vec2 center);
    void killBody() {
        box2d.destroyBody(this.body);
    }
    abstract void display();
    void speedUp() {
        body.setLinearVelocity(new Vec2(0, -60));
    }
    void speedDown() {
        body.setLinearVelocity(new Vec2(0, -20));
    }
    void move(float x){
        Vec2 oldVec = body.getPosition();
        oldVec.x += box2d.scalarPixelsToWorld(x);
        body.setTransform(oldVec, body.getAngle());
    }
    void moveLeft() {
        Vec2 oldVec = body.getPosition();
        oldVec.x -= box2d.scalarPixelsToWorld(10);
        body.setTransform(oldVec, body.getAngle());
    }
    void moveRight() {
        Vec2 oldVec = body.getPosition();
        oldVec.x += box2d.scalarPixelsToWorld(10);
        body.setTransform(oldVec, body.getAngle());
    }
    Vec2 gravity = new Vec2(0, -2000);
    void applyGravity() {
        body.applyForceToCenter(gravity);
    }
    void applyWind(Vec2 dir){
        body.applyForceToCenter(dir);
    }
    FixtureDef defaultFix() {
        // Define fixture
        FixtureDef fd = new FixtureDef();
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.6;
        fd.restitution = 0;
        return fd;
    }
    FixtureDef defaultFix(PolygonShape sd) {
        // Define fixture
        FixtureDef fd = defaultFix();
        fd.setShape(sd);
        return fd;
    }
    void spin() {
        body.setTransform(body.getPosition(), body.getAngle()-PI/2);
    }
}
