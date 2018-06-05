package tankgame;

public class GameEngine implements Runnable{

    private Thread thread;
    private boolean isRunning = false;
    private final double UPDATE_LIMIT = 1.0/60.0;

    @Override
    public void run(){

        isRunning = true;

        System.out.println("Been here");

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while(isRunning){

            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while(unprocessedTime >= UPDATE_LIMIT){

                unprocessedTime -= UPDATE_LIMIT;
                render = true;

                //lowerBall();

                if(frameTime >= 1.0){
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }

            if(render){
                frames++;
            }
            else{
                try{
                    Thread.sleep(1);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        dispose();
    }

    public void startEngine(){

        thread = new Thread(this);
        thread.run();
    }

    public void stop(){

    }

    private void dispose(){

    }
}