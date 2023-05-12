// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var ctx = document.getElementById("myPieChart1");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["0-9", "10-19", "20-29", "30-39", "40-49", "50-59", ">=60"],
    datasets: [{
      data: [382, 125, 76, 51, 10, 4, 7],
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#c51e1e', '#ddda1e', '#dd8a18', '#1fd632'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#971818', '#c1bf1e', '#bd7b20', '#24b233'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});