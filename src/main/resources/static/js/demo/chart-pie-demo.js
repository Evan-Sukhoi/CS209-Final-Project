// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["0-19", "20-39", "40-59", ">=60"],
    datasets: [{
      data: [507, 127, 14, 7],
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc','#c51e1e','#ddda1e','#dd8a18','#1fd632','#d023d3','#e2d0d0','#aeff00','#7e29c8','#007fff','#eb1592','#00f91a','#ffb700'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf','#971818','#c1bf1e','#bd7b20','#24b233','#ad23af','#c4a9a9','#9ecf34','#5c207e','#166dc4','#ca2184','#1dc12e','#ce9d20'],
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