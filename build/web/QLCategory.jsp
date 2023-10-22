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
        <title>QLCategory</title>

        <script type="text/javascript">
            var eCheck = true, pCheck = true, admin = false;
            $(function () {
                $(document).on("click", "#editbutton", function () {
                    $.ajax({
                        type: "post",
                        url: "qlloai", //this is my servlet
                        data: {
                            action: "getCategory",
                            id: $(this).val()
                        },
                        success: function (msg) {
                            var user = $.parseJSON(msg);
                            $('#name').val(user.name);
                            $('#id').val(user.id);
                            
                        }
                    });
                });
                
            });
        </script>
    </head>
    <body id="page-top">

        <!-- Begin Page Content -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <h1 class="h3 mb-2 text-gray-800">User List</h1>
            <div class="d-sm-flex align-items-center justify-content-between mb-4">
                <a data-target="#addModal" data-toggle="modal" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm">Add Category</a>
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
                                    <th>Created at</th>
                                    <th>Modified at</th>
                                    <th>Deleted at</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Created at</th>
                                    <th>Modified at</th>
                                    <th>Deleted at</th>
                                    <th></th>
                                </tr>
                            </tfoot>
                            <tbody>
                                <c:forEach items="${category}" var="c">
                                    <tr>
                                        <td>${c.id}</td>
                                        <td>${c.name}</td>
                                        <td>${c.created_at}</td>
                                        <td>${c.modified_at}</td>
                                        <td>${c.deleted_at}</td>
                                        <td><button class='btn btn-default' id="editbutton" data-target="#editModal" data-toggle="modal" value="${c.id}"><em class='fa fa-pencil-ruler'></em></button>
                                            <a href='qlloai?action=delete&id=${c.id}'><em class='fa fa-trash'></em></a></td>
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
            <form action="qlloai" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add Category</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="" name="name">
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="add" name="action"></input>
                    
                    <button id="aSubmit_btn" type="submit" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- edit Modal-->

<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form action="qlloai" method="">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Category</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">Name</label>
                        <input type="text" class="form-control" id="name" name="name">
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" value="edit" name="action"></input>
                    <input type="hidden" value="add" id="id" name="id"></input>
                    <button  type="submit" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>



</body>

</html>