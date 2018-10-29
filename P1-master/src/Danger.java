public class Danger extends Sprite {

    public Danger(int x, int y){

        super(x, y);    //invokes main method of super class(Sprite)

        initDanger();

        super.y-=height;
    }

    private void initDanger(){

        loadImage("/danger.png");   //gives path to the file with picture to method from Sprite class that will load the image
        getImageDimensions();        //method from Sprite class
    }
}
