import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SchedulerService } from 'src/app/services/scheduler.service';
import { NgxSpinnerService } from 'ngx-spinner';

declare var $;
@Component({
  selector: 'app-components',
  templateUrl: './components.component.html',
  styleUrls: ['./components.component.scss']
})
export class ComponentsComponent implements OnInit {
  public componentData: any = [];
  public dataTable: any;
  public dtOptions: any = {};
  @ViewChild('componentTable') Table;

  constructor(private schedulerService: SchedulerService, private router: Router, private spinner: NgxSpinnerService) { 
  }

  ngOnInit() {
    this.dtOptions = {
      dom: '<"top"Bf>rt<"bottom"lip><"clear">',
      buttons: [
          {
            extend: 'excel',
            text: 'Download',
            filename: 'Components',
            title: 'Below are the Components loaded to the Tool. For any change, please contact the Administrator',
            exportOptions: {
              columns: ":visible"
            }
          },
          {
            extend: 'colvis',
            text: 'Columns'
          }
      ]
    };
    this.loadComponents();
    //console.log(this.componentData);
  }

  loadComponents() {
    this.spinner.show();
    this.schedulerService.getComponents().subscribe(
      data => {
        this.componentData = data;
        this.dataTable = $(this.Table.nativeElement);
        //this.dataTable.dataTable(this.dtOptions);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions); this.spinner.hide()}, 1000);
      }
    );
  }
}
