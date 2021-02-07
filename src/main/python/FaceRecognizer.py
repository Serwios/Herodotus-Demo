import cv2
import numpy as np
import os


PATH = 'ImagesQuery'
images = []
classNames = []
photoNameList = os.listdir(PATH)

print('Len of photos list \n', len(photoNameList))

for c1 in photoNameList:
	imgCur = cv2.imread(f'{PATH}/{c1}')
	images.append(imgCur)
	print(c1)
	classNames.append(os.path.splitext(c1)[0])

orb = cv2.ORB_create(nfeatures=10000)
camera_out = cv2.imread('Imagebuf_cam_test.png')
camera_out_orig = cv2.imread('Imagebuf_cam_test.png')
kp1, des1 = orb.detectAndCompute(camera_out, None)
    
def findDes(images):
	desList = []
	
	for img in images:
		kp, des = orb.detectAndCompute(img, None)
		desList.append(des)
	return desList

def findID(img, desList, thresh):
	kp2, des2 = orb.detectAndCompute(img, None)
	bf = cv2.BFMatcher()
	matchList = []
	finalVal = -1
	
	for des in desList:
		matches = bf.knnMatch(des, des2, k=2)
		good = []
		for m,n in matches:
			if m.distance < 0.75 * n.distance:
				good.append([m])
		matchList.append(len(good))
		print(matchList)
			
	
	if len(matchList) != 0:
		if max(matchList) > thresh:
			finalVal = matchList.index(max(matchList))
			print("\nFinal val: ", finalVal)
	return 	finalVal

desList = findDes(images)
id = findID(camera_out, desList, 1)
print("It was =", classNames[id])

if id > -1:
	zero_read = cv2.imread('ImagesQuery/' + classNames[id] + '.png')
	kp2, des2 = orb.detectAndCompute(zero_read, None)
	bf = cv2.BFMatcher()
	good = []
	matches = bf.knnMatch(des1, desList[0], k=2)
	for m,n in matches:
		if m.distance < 0.75*n.distance:
			good.append([m])
	
	compared_images = cv2.drawMatchesKnn(camera_out_orig, kp1, zero_read, kp2, good, None, flags=2)
	cv2.imshow("img3", compared_images)
	cv2.waitKey(0)
	
