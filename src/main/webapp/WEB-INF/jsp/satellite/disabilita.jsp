<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	   
	   <title>Pagina dei Risultati</title>
	 </head>
	 
	<body class="d-flex flex-column h-100">
	 
		<!-- Fixed navbar -->
		<jsp:include page="../navbar.jsp"></jsp:include>
	 
	
		<!-- Begin page content -->
		<main class="flex-shrink-0">
		  <div class="container">
		  
		  		<div class="alert alert-success alert-dismissible fade show  ${successMessage==null?'d-none':'' }" role="alert">
				  ${successMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-danger alert-dismissible fade show d-none" role="alert">
				  Esempio di operazione fallita!
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-info alert-dismissible fade show d-none" role="alert">
				  Aggiungere d-none nelle class per non far apparire
				   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
		  
		  
		  
		  		<div class='card'>
				    <div class='card-header'>
				        <h5>Lista dei risultati</h5> 
				    </div>
				        <div class="p-5 mb-4 bg-light rounded-3">    
				        <div class="container-fluid py-5">     	               
				        <form:form modelAttribute="numeroSatelliteModificati_list_attribute" method="post" action="saveDisabilita" class="row g-3" novalidate="novalidate">
				        <h1 class="display-5 fw-bold">Disabilita tutti</h1>
				        <p class="col-md-8 fs-4">Se si conferma verrano disattivati tutti i satelliti , si è sicuri di continuare? </p>
				        <p class="col-md-8 fs-4">Satelliti presenti: ${numeroSatellite_list_attribute}</p>
				        <p class="col-md-8 fs-4">Satelliti modificati: ${numeroSatelliteModificati_list_attribute} </p>
				        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/satellite/home">Annulla</a>
						<button type="submit" name="submit" value="submit" id="submit" class="btn btn-primary">Conferma</button>
				      </form:form>
				      </div>
				       </div>
			      </div>
					<!-- end card-body -->			   
			    </div>
			<!-- end card -->
			</div>	
		 
		   
		 <!-- end container -->  
		  </div>
		  
		</main>
		
		<!-- Footer -->
		<jsp:include page="../footer.jsp" />
		
	</body>
</html>