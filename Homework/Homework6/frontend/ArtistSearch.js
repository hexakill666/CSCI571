const httpRequest = new XMLHttpRequest();
const baseUrl = "https://hw6backend-dot-sound-splicer-351619.wl.r.appspot.com/";
let lastClickedTarget = null;

function init(){
    let formTag = document.getElementsByClassName("search_bar")[0];
    formTag.addEventListener("submit", (e) => {
        e.preventDefault();
        search();
    });
}

function search(){
    let inputTag = document.getElementsByClassName("search_input")[0];
    if(inputTag.value != undefined && inputTag.value != null && inputTag.value != ""){
        httpRequest.onreadystatechange = function(){
            let resultListSectionTag = document.getElementsByClassName("search_result_list_section")[0];
            let detailSectionTag = document.getElementsByClassName("search_detail_section")[0];
            if(httpRequest.readyState == 4 && httpRequest.status == 200){
                removeAllChild(resultListSectionTag);
                removeAllChild(detailSectionTag);
                let resultList = JSON.parse(httpRequest.responseText)._embedded.results.filter(function(item){
                    return item.og_type == "artist";
                });
                console.log("resultList: ", resultList);
                if(resultList.length == 0){
                    let noneTag = document.createElement("div");
                    noneTag.className = "search_result_none";
                    noneTag.innerText = "No results found.";
                    resultListSectionTag.append(noneTag);
                }
                else{
                    let resultListTage = document.createElement("div");
                    resultListTage.className = "search_result_list";
                    for(let a = 0; a < resultList.length; a++){
                        let title = resultList[a].title;
                        let artistId = getPathLastStr(resultList[a]._links.self.href);
                        let lastStr = getPathLastStr(resultList[a]._links.thumbnail.href);
                        let thumbnailUrl = lastStr.startsWith("missing_image") ? "image/artsy_logo.svg" : resultList[a]._links.thumbnail.href;
                        let background = lastStr.startsWith("missing_image") ? "url(" + thumbnailUrl + ") no-repeat center center/100% 100%" : "url(" + thumbnailUrl + ") no-repeat center center";
                        
                        console.log("title: ", title);
                        console.log("lastStr: ", lastStr);
                        console.log("thumbnailUrl: ", thumbnailUrl);
                        console.log("background: ", background)

                        let itemTag = document.createElement("div");
                        let thumbnailFrameTag = document.createElement("div");
                        let thumbnailTag = document.createElement("div");
                        let titleTag = document.createElement("div");

                        itemTag.className = "item";
                        itemTag.setAttribute("artistId", artistId);
                        thumbnailFrameTag.className = "thumbnail_frame";
                        thumbnailTag.className = "thumbnail";
                        titleTag.className = "title";
                        thumbnailTag.style.background = background;
                        titleTag.innerText = title;
                        itemTag.addEventListener("click", (e) => {
                            searchDetail(e);
                        });

                        thumbnailFrameTag.appendChild(thumbnailTag);
                        itemTag.appendChild(thumbnailFrameTag);
                        itemTag.appendChild(titleTag);
                        resultListTage.appendChild(itemTag);
                    }
                    resultListSectionTag.appendChild(resultListTage);
                }
            }
            else{
                if(detailSectionTag.hasChildNodes() && detailSectionTag.firstChild.className != "search_detail_loading"){
                    removeAllChild(detailSectionTag);
                }
                if(!resultListSectionTag.hasChildNodes() || resultListSectionTag.firstChild.className != "search_result_loading"){
                    if(resultListSectionTag.firstChild.className != "search_result_list"){
                        removeAllChild(resultListSectionTag);
                    }
                    let loadingTag = document.createElement("div");
                    loadingTag.className = "search_result_loading";
                    resultListSectionTag.appendChild(loadingTag);
                }
                else{
                    if(resultListSectionTag.firstChild.className == "search_result_list"){
                        removeAllChild(detailSectionTag);
                        let loadingTag = document.createElement("div");
                        loadingTag.className = "search_detail_loading";
                        detailSectionTag.appendChild(loadingTag);
                    }
                }
            }
        }
        httpRequest.open("get", baseUrl + "search/" + inputTag.value);
        httpRequest.send();
    }
}

function searchClear(){
    let inputTag = document.getElementsByClassName("search_input")[0];
    inputTag.value = "";
}

function searchDetail(e){
    console.log("itemTag", e.currentTarget);
    console.log("artistId:", e.currentTarget.getAttribute("artistId"));
    let itemTag = e.currentTarget;
    itemTag.style.background = "#112B3C";
    if(lastClickedTarget != null){
        lastClickedTarget.style.background = "#205375";
    }
    lastClickedTarget = itemTag;
    httpRequest.onreadystatechange = function(){
        let detailSectionTag = document.getElementsByClassName("search_detail_section")[0];
        if(httpRequest.readyState == 4 && httpRequest.status == 200){
            removeAllChild(detailSectionTag);
            let resultJson = JSON.parse(httpRequest.responseText);
            let name = resultJson.name;
            let birthday = resultJson.birthday;
            let deathday = resultJson.deathday;
            let nationality = resultJson.nationality;
            let biography = resultJson.biography;

            let detailTag = document.createElement("div");
            let h1Tag = document.createElement("h1");
            let h3Tag = document.createElement("h3");
            let pTag = document.createElement("p");

            detailTag.className = "detail";
            h3Tag.className = "nationality";
            pTag.className = "biography";
            h1Tag.innerText = name + " (" + birthday + " - " + deathday + ")";
            h3Tag.innerText = nationality;
            pTag.innerText = biography;

            detailTag.appendChild(h1Tag);
            detailTag.appendChild(h3Tag);
            detailTag.appendChild(pTag);
            detailSectionTag.appendChild(detailTag);
        }
        else{
            if(!detailSectionTag.hasChildNodes() || detailSectionTag.firstChild.className != "search_detail_loading"){
                removeAllChild(detailSectionTag);
                let loadingTag = document.createElement("div");
                loadingTag.className = "search_detail_loading";
                detailSectionTag.appendChild(loadingTag);
            }
        }
    }
    httpRequest.open("get", baseUrl + "getArtistDetail/" + itemTag.getAttribute("artistId"));
    httpRequest.send();
}

function removeAllChild(parent){
    while(parent.hasChildNodes()){
        parent.removeChild(parent.firstChild);
    }
}

function getPathLastStr(url){
    return url.substring(url.lastIndexOf("/") + 1);
}

init();