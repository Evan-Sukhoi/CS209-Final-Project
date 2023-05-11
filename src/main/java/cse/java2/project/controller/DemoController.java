package cse.java2.project.controller;

import cse.java2.project.model.Questions;
import cse.java2.project.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;


@Controller
public class DemoController {
    private final QuestionService questionService;

    static String[] colors = new String[]{"#4e73df", "#1cc88a", "#36b9cc","#c51e1e","#ddda1e","#dd8a18","#1fd632","#d023d3","#e2d0d0","#aeff00","#7e29c8","#007fff","#eb1592","#00f91a","#ffb700"};
    static String[] hoverColors = new String[]{"#2e59d9", "#17a673", "#2c9faf", "#971818", "#c1bf1e", "#bd7b20", "#24b233", "#ad23af", "#c4a9a9", "#9ecf34", "#5c207e", "#166dc4", "#ca2184", "#1dc12e", "#ce9d20"};

    public DemoController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * This method is called when the user requests the root URL ("/") or "/demo".
     * In this demo, you can visit localhost:9090 or localhost:9090/demo to see the result.
     * @return the name of the view to be rendered
     * You can find the static HTML file in src/main/resources/templates/home.html
     */
    @GetMapping({"/","/Homepage"})
    public String login(Model model) {
        return "Homepage";
    }
    @GetMapping({ "/AnswersPage"})
    public String answers(Model model) {
        //The percentage of questions that has no answers
        Long[] data = new Long[2];
        data[1] = questionService.getQuestionRepository().findByAnswer_countRange(0, 0);
        data[0] = questionService.getQuestionRepository().findTheQuestionsSize() - data[1];
        double percentage1 = (double) data[1] / data[0];
        String per = NumberFormat.getInstance().format(percentage1);
        model.addAttribute("noAnswersPer",per);
        writeJS("chart-pie-noAnswersPer", new String[]{"Having", "Not having"}, data, "myPieChart");
        //The average and maximum number of answers
        double avg = questionService.getQuestionRepository().findByAnswer_countAverage();
        Long max = questionService.getQuestionRepository().findByAnswer_countMax();
        model.addAttribute("avg",avg);
        model.addAttribute("max",max);
        //The distribution of the number of answers
        Long[] data1 = new Long[7];
        data1[0] = questionService.getQuestionRepository().findByAnswer_countRange(0, 9);
        data1[1] = questionService.getQuestionRepository().findByAnswer_countRange(10, 19);
        data1[2] = questionService.getQuestionRepository().findByAnswer_countRange(20, 29);
        data1[3] = questionService.getQuestionRepository().findByAnswer_countRange(30, 39);
        data1[4] = questionService.getQuestionRepository().findByAnswer_countRange(40, 49);
        data1[5] = questionService.getQuestionRepository().findByAnswer_countRange(50, 59);
        data1[6] = questionService.getQuestionRepository().findByAnswer_countRange(60);
        writeJS("chart-pie-answersDistribution", new String[]{"0-9", "10-19", "20-29", "30-39", "40-49", "50-59",">=60"}, data1, "myPieChart1");
        //The percentage of having an accepted answer
        Long[] data2 = new Long[2];
        data2[1] = questionService.getQuestionRepository().findByHavingAcceptedAnswers();
        data2[0] = questionService.getQuestionRepository().findTheQuestionsSize() - data2[1];
        double percentage2 =  (double) data2[1] / data2[0];
        String per1 = NumberFormat.getInstance().format(percentage2);
        model.addAttribute("acceptedAnswersPer",per1);
        System.out.println(percentage2);
        System.out.println(per1);
        writeJS("chart-pie-acceptedAnswersPer", new String[]{"Having", "Not having"}, data2, "myPieChart2");
        //The distribution of the question resolution duration
        Long[] data3 = new Long[6];
        data3[0] = questionService.getQuestionRepository().findByResolutionTimeRange(0, 1);
        data3[1] = questionService.getQuestionRepository().findByResolutionTimeRange(1, 7);
        data3[2] = questionService.getQuestionRepository().findByResolutionTimeRange(7, 30);
        data3[3] = questionService.getQuestionRepository().findByResolutionTimeRange(30, 180);
        data3[4] = questionService.getQuestionRepository().findByResolutionTimeRange(180, 365);
        data3[5] = questionService.getQuestionRepository().findByResolutionTimeRange(365);
        writeJS("chart-pie-acceptedAnswersDistribution", new String[]{"within 1 day", "1-7 days", "7-30 days", "30-180 days", "180-365days ", "above 1 year"}, data3, "myPieChart3");
        //The percentage of questions have non-accepted answers that have received more upvotes than the accepted answers
        double percentage3 = 2;
        Long[] data4 = new Long[6];
        String per2 = NumberFormat.getInstance().format(percentage3);
        model.addAttribute("answerNotWillPer", per2);
        writeJS("chart-pie-acceptedAnswersNotWillPer", new String[]{"More upvotes", "Not more upvotes"}, data4, "myPieChart3");
        return "AnswersPage";
    }
    @GetMapping({ "/TagsPage"})
    public String tags() {
        return "TagsPage";
    }
    @GetMapping({ "/UsersPage"})
    public String users() {
        return "UsersPage";
    }

    private static void writeJS(String name , String[] names, Long[] data, String chartId) {
        String filePath = "src/main/resources/static/js/demo/" + name +".js";
        StringBuilder str = new StringBuilder();
        str.append("""
                // Set new default font family and font color to mimic Bootstrap's default styling
                Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
                Chart.defaults.global.defaultFontColor = '#858796';

                // Pie Chart Example
                var ctx = document.getElementById(\"""").append(chartId).append("\");\n" ).append("var myPieChart = new Chart(ctx, {\n" ).append("  type: 'doughnut',\n" ).append("  data: {\n" ).append("    labels: [\"" );
        for (int i = 0; i < names.length; i++) {
            str.append(names[i]);
            if (i == names.length - 1) {
                str.append("\"],\n    datasets: [{\n      data: [");
            } else {
                str.append("\", \"");
            }
        }
        for (int i = 0; i < data.length; i++) {
            str.append(data[i]);
            if (i == names.length - 1) {
                str.append("],\n      backgroundColor: ['");
            } else {
                str.append(", ");
            }
        }
        for (int i = 0; i < data.length; i++) {
            str.append(colors[i]);
            if (i == names.length - 1) {
                str.append("'],\n      hoverBackgroundColor: ['");
            } else {
                str.append("', '");
            }
        }
        for (int i = 0; i < data.length; i++) {
            str.append(hoverColors[i]);
            if (i == names.length - 1) {
                str.append("""
                        '],
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
                        });""");
            } else {
                str.append("', '");
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(str.toString());
            fileWriter.close();
            System.out.println("JavaScript file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the JavaScript file.");
            e.printStackTrace();
        }
    }
}
