from flask import Flask, request
from datetime import datetime 
from operations import multiply
from operations import matrixgenerator

app = Flask(__name__)

@app.route("/data", methods=['GET'])
def get_data():
    return "Status OK"

@app.route("/data", methods=['POST'])
def post_data():
    dat = int(request.get_data().decode("utf-8")[5:-1])
    print(dat)
    matrix1 = matrixgenerator(dat)
    matrix2 = matrixgenerator(dat)
    starttime = datetime.now()
    multiply(matrix1,matrix2)
    endtime = datetime.now()
    delta =str(endtime - starttime)
    print(delta)
    return(delta)

if __name__=="__main__":
    app.run(debug=True, host = "0.0.0.0",threaded=True)