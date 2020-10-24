import cv2
import numpy as np
import os

#IMPORT ALL IMAGES
path = 'ImagesQuery'
images = []
classNames = []
myList = os.listdir(path)
print(myList)
print('I found objects: ', len(myList))
print('\n')

for c1 in myList:
	imgCur = cv2.imread(f'{path}/{c1}', 0)
	images.append(imgCur)
	print(c1)
	print('\n')
	classNames.append(os.path.splitext(c1)[0])

#MAKE FROM THEM DESKRIPTORS AND KEYPOINTS
orb = cv2.ORB_create(nfeatures=500)
camera_out = cv2.imread('Imagebuf_cam_test.png', 1)
camera_out_orig = cv2.imread('Imagebuf_cam_test.png', 1)
camera_out = cv2.cvtColor(camera_out, cv2.COLOR_BGR2GRAY)
#cv2.imshow("img1", camera_out)
#cv2.waitKey(0)
kp1, des1 = orb.detectAndCompute(camera_out, None)

def findDes(images):
	desList=[]
	for img in images:
		kp, des = orb.detectAndCompute(img, None)
		desList.append(des)
	return desList

def findID(img, desList, thresh):
	kp2, des2 = orb.detectAndCompute(img, None)
	bf = cv2.BFMatcher()
	matchList = []
	finalVal = -1
	good = [] 
	for des in desList:
		matches = bf.knnMatch(des1, des2, k=2)
		for m,n in matches:
			if m.distance < 0.75*n.distance:
				good.append([m])

		matchList.append(len(good))
		print(matchList)

		if len(matchList)!=0:
			if max(matchList) > thresh:
				finalVal = matchList.index(max(matchList))
				print(finalVal)
	return finalVal

desList = findDes(images)
id = findID(camera_out, desList, 15)
#print(id)

if id == 0:
	print(camera_out_orig, classNames[id])
	zero_path = 'ImagesQuery/'+classNames[0]+'.jpg'
	#print(zero_path)
	zero_read = cv2.imread(zero_path)
	#print(zero_read)
	kp2, des2 = orb.detectAndCompute(zero_read, None)
	bf = cv2.BFMatcher()
	good = []
	matches = bf.knnMatch(des1, desList[0], k=2)
	for m,n in matches:
		if m.distance < 0.75*n.distance:
			good.append([m])
	
	img3 = cv2.drawMatchesKnn(camera_out_orig, kp1, zero_read, kp2, good, None, flags=2)
	cv2.imshow("img3", img3)
	cv2.waitKey(0)


#TRASH

# imgKp1 = cv2.drawKeypoints(img1, kp1, None)
# imgKp2 = cv2.drawKeypoints(img2, kp2, None)

 #good = []
 #for m,n in matches:
#	if m.distance < 0.75*n.distance:
#		good.append([m])

# img3 = cv2.drawMatchesKnn(img1, kp1, img2, kp2, good, None, flags=2)


# cv2.imshow("kp1", imgKp1)
# cv2.imshow("kp2", imgKp2)
# cv2.imshow("img3", img3)


# cv2.waitKey(0)