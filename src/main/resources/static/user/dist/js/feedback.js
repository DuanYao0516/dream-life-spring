    $(document).ready(function () {
        $('#feedbackForm').on('submit', function (e) {
            e.preventDefault();
            $.ajax({
                type: 'POST',
                url: '/feedback',
                data: $(this).serialize(),
                success: function (response) {
                    if (response.resultCode === 200 || response.resultCode === '200') {
                        alert(response.data);
                        setTimeout(function () {
                            window.location.href = '/index';
                        }, 1000);
                    } else {
                        alert("提交失败，请重试！");
                    }
                },
                error: function (xhr, status, error) {
                    alert("提交过程中发生错误，请检查网络后重试。");
                }
            });
        });
    });