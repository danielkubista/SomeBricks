class Swiper extends Observable implements Runnable {

    class Mouse {
        PVector pos;
        boolean isPressed;
        Mouse(int mX, int mY, boolean isPressed) {
            pos = new PVector(mX, mY);
            this.isPressed = isPressed;
        }
    }
    ArrayList<Mouse> mice;
    
    //TODO add lastDraggedMouse
    Mouse prevMouse;
    Mouse startPressMouse;

    {
        //mice = new ArrayList<Mouse>();
        //mice.add(new Mouse(0, 0, false));
        prevMouse = new Mouse(0, 0, false);
    }

    Thread t;
    void startThread() {
        t = new Thread(this);
        t.start();
    }
    volatile boolean truu = true;



    @Override
        void run() {
        while (truu) {
            //mice.add(new Mouse(mouseX, mouseY, mousePressed));
            Mouse thisMouse = new Mouse(mouseX, mouseY, mousePressed);
            if (!prevMouse.isPressed && mousePressed) {
                println("ad 1");
                startPressMouse = new Mouse(mouseX, mouseY, true);
            }
            if (prevMouse.isPressed && !mousePressed) {
                println("ad 2");
                if (PVector.dist(startPressMouse.pos, thisMouse.pos) < 10) {
                    println("ad 3");
                    setChanged();
                    notifyObservers(new SwipeEvent(EventType.CLICK, null));
                }
                setChanged();
                notifyObservers(new SwipeEvent(EventType.RELEASED, null));
            }
            if (prevMouse.isPressed && mousePressed) {
                if (PVector.dist(prevMouse.pos, thisMouse.pos) > 0) {
                    println("ad 4");
                    PVector diff = PVector.sub(thisMouse.pos, prevMouse.pos);
                    setChanged();
                    notifyObservers(new SwipeEvent(getSwipe(diff), diff));
                }
            }
            try {
                Thread.sleep(10);
            } 
            catch (Exception e) {
            }
            prevMouse = thisMouse;
        }
    }
    float treshold = 0.25;
    EventType getSwipe(PVector diff) {
        if (abs(diff.y/diff.x) < treshold) {
            println("horizontal swipe");
            return EventType.HOR_SWIPE;
        } else if (abs(diff.x/diff.y) < treshold) {
            println("vertical swipe");
            return EventType.VER_SWIPE;
        }
        return EventType.OTH_SWIPE;
    }
    //void isHorSwipe(PVector diff) {
    //    if (abs(diff.y/diff.x) < treshold) {
    //        println("horizontal swipe");
    //    }
    //}
    //void isVerSwipe(PVector diff) {
    //    if (abs(diff.x/diff.y) < treshold) {
    //        println("vertical swipe");
    //    }
    //}
}
