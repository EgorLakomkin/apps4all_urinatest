import cv2
import cv2.cv as cv
import numpy as np
from scipy.signal import medfilt2d 

def process_file(filename):
  img = cv2.imread(filename)
  img = cv2.resize(img,(100,400))
  hsv = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)
  s = cv2.split(hsv)[1]
  h = cv2.split(hsv)[0]
  v = cv2.split(hsv)[2]
  #print np.median(s)
  #s = medfilt2d(s,3)
  s = cv2.resize(s,(100,400))
  h = cv2.resize(h,(100,400))
  v = cv2.resize(v,(100,400))

  (thresh,imgs) = cv2.threshold(s, 0 ,255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
  element = cv2.getStructuringElement(cv2.MORPH_RECT ,(5,5))
  imgs = cv2.dilate(imgs,element)
  contours0, hierarchy = cv2.findContours(imgs, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
  contours0 = [cv2.approxPolyDP(cnt, 3, True) for cnt in contours0]

  bounding_boxes = [ cv2.boundingRect(cnt) for cnt in contours0]

  sorted_bounding_box = sorted(bounding_boxes, key = lambda x: x[1], reverse=True)

  max_width_bb = sorted(bounding_boxes, key = lambda x: x[2])[-1][2]

  print "Max w bb", max_width_bb
  filtered_bb = filter(lambda x : x[2] >= max_width_bb/2, bounding_boxes )

  glukoze_bb = filtered_bb[0]
  center_gk_bb_x,center_gk_bb_y  = glukoze_bb[0] + glukoze_bb[2] /2, glukoze_bb[1] + glukoze_bb[3] /2
  cv2.circle(img, (center_gk_bb_x,center_gk_bb_y) , 5 , (255,255,0) , 2)
  print "glukoze bb", glukoze_bb
  hue_arr = []
  sat_arr = []
  val_arr = []
  for x in xrange(center_gk_bb_x - 3, center_gk_bb_x + 3 +1):
	  for y in xrange(center_gk_bb_y -3, center_gk_bb_y + 3 +1):
		  hue_arr.append( h[y][x] )
		  sat_arr.append( s[y][x] )
		  val_arr.append( v[y][x] )
  glu_h = np.mean(hue_arr) * 360 / 255
  print "expected hue", glu_h
  print "expected sat", np.mean(sat_arr) * 100 / 255
  print "expected val", np.mean(val_arr) * 100 / 255
  for rect in filtered_bb:
  	print rect
  	cv2.rectangle(img,(rect[0], rect[1]),(rect[0]+rect[2],rect[1] + rect[3]), (255,255,0), 2)
  cv2.imwrite("img_processed.jpg",img)  
  if glu_h < 85:
	  return 1
  else :
	  return 0

if __name__ == "__main__":
  process_file('test_ref.jpg')

