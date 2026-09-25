<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Déclaration de la taglib JSTL Core --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <%-- Pour Jakarta EE 9+ / Servlet 5+ --%>
<%-- Remarque : Pour les anciennes versions Java EE (javax.*), utilisez uri="http://java.sun.com/jsp/jstl/core" --%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Utilisateurs</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            font-family: Arial, sans-serif;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>

    <h2>Liste des Utilisateurs</h2>

    <c:choose>
        <%-- Si la liste est vide ou nulle --%>
        <c:when test="${empty userList}">
            <p>Aucun utilisateur trouvé.</p>
        </c:when>

        <%-- Sinon, afficher le tableau --%>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${userList}" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.firstName}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>