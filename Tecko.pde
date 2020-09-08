class Tecko extends Brick {
    PImage img;
    final int totalW = 3*UNIT;
    final int totalH = 2*UNIT;
    Tecko(float x, float y) {
        super(x, y);
        this.type = Types.TECKO;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
        img = loadImage("tecko.png");
    }
    int rectW = 3*UNIT - SPACE;
    int rectH = UNIT - SPACE;
    int squareW = UNIT - SPACE;
    int squareH = UNIT - SPACE;
    int yOffset = -UNIT;
    Vec2 offsetInWorld = new Vec2(0, box2d.scalarPixelsToWorld(UNIT));
    void makeBody(Vec2 center) {
        // Define shape
        PolygonShape rect = new PolygonShape();
        float rect2dW = box2d.scalarPixelsToWorld(rectW/2);
        float rect2dH = box2d.scalarPixelsToWorld(rectH/2);
        rect.setAsBox(rect2dW, rect2dH);

        PolygonShape square = new PolygonShape();
        float square2dW = box2d.scalarPixelsToWorld(squareW/2);
        float square2dH = box2d.scalarPixelsToWorld(squareH/2);
        
        square.setAsBox(square2dW, square2dH, offsetInWorld, 0);

        //println(offsetInWorld.toString());
        

        //FixtureDef fd = super.defaultFix();


        // Define the body and make it a shape
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(box2d.coordPixelsToWorld(center));

        body = box2d.createBody(bd);
        
        //FixtureDef fd1 = super.defaultFix();
        //fd.setShape(rect);
        body.createFixture(super.defaultFix(rect));

        //fd.setShape(square);
        body.createFixture(super.defaultFix(square));



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
        imageMode(CENTER);
        image(img, 0, -squareH/2, totalW, totalH);
        popMatrix();
    }
}
