public class BgSprite extends Sprite {  //class for sprites in background, right now we only have clouds

    private final int SPRITE_SPEED = 2;

    public BgSprite(int x, int y) {

        super(x, y);

        initBgSprite();
    }

    private void initBgSprite(){

        loadImage("/cloud.png");
        getImageDimensions();
    }

    public void move(){

        x-= SPRITE_SPEED;

        if(x < 0 - getWidth()){
            visible = false;
        }
    }
}
