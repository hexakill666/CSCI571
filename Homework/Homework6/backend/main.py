from flask import Flask
from flask_cors import CORS
import requests

clientId = "yourClientId"
clientSecret = "yourClientSecret"
app = Flask(__name__)
CORS(app)

@app.route('/')
def hello():
    return 'Hello! This is my Flask!'

@app.route("/getAuthenticateInfo")
def getAuthenticateInfo():
    res = requests.post("https://api.artsy.net/api/tokens/xapp_token", data = {
        "client_id" : clientId,
        "client_secret" : clientSecret
    })
    return res.json()

@app.route("/search/<name>")
def search(name):
    res = requests.get("https://api.artsy.net/api/search?q=" + name, headers = {
        "X-XAPP-Token" : getAuthenticateInfo().get("token")
    })
    return res.json() 

@app.route("/getArtistDetail/<id>")
def getArtistDetail(id):
    res = requests.get("https://api.artsy.net/api/artists/" + id, headers = {
        "X-XAPP-Token" : getAuthenticateInfo().get("token")
    })
    return res.json()

if __name__ == "__main__":
    app.run(
        host = '0.0.0.0', 
        port = '8888',
        debug = True
    )