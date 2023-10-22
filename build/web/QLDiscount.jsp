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
        <title>QLDiscount</title>

        <script type="text/javascript">
            var check = true;
            $(function () {
                $(document).on("click", "#editbutton", function () {
                    $.ajax({
                        type: "get",
                        url: "qlgg", //this is my servlet
                        data: {
                            action: "getDiscount",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var discount = $.parseJSON(msg);
                            $('#e_percent').val(discount.discount_percent);
                            $('#e_name').val(discount.name);
                            $('#e_active').val(discount.active);
                            $('#e_coupon').val(discount.coupon);
                            $('#d_id').val(discount.id);
                            if (discount.deleted_at != null) {
                                $('#eSubmit_btn').attr("type", "hidden");
                            } else {
                                $('#eSubmit_btn').attr("type", "button");
                            }
                        }
                    });
                });

                $(document).on("click", "#aSubmit_btn", function () {
                    if (check) {
                        $.ajax({
                            type: "get",
                            url: "qlgg", //this is my servlet
                            data: {
                                action: "add",
                                percent: $('#percent').val(),
                                name: $('#name').val(),
                                active: $('#active').val(),
                                coupon: $('#coupon').val(),
                            },
                            success: function (msg) {
                                alert("Success");
                                location.reload();
                            }
                        });
                    } else {
                        alert("Check your coupon again");
                    }
                });

                $(document).on("click", "#eSubmit_btn", function () {
                    if (check) {
                        $.ajax({
                            type: "get",
                            url: "qlgg", //this is my servlet
                            data: {
                                action: "edit",
                                percent: $('#e_percent').val(),
                                name: $('#e_name').val(),
                                active: $('#e_active').val(),
                                coupon: $('#e_coupon').val(),
                                id: $('#d_id').val()
                            },
                            success: function (msg) {
                                alert("Success");
                                location.reload();
                            }
                        });
                    } else {
                        alert("Check your coupon again");
                    }
                });
            });

            function checkCoupon(id)
            {
                var coupon = $(id).val();
                if (coupon !== '') {
                    $.ajax({
                        type: "get",
                        url: "qlgg", //this is my servlet
                        data: {
                            action: "checkCoupon",
                            coupon: coupon
                        },
                        success: function (msg) {
                            if (msg == "Invalid coupon") {
                                check = false;
                            } else {
                                check = true;
                            }
                            alert(msg);
                        }
                    });
                }
            }
        </script>
    </head>
    <c:if test="${not empty nofi}">
        <script type="text/javascript">
            window.addEventListener('load', function () {
                alert("${nofi}");
            });
        </script>
    </c:if>
    <body id="page-top">

        <!-- Begin Page Content -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <h1 class="h3 mb-2 text-gray-800">Discount List</h1>
            <div class="d-sm-flex align-items-center justify-content-between mb-4">
                <a data-target="#addModal" data-toggle="modal" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm">Add Discount</a>
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
                                    <th>Active</th>
                                    <th>Discount percent</th>
                                    <th>Coupon</th>
                                    <th>Created_at</th>
                                    <th>Modified_at</th>
                                    <th>Deleted_at</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Active</th>
                                    <th>Discount percent</th>
                                    <th>Coupon</th>
                                    <th>Created_at</th>
                                    <th>Modified_at</th>
                                    <th>Deleted_at</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                            <tbody>
                                <c:forEach items="${discount}" var="d">
                                    <tr>
                                        <td>${d.id}</td>
                                        <td>${d.name}</td>
                                        <td>${d.active}</td>
                                        <td>${d.discount_percent}</td>
                                        <td>${d.coupon}</td>
                                        <td>${d.created_at}</td>
                                        <td>${d.modified_at}</td>
                                        <td>${d.deleted_at}</td>
                                        <td><button class='btn btn-default' id="editbutton" value="${d.id}" data-target="#editModal" data-toggle="modal"><em class='fa fa-pencil-ruler'></em></button>
                                            <a class='btn btn-default' href="qlgg?action=delete&id=${d.id}"><em class='fa fa-trash'></em></a></td>
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



<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qlsp" method="post" enctype='multipart/form-data'>
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add Discount</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" >
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Discount Percent</label>
                        <input type="number" min="0" max="100" class="form-control" id="percent" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Coupon</label>
                        <input type="text" onfocusout="checkCoupon(this)"  class="form-control" id="coupon"  required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Active</label>
                        <select width="100%" name="d_id" id="active">
                            <option value="0">No</option>
                            <option value="1">Yes</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="edit" name="action"></input>
                    <input  id="aSubmit_btn" value="Submit" type="button" class="btn btn-primary"></input>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- edit Modal-->

<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qlsp"  method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Product</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="e_name" name="e_name" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Discount Percent</label>
                        <input type="number" min="0" max="100" class="form-control" id="e_percent" name="e_percent" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Coupon</label>
                        <input type="text" onfocusout="checkCoupon(this)"  class="form-control" id="e_coupon"  required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Active</label>
                        <select width="100%" name="d_id" id="e_active">
                            <option value="0">No</option>
                            <option value="1">Yes</option>
                        </select>
                    </div>

                </div>
                <div class="modal-footer">
                    <input type="hidden" id="d_id" name="action"></input>
                    <input value="Submit" id="eSubmit_btn" type="button" class="btn btn-primary"></input>
                </div>
            </form>
        </div>
    </div>
</div>
</body>

</html>