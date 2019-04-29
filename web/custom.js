function closenotification() {
    $('#notification').fadeOut(1000);
}

function shownotification(msg) {
    $('#notification-message').html(msg);
    $('#notification').fadeIn(1000);
    setTimeout(function () {
        $('#notification').fadeOut(1000);
    }, 5000);
}

function confirmdeletion() {
    var cnf = confirm("Are you sure?\nAll information related to this will also be deleted!");
    return cnf;
}