package org.acme.api;

import java.util.List;

import org.acme.domain.Students;
import org.acme.dto.StudentDTO;
import org.acme.service.StudentsService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/api/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentsResource {
    @Inject
    private StudentsService studentsService;

    @POST
    @Transactional
    public Response createStudent(@Valid StudentDTO studentDTO) {
        try {
            studentsService.addStudentFromDTO(studentDTO);
            return Response.status(Response.Status.CREATED).entity(studentDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating student: " + e.getMessage())
                .build();
        }
    }

    @POST
    @Path("/delete/{id}")
    @Transactional
    public Response deleteStudent(@PathParam("id") Long id) {
        try {
            Students student = studentsService.getStudentById(id);
            if(student == null){
                return Response.status(Response.Status.NOT_FOUND)
                .entity("Student with Id " + id + " not found")
                .build();
            }
            studentsService.deleteStudents(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting student: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getStudent(@PathParam("id") Long id){
        try {
            Students student = studentsService.getStudentById(id);
            if(student == null){
                return Response.status(Response.Status.NOT_FOUND)
                .entity("Student with Id " + id + " not found")
                .build();
            }
            return Response.ok(student).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving student: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/all")
    public Response getAllStudents() {
        try {
            List<Students> students = studentsService.getAllStudents();
            return Response.ok(students).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving students: " + e.getMessage())
                .build();
        }
    }


}
