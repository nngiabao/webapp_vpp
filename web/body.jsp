<%-- 
    Document   : body
    Created on : May 8, 2023, 11:09:05 AM
    Author     : askm4
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
            $(function () {
                var totalPages = ${maxP};
                //var visiblePages = ${maxP};
                var currentPage = ${current};
                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: totalPages,
                    visiblePages: totalPages,
                    startPage: currentPage,
                    onPageClick: function (event, page) {
                        if (currentPage != page) {
                            $('#page').val(page);
                            $('#pagenav').submit();
                        }
                    }
                });
            });
            function addCart(id) {
                $.ajax({
                    type: "post",
                    url: "cart", //this is my servlet
                    data: {
                        action: "addCart",
                        id: id.value
                    },
                    success: function (msg) {
                        if (msg == "Out of stock") {
                            alert(msg);
                        } else {
                            $('#cart_quan').html(msg);
                            alert('Add cart succesfully');
                        }

                    }
                });
            }
        </script>
    </head>
    <body>
        <!-- Products Start -->
        <div class="container-fluid pt-5">
            <div class="text-center mb-4">
                <h1 class="section-title px-5"><span class="px-2">San pham</span></h1>
            </div>
            <form id="pagenav" action="<c:url value='/index'/>" method="get">
                <div class="row px-xl-5 pb-3">
                    <c:forEach items="${products}" var="p">
                        <div class="col-lg-3 col-md-6 col-sm-12 pb-1">
                            <div class="card product-item border-0 mb-4">
                                <div class="card-header product-img position-relative overflow-hidden bg-transparent border p-0">
                                    <img style="height:400px; weight:400px" src="img/${p.img}" alt="">
                                </div>
                                <div class="card-body border-left border-right text-center p-0 pt-4 pb-3">
                                    <h6 class="text-truncate mb-3">${p.name}</h6>
                                    <div class="d-flex justify-content-center">
                                        <h6>${p.price}</h6><h6 class="text-muted ml-2"></h6>
                                    </div>
                                </div>
                                <div class="card-footer d-flex justify-content-between bg-light border">
                                    <button onclick="addCart(this)" type="button" class="btn btn-sm text-dark p-0" value="${p.id}"><i class="fas fa-shopping-cart text-primary mr-1"></i>Add To Cart</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <ul class="pagination" id="pagination"></ul>
                <input type="hidden" value="" id="page" name="page">
            </form>
        </div>
        <!-- Products End -->
        <script type="text/javascript">

        </script>
    </body>
</html>
