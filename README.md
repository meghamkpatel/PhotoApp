# PhotoApp

In this app, I attempted to build a camera app. While it is difficult to test the app, I wanted to learn the structure of how it works and how it saves the image to the phone's memory.
I tried to recreate an image to take a picture of by using the GraphApp code so that the user can change the image and I can utilize SurfaceView to build a camera. Hence I created a CameraPreview activity that extends SurfaceView and implements SurfaceHolder.Callback, Camera.ShutterCallback, and Camera.PictureCallback.
I am having difficulties trying to get the app to take the picture as it is still breaks the app when I try to get a preview. There is an internal issue with the relationship between the graph on the screen and the camera which I am still trying to figure out. Besides that, I have learned cameras work by having a SurfaceView and SurfaceHolder and ensuring the app can get a preview by connecting to the screen. 

Day1: Researching and adding features
Day2: added permission to use camera
Day3: build the screen to take picture
Day4: use graph app to create the image
Day5: separate the code to two classes (Main and CameraPreview)
Day6: still having issues with camera breaking but added a gallery button
