<%-- 
    Document   : QLUser
    Created on : Oct 2, 2023, 12:55:47 PM
    Author     : askm4
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <%@include file="adminindex.jsp" %>
        <title>QLSession</title>

        <script type="text/javascript">
            check = true;
            $(function () {
                $(document).on("click", "#detailbutton", function () {
                    $.ajax({
                        type: "post",
                        url: "qlsession", //this is my servlet
                        data: {
                            action: "getSessionDetail",
                            id: $(this).closest('tr').find('#u_id').attr("value"),
                        },
                        success: function (msg) {
                            $('#detail_table').html(msg);
                        }
                    });
                });
                $(document).on("click", "#editbutton", function () {
                    $('#detailModal').modal('hide');
                    var u_id = $(this).closest('tr').find('#d_u_id').val();
                    $.ajax({
                        type: "post",
                        url: "qlsession", //this is my servlet
                        data: {
                            action: "getSession",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var cart_items = JSON.parse(msg);
                            $('#c_id').val(cart_items.id);
                            $('#p_id').val(cart_items.p_id);
                            $('#quan').val(cart_items.quan);
                            $('#o_quan').val(cart_items.quan);
                            $('#e_u_id').val(u_id);
                            $('#eSubmit_btn').val(cart_items.s_id);
                            $('#editModal').modal('show');
                            
                        }
                    });
                });

                $(document).on("click", "#eSubmit_btn", function () {
                    if (check) {
                        $.ajax({
                            type: "post",
                            url: "qlsession", //this is my servlet
                            data: {
                                action: "edit",
                                id: $(this).val(),
                                p_id: $('#p_id').val(),
                                quan: $('#quan').val(),
                                u_id: $('#e_u_id').val()
                                
                            },
                            success: function (msg) {
                                $('#editModal').modal('hide');
                                alert("Edit successfully");
                                //location.reload();
                            }
                        });
                    } else {
                        alert("Check product quantity again");
                    }
                });
            });

            function checkQuan(id) {
                var p_id = $(id).closest('form').find('#p_id').val();
                $.ajax({
                    type: "post",
                    url: "qlsession", //this is my servlet
                    data: {
                        action: "checkQuan",
                        new_quan: id.value,
                        id: p_id
                    },
                    success: function (msg) {
                        if (msg == 'Valid quantity') {
                            check = true;
                        } else {
                            $('#quan').val($('#o_quan').val());
                            check = false;
                        }
                        alert(msg);
                    }
                });
            }


        </script>
    </head>
    <body id="page-top">

        <!-- Begin Page Content -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <h1 class="h3 mb-2 text-gray-800">Session List</h1>
            <div class="d-sm-flex align-items-center justify-content-between mb-4">
                <a data-target="#addModal" data-toggle="modal" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm">Them tai khoan</a>
            </div>
            <!-- DataTales Example -->
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>User ID</th>
                                    <th>Total</th>
                                    <th>Created_at</th>
                                    <th>Modified_at</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th>ID</th>
                                    <th>User ID</th>
                                    <th>Total</th>
                                    <th>Created_at</th>
                                    <th>Modified_at</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                            <tbody>
                                <c:forEach items="${session}" var="s">
                                    <tr>
                                        <td>${s.id}</td>
                                        <td id="u_id" value="${s.user_id}">${s.user_id}</td>
                                        <td>${s.total}</td>
                                        <td>${s.created_at}</td>
                                        <td>${s.modified_at}</td>
                                        <td><button class='btn btn-default' id="detailbutton" value="${s.id}" data-target="#detailModal" data-toggle="modal"><em class='fa fa-file'></em></button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.container-fluid -->

    </div>
    <!-- End of Main Content -->

</div>
<!-- End of Content Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- detail Modal -->

<div class="modal fade" style="max-width: 1400px;" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" >
    <div class="modal-dialog test" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Add Session</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" >
                <table class="table-bordered" cellspacing="0">
                    <thead>
                        <tr>
                            <th>CartItem ID</th>
                            <th>Product ID</th>
                            <th>Quantity</th>
                            <th>Created at</th>
                            <th>Modified at</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th>CartItem ID</th>
                            <th>Product ID</th>
                            <th>Quantity</th>
                            <th>Created at</th>
                            <th>Modified at</th>
                            <th></th>
                        </tr>
                    </tfoot>
                    <tbody id="detail_table">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- edit Modal-->

<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qlsession" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit User</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">CartItem ID</label>
                        <input type="text" class="form-control" id="c_id" name="s_id" readonly>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Product ID</label>
                        <input type="text" class="form-control" id="p_id" name="p_id" readonly>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Quantity</label>
                        <input type="number" min="0" onfocusout="checkQuan(this)" class="form-control" id="quan" name="quan" required>
                        <input type="hidden" class="form-control" id="o_quan" name="quan">
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="e_u_id" name="u_id"></input>
                    <input type="hidden" value="edit" name="action"></input>
                    <button id="eSubmit_btn" type="button" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>



</body>

</html>