#!-*- coding:utf-8 -*-
from flask import Flask, render_template, jsonify, request

import os

@app.route('/statistics', methods=['GET'])
def statistics():
  pass

@app.route('/last_analysis', methods=['GET'])
def statistics():
  pass

  
@app.route('/push_result', methods=['POST'])
def statistics():
  pass
  
  
if __name__ == "__main__":
  root_dirname, root_filename = os.path.split(os.path.abspath(__file__))
  
  port = int(sys.argv[1])
  
  app.run(host = '0.0.0.0', port = port, debug=True)
  
  