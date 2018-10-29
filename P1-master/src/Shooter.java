public class Shooter extends Sprite {

    public Shooter(int x, int y){

        super(x,y);     //invokes main method of super class(Sprite)

        initShooter();
    }

    private void initShooter(){

        loadImage("/shooter.png");  //gives path to the file with picture to method from Sprite class that will load the image
        getImageDimensions();   //method from Sprite class
    }
}
