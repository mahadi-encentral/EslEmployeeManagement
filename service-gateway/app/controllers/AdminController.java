package controllers;

import com.mamt4real.app.api.IAdmin;
import com.mamt4real.app.api.IAttendance;
import com.mamt4real.app.api.IEmployee;
import com.mamt4real.app.model.Admin;
import com.mamt4real.app.model.Attendance;
import com.mamt4real.app.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.*;

import static com.encentral.scaffold.commons.ControllerUtils.getJSONStringField;
import static com.encentral.scaffold.commons.ControllerUtils.isValidEmail;

@Transactional
@Api(value = "Admin")
public class AdminController extends Controller {
    @Inject
    IAdmin iAdmin;

    @Inject
    IEmployee iEmployee;

    @Inject
    IAttendance iAttendance;

    @Inject
    FormFactory formFactory;

    @Inject
    ObjectMapper objectMapper;

    @ApiOperation(value = "Add new Admin")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Admin.class, message = "Newly created Admin")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Admin details",
                    paramType = "body",
                    required = true,
                    dataType = "com.encentral.app.model.Admin"
            )
    })
    public Result add() throws JsonProcessingException {
        Form<Admin> adminForm = formFactory.form(Admin.class).bindFromRequest();
        if (adminForm.hasErrors())
            return badRequest(adminForm.errorsAsJson());
        else
            return ok(objectMapper.writeValueAsString(iAdmin.addAdmin(adminForm.get())));
    }

    @ApiOperation(value = "Login as Admin")
    public Result login() throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String email = getJSONStringField(data, "email");
        if (email == null || email.isEmpty())
            errors.add("email is required");

        if (email != null && !email.isEmpty() && !isValidEmail(email))
            errors.add("email is not valid");

        String password = getJSONStringField(data, "password");
        if (password == null || password.isEmpty())
            errors.add("password is required");

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Admin> admin = iAdmin.getAdmin(email);
        if (admin.isEmpty())
            return unauthorized("Invalid email or password");

        if (!admin.get().getPassword().equals(password))
            return unauthorized("Invalid email or password");

        Map<String, String> result = new HashMap<>();
        result.put("access_token", admin.get().getToken());

        return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }

    public Result addEmployee() throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String accessToken = getJSONStringField(data, "access_token");
        if (accessToken == null || accessToken.isEmpty())
            errors.add("access_token is required");

        String email = getJSONStringField(data, "email");
        if (email == null || email.isEmpty())
            errors.add("email is required");

        if (email != null && !email.isEmpty()) {
            if (!isValidEmail(email))
                errors.add("email is not valid");
            else if (iEmployee.getEmployee(email).isPresent())
                errors.add("This email is already registered");
        }

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Admin> admin = iAdmin.getAdminByToken(accessToken);
        if (admin.isEmpty())
            return unauthorized("Invalid access_token");

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 4; i++)
            password.append(random.nextInt(10));

        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(password.toString());

        iEmployee.addEmployee(employee);

        Map<String, String> result = new HashMap<>();
        result.put("password", password.toString());

        return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }

    public Result getEmployees() throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String accessToken = getJSONStringField(data, "access_token");
        if (accessToken == null || accessToken.isEmpty())
            errors.add("access_token is required");

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Admin> admin = iAdmin.getAdminByToken(accessToken);
        if (admin.isEmpty())
            return unauthorized("Invalid access_token");

        Optional<List<Employee>> employees = iEmployee.getEmployees();
        if (employees.isEmpty())
            return notFound("No employee found");

        return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees.get()));
    }

    public Result getEmployeeAttendance(String id) throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String accessToken = getJSONStringField(data, "access_token");
        if (accessToken == null || accessToken.isEmpty())
            errors.add("access_token is required");

        int idInt = -1;
        try {
            idInt = Integer.parseInt(id);
        } catch (Exception e) {
            errors.add("id is invalid");
        }

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Admin> admin = iAdmin.getAdminByToken(accessToken);
        if (admin.isEmpty())
            return unauthorized("Invalid access_token");

        Optional<Employee> employee = iEmployee.getEmployee(idInt);
        if (employee.isEmpty())
            return notFound("No such employee");

        Optional<List<Attendance>> attendances = iAttendance.getAttendance(employee.get());
        if (attendances.isEmpty())
            return notFound("No attendance recorded yet");

        return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(attendances.get()));
    }

    public Result removeEmployee(String id) throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String accessToken = getJSONStringField(data, "access_token");
        if (accessToken == null || accessToken.isEmpty())
            errors.add("access_token is required");

        int idInt = -1;
        try {
            idInt = Integer.parseInt(id);
        } catch (Exception e) {
            errors.add("id is invalid");
        }

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Admin> admin = iAdmin.getAdminByToken(accessToken);
        if (admin.isEmpty())
            return unauthorized("Invalid access_token");

        Optional<Employee> employee = iEmployee.getEmployee(idInt);
        if (employee.isEmpty())
            return notFound("No such employee");

        if (iEmployee.removeEmployee(employee.get()))
            return ok("Employee removed");
        else
            return internalServerError("Failed to remove employee");
    }
}




























