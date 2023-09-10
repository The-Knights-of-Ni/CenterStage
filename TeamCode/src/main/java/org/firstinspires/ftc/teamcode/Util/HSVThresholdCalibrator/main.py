import cv2
import numpy as np

def stackImages(scale,imgArray):
    rows = len(imgArray)
    cols = len(imgArray[0])
    rowsAvailable = isinstance(imgArray[0], list)
    width = imgArray[0][0].shape[1]
    height = imgArray[0][0].shape[0]
    if rowsAvailable:
        for x in range ( 0, rows):
            for y in range(0, cols):
                if imgArray[x][y].shape[:2] == imgArray[0][0].shape [:2]:
                    imgArray[x][y] = cv2.resize(imgArray[x][y], (0, 0), None, scale, scale)
                else:
                    imgArray[x][y] = cv2.resize(imgArray[x][y], (imgArray[0][0].shape[1], imgArray[0][0].shape[0]), None, scale, scale)
                if len(imgArray[x][y].shape) == 2: imgArray[x][y]= cv2.cvtColor( imgArray[x][y], cv2.COLOR_GRAY2BGR)
        imageBlank = np.zeros((height, width, 3), np.uint8)
        hor = [imageBlank]*rows
        hor_con = [imageBlank]*rows
        for x in range(0, rows):
            hor[x] = np.hstack(imgArray[x])
        ver = np.vstack(hor)
    else:
        for x in range(0, rows):
            if imgArray[x].shape[:2] == imgArray[0].shape[:2]:
                imgArray[x] = cv2.resize(imgArray[x], (0, 0), None, scale, scale)
            else:
                imgArray[x] = cv2.resize(imgArray[x], (imgArray[0].shape[1], imgArray[0].shape[0]), None,scale, scale)
            if len(imgArray[x].shape) == 2: imgArray[x] = cv2.cvtColor(imgArray[x], cv2.COLOR_GRAY2BGR)
        hor= np.hstack(imgArray)
        ver = hor
    return ver

def empty(a):
    pass

path = 'resources/image.jpg'
cv2.namedWindow("trackbars")
cv2.resizeWindow("trackbars",640,240)
cv2.createTrackbar("hue min",'trackbars',142,179,empty)
cv2.createTrackbar("hue max",'trackbars',179,179,empty)
cv2.createTrackbar("sat min",'trackbars',55,255,empty)
cv2.createTrackbar("sat max",'trackbars',255,255,empty)
cv2.createTrackbar("val min",'trackbars',67,255,empty)
cv2.createTrackbar("val max",'trackbars',255,255,empty)



while True:
    img = cv2.imread(path)
    imgHSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    h_min = cv2.getTrackbarPos('hue min', 'trackbars')
    h_max = cv2.getTrackbarPos('hue max', 'trackbars')
    s_min = cv2.getTrackbarPos('sat min', 'trackbars')
    s_max = cv2.getTrackbarPos('sat max', 'trackbars')
    v_min = cv2.getTrackbarPos('val min', 'trackbars')
    v_max = cv2.getTrackbarPos('val max', 'trackbars')
    lower = np.array([h_min, s_min, v_min])
    upper = np.array([h_max, s_max, v_max])
    mask = cv2.inRange(imgHSV,lower,upper,)
    imgResult = cv2.bitwise_and(img, img, mask=mask)

    imgStack = stackImages(0.6, ([img, imgResult]))
    cv2.imshow("images", imgStack)


    cv2.waitKey(1)
