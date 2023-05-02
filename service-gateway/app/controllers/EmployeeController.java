package controllers;

import com.mamt4real.app.api.IAttendance;
import com.mamt4real.app.api.IEmployee;
import com.mamt4real.app.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.*;

import static com.encentral.scaffold.commons.ControllerUtils.getJSONStringField;
import static com.encentral.scaffold.commons.ControllerUtils.isValidEmail;

@Transactional
@Api(value = "Employee")
public class EmployeeController extends Controller {
    @Inject
    IEmployee iEmployee;

    @Inject
    IAttendance iAttendance;

    @Inject
    FormFactory formFactory;

    @Inject
    ObjectMapper objectMapper;

    @ApiOperation(value = "Change Password")
    public Result updatePassword() throws JsonProcessingException {
        JsonNode data = request().body().asJson();
        if (data == null )
            return notFound("No data provided");

        List<String> errors = new ArrayList<>();

        String accessToken = getJSONStringField(data, "access_token");
        if (accessToken == null || accessToken.isEmpty())
            errors.add("access_token is required");

        String currentPassword = getJSONStringField(data, "current_password");
        if (currentPassword == null || currentPassword.isEmpty())
            errors.add("current_password is required");

        if (currentPassword != null && !currentPassword.isEmpty() && !isValidEmail(currentPassword))
            errors.add("current_password is not valid");

        String newPassword = getJSONStringField(data, "new_password");
        if (newPassword == null || newPassword.isEmpty())
            errors.add("new_password is required");

        if (!errors.isEmpty())
            return badRequest(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors));

        Optional<Employee> employee = iEmployee.getEmployeeByToken(accessToken);
        if (employee.isEmpty())
            return unauthorized("Invalid access_token");

        if (!employee.get().getPassword().equals(currentPassword))
            return unauthorized("Invalid current_password");

        if (iEmployee.changePassword(employee.get(), newPassword))
            return ok("Password changed");
        else
            return internalServerError("Failed to change password");
    }

    @ApiOperation(value = "Login")
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

        Optional<Employee> employee = iEmployee.getEmployee(email);
        if (employee.isEmpty())
            return unauthorized("Invalid email or password");

        if (!employee.get().getPassword().equals(password))
            return unauthorized("Invalid email or password");

        Map<String, String> result = new HashMap<>();
        result.put("access_token", employee.get().getToken());

        return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }
}





















