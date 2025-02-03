<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Admin Dashboard</title>
		<style type="text/css">
			a.active-home {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}

			.links-section {
				display: flex;
				justify-content: center;
				align-self: start;
				flex-wrap: wrap;
		        margin-top: 10em;
			}

			.link {
				width: 200px;
				height: 150px;
				margin: 10px;
				border: 2px solid #ccc;
				border-radius: 4px;
				display: flex;
				align-items: center;
				justify-content: center;
				border-radius: 20px;
				background-color: #fff;
				transition: transform 0.3s ease-in-out;
			}

			.link:hover {
				transform: scale(1.1);
			}

			.card-link {
				text-decoration: none;
				color: #000;
				font-weight: bold;
				font-size: 20px;
				padding: 20px;
				border-radius: 20px;
			}

			.card-link:hover {
				background: none;
			}

			.link:hover{
				border:3px solid #774c3494;
			}

		</style>
	</head>

	<body>

		<!-- navbar -->
		<%@ include file="admin-navbar.jsp"%>

		<!-- Links section -->
		<div class="container">
			<div class="links-section">
				<div class="link">
					<a class="card-link" href="/admin/consumers">View Consumers</a>
				</div>
				<div class="link">
					<a class="card-link" href="/admin/connections">View Connections</a>
				</div>
				<div class="link">
					<a class="card-link" href="/admin/bills">View Bills</a>
				</div>
			</div>
		</div>

	</body>
</html>
