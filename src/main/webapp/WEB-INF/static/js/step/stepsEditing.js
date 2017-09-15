$(function()
{
    $(document).on('click', '.add-step-icon', function(e)
    {
        var clickedEntry = $(this).parent().parent();

        $(clickedEntry).after("<div class=\"sortable-step-container\">\n" +
            "                                            <div class=\"step-info-handle\">\n" +
            "                                                <img src=\"/static/images/handle-icon.png\" class=\"handle-icon\">\n" +
            "                                                <div style=\"margin: 0; border: 1px dotted gray; width: 620px; float: left; padding: 5px;\">\n" +
            "                                                    <div class=\"select-step-type-container\">\n" +
            "                                                        <select class=\"step-type-select-tag\">\n" +
            "                                                            <option value=\"0\" disabled selected>-----</option>\n" +
            "                                                            <option value=\"1\">Given</option>\n" +
            "                                                            <option value=\"2\">When</option>\n" +
            "                                                            <option value=\"3\">Then</option>\n" +
            "                                                            <option value=\"4\">And</option>\n" +
            "                                                            <option value=\"5\">But</option>\n" +
            "                                                        </select>\n" +
            "                                                    </div>\n" +
            "                                                   <input type=\"text\" class=\"step-code-line\">\n" +
            "                                                <div style=\"clear: both; width: 0px;\"></div>\n" +
            "                                                </div>\n" +
            "                                                <img src=\"/static/images/addRow-icon.png\" class=\"add-step-icon\">\n" +
            "                                                <img src=\"/static/images/deleteStep-icon.png\" class=\"delete-step-icon\">\n" +
            "                                            </div>\n" +
            "                                        </div>");
    }).on('click', '.delete-step-icon', function(e)
    {

        var steps = $(".sortable-step-container");

        if (steps.length > 1) {
            var clickedEntry = $(this).parent().parent();
            clickedEntry.remove();
        }
    });
});

$( function() {
    $( "#steps_container" ).sortable({
        revert: true,
        handle: '.step-info-handle'
//                cancel: ''
    });
    $( "#steps_container" ).disableSelection();
} );