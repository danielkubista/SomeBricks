import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;
import java.util.*;

// A reference to our box2d world
Box2DProcessing box2d;

// Brick array
ArrayList<Brick> bricks;

// Base
Base base;

float topY;

//float windowOffset = 0;

int bricksSpawned = 0;

void setup() {

    smooth();

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
    fullScreen();
    orientation(PORTRAIT);

    Swiper swiper = new Swiper();
    swiper.addObserver(new Observer() {
        public void update(Observable obs, Object obj) {
            SwipeEvent event = (SwipeEvent) obj;
            switch(event.type) {
            case CLICK:
                current.spin();
                break;
            case HOR_SWIPE:
                current.move(event.swipeVec.x);
                break;
            case VER_SWIPE:
                if (event.swipeVec.y < -15) {
                    current.speedDown();
                } else if (event.swipeVec.y > 15){
                    current.speedUp();
                }
                break;
            case RELEASED:
                current.speedDown();
                break;
            default:
                break;
            }
        }
    }
    );
    swiper.startThread();
}
Vec2 wind = new Vec2(20, 0);
void draw() {
    background(255);

    // step ahead in time
    box2d.step();
    
    // collision occured
    if (collision) {
        // new wind
        if (random(1) < 0.05) {
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

    //println("wind: " + wind.toString());
    // apply wind
    for (Brick b : bricks) {
        if (!b.underControl) {
            //b.applyWind(wind);
        }
    }
    // display
    translate(0, getWindowOffset());
    //println(getWindowOffset());
    for (Brick b : bricks) {
        if (b.underControl) {
            Vec2 pos = box2d.getBodyPixelCoord(b.body);
            pushMatrix();
            line(pos.x, pos.y, pos.x, height);
            popMatrix();
        }
        b.display();
    }
    base.display();
}
Brick current;
void spawnRandomBrick() {
    bricksSpawned += 1;
    Brick b;// = new Rectangle(width/2, 100);
    int x = (int) random(7);
    //println(x);
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
boolean comeback = true;
boolean collision = false;
// Collision event functions!
void beginContact(Contact cp) {
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
void endContact(Contact cp) {
}

void moveDisplay() {
}

/*
boolean isIn(Brick b) { 
 float x = b.body.getPosition().x;
 float y = b.body.getPosition().y;
 return x < width && x >= 0 && y < height + getWindowOffset();
 }*/

float getWindowOffset() {
    if (topY > 0) {
        return box2d.scalarWorldToPixels(topY-box2d.coordPixelsToWorld(new Vec2(width/2, height)).y-box2d.scalarPixelsToWorld(height/2));
    }
    return 0;
}
