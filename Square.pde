class Square extends Brick {
    Square(float x, float y){
        super(x);
        makeBody(new Vec2(x, y));
        body.setUserData(this);
    }
    final int WIDTH = 2*UNIT - SPACE;
    final int HEIGHT = 2*UNIT - SPACE;
    void makeBody(Vec2 center) {
        // Define shape
        PolygonShape sd = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(WIDTH/2);
        float box2dH = box2d.scalarPixelsToWorld(HEIGHT/2);
        sd.setAsBox(box2dW, box2dH);

        /*
        // Define fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.3;
        fd.restitution = 0.5;
        */
        FixtureDef fd = super.defaultFix();
        fd.shape = sd;

        // Define the body and make it a shape
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(box2d.coordPixelsToWorld(center));

        body = box2d.createBody(bd);
        body.createFixture(fd);

        // Give it some initial random velocity
        body.setLinearVelocity(new Vec2(0, -10));
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
        fill(0, 255, 0);
        rect(0, 0, WIDTH, HEIGHT);
        popMatrix();
    }
}
