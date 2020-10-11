const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('http://localhost:8080/service');

fetch(servicesRequest)
.then(response => response.json())
    //.then(data => data)
.then(function(serviceList) {
  serviceList.forEach(service => {
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(service.name + ': ' + service.status));
    let button = document.createElement("button");
    button.innerHTML = "Delete service";
    button.setAttribute("class","deleteButton");
    button.addEventListener('click', function(event) {
        deleteService(service.name)
    });
    li.appendChild(button);
    listContainer.appendChild(li);
  });
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    fetch('http://localhost:8080/service', {
    method: 'post',
    headers: {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
    },
  body: JSON.stringify({url:urlName})
}).then(res=> location.reload());
}

function deleteService(url) {
    console.log("delete")
    fetch('http://localhost:8080/delete', {
        method: 'post',
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({url:url})
    }).then(res=> location.reload());
}
