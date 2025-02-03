<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
		<title>About</title>
		<style>
			a.active-about {
            	background: var(--c-menu-link-selected);
            	transition: 0.5s;
            }

            .about-container {
            	max-width: 50rem;   /* 800px */
            	margin: 1.25rem auto;
            	padding: 2.5rem;    /* 40px */
            	background-color: #fff;
            	border-radius: 0.3125rem; /* 5px */
            	box-shadow: 0 0 0.625rem rgba(0, 0, 0, 0.1); /* 10px */
            }

            h1 {
            	text-align: center;
            	margin-bottom: 1.875rem; /* 30px */
            }

            p {
            	line-height: 1.6;
            	margin-bottom: 1.25rem; /* 20px */
            }

            .highlight {
            	background-color: #eaf6ff;
            	padding: 0.625rem; /* 10px */
            	border-radius: 0.25rem; /* 4px */
            }

            .text-center {
            	text-align: center;
            }

            .quote {
            	font-style: italic;
            	font-size: 1.125rem; /* 18px */
            	margin-bottom: 1.875rem; /* 30px */
            }

            .mission {
            	font-weight: bold;
            	font-size: 1.375rem; /* 22px */
            	margin-bottom: 1.25rem; /* 20px */
            }

		</style>
	</head>
	<body>
    	<%@ include file="normal-navbar.jsp"%>

    	<main class="about-container">
    		<header>
    			<h1>About Us</h1>
    		</header>

    		<section>
    			<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sed nulla lacinia, tristique dui et, consequat nisi. Nullam tempor nibh vel tellus volutpat, vitae venenatis tortor dignissim. Sed iaculis nunc ac orci feugiat aliquet.</p>
    			<p>Suspendisse semper vulputate neque, nec iaculis quam tempor vel. Curabitur ut elementum felis. Donec congue felis vel felis volutpat tristique. Vestibulum lacinia elit vel mauris gravida, vitae elementum mi semper. In hac habitasse platea dictumst. Sed rhoncus turpis id quam blandit varius. Nam feugiat condimentum diam sed congue.</p>
    			<p class="highlight">We are dedicated to providing excellent services to our customers and strive to deliver the best user experience.</p>
    		</section>

    		<blockquote class="quote">"Quality and customer satisfaction are at the core of our values."</blockquote>

    		<section aria-labelledby="mission-title">
    			<h2 id="mission-title" class="mission">Our Mission</h2>
    			<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sed nulla lacinia, tristique dui et, consequat nisi. Nullam tempor nibh vel tellus volutpat, vitae venenatis tortor dignissim. Sed iaculis nunc ac orci feugiat aliquet. Suspendisse semper vulputate neque, nec iaculis quam tempor vel.</p>
    			<p>Curabitur ut elementum felis. Donec congue felis vel felis volutpat tristique. Vestibulum lacinia elit vel mauris gravida, vitae elementum mi semper. In hac habitasse platea dictumst. Sed rhoncus turpis id quam blandit varius. Nam feugiat condimentum diam sed congue.</p>
    		</section>

    		<footer class="text-center">
    			<img src="images/logo.png" alt="Company Logo" width="100">
    		</footer>
    	</main>
    </body>

</html>
