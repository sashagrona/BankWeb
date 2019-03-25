<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <title>Convert</title>
</head>
<body>
<div align="center">
    <h1>Convertion</h1>
    <h2>Fill in blanks, please: </h2>
    <form action="/main" method="get">
        <table class="table table-bordered table-hover">
            <thead class="active">
            <tr>
                <th>Acc No</th>
                <th>User</th>
                <th>Money</th>
                <th>Currency</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="o">
                <tr>
                    <td><c:out value="${o.number}"/></td>
                    <td><c:out value="${o.user}"/></td>
                    <td><c:out value="${o.money}"/></td>
                    <td><c:out value="${o.rate}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table><br>
        Choose the account you wanna to convert<br><br>
        <p>Acc No:<input type="text" name="convert" class="input-medium search-query"></p>
        <p>Choose the currency:</p>
        <div class="form-check">
            <p class="text-info"><input type="radio" name="currency" value="UAH" class="form-check">UAH</p>
            <p class="text-info"><input type="radio" name="currency" value="USD" class="form-check">USD<br></p>
            <p class="text-info"><input type="radio" name="currency" value="EUR" class="form-check">EUR<br></p>
        </div>
        <input type="submit" value="Convert" class="btn btn-primary">
    </form>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>