import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-searchall',
  templateUrl: './searchall.component.html',
  styleUrls: ['./searchall.component.css']
})
export class SearchallComponent implements OnInit {

  public searchInputValue = "";
  public baseUrl = "https://hw8backend-dot-sound-splicer-351619.wl.r.appspot.com/";

  public isResultListLoading = false;
  public isResultListNone = false;
  public isResultListDone = false;
  public resultList:any[] = [];
  public lastClickedTarget:any;

  public isArtistInfoLoading = false;
  public isArtWorksLoading = false;
  public isArtistDetailDone = false;
  public isArtWorksNone = false;
  public isArtWorksDone = false;
  public isArtistInfoActive = "active";
  public isArtWorksActive = "";
  public artistInfoJson:any;
  public artWorksList:any[] = [];

  public isCategoryLoading = false;
  public isCategoryNone = false;
  public isCategoryDone = false;
  public categoryList:any[] = [];
  public lastClickedArtwork = {
    thumbnailUrl: "",
    title: "",
    date: ""
  }

  constructor() {
  }

  ngOnInit(): void {
  }

  public clearAll() : void {
    this.searchInputValue = "";
    this.isResultListLoading = false;
    this.isResultListNone = false;
    this.isResultListDone = false;
    this.isArtistInfoLoading = false;
    this.isArtWorksLoading = false;
    this.isArtistDetailDone = false;
    this.isArtWorksNone = false;
    this.isArtWorksDone = false;
    this.isArtistInfoActive = "active";
    this.isArtWorksActive = "";
    this.isCategoryLoading = false;
    this.isCategoryNone = false;
    this.isCategoryDone = false;
  }

  public search() : void {
    let _this = this;
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.onreadystatechange = function(){
      if(this.readyState == XMLHttpRequest.DONE && this.status == 200){
        _this.isResultListLoading = false;
        let resList = JSON.parse(this.responseText)._embedded.results
        .filter(function(item : any){
          return item.og_type == "artist";
        });
        console.log("search resList", resList);
        if(resList.length == 0){
          _this.isResultListNone = true;
          _this.isResultListDone = false;
        }
        else{
          for(let a = 0; a < resList.length; a++){
            _this.resultList[a] = {
              title: resList[a].title,
              artistId: _this.getPathLastStr(resList[a]._links.self.href),
              thumbnailUrl: _this.getPathLastStr(resList[a]._links.thumbnail.href)
                            .startsWith("missing_image") ? "assets/artsy_logo.svg" : 
                            resList[a]._links.thumbnail.href
            }
          }

          _this.isResultListNone = false;
          _this.isResultListDone = true;
        }
        console.log("_this.resultList", _this.resultList);
      }
      else{
        _this.isResultListLoading = true;
        _this.isArtistDetailDone = false;
      }
    };
    xmlHttpReq.open("get", this.baseUrl + "search/" + this.searchInputValue);
    xmlHttpReq.send();
  }

  public searchDetail(item : any, ev : any) : void {
    console.log("item:", item);
    console.log("itemTag", ev.currentTarget);
    let cardTag = ev.currentTarget;
    cardTag.style.background = "#112B3C";
    if(this.lastClickedTarget != null){
      this.lastClickedTarget.style.background = "#205375";
    }
    this.lastClickedTarget = cardTag;
    this.searchArtistInfo(item, ev);
    this.searchArtworks(item, ev);
  }

  public searchArtistInfo(item : any, ev : any) : void {
    let _this = this;
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.onreadystatechange = function(){
      if(this.readyState == XMLHttpRequest.DONE && this.status == 200){
        _this.artistInfoJson = JSON.parse(this.responseText);
        console.log("_this.artistInfoJson", _this.artistInfoJson);
        _this.isArtistInfoLoading = false;
        _this.isArtistInfoActive = "active";
        _this.isArtWorksActive = "";
        if(!_this.isArtistInfoLoading && !_this.isArtWorksLoading){
          _this.isArtistDetailDone = true;
        }
      }
      else{
        _this.isArtistInfoLoading = true;
        _this.isArtistDetailDone = false;
      }
    }
    xmlHttpReq.open("get", this.baseUrl + "getArtist/" + item.artistId);
    xmlHttpReq.send();
  }

  public searchArtworks(item : any, ev : any) : void {
    let _this = this;
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.onreadystatechange = function(){
      if(this.readyState == XMLHttpRequest.DONE && this.status == 200){
        let resList = JSON.parse(this.responseText)._embedded.artworks;
        console.log("searchArtworks resList", resList);
        
        if(resList.length == 0){
          _this.isArtWorksNone = true;
          _this.isArtWorksDone = false;
        }
        else{
          for(let a = 0; a < resList.length; a++){
            _this.artWorksList[a] = {
              artistId: item.artistId,
              artworkId: resList[a].id,
              title: resList[a].title,
              date: resList[a].date,
              thumbnailUrl: resList[a]._links.thumbnail.href,
              background: "url(" + resList[a]._links.thumbnail.href + ") no-repeat center top"
            }
          }
        }
        console.log("_this.artWorksList", _this.artWorksList);

        _this.isArtWorksLoading = false;
        _this.isArtistInfoActive = "active";
        _this.isArtWorksActive = "";
        if(!_this.isArtistInfoLoading && !_this.isArtWorksLoading){
          _this.isArtistDetailDone = true;
        }
      }
      else{
        _this.isArtWorksLoading = true;
        _this.isArtWorksNone = false;
        _this.isArtistDetailDone = false;
      }
    }
    xmlHttpReq.open("get", this.baseUrl + "getArtworks/" + item.artistId);
    xmlHttpReq.send();
  }

  public searchCategory(item : any, ev : any) : void {
    console.log("item: ", item);
    console.log("itemTag", ev.currentTarget);
    let _this = this;
    let xmlHttpReq = new XMLHttpRequest();
    _this.lastClickedArtwork = item;
    xmlHttpReq.onreadystatechange = function(){
      if(this.readyState == XMLHttpRequest.DONE && this.status == 200){
        let resList = JSON.parse(this.responseText)._embedded.genes;
        console.log("searchCategory resList: ", resList);

        if(resList.length == 0){
          _this.isCategoryNone = true;
          _this.isCategoryDone = false;
        }
        else{
          for(let a = 0; a < resList.length; a++){
            _this.categoryList[a] = {
              categoryId: resList[a].id,
              name: resList[a].name,
              thumbnailUrl: resList[a]._links.thumbnail.href,
              background: "url(" + resList[a]._links.thumbnail.href + ") no-repeat center top/100%"
            }
          }
          _this.isCategoryNone = false;
          _this.isCategoryDone = true;
        }
        _this.isCategoryLoading = false;

        console.log("_this.categoryList", _this.categoryList);
      }
      else{
        _this.isCategoryLoading = true;
        _this.isCategoryNone = false;
        _this.isCategoryDone = false;
      }
    }
    xmlHttpReq.open("get", this.baseUrl + "getGenes/" + item.artworkId);
    xmlHttpReq.send();
  }

  public removeAllChild(parent : any) : void {
    while(parent.hasChildNodes()){
      parent.removeChild(parent.firstChild);
    }
  }

  public getPathLastStr(url : any) : any {
    return url.substring(url.lastIndexOf("/") + 1);
  }

}
