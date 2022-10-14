#!/usr/bin/python
import time
import picamera
import os
path=os.getenv("HOME")+""           #adjust path for location of this program
with picamera.PiCamera() as picam:
    picam.rotation=90               #adjust as necessary
    picam.start_preview()
    time.sleep(5)
    picam.capture('pic.jpg')
    # picam.capture(path+'/photo.jpg',resize=(640,480))
    # time.sleep(2)
    picam.stop_preview()
    picam.close()
