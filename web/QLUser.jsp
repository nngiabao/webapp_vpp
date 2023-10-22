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
        <title>QLUser</title>

        <script type="text/javascript">
            var eCheck = true, pCheck = true, admin = false;
            $(function () {
                $(document).on("click", "#editbutton", function () {
                    $.ajax({
                        type: "post",
                        url: "qluser", //this is my servlet
                        data: {
                            action: "getUser",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var user = $.parseJSON(msg);
                            $('#e_name').val(user.name);
                            $('#e_phone').val(user.phone);
                            $('#o_e_phone').val(user.phone);
                            $('#e_email').val(user.email);
                            $('#o_e_email').val(user.email);
                            $('#e_pass').val(user.pass);
                            $('#eSubmit_btn').val(user.id);
                            $('#u_c_id').val(user.u_c_id);
                        }
                    });
                });
                $(document).on("click", "#deletebutton", function () {
                    alert("qlsp?action=del&id=" + $(this).val());
                    document.getElementById('del_servlet').href = "qlsp?action=del&" + $(this).val();
                });
            });
            function add() {
                if (eCheck == false || pCheck == false) {
                    alert("Check email or phone number again");
                } else {
                    $.ajax({
                        type: "post",
                        url: "qluser", //this is my servlet
                        data: {
                            action: "add",
                            phone: $("#phone").val(),
                            email: $("#email").val(),
                            name: $("#name").val(),
                            pass: $("#pass").val(),
                            u_c_id: $("#u_c_id").val(),
                            id: id.value
                        },
                        success: function (msg) {
                            $('#addModal').modal('hide');
                        }
                    });
                }
            }

            function edit(id) {
                if (eCheck == false || pCheck == false) {
                    alert("Check email or phone number again");
                } else {
                    $.ajax({
                        type: "post",
                        url: "qluser", //this is my servlet
                        data: {
                            action: "edit",
                            phone: $("#e_phone").val(),
                            email: $("#e_email").val(),
                            name: $("#e_name").val(),
                            pass: $("#e_pass").val(),
                            u_c_id: $("#u_c_id").val(),
                            id: id.value
                        },
                        success: function (msg) {
                            $('#editModal').modal('hide');
                        }
                    });
                }
            }

            function checkEmail(email) {
                if ($('#o_e_email').val() != email.value)
                {
                    $.ajax({
                        type: "get",
                        url: "user", //this is my servlet
                        data: {
                            action: "checkEmail",
                            phone: $("#email").val()
                        },
                        success: function (msg) {
                            if (msg == 'Invalid Phone') {
                                check = false;
                            } else {
                                check = true;
                            }
                            alert(msg);
                        }
                    });
                } else {

                }

            }

            function checkPhone(phone)
            {
                console.log($('#o_e_phone').val());
                console.log(phone.value);
                if ($('#o_e_phone').val() != phone.value) {
                    console.log("cc");
                    $.ajax({
                        type: "get",
                        url: "user", //this is my servlet
                        data: {
                            action: "checkPhone",
                            phone: $("#phone").val()
                        },
                        success: function (msg) {
                            if (msg == 'Invalid Phone'){
                                check = false;
                            }else{
                                check = true;
                            }
                            alert(msg);
                        }
                    });
                }

            }


        </script>
    </head>
    <body id="page-top">

        <!-- Begin Page Content -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <h1 class="h3 mb-2 text-gray-800">User List</h1>
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
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Pass</th>
                                    <th>Phone</th>
                                    <th>Created at</th>
                                    <th>Modified at</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Pass</th>
                                    <th>Phone</th>
                                    <th>Created at</th>
                                    <th>Modified at</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                            <tbody>
                                <c:forEach items="${users}" var="u">
                                    <tr>
                                        <td>${u.id}</td>
                                        <td>${u.name}</td>
                                        <td>${u.email}</td>
                                        <td>${u.pass}</td>
                                        <td>${u.phone}</td>
                                        <td>${u.created_at}</td>
                                        <td>${u.modified_at}</td>
                                        <td><button class='btn btn-default' id="editbutton" data-target="#editModal" data-toggle="modal" value="${u.id}"><em class='fa fa-pencil-ruler'></em></button>
                                            <a class='btn btn-default' href='qluser?action=del'><em class='fa fa-trash'></em></td>
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

<!-- add Modal-->

<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qluser" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add User</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="output" name="output">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="name" name="name">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Phone</label>
                        <input type="text" onfocusout="checkPhone(this)" class="form-control" id="phone" name="phone">
                        <input id="alert-email" class="alert-light" type="hidden"></span>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Email</label>
                        <input type="text" onfocusout="checkEmail(this)" class="form-control" id="" name="email">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Pass</label>
                        <input type="text" class="form-control" id="pass" name="pass">
                    </div>
                    <c:if test="${sessionScope.user.u_c_id == 1}">
                        <div class="form-group" type="hidden">
                            <label for="recipient-name" class="col-form-label">Account type</label>
                            <select width="100%" name="u_c_id" id="u_c_id">
                                <option value="2">Manager</option>
                                <option value="3">User</option>
                            </select>
                        </div>
                    </c:if>
                    <c:if test="${sessionScope.user.u_c_id > 1}"><input type="hidden" class="form-control" id="u_c_id" value="1" name="email"></c:if>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="add" name="action"></input>
                    <button id="aSubmit_btn" onclick="add(this)" type="button" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- edit Modal-->

<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qluser">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit User</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">

                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="e_name" name="name">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Phone</label>
                        <input type="text" onfocusout="checkPhone(this)" class="form-control" id="e_phone" name="phone">
                        <input type="hidden" class="form-control" id="o_e_phone" name="email">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Email</label>
                        <input type="text" onfocusout="checkEmail(this)"  class="form-control" id="e_email" name="email">
                        <input type="hidden" class="form-control" id="o_e_email" name="email">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Pass</label>
                        <input type="text" class="form-control" id="e_pass" name="pass">
                    </div>
                    <c:if test="${sessionScope.user.u_c_id == 1}">
                        <div class="form-group" type="hidden">
                            <label for="recipient-name" class="col-form-label">Account type</label>
                            <select width="100%" name="e_u_c_id" id="u_c_id">
                                <option value="2">Manager</option>
                                <option value="3">User</option>
                            </select>
                        </div>
                    </c:if>
                    <c:if test="${sessionScope.user.u_c_id > 1}"><input type="hidden" class="form-control" id="u_c_id" name="email"></c:if>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="add" name="action"></input>
                    <button id="eSubmit_btn" onclick="edit(this)" type="button" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>



</body>

</html>