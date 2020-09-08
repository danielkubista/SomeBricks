class RedZ extends Brick {
    PImage img;
    RedZ(float x, float y) {
        super(x, y);
        this.type = Types.REDZ;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
        img = loadImage("redZ.png");
    }
    int totalW = 3*UNIT;
    int totalH = 2*UNIT;
    int rect1W = 2*UNIT - SPACE;
    int rect1H = UNIT - SPACE;
    int rect2W = 2*UNIT - SPACE;
    int rect2H = UNIT - SPACE;
    Vec2 offset = new Vec2(UNIT/2, -UNIT/2);
    Vec2 offsetInWorld = box2d.vectorPixelsToWorld(offset);
    void makeBody(Vec2 center) {
        // Define shape
        PolygonShape rect = new PolygonShape();
        float rect12dW = box2d.scalarPixelsToWorld(rect1W/2);
        float rect12dH = box2d.scalarPixelsToWorld(rect1H/2);
        rect.setAsBox(rect12dW, rect12dH, offsetInWorld, 0);

        PolygonShape square = new PolygonShape();
        float rect22dW = box2d.scalarPixelsToWorld(rect2W/2);
        float rect22dH = box2d.scalarPixelsToWorld(rect2H/2);
        
        square.setAsBox(rect22dW, rect22dH, offsetInWorld.mul(-1), 0);

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
        image(img, 0, 0, totalW, totalH);
        popMatrix();
    }
}
