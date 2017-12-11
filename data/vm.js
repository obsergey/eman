// model

function AggPagination(data) {
    this.total = data.total;
    this.pages = data.pages;
    this.current = data.current + 1;
}

function DeptLabel(data) {
	this.id = data.id;
    this.name = data.name;
    this.description = data.description;
}

function DeptLabelListPage(data) {
    this.pagination = new AggPagination(data.pagination);
    this.deptLabels = $.map(data.deptLabels, function(label) { return new DeptLabel(label) });
}

function EmployeePublic(data) {
	this.id = data.id;
    this.name = data.name;
    this.position = data.position;
}

function DeptDetail(data) {
    this.name = data.name;
    this.description = data.description;
    this.employees = ko.observableArray($.map(data.employees, function(employee) { return new EmployeePublic(employee) }));
}

function EmployeeFull(data) {
    this.name = ko.observable(data.name);
    this.position = ko.observable(data.position);
    this.phone = ko.observable(data.phone);
    this.salary = ko.observable(data.salary);
    this.account = ko.observable(data.account);
}


// service mock

function AggregationServiceMock() {
	var self = this;
	self.processed = ko.observable(false);
	self.errmessage = ko.observable();
	self.setErrMessage = function(xhdr) {
		if(xhdr.status !== 201 && xhdr.status !== 204)
			self.errmessage(xhdr.status + ": " + (xhdr.responseJSON === null ? "" : xhdr.responseJSON.message));
	};
    self.findAllDeptLabel = function(allDeptLabel, page, size) {
		console.log("service findAllDeptLabel page %s size %s", page, size);
		$.ajax({ method: "GET", url: "/dept", data: { "page": page, "size": size },
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			success: function(data) { allDeptLabel(new DeptLabelListPage(data)) },
			error: function(xhdr) { allDeptLabel(null); self.setErrMessage(xhdr) },
			complete: function() { self.processed(false) }
		})
	};
    self.findOneDeptDetail = function(oneDeptDetail, dept) {
		console.log("service findOneDeptDetail dept %s", dept);
		$.ajax({ method: "GET", url: "/dept/" + dept,
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			success: function(data) { oneDeptDetail(new DeptDetail(data)) },
			error: function(xhdr) { oneDeptDetail(null); self.setErrMessage(xhdr) },
			complete: function() { self.processed(false) }
		})
	};
    self.findOneEmployee = function(employee, id, dept) {
		console.log("service findOneEmployee id %s dept %s", id, dept);
		$.ajax({ method: "GET", url: "/dept/" + dept + "/employee/" + id,
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			success: function(data) { employee(new EmployeeFull(data)) },
			error: function(xhdr) { employee(null); self.setErrMessage(xhdr) },
			complete: function() { self.processed(false) }
		})
	};
    self.appendEmployee = function(employee, dept) {
		console.log("service appendEmployee dept %s employee " + employee(), dept);
		$.ajax({ method: "POST", url: "/dept/" + dept + "/employee", data: ko.toJSON(employee()), 
			dataType: 'json', contentType: "application/json; charset=utf-8",
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			error: function(xhdr) { self.setErrMessage(xhdr); if(xhdr.status === 201) location.hash = "dept/" + dept },
			complete: function() { self.processed(false) }
		})
	};
    self.updateEmployee = function(employee, id, dept) {
		console.log("service updateEmployee id %s dept %s employee " + employee(), id, dept);
		$.ajax({ method: "PATCH", url: "/dept/" + dept + "/employee/" + id, data: ko.toJSON(employee()),
			dataType: 'json', contentType: "application/json; charset=utf-8",
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			success: function(data) { employee(new EmployeeFull(data)); location.hash = "dept/" + dept },
			error: function(xhdr) { self.setErrMessage(xhdr) },
			complete: function() { self.processed(false) }
		})
	};
    self.removeEmployee = function(oneDeptDetail, emp, dept) {
		console.log("service removeEmployee id %s dept %s", emp.id, dept);
		$.ajax({ method: "DELETE", url: "/dept/" + dept + "/employee/" + emp.id,
			beforeSend: function() { self.processed(true); self.errmessage(null) },
			success: function() { if(oneDeptDetail() !== null) oneDeptDetail().employees.remove(emp) },
			error: function(xhdr) { self.setErrMessage(xhdr) },
			complete: function() { self.processed(false) }
		})		
	}
}


// view model

var DeptsViewModel = function DeptsViewModel() {
	var self  = this;
	self.service = ko.observable(new AggregationServiceMock());
	self.deptId = ko.observable();
	self.employeeId = ko.observable();
	self.allDeptLabel = ko.observable();
	self.oneDeptDetail = ko.observable();
	self.employee = ko.observable();

	self.newEmployee = function() {
		location.hash = "dept/" + self.deptId() + "/employee/new";
	};
	self.editDept = function(dept) {
		location.hash = "dept/" + dept.id
	};
	self.editEmployee = function(emp) {
		location.hash = "dept/" + self.deptId() + "/employee/" + emp.id;
	};
	self.cancelEditEmployee = function() {
		location.hash = "dept/" + self.deptId();
	};
	self.prevPage = function() {
		location.hash = "dl/" + (Number(self.allDeptLabel().pagination.current) - 2);
	};
	self.nextPage = function() {
		location.hash = "dl/" + (Number(self.allDeptLabel().pagination.current));
	};
	self.applyEditEmployee = function() {
		if(self.employeeId() === "new")
			self.service().appendEmployee(self.employee, self.deptId());
		else
			self.service().updateEmployee(self.employee, self.employeeId(), self.deptId());
	};
	self.removeEmployee = function(emp) {
		self.service().removeEmployee(self.oneDeptDetail, emp, self.deptId());
	};

	
	Sammy(function() {
		this.get('#dept/:deptId/employee/:employeeId', function() {
			self.deptId(this.params.deptId);
			self.employeeId(this.params.employeeId);
			self.allDeptLabel(null);
			self.oneDeptDetail(null);
			if(self.employeeId() === "new")
				self.employee(new EmployeeFull({
                    name: "", phone: "", position: "", salary: 0, account: ""
                }));
			else
				self.service().findOneEmployee(self.employee, self.employeeId(), self.deptId());
		});
		this.get('#dept/:deptId', function() {
			self.deptId(this.params.deptId);
			self.employeeId(null);
			self.allDeptLabel(null);
			self.service().findOneDeptDetail(self.oneDeptDetail, self.deptId());
			self.employee(null);
		});
		this.get('#dl/:page', function() {
			self.deptId(null);
			self.employeeId(null);
			self.service().findAllDeptLabel(self.allDeptLabel, this.params.page, 2);
			self.oneDeptDetail(null);
			self.employee(null);
		});
		this.get('#dl', function() {
			this.app.runRoute('get', '#dl/0');
		});
		this.get('', function() {
			this.app.runRoute('get', '#dl/0');
		});
	}).run();
};

ko.applyBindings(new DeptsViewModel());