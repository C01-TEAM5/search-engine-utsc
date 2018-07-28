var query = document.getElementById("query").innerHTML;
console.log(query);
var perm = document.getElementById("perm").innerHTML;
console.log(perm);
var type = document.getElementById("type").innerHTML;
console.log(type);

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
  // click filter
  function selectTypeHandler() {
      var selectType = fileTypechart.getSelection()[0];
      if (selectType) {
          var type = fileTyepData.getValue(selectType.row, 0);
          // filter document type
          if (type=="PDF") {
              console.log(type);
              //console.log(query);
              document.getElementById("searchPdf").checked = true;
          } else if (type=="TXT") {
              document.getElementById("searchTxt").checked = true;
          } else if (type=="HTML") {
              document.getElementById("searchHtml").checked = true;
          } else if (type=="DOCX") {
              document.getElementById("searchDocx").checked = true;
          }
      }
      // query string
      document.getElementById("search").value = query;
      // owner type
      if (perm=="0") {
          document.getElementById("perm-all").click();
      } else if (perm=="3") {
          document.getElementById("perm-instructor").click();
      } else if (perm=="2") {
          document.getElementById("perm-student").click();
      }
      document.getElementById("submit").click();
  }
  google.visualization.events.addListener(fileTypechart, 'select', selectTypeHandler);
  fileTypechart.draw(fileTyepData, fileTypeoptions);


  //File Owner
  var fileOwnerData = new google.visualization.DataTable();
  fileOwnerData.addColumn('string', 'File Owner');
  fileOwnerData.addColumn('number', 'Occurence');
  var OwnerTable = document.getElementById('OwnerData');
  for (var i=0; i<OwnerTable.rows.length; i++) {
       fileOwnerData.addRow([OwnerTable.rows[i].cells[0].innerHTML, parseInt(OwnerTable.rows[i].cells[1].innerHTML)]);
  }
  var len = OwnerTable.rows.length;
  // console.log(len);
  var fileOwneroptions = {'title' : 'File Owner Statistics', 'width':300, 'height':180};
  var fileOwnerchart = new google.visualization.PieChart(document.getElementById('fileOwnerFilter'));
  function selectOwnerHandler() {
      var selectOwner = fileOwnerchart.getSelection()[0];
      if (selectOwner) {
          var owner = fileOwnerData.getValue(selectOwner.row, 0);
          document.getElementById("search").value = query + " owner:" + owner;
      }
      // owner type
      if (perm=="0") {
          document.getElementById("perm-all").click();
      } else if (perm=="3") {
          document.getElementById("perm-instructor").click();
      } else if (perm=="2") {
          document.getElementById("perm-student").click();
      }
      // file type
      if (parseInt($("#docxNum").html()) > 0) {
            document.getElementById("searchDocx").checked = true;
      } else  if (parseInt($("#htmlNum").html()) > 0) {
            document.getElementById("searchHtml").checked = true;
      } else if (parseInt($("#pdfNum").html()) > 0) {
            document.getElementById("searchPdf").checked = true;
      } else  if (parseInt($("#txtNum").html()) > 0) {
            document.getElementById("searchTxt").checked = true;
      }
      document.getElementById("submit").click();
  }
  google.visualization.events.addListener(fileOwnerchart, 'select', selectOwnerHandler);
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
  function selectCourseHandler() {
      var selectCourse = coursechart.getSelection()[0];
      if (selectCourse) {
          var course = courseData.getValue(selectCourse.row, 0);
          document.getElementById("search").value = "courseCode:" + course;
      }
      // owner type
      if (perm=="0") {
          document.getElementById("perm-all").click();
      } else if (perm=="3") {
          document.getElementById("perm-instructor").click();
      } else if (perm=="2") {
          document.getElementById("perm-student").click();
      }
      // file type
      if (parseInt($("#docxNum").html()) > 0) {
            document.getElementById("searchDocx").checked = true;
      } else  if (parseInt($("#htmlNum").html()) > 0) {
            document.getElementById("searchHtml").checked = true;
      } else if (parseInt($("#pdfNum").html()) > 0) {
            document.getElementById("searchPdf").checked = true;
      } else  if (parseInt($("#txtNum").html()) > 0) {
            document.getElementById("searchTxt").checked = true;
      }
      document.getElementById("submit").click();
  }
  google.visualization.events.addListener(coursechart, 'select', selectCourseHandler);
  coursechart.draw(courseData, courseoptions);

}
