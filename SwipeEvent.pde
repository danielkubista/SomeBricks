class SwipeEvent{
    EventType type;
    PVector swipeVec;
    SwipeEvent(EventType type, PVector swipeVec){
        this.type = type;
        this.swipeVec = swipeVec;
    }
}
enum EventType {
    CLICK, VER_SWIPE, HOR_SWIPE, OTH_SWIPE, RELEASED
}
