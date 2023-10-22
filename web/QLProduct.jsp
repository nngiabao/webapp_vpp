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
        <title>QLProduct</title>

        <script type="text/javascript">
            $(function () {
                $(document).on("click", "#editbutton", function () {
                    $.ajax({
                        type: "post",
                        url: "qlsp", //this is my servlet
                        data: {
                            action: "getProduct",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var product = $.parseJSON(msg);
                            $('#e_name').val(product.name);
                            $('#id').val(product.id);
                            $('#e_price').val(product.price);
                            $('#e_d_id').val(product.d_id);
                            $('#e_c_id').val(product.c_id);
                            $('#e_i_id').val(product.i_id);
                            $('#e_quant').val(product.quant);
                        }

                    });
                });

                $(document).on("click", "#deletebutton", function () {
                });
                
                $(document).on("click", "#editIbutton", function () {
                    var p_name = $(this).closest('tr').find('#p_name').attr("value");
                    $.ajax({
                        type: "post",
                        url: "qlsp", //this is my servlet
                        data: {
                            action: "getStock",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var inven = $.parseJSON(msg);
                            $('#i_name').val( p_name );
                            $('#i_d_date').val(inven.deleted_date);
                            $('#i_m_date').val(inven.modified_date);
                            $('#i_c_date').val(inven.created_date);
                            $('#i_quan').val(inven.quan);
                            if(inven.deleted_at != null) $('#iSubmit_btn').attr("type","hidden");
                        }

                    });
                });
            });
            function checkEmail()
            {
                $.ajax({
                    type: "get",
                    url: "user", //this is my servlet
                    data: {
                        action: "checkEmail",
                        email: $("#email").val()
                    },
                    success: function (msg) {
                        $("#alert-email").removeAttr("hidden");
                        $("#alert-email").text(msg);
                    }
                });
            }



            function checkPhone()
            {
                $.ajax({
                    type: "get",
                    url: "user", //this is my servlet
                    data: {
                        action: "checkPhone",
                        email: $("#email").val()
                    },
                    success: function (msg) {
                        $('#output').append(msg);
                    }
                });
            }
        </script>
    </head>
    <c:if test="${not empty nofi}">
         <!-- <script type="text/javascript">
            window.addEventListener('load', function () {
                alert("${nofi}");
            });
        </script> -->
    </c:if>
    <body id="page-top">

        <!-- Begin Page Content -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <h1 class="h3 mb-2 text-gray-800">List Product</h1>
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
                                    <th>Price</th>
                                    <th>Img</th>
                                    <th>Discount_ID</th>
                                    <th>Inventory_ID</th>
                                    <th>In stock</th>
                                    <th>Category_ID</th>
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
                                    <th>Price</th>
                                    <th>Img</th>
                                    <th>Discount_ID</th>
                                    <th>Inventory_ID</th>
                                    <th>In stock</th>
                                    <th>Category_ID</th>
                                    <th>Created_at</th>
                                    <th>Modified_at</th>
                                    <th>Deleted_at</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                            <tbody>
                                <c:forEach items="${products}" var="p">
                                    <tr>
                                        <td>${p.id}</td>
                                        <td id="p_name" value="${p.name}">${p.name}</td>
                                        <td>${p.price}</td>
                                        <td>${p.img}</td>
                                        <td>${p.d_id}</td>
                                        <td>${p.i_id}</td>
                                        <td>${p.quant}<button class='btn btn-default' id="editIbutton" value="${p.i_id}" data-target="#editIModal" data-toggle="modal"><em class='fa fa-pencil-ruler'></em></button></td>
                                        <td>${p.c_id}</td>
                                        <td>${p.created_at}</td>
                                        <td>${p.modified_at}</td>
                                        <td>${p.deleted_at}</td>
                                        <td><button class='btn btn-default' id="editbutton" value="${p.id}" data-target="#editModal" data-toggle="modal"><em class='fa fa-pencil-ruler'></em></button>
                                            <button class='btn btn-default' id="deletebutton" value="${p.id}" data-target="#deleteModal" data-toggle="modal"><em class='fa fa-trash'></em></button></td>
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
            <form action="qlsp" method="post" enctype='multipart/form-data'>
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add Product</h5>
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
                        <label for="recipient-name" class="col-form-label">Price</label>
                        <input type="number" min="0" class="form-control" id="" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Quantity</label>
                        <input type="number" min="0" class="form-control" id="" name="quant" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Discount</label>
                        <select width="100%" name="d_id" id="d_id">
                            <c:forEach items="${discount}" var="l">
                                <option value="${l.id}">${l.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Category</label>
                        <select width="100%" name="c_id" id="c_id">
                            <c:forEach items="${category}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Image</label>
                        <input type="file" class="form-control" id="" name="image" accept="image/png, image/jpeg" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="add" name="action"></input>
                    <input value="Submit" type="submit" class="btn btn-primary"></input>
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
                        <input type="text" class="form-control" id="e_name" name="e_name">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Price</label>
                        <input type="number" class="form-control" id="e_price" name="e_price">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Quantity</label>
                        <input type="number" min="0" class="form-control" id="e_quant" name="e_quant" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Discount ID</label>
                        <select name="e_d_id" id="e_d_id">
                            <c:forEach items="${discount}" var="l">
                                <option value="${l.id}">${l.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Category</label>
                        <select width="100%" name="e_c_id" id="e_c_id">
                            <c:forEach items="${category}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Image</label>
                        <input type="file" class="form-control" id="e_image" name="image" accept="image/png, image/jpeg" >
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="e_i_id"  name="e_i_id"></input>
                    <input type="hidden" id="id"  name="id"></input>
                    <input type="hidden" value="edit" name="action"></input>
                    <input value="Xac nhan" type="submit" class="btn btn-primary"></input>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- edit InventoryModal-->

<div class="modal fade" id="editIModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qlsp"  method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Sotck</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="i_name" name="name">
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Quantity</label>
                        <input type="number" min="0" class="form-control" id="i_quan" name="quan" required>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Created at</label>
                        <input type="text" class="form-control" id="i_c_date" name="" readonly>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Modfied at</label>
                        <input type="text" class="form-control" id="i_m_date" name="m_date" readonly>
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Deleted at</label>
                        <input type="text" class="form-control" id="i_d_date" name="e_price" readonly="">
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="edit_quan" name="action"></input>
                    <input value="Submit" type="submit" id="iSubmit_btn" class="btn btn-primary"></input>
                </div>
            </form>
        </div>
    </div>
</div>



</body>

</html>