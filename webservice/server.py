#!-*- coding:utf-8 -*-
from flask import Flask, render_template, jsonify, request
from parse_rest.connection import register
from parse_rest.datatypes import Object
import os, sys
from parse_rest.user import User


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
  return str(analysis).split(';')
  
def append_result_by_user(user_id,result):
  new_analysis = AnalysisResult(analysis=result, user_id = user_id)
  new_analysis.save()

@app.route('/statistics', methods=['GET'])
def statistics():
  user_id = request.args.get('user_id')
  user_analysis_history = [ {'id' : i,'analysis' : convert_analysis(analysis_obj.analysis) }  for i, analysis_obj in enumerate(get_results_by_user(  user_id) )  ]
  print user_analysis_history
  return render_template("statistics.html",
        analysis = user_analysis_history)

  
@app.route('/last_analysis', methods=['GET'])
def last_analysis():
  user_id = request.args.get('user_id')
  
  
  analysises = get_results_by_user(  user_id)
  if len(analysises) > 0:
    last_analysis = { 'id' : 0, 'analysis' : analysises[-1].analysis }
    print last_analysis
    
    return render_template("result.html",
	  analysis = last_analysis)
  else:
    return "No analysis for user"

  
@app.route('/push_result', methods=['POST'])
def push_result():
  user_id = request.args.get('user_id')
  return "OK"
  
  
if __name__ == "__main__":
  root_dirname, root_filename = os.path.split(os.path.abspath(__file__))  
      
  port = int(sys.argv[1])
  
  app.run(host = '0.0.0.0', port = port, debug=True)
  