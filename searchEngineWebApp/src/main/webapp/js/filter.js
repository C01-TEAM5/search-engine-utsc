// Load the Visualization API and the corechart package.
google.charts.load('current', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawChart);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChart() {

  // File Type
  // Create the data table.
  var fileTyepData = new google.visualization.DataTable();
  fileTyepData.addColumn('string', 'File Type');
  fileTyepData.addColumn('number', 'Occurence');
  fileTyepData.addRows([
      ['DOCX', parseInt($("#docxNum").html())],
      ['HTML', parseInt($("#htmlNum").html())],
      ['PDF', parseInt($("#pdfNum").html())],
      ['TXT', parseInt($("#txtNum").html())]
  ]);
  // Set chart options
  var fileTypeoptions = {'title':'File Type Statistics', 'width':300, 'height':180};
  // Instantiate and draw our chart, passing in some options.
  var fileTypechart = new google.visualization.PieChart(document.getElementById('fileTypeFilter'));
  fileTypechart.draw(fileTyepData, fileTypeoptions);

  //File Owner
  var fileOwnerData = new google.visualization.DataTable();
  fileOwnerData.addColumn('string', 'File Owner');
  fileOwnerData.addColumn('number', 'Occurence');
  var OwnerTable = document.getElementById('OwnerData');
  for (var i=0; i<OwnerTable.rows.length; i++) {
       fileOwnerData.addRow([OwnerTable.rows[i].cells[0].innerHTML, parseInt(OwnerTable.rows[i].cells[1].innerHTML)]);
       //fileOwnerData.addRow(['sdfs', 12]);
       console.log(OwnerTable.rows[i].cells[0].innerHTML);
       console.log(parseInt(OwnerTable.rows[i].cells[1].innerHTML));
  }
  var len = OwnerTable.rows.length;
  console.log(len);
  var fileOwneroptions = {'title' : 'File Owner Statistics', 'width':300, 'height':180};
  var fileOwnerchart = new google.visualization.PieChart(document.getElementById('fileOwnerFilter'));
  fileOwnerchart.draw(fileOwnerData, fileOwneroptions);

  //Course Code
  var courseData = new google.visualization.DataTable();
  courseData.addColumn('string', 'Course Code');
  courseData.addColumn('number', 'Occurence');
  var CourseTable = document.getElementById('CourseData');
  for (var i=0; i<CourseTable.rows.length; i++) {
       courseData.addRow([CourseTable.rows[i].cells[0].innerHTML, parseInt(CourseTable.rows[i].cells[1].innerHTML)]);
  }
  var courseoptions = {'title' : 'Course Code Statistics', 'width':300, 'height':180};
  var coursechart = new google.visualization.PieChart(document.getElementById('courseCodeFilter'));
  coursechart.draw(courseData, courseoptions);

}
