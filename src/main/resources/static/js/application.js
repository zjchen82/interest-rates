var com = {};
com.ufinity = {};

com.ufinity.rc = {
	url: '/apis/interest-rates',
	state: {
	  'startMonth': '',
	  'endMonth': '',
	  'depositType': 'fixed_deposits_3m'
	},

	chartColors: {
	  red: 'rgb(255, 99, 132)',
	  orange: 'rgb(255, 159, 64)',
	  yellow: 'rgb(255, 205, 86)',
	  green: 'rgb(75, 192, 192)',
	  blue: 'rgb(54, 162, 235)',
	  purple: 'rgb(153, 102, 255)',
	  grey: 'rgb(201, 203, 207)'
	},
	
	getData: function(callback) {
    var params = {
      'startMonth': com.ufinity.rc.state.startMonth,
      'endMonth': com.ufinity.rc.state.endMonth
    };
    console.log(params);

	  jQuery.ajax({
		  'url': this.url,
		  'data': params,
		  'dataType': 'json'
		}).done(function(data) {
		  if (data.errors) {
		    alert("We have encountered a problem, please try again later or contact Helpdesk for assistant.");
		    return false;
		  }

      com.ufinity.rc.records = data.data;
      callback();
		}).fail(function() {
		  alert("We have encountered a problem, please try again later or contact Helpdesk for assistant.");
      return false;
		})
	},
	
	plotChart: function(records, depositType) {	  
	  // console.log(records);
	  
	  records = com.ufinity.rc.records;
	  depositType = com.ufinity.rc.state.depositType;
	  
	  console.log(depositType);
		
	  var tableContent = '';
	  var months = [];
	  var banksData = [];
	  var fcData = [];
	  
	  $.each(records, function(index, record) {
		
		months.push(record.end_of_month);
		if ('fixed_deposits_3m' === depositType) {
			banksData.push(record.banks_fixed_deposits_3m);
			fcData.push(record.fc_fixed_deposits_3m);
		} else if ('fixed_deposits_6m' === depositType) {
			banksData.push(record.banks_fixed_deposits_6m);
			fcData.push(record.fc_fixed_deposits_6m);
		} else if ('fixed_deposits_12m' === depositType) {
			banksData.push(record.banks_fixed_deposits_12m);
			fcData.push(record.fc_fixed_deposits_12m);
		} else if ('savings_deposits' === depositType) {
			banksData.push(record.banks_savings_deposits);
			fcData.push(record.fc_savings_deposits);
		} else {
			// fallback to default 
			banksData.push(record.banks_fixed_deposits_3m);
			fcData.push(record.fc_fixed_deposits_3m);
		}
		
	  });
	  
	  // console.log('fc avg: ' + fcData.average());
	  // console.log('bank avg: ' + banksData.average());
	  var fcAvg = fcData.average();
	  var banksAvg = banksData.average();
	  
	  var banksAvgData = [];
	  var fcAvgData = [];
	  var size = months.length;
	  while(size--) {
		  banksAvgData[size] = banksAvg;
		  fcAvgData[size] = fcAvg;
	  }
				
	  var chartData = {
		  labels: months,
		  datasets: [{
			label: 'Finacial Companies',
			borderColor: com.ufinity.rc.chartColors.blue,
			data: fcData,
			fill: false,
			backgroundColor: com.ufinity.rc.chartColors.blue
		  }, {
			label: 'Finacial Companies Average',
			borderColor: com.ufinity.rc.chartColors.blue,
			data: fcAvgData,
			fill: false,
			borderDash: [5, 5],
			backgroundColor: com.ufinity.rc.chartColors.blue
		  }, {
			label: 'Banks',
			borderColor: com.ufinity.rc.chartColors.red,
			data: banksData,
			fill: false,
			backgroundColor: com.ufinity.rc.chartColors.red
		  }, {
			label: 'Banks Average',
			borderColor: com.ufinity.rc.chartColors.red,
			data: banksAvgData,
			fill: false,
			borderDash: [5, 5],
			backgroundColor: com.ufinity.rc.chartColors.red
		  }]
		}
		com.ufinity.rc.renderChart(chartData);
	},
	
	renderChart: function(chartData) {
	  if (com.ufinity.rc.myChart) {
		// console.log('update chart with new data');
		com.ufinity.rc.myChart.data = chartData;
		com.ufinity.rc.myChart.update();
		
		return false;
	  }
	  
	  var ctx = document.getElementById("myChart");
	  com.ufinity.rc.myChart = new Chart(ctx, {
		type: 'line',
		data: chartData,
		
		options: {
			tooltips: {
				mode: 'index',
				intersect: true,
			},
			hover: {
				mode: 'index',
				intersect: true,
			},
		  responsive: true,
		  scales: {
			yAxes: [{
				type: 'linear', // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
				display: true,
				position: 'left'
			}],
		  },
		  legend: {
			display: true,
		  }
		}
	  });
	},
	
  formatDate: function (date) {
    var month = date.getMonth() + 1;
    month = month > 9 ? month : '0' + month;
    var year = date.getFullYear();

    return year + '-' + month;
  }

}

// provide all the UI related functions
com.ufinity.rc.ui = {
  // init date fields
  initDatePicker: function() {
    var lastDayOfPrevMonth = new Date();
    lastDayOfPrevMonth.setDate(1);
    lastDayOfPrevMonth.setHours(-1);

    $('.datepicker').datepicker({
      format: 'yyyy-mm',
      endDate: lastDayOfPrevMonth,
      startView: 'months',
      minViewMode: "months"
    });

    var defaultStartMonth = new Date();
    defaultStartMonth.setMonth(defaultStartMonth.getMonth() - 11);
    defaultStartMonth.setDate(1);
    defaultStartMonth.setHours(-1);

    var defaultStartMonthStr = com.ufinity.rc.formatDate(defaultStartMonth);
    var defaultEndMonthStr = com.ufinity.rc.formatDate(lastDayOfPrevMonth);
    $('#startMonth .datepicker').datepicker('update', defaultStartMonthStr);
    $('#endMonth .datepicker').datepicker('update', defaultEndMonthStr);

    com.ufinity.rc.state.startMonth = defaultStartMonthStr;
    com.ufinity.rc.state.endMonth = defaultEndMonthStr;
  },

  // handle the event when user choose different period to compare the information
  handlePeriodEvent: function(e) {
    e.preventDefault();
    var hasError = false;
    var monthFormatRex = /^(\d{4})-(\d{2})$/;

    // clear the feedback
    $('.invalid-feedback').removeClass('d-block').addClass('d-none').html('');

    // get the startMonth and endMonth;
    var startDate = $("#startMonth .datepicker").datepicker('getDate');
    if (!startDate) {
      // $('#startMonth').addClass('was-validated');
      $('#startMonth .invalid-feedback').removeClass('d-none').addClass('d-block').html('Please supply your month');
      // alert("Please select the start month or key in in yyyy-mm format.");
      hasError = true;
    } else {
      var startMonth = com.ufinity.rc.formatDate(startDate);
      if (!startMonth.match(monthFormatRex)) {
        $('#startMonth .invalid-feedback').removeClass('d-none').addClass('d-block').html("Please supply the month in YYYY-MM format, e.g. 2018-05.");
        hasError = true;
      }
    }

    var endDate = $("#endMonth .datepicker").datepicker('getDate');
    if (!endDate) {
      $('#endMonth .invalid-feedback').removeClass('d-none').addClass('d-block').html('Please supply your month');
      hasError = true;
    } else {
      var endMonth = com.ufinity.rc.formatDate(endDate);
      if (!endMonth.match(monthFormatRex)) {
      $('#endMonth .invalid-feedback').removeClass('d-none').addClass('d-block').html("Please supply the month in YYYY-MM format, e.g. 2018-05.");
      hasError = true;
      }
    }
    // console.log('start: ' + startMonth + ', end: ' + endMonth);

    if (hasError) {
      return false;
    }

    // retrieve the data and replot the chart
    com.ufinity.rc.state.startMonth = startMonth;
    com.ufinity.rc.state.endMonth = endMonth;

    com.ufinity.rc.state.depositType = $('.btn-toolbar button.active').data('deposit-type');
    com.ufinity.rc.getData(com.ufinity.rc.plotChart);

    return false;
  },

  // handle the event when user click to see different deposit type
  handleDepositTypeEvent: function() {
    $('.btn-toolbar button').removeClass('active');
    $(this).addClass('active');
    var depositType = $(this).data('deposit-type');
    com.ufinity.rc.state.depositType = depositType;
    com.ufinity.rc.plotChart();
  }

}

Array.prototype.sum = Array.prototype.sum || function() {
  return this.reduce(function(sum, a) { return sum + Number(a) }, 0);
}

Array.prototype.average = Array.prototype.average || function() {
  return this.sum() / (this.length || 1);
}

