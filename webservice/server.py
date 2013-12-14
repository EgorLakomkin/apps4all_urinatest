#!-*- coding:utf-8 -*-
from flask import Flask, render_template, jsonify, request
from parse_rest.connection import register
from parse_rest.datatypes import Object
import os, sys
from parse_rest.user import User
from collections import defaultdict
import numpy as np
import cv2
import cv2.cv as cv
from temp1 import process_file

root_dirname, root_filename = os.path.split(os.path.abspath(__file__))  
titles = [u'Лейкоциты', u'Нитриты', u'Уробилиноген', u'Белок', u'pH', u'Кровь', u'Удельный вес', u'Кетоны', u'Билирубин', u'Глюкоза']

class AnalysisResult(Object):
    pass

APPLICATION_ID="a23FF44FCcuteo4tNCYrSFYEtbvphkjR756odQrA"
REST_API_KEY="cyFU8hNyoDyU3SeXVeinPIUhoqDfXM4VKmgtXgX0"
MASTER_KEY = "fBvHbkZ6DS6u52UWLyqDx4WL2ghMTbbUBPHszXPF"

app = Flask('flaskwp1')
register(APPLICATION_ID, REST_API_KEY)

def get_results_by_user(user_id):
  return AnalysisResult.Query.filter(user_id=user_id).order_by("createdAt")
  
def convert_analysis(analysis):
  return map(int, analysis.split(';') )
  
def append_result_by_user(user_id,result):
  new_analysis = AnalysisResult(analysis=result, user_id = user_id)
  new_analysis.save()

@app.route('/upload_jpeg', methods=['GET','POST'])
def uploadjpeg():
  print "PrevedUploadJPEG"
  #print request.args
  #print request.form
  #print request.files
  #print request
  file =  request.files['file']
  filename = os.path.join(root_dirname, 'test.bin')
  file.save( filename )
  glukoze = process_file(filename)
  print "Found glukoze", glukoze
  #print image_data
  #user_id = request.args.get('user_id')
  #jpeg_data = request.form['image']
  #with open(os.path.join(root_dirname, 'test'),'w') as f:
  #  f.write(image_data)
  return str(glukoze)
  
  
@app.route('/statistics', methods=['GET'])
def statistics():
  user_id = request.args.get('user_id')
  grouped_history = defaultdict(list)
  user_analysis_history = [ convert_analysis(analysis_obj.analysis) for analysis_obj in get_results_by_user(  user_id)  ]
  for obj_hist_id, analysis in enumerate(user_analysis_history):
    for i, val in enumerate(analysis):
      grouped_history[i].append( [obj_hist_id, val] )
  
  result_dict = {}
  result_data = []
  
  for key in grouped_history:
    result_data.append( {'title' : titles[ key ], 'values' : grouped_history[key], 'display' : True } )
  
  result_dict['data'] = result_data
  
  print result_data
  return render_template("statistics.html",
        analysis = result_dict)

  
@app.route('/last_analysis', methods=['GET'])
def last_analysis():
  user_id = request.args.get('user_id')
  
  
  analysises = list( get_results_by_user(  user_id) )
  if len(analysises) > 0:
    last_analysis = convert_analysis(analysises[-1].analysis)
    
    result_dict = {}    
    
    result_data = []
    for i, val in enumerate(last_analysis):
      result_data.append( {'title' : titles[ i ], 'values' : [0,val], 'display' : True } )
    result_dict['data'] = result_data
   
    print result_data
    
    return render_template("result.html",
	  analysis = result_dict)
  else:
    return "No analysis for user"

  
@app.route('/push_result')
def push_result():
  user_id = request.args.get('user_id')
  analysis = request.args.get('analysis')
  append_result_by_user(user_id, analysis)
  return "OK"
  
  
if __name__ == "__main__":      
  port = int(sys.argv[1])  
  app.run(host = '0.0.0.0', port = port, debug=True)
  
