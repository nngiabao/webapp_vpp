<%-- 
    Document   : shoppingcart
    Created on : Oct 5, 2023, 10:51:39 AM
    Author     : askm4
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>EShopper - Bootstrap Shop Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Free HTML Templates" name="keywords">
        <meta content="Free HTML Templates" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet"> 

        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
        <!-- JavaScript Libraries -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>

        <!-- Contact Javascript File -->
        <script src="mail/jqBootstrapValidation.min.js"></script>
        <script src="mail/contact.js"></script>

        <!-- Template Javascript -->
        <script src="js/main.js"></script>
        <script type="text/javascript">
            function plus(id) {
                var ancestor = $(id).closest('tr');
                var subtotal = $('#subtotal').attr("value");
                var total = ancestor.find('#total').attr("value");
                var price = ancestor.find('#price').attr("value");
                var quan = parseInt(ancestor.find('#quan').val()) + 1;
                subtotal = subtotal - total;
                $.ajax({
                    type: "get",
                    url: "cart", //this is my servlet
                    data: {
                        action: "setQuan",
                        id: id.value,
                        quan: quan
                    },
                    success: function (msg) {
                        subtotal = subtotal + price * quan;
                        total = price * quan;
                        $('#subtotal').html(subtotal);
                        $('#subtotal').attr("value", subtotal);
                        ancestor.find('#total').html(total);
                        ancestor.find('#total').attr("value", total);
                        $('#last_total').html(subtotal);
                        $('#last_total').attr("value", subtotal);
                        ancestor.find('#quan').html(quan);
                        ancestor.find('#quan').val(quan);
                        $('#total_discount').html(0);
                        $('#total_discount').attr("value", 0);
                        $(cart_quan).html(msg);
                    }
                });
            }

            function minus(id) {
                var ancestor = $(id).closest('tr');
                var subtotal = $('#subtotal').attr("value");
                var total = ancestor.find('#total').attr("value");
                var price = ancestor.find('#price').attr("value");
                var quan = parseInt(ancestor.find('#quan').val()) - 1;
                subtotal = subtotal - total;
                $.ajax({
                    type: "get",
                    url: "cart", //this is my servlet
                    data: {
                        action: "setQuan",
                        id: id.value,
                        quan: quan
                    },
                    success: function (msg) {
                        subtotal = subtotal + price * quan;
                        total = price * quan;
                        $('#subtotal').html(subtotal);
                        $('#subtotal').attr("value", subtotal);
                        ancestor.find('#total').html(total);
                        ancestor.find('#total').attr("value", total);
                        $('#last_total').html(subtotal);
                        $('#last_total').attr("value", subtotal);
                        ancestor.find('#quan').html(quan);
                        ancestor.find('#quan').val(quan);
                        $('#total_discount').html(0);
                        $('#total_discount').attr("value", 0);
                        $(cart_quan).html(msg);
                    }
                });
            }

            function setQuan(id) {
                var ancestor = $(id).closest('tr');
                var subtotal = $('#subtotal').attr("value");
                var total = ancestor.find('#total').attr("value");
                console.log(total);
                var price = ancestor.find('#price').attr("value");
                var quan = id.value;
                subtotal = subtotal - total;
                $.ajax({
                    type: "get",
                    url: "cart", //this is my servlet
                    data: {
                        action: "setQuan",
                        id: ancestor.find('#p_id').val(),
                        quan: quan
                    },
                    success: function (msg) {
                        subtotal = subtotal + price * quan;
                        total = price * quan;
                        $('#subtotal').html(subtotal);
                        $('#subtotal').attr("value", subtotal);
                        ancestor.find('#total').html(total);
                        ancestor.find('#total').attr("value", total);
                        $('#last_total').html(subtotal);
                        $('#last_total').attr("value", subtotal);
                        $('#total_discount').html(0);
                        $('#total_discount').attr("value", 0);
                        $(cart_quan).html(msg);
                    }
                });
            }

            function removeCart(id) {
                var ancestor = $(id).closest('tr');
                var subtotal = $('#subtotal').attr("value");
                var total = ancestor.find('#total').attr("value");
                $.ajax({
                    type: "get",
                    url: "cart", //this is my servlet
                    data: {
                        action: "removeCart",
                        p_id: id.value
                    },
                    success: function (msg) {
                        subtotal = subtotal - total;
                        $('#subtotal').html(subtotal);
                        $('#subtotal').attr("value", subtotal);
                        $('#last_total').html(subtotal);
                        $('#last_total').attr("value", subtotal);
                        $('#total_discount').html(0);
                        $('#total_discount').attr("value", 0);
                        $(cart_quan).html(msg);
                        ancestor.remove();
                    }
                });
            }

            function getPromotion() {
                var coupon = $('#coupon').val();
                if ((coupon == '')) {
                    alert("Invalid coupon");
                } else {
                    $.ajax({
                        type: "post",
                        url: "cart", //this is my servlet
                        data: {
                            action: "getPromotion",
                            coupon: coupon
                        },
                        success: function (msg) {
                            if (msg != 'Invalid coupon') {
                                var subtotal = $('#subtotal').attr("value");
                                var last_total = subtotal - msg;
                                $('#last_total').html(last_total);
                                $('#last_total').attr("value", last_total);
                                $('#total_discount').html(msg);
                                $('#total_discount').attr("value", msg);
                            } else {
                                alert(msg);
                            }
                        }
                    });
                }
            }

            function checkOut() {
                $.ajax({
                    type: "post",
                    url: "cart", //this is my servlet
                    data: {
                        action: "checkOut",
                        total: $('#last_total').attr('value')
                    },
                    success: function (msg) {
                        if (msg == 'Order successfully') {
                            $('#subtotal').html(0);
                            $('#last_total').html(0);
                            $('#total_discount').html(0);
                            $('#cart_quan').html(0);
                            $('.table-responsive').html("<h1>Shopping cart is empty</h1>");
                            $('.table').remove();
                        }
                        alert(msg);

                    }
                });
            }


        </script>
    </head>

    <body>
        <jsp:include page="header.jsp"/>
        <!-- Page Header Start -->
        <div class="container-fluid bg-secondary mb-5">
            <div class="d-flex flex-column align-items-center justify-content-center" style="min-height: 300px">
                <h1 class="font-weight-semi-bold text-uppercase mb-3">Shopping Cart</h1>
                <div class="d-inline-flex">
                    <p class="m-0"><a href="">Home</a></p>
                    <p class="m-0 px-2">-</p>
                    <p class="m-0">Shopping Cart</p>
                </div>
            </div>
        </div>
        <!-- Page Header End -->


        <!-- Cart Start -->
        <div class="container-fluid pt-5">
            <div class="row px-xl-5">
                <div class="col-lg-8 table-responsive mb-5">
                    <c:choose>
                        <c:when test="${sessionScope.shoppingcart.size()==0 || sessionScope.shoppingcart == null}">
                            <h1>Shopping cart is empty</h1>
                            <c:set var="subtotal" value="${0}"/>
                        </c:when>
                        <c:otherwise>    
                            <table class="table table-bordered text-center mb-0">
                                <thead class="bg-secondary text-dark">
                                    <tr>
                                        <th>Products</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total</th>
                                        <th>Remove</th>
                                    </tr>
                                </thead>

                                <tbody class="align-middle">
                                    <c:forEach items="${listCartProduct}" var="p">    
                                        <tr>
                                            <td class="align-middle"><img src="img/${p.img}" alt="" style="width: 50px;">${p.name}</td>
                                            <td class="align-middle" id="price" value="${p.price}">${p.price}</td>
                                            <td class="align-middle">
                                                <div class="input-group quantity mx-auto" style="width: 100px;">
                                                    <div class="input-group-btn">
                                                        <button class="btn btn-sm btn-primary btn-minus" onclick="minus(this)" value="${p.id}">
                                                            <i class="fa fa-minus"></i>
                                                        </button>
                                                    </div>
                                                    <input type="hidden" id="p_id" class="form-control form-control-sm bg-secondary text-center" value="${p.id}">
                                                    <input type="text" onfocusout="setQuan(this)" id="quan" class="form-control form-control-sm bg-secondary text-center" value="${p.quant}">
                                                    <div class="input-group-btn">
                                                        <button type=button" class="btn btn-sm btn-primary btn-plus" onclick="plus(this)" value="${p.id}">
                                                            <i class="fa fa-plus"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="align-middle" id="total" value="${p.price*p.quant}">${p.price*p.quant}</td>
                                            <c:set var="subtotal" value="${subtotal+p.price*p.quant}" />
                                            <td class="align-middle"><button onclick="removeCart(this)" class="btn btn-sm btn-primary" value="${p.id}"><i class="fa fa-times"></i></button></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>

                            </table>
                        </c:otherwise>  
                    </c:choose>
                </div>
                <div class="col-lg-4">
                    <form class="mb-5" action="">
                        <div class="input-group">
                            <input type="text" class="form-control p-4" placeholder="Coupon Code" id="coupon">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="button" onclick="getPromotion()">Apply Coupon</button>
                            </div>
                        </div>
                    </form>
                    <div class="card border-secondary mb-5">
                        <div class="card-header bg-secondary border-0">
                            <h4 class="font-weight-semi-bold m-0">Cart Summary</h4>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-3 pt-1">
                                <h6 class="font-weight-medium">Subtotal</h6>
                                <h6 class="font-weight-medium" id="subtotal" value="${subtotal}">${subtotal}</h6>
                            </div>
                            <div class="d-flex justify-content-between">
                                <h6 class="font-weight-medium">Discount</h6>
                                <h6 class="font-weight-medium" id="total_discount" value="0">$0</h6>
                            </div>
                        </div>
                        <div class="card-footer border-secondary bg-transparent">
                            <div class="d-flex justify-content-between mt-2">
                                <h5 class="font-weight-bold">Total</h5>
                                <h5 class="font-weight-bold" id="last_total" value="${subtotal}">${subtotal}</h5>
                            </div>
                            <button class="btn btn-block btn-primary my-3 py-3" onclick="checkOut()">Proceed To Checkout</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Cart End -->

        <!-- Back to Top -->
        <a href="#" class="btn btn-primary back-to-top"><i class="fa fa-angle-double-up"></i></a>



    </body>

</html>
