class Rectangle extends Brick {
    final int WIDTH = 4*UNIT - super.SPACE;
    final int HEIGHT = UNIT - SPACE;
    Rectangle(float x, float y) {
        super(x, y);
        this.type = Types.RECTANGLE;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
    }
    void makeBody(Vec2 center) {
        // Define shape
        PolygonShape sd = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(WIDTH/2);
        float box2dH = box2d.scalarPixelsToWorld(HEIGHT/2);
        sd.setAsBox(box2dW, box2dH);

        FixtureDef fd = super.defaultFix();
        fd.shape = sd;
        /*
        // Define fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.3;
        fd.restitution = 0.5;
        */

        // Define the body and make it a shape
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(box2d.coordPixelsToWorld(center));

        body = box2d.createBody(bd);
        body.createFixture(fd);

        // Give it some initial random velocity
        body.setLinearVelocity(new Vec2(0, -10));
    }
    void killBody() {
        box2d.destroyBody(this.body);
    }
    void display() {
        // position
        Vec2 pos = box2d.getBodyPixelCoord(this.body);
        // get angle
        float a = this.body.getAngle();

        rectMode(CENTER);
        pushMatrix();
        translate(pos.x, pos.y);
        rotate(-a);
        strokeWeight(0);
        stroke(0);
        fill(0);
        rect(0, 0, WIDTH+SPACE, HEIGHT+SPACE);
        fill(255, 0, 255);
        rect(0, 0, WIDTH, HEIGHT);
        popMatrix();
    }
}
