package processing.test.mobiletowers;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MobileTowers extends PApplet {











// A reference to our box2d world
Box2DProcessing box2d;

// Brick array
ArrayList<Brick> bricks;

// Base
Base base;

float topY;

//float windowOffset = 0;

int bricksSpawned = 0;

public void setup() {
    
    
    
    // Initialize box2d physics and create world
    box2d = new Box2DProcessing(this);
    box2d.createWorld();
    // No gravity
    box2d.setGravity(0, 0);

    // Turn on collision listening!
    box2d.listenForCollisions();

    // set topY as bottom of the screen
    topY = box2d.coordPixelsToWorld(new Vec2(width/2, height)).y;

    base = new Base(width/2, height, 6*Brick.UNIT, 10*Brick.UNIT);
    bricks = new ArrayList<Brick>();
    spawnRandomBrick();

    // display settings
    //size(640, 960);
    
    orientation(PORTRAIT);
}
Vec2 wind = new Vec2(20, 0);
public void draw() {
    background(255);

    // step ahead in time
    box2d.step();
    /* check out of world bricks
     for (int i = 0; i < bricks.size(); i++) {
     Brick b = bricks.get(i);
     if (b.underControl && !isIn(b)) {
     
     println("OUT");
     b.killBody();
     bricks.remove(i);
     
     }
     }
     */
    // collision occured
    if (collision) {
        // new wind
        if (random(1) < 0.05f) {
            wind = new Vec2(random(-20, 20), random(-20, 20));
        }
        spawnRandomBrick();
        collision = false;
    }
    // apply gravity
    for (Brick b : bricks) {
        if (!b.underControl) {
            b.applyGravity();
        }
    }

    println("wind: " + wind.toString());
    // apply wind
    for (Brick b : bricks) {
        if (!b.underControl) {
            b.applyWind(wind);
        }
    }
    // display
    translate(0, getWindowOffset());
    println(getWindowOffset());
    for (Brick b : bricks) {
        if (b.underControl) {
            Vec2 pos = box2d.getBodyPixelCoord(b.body);
            pushMatrix();
            line(pos.x, pos.y, pos.x, height);
            popMatrix();
        }
        switch (b.type) {
        case RECTANGLE:
            ((Rectangle) b).display();
            break;

        case SQUAR:
            ((Square) b).display();
            break;
        case TECKO:
            ((Tecko) b).display();
            break;
        case YELLOWL: 
            ((YellowL) b).display();
            break;
        case PINKL: 
            ((PinkL) b).display();
            break;
        case ORANGEZ:
            ((OrangeZ) b).display();
            break;
        case REDZ:
            ((RedZ) b).display();
            break;
        }
    }
    base.display();
}
Brick current;
public void spawnRandomBrick() {
    bricksSpawned += 1;
    Brick b;// = new Rectangle(width/2, 100);
    int x = (int) random(7);
    println(x);
    switch (x) {
    case 0:
        b = new Rectangle(width/2, 100-getWindowOffset());
        break;
    case 1:
        b = new Tecko(width/2, 100-getWindowOffset());
        break;
    case 2: 
        b = new YellowL(width/2, 100-getWindowOffset()); 
        break;
    case 3:
        b = new PinkL(width/2, 100-getWindowOffset()); 
        break;
    case 4:
        b = new OrangeZ(width/2, 100-getWindowOffset()); 
        break;
    case 5:
        b = new RedZ(width/2, 100-getWindowOffset());
        break;
    default:
        b = new Square(width/2, 100-getWindowOffset());
        break;
    }
    current = b;
    bricks.add(b);
}

public void mousePressed() {
    switch (mouseButton) {
    case 39:
        current.spin();
        break;
    case 37:
        current.speedUp();
        break;
    }
}
public void mouseReleased() {
    current.speedDown();
}
public void keyPressed() {
    switch (key) {
    case 'a':
        current.moveLeft();
        break;
    case 'd':
        current.moveRight();
        break;
    case 'q':
        translate(0, 100);
        break;
    case 'x':
        if (bricks.size() >= 2) {
            bricks.get(bricks.size()-2).killBody();
            bricks.remove(bricks.size()-2);
        }
        break;
    }
}
boolean comeback = true;
boolean collision = false;
// Collision event functions!
public void beginContact(Contact cp) {
    // Get both fixtures
    Fixture f1 = cp.getFixtureA();
    Fixture f2 = cp.getFixtureB();
    // Get both bodies
    Body b1 = f1.getBody();
    Body b2 = f2.getBody();
    // Get our objects that reference these bodies
    Object o1 = b1.getUserData();
    Object o2 = b2.getUserData();
    
    if (o1.getClass() != Base.class && ((Brick) o1).underControl) {
        if (comeback) topY = -1000;
        if (((Brick) o1).body.getPosition().y > topY) {
            topY = ((Brick) o1).body.getPosition().y;
            //translate(0, -box2d.scalarPixelsToWorld(topY));
        }
        ((Brick) o1).underControl = false;
        collision = true;
    }
    if (o2.getClass() != Base.class && ((Brick) o2).underControl) {
        if (comeback) topY = -1000;
        if (((Brick) o2).body.getPosition().y > topY) {
            topY = ((Brick) o2).body.getPosition().y;
            //translate(0, -box2d.scalarPixelsToWorld(topY));
        }
        ((Brick) o2).underControl = false;
        collision = true;
    }
}


// Objects stop touching each other
public void endContact(Contact cp) {
}

public void moveDisplay() {
}

/*
boolean isIn(Brick b) { 
 float x = b.body.getPosition().x;
 float y = b.body.getPosition().y;
 return x < width && x >= 0 && y < height + getWindowOffset();
 }*/

public float getWindowOffset() {
    if (topY > 0) {
        return box2d.scalarWorldToPixels(topY-box2d.coordPixelsToWorld(new Vec2(width/2, height)).y-box2d.scalarPixelsToWorld(height/2));
    }
    return 0;
}
class Base {
    // A boundary is a simple rectangle with x,y,width,and height
    float x;
    float y;
    float w;
    float h;

    // But we also have to make a body for box2d to know about it
    Body b;

    Base(float x_, float y_, float w_, float h_) {
        x = x_;
        y = y_;
        w = w_;
        h = h_;

        // Define the polygon
        PolygonShape sd = new PolygonShape();
        // Figure out the box2d coordinates
        float box2dW = box2d.scalarPixelsToWorld(w/2);
        float box2dH = box2d.scalarPixelsToWorld(h/2);
        // We're just a box
        sd.setAsBox(box2dW, box2dH);


        // Create the body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(box2d.coordPixelsToWorld(x, y));
        b = box2d.createBody(bd);

        // Attached the shape to the body using a Fixture
        b.createFixture(sd, 1);

        b.setUserData(this);
    }

    // Draw the boundary, if it were at an angle we'd have to do something fancier
    public void display() {
        pushMatrix();
        fill(0);
        stroke(0);
        rectMode(CENTER);
        rect(x, y, w, h);
        popMatrix();
    }
}
abstract class Brick {
    boolean underControl = true;
    Types type;
    Body body;
    float x;
    float y;
    final int SPACE = 2;
    final static int UNIT = 30;
    Brick(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public abstract void makeBody(Vec2 center);
    public void killBody() {
        box2d.destroyBody(this.body);
    }
    public abstract void display();
    public void speedUp() {
        //body.setLinearVelocity(new Vec2(0, -20 * log(bricksSpawned+1)));
        body.setLinearVelocity(new Vec2(0, -20));
    }
    public void speedDown() {
        //body.setLinearVelocity(new Vec2(0, -10 * log(bricksSpawned+1)));
        body.setLinearVelocity(new Vec2(0, -10));
    }
    public void moveLeft() {
        Vec2 oldVec = body.getPosition();
        oldVec.x -= box2d.scalarPixelsToWorld(10);
        body.setTransform(oldVec, body.getAngle());
    }
    public void moveRight() {
        Vec2 oldVec = body.getPosition();
        oldVec.x += box2d.scalarPixelsToWorld(10);
        body.setTransform(oldVec, body.getAngle());
    }
    Vec2 gravity = new Vec2(0, -200);
    public void applyGravity() {
        body.applyForceToCenter(gravity);
    }
    public void applyWind(Vec2 dir){
        body.applyForceToCenter(dir);
    }
    public FixtureDef defaultFix() {
        // Define fixture
        FixtureDef fd = new FixtureDef();
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.6f;
        fd.restitution = 0;
        return fd;
    }
    public FixtureDef defaultFix(PolygonShape sd) {
        // Define fixture
        FixtureDef fd = defaultFix();
        fd.setShape(sd);
        return fd;
    }
    public void spin() {
        body.setTransform(body.getPosition(), body.getAngle()-PI/2);
    }
}
class OrangeZ extends Brick {
    PImage img;
    final int totalW = 3*UNIT;
    final int totalH = 2*UNIT;
    OrangeZ(float x, float y) {
        super(x, y);
        this.type = Types.ORANGEZ;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
        img = loadImage("orangeZ.png");
    }
    
    int rect1W = 2*UNIT - SPACE;
    int rect1H = UNIT - SPACE;
    int rect2W = 2*UNIT - SPACE;
    int rect2H = UNIT - SPACE;
    Vec2 offset = new Vec2(UNIT/2, UNIT/2);
    Vec2 offsetInWorld = box2d.vectorPixelsToWorld(offset);
    public void makeBody(Vec2 center) {
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
    public void display() {
        // position
        Vec2 pos = box2d.getBodyPixelCoord(this.body);
        // get angle
        float a = this.body.getAngle();
        pushMatrix();
        translate(pos.x, pos.y);
        rotate(-a);
        imageMode(CENTER);
        image(img, 0, 0, totalW, totalH);
        popMatrix();
    }
}
class PinkL extends Brick {
    PinkL(float x, float y) {
        super(x, y);
        this.type = Types.PINKL;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
    }
    int rect1W = 3*UNIT - SPACE;
    int rect1H = UNIT - SPACE;
    int rect2W = UNIT - SPACE;
    int rect2H = 2*UNIT - SPACE;
    //int yOffset = -UNIT;
    Vec2 offset = new Vec2(-UNIT, -UNIT/2);
    Vec2 offsetInWorld = box2d.vectorPixelsToWorld(offset);
    public void makeBody(Vec2 center) {
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
    public void killBody() {
        box2d.destroyBody(this.body);
    }
    public void display() {
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
class Rectangle extends Brick {
    final int WIDTH = 4*UNIT - super.SPACE;
    final int HEIGHT = UNIT - SPACE;
    Rectangle(float x, float y) {
        super(x, y);
        this.type = Types.RECTANGLE;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
    }
    public void makeBody(Vec2 center) {
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
    public void killBody() {
        box2d.destroyBody(this.body);
    }
    public void display() {
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
    public void makeBody(Vec2 center) {
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
    public void display() {
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
class Square extends Brick {
    Square(float x, float y){
        super(x, y);
        this.type = Types.SQUAR;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
    }
    final int WIDTH = 2*UNIT - SPACE;
    final int HEIGHT = 2*UNIT - SPACE;
    public void makeBody(Vec2 center) {
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
    public void killBody() {
        box2d.destroyBody(this.body);
    }
    public void display() {
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
    public void makeBody(Vec2 center) {
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
    public void killBody() {
        box2d.destroyBody(this.body);
    }
    public void display() {
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
enum Types{
    SQUAR,
    RECTANGLE,
    TECKO,
    YELLOWL,
    PINKL,
    ORANGEZ,
    REDZ
}
class YellowL extends Brick {
    YellowL(float x, float y) {
        super(x, y);
        this.type = Types.YELLOWL;
        makeBody(new Vec2(this.x, this.y));
        body.setUserData(this);
    }
    int rect1W = 3*UNIT - SPACE;
    int rect1H = UNIT - SPACE;
    int rect2W = UNIT - SPACE;
    int rect2H = 2*UNIT - SPACE;
    //int yOffset = -UNIT;
    Vec2 offset = new Vec2(UNIT, -UNIT/2);
    
    Vec2 offsetInWorld = box2d.vectorPixelsToWorld(offset);
    public void makeBody(Vec2 center) {
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
    public void display() {
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
        fill(255, 255, 0);
        rect(0, 0, rect1W, rect1H);
        rect(offset.x, offset.y, rect2W, rect2H);
        popMatrix();
    }
}
    public void settings() {  fullScreen();  smooth(); }
}
