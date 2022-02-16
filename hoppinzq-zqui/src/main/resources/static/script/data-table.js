$(function () {
    "use strict";

    $('#example1').DataTable();
    $('#example2').DataTable({
        'paging': true,
        'lengthChange': false,
        'searching': false,
        'ordering': true,
        'info': true,
        'autoWidth': false
    });

    $('#example').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ]
    });

    $('#tickets').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': false,
    });

    $('#productorder').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': false,
    });


    $('#complex_header').DataTable();

    $('#example5 tfoot th').each(function () {
        var title = $(this).text();
        $(this).html('<input type="text" placeholder="搜索 ' + title + '" />');
    });

    var table = $('#example5').DataTable();

    table.columns().every(function () {
        var that = this;
        $('input', this.footer()).on('keyup change', function () {
            if (that.search() !== this.value) {
                that
                    .search(this.value)
                    .draw();
            }
        });
    });

    var table = $('#example6').DataTable();
    $('button').click(function () {
        var data = table.$('input, select').serialize();
        alert(
            "准备导出以下数据: \n\n" +
            data.substr(0, 120) + '...'
        );
        return false;
    });

});