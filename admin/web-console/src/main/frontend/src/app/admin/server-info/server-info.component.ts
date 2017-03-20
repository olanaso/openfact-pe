import * as Collections from 'typescript-collections';

import { Component, OnInit } from '@angular/core';

import { DataService } from './../../core/data/data.service';

@Component({
  selector: 'of-server-info',
  templateUrl: './server-info.component.html',
  styles: [`
    .table {
      margin-top: 0;
    }
    .list-unstyled {
      margin-bottom: 0px;
    }
  `]
})
export class ServerInfoComponent implements OnInit {

  serverInfo: any = {
    systemInfo: {},
    memoryInfo: {}
  };
  spis = new Collections.Dictionary<String, any>();

  tab = 'serverInfo';

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.loadData();
  }

  loadData(): void {
    this.dataService.serverInfo().get().subscribe(
      (data) => {
        this.serverInfo = data;

        this.spis = new Collections.Dictionary<String, any>();
        for (const key in data['providers']) {
          if (key) {
            this.spis.setValue(key, data['providers'][key]);
          }
        }
      }
    );
  }

  changeTab(tabName: string): void {
    this.tab = tabName;
  }

}
