from flask import Flask, request, jsonify
from flask_cors import CORS
import datetime

app = Flask(__name__)
CORS(app, origins=["http://localhost:8080"])

@app.route('/hello', methods=['GET'])
def get_hello():
    return jsonify({
        "message": "Hello from Flask!",
        "timestamp": datetime.datetime.now().isoformat()
    })

@app.route('/data', methods=['POST'])
def post_data():
    data = request.get_json()
    return jsonify({
        "received": data,
        "response": "Data processed by Flask",
        "timestamp": datetime.datetime.now().isoformat()
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)