import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SchedulerService } from 'src/app/services/scheduler.service';
import { NgxSpinnerService } from 'ngx-spinner';

declare var $;
@Component({
  selector: 'app-plans',
  templateUrl: './plans.component.html',
  styleUrls: ['./plans.component.scss']
})
export class PlansComponent implements OnInit {
  public planData: any = [];
  public plansData: any = [];
  public dataTable: any;
  public dtOptions: any = {};
  @ViewChild('plansTable') Table;

  constructor(private schedulerService: SchedulerService, private router: Router, private spinner: NgxSpinnerService) { 
  }

  ngOnInit() {
    this.dtOptions = {
      dom: '<"top"Bf>rt<"bottom"lip><"clear">',
      buttons: [
          {
            extend: 'excel',
            text: 'Download',
            filename: 'Plans',
            title: 'Below are the Plans loaded to the Tool. For any change, please contact the Administrator',
            exportOptions: {
              columns: ":visible"
            }
          },
          {
            extend: 'colvis',
            text: 'Columns'
          }
      ],
      language: {
        emptyTable: "Add Parent(s) to show data for the Plan"
      }
    };
    this.loadPlans();
  }

  loadPlans() {
    this.spinner.show();
    this.planData = this.schedulerService.getPlan();
    this.schedulerService.getPlans(this.planData).subscribe(
      data => {
        this.plansData = data;
        this.dataTable = $(this.Table.nativeElement);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions); this.spinner.hide();}, 10);
      }
    );
  }

  loadAllPlans() {
    this.spinner.show();
    var table = $(this.Table.nativeElement).DataTable();
    table.clear().destroy();
    this.schedulerService.getAllPlans().subscribe(
      data => {
        this.plansData = data;
        this.dataTable = $(this.Table.nativeElement);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions);this.spinner.hide();}, 1000);
      }
    );
  }
}
