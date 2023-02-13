const clientId = "yourClientId";
const clientSecret = "yourClientSecret";

const express = require("express");
const SyncRequest = require("sync-request");
const cors = require("cors");
const app = express();
app.use(cors())
app.listen(8080, "0.0.0.0");

function getAuthInfo(){
    let res = SyncRequest("post", "https://api.artsy.net/api/tokens/xapp_token", 
        {
            json: {
                "client_id": clientId,
                "client_secret": clientSecret
            }
        }
    );
    return JSON.parse(res.getBody("utf-8"));
}

function search(name){
    let res = SyncRequest("get", "https://api.artsy.net/api/search",
        {
            qs: {
                "q": name
            },
            headers: {
                "X-XAPP-Token": getAuthInfo().token
            }
        }
    );
    return JSON.parse(res.getBody("utf-8"));
}

function getArtist(artistId){
    let res = SyncRequest("get", "https://api.artsy.net/api/artists/" + artistId,
        {
            headers: {
                "X-XAPP-Token": getAuthInfo().token
            }
        }
    );
    return JSON.parse(res.getBody("utf-8"));
}

function getArtworks(artistId){
    let res = SyncRequest("get", "https://api.artsy.net/api/artworks",
        {
            qs: {
                "artist_id": artistId
            },
            headers: {
                "X-XAPP-Token": getAuthInfo().token
            }
        }
    );
    return JSON.parse(res.getBody("utf-8"));
}

function getGenes(artworkId){
    let res = SyncRequest("get", "https://api.artsy.net/api/genes",
        {
            qs: {
                "artwork_id": artworkId
            },
            headers: {
                "X-XAPP-Token": getAuthInfo().token
            }
        }
    );
    return JSON.parse(res.getBody("utf-8"));
}

app.get("/", (req, res) => {
    res.send("Hello! This is my Node.js Express!");
});

app.get("/getAuthInfo", (req, res) => {
    res.send(getAuthInfo());
})

app.get("/search/:name", (req, res) => {
    res.send(search(req.params.name));
})

app.get("/getArtist/:artistId", (req, res) => {
    res.send(getArtist(req.params.artistId));
})

app.get("/getArtworks/:artistId", (req, res) => {
    res.send(getArtworks(req.params.artistId));
})

app.get("/getGenes/:artworkId", (req, res) => {
    res.send(getGenes(req.params.artworkId));
})