class PinkL extends Brick {
    PinkL(float x, float y) {
        super(x);
        makeBody(new Vec2(x, y));
        body.setUserData(this);
    }
    int rect1W = 3*UNIT - SPACE;
    int rect1H = UNIT - SPACE;
    int rect2W = UNIT - SPACE;
    int rect2H = 2*UNIT - SPACE;
    //int yOffset = -UNIT;
    Vec2 offset = new Vec2(-UNIT, -UNIT/2);
    Vec2 offsetInWorld = box2d.vectorPixelsToWorld(offset);
    void makeBody(Vec2 center) {
        // Define shape
        PolygonShape rect = new PolygonShape();
        float rect12dW = box2d.scalarPixelsToWorld(rect1W/2);
        float rect12dH = box2d.scalarPixelsToWorld(rect1H/2);
        rect.setAsBox(rect12dW, rect12dH);

        PolygonShape square = new PolygonShape();
        float rect22dW = box2d.scalarPixelsToWorld(rect2W/2);
        float rect22dH = box2d.scalarPixelsToWorld(rect2H/2);
        
        square.setAsBox(rect22dW, rect22dH, offsetInWorld, 0);

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
        strokeWeight(0);
        stroke(0);
        fill(0);
        rect(0, 0, rect1W+SPACE, rect1H+SPACE);
        rect(offset.x, offset.y, rect2W+SPACE, rect2H+SPACE);
        fill(127, 9, 119);
        rect(0, 0, rect1W, rect1H);
        rect(offset.x, offset.y, rect2W, rect2H);
        popMatrix();
    }
}
