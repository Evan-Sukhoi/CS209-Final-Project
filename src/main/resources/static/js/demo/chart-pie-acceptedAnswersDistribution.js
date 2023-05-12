// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var ctx = document.getElementById("myPieChart3");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["within 1 day", "1-7 days", "7-30 days", "30-180 days", "180-365days ", "above 1 year"],
    datasets: [{
      data: [368, 0, 0, 0, 0, 0],
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#c51e1e', '#ddda1e', '#dd8a18'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#971818', '#c1bf1e', '#bd7b20'],
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