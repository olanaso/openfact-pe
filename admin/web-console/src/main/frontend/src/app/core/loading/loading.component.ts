import { Component, OnInit } from '@angular/core';

import { LoadingService } from './loading.service';

@Component({
  selector: 'of-loading',
  templateUrl: './loading.component.html',
  styles: [`
    .loading {
      background-color: #f5f5f5;
      border: 1px solid #8b8d8f;
      position: fixed;
      top: 5px;
      left: 45%;
      padding: 0px 10px 0px 10px;
      z-index: 10000;
      display: block;
      color: #363636;
      border-radius: 3px;
    }
  `]
})
export class LoadingComponent implements OnInit {

  loading: boolean;

  constructor(private loadingService: LoadingService) { }

  ngOnInit() {
    this.loadingService.loading.subscribe(data => {
      this.loading = data;
    });
  }

}
