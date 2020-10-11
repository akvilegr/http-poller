# KRY code assignment

Critical issues (required to complete the assignment):

- Whenever the server is restarted, any added services disappear - not done because of difficulties with the database connection 
- There's no way to delete individual services - done
- We want to be able to name services and remember when they were added - not done (should save a timestamp as additional data)
- The HTTP poller is not implemented - done

Frontend/Web track:
- We want full create/update/delete functionality for services - create and delete is working, update not implemented
- The results from the poller are not automatically shown to the user (you have to reload the page to see results) - periodical refresh not done
- We want to have informative and nice looking animations on add/remove services - not done

Backend track
- Simultaneous writes sometimes causes strange behavior
- Protect the poller from misbehaving services (for example answering really slowly)
- Service URL's are not validated in any way ("sdgf" is probably not a valid service)
- A user (with a different cookie/local storage) should not see the services added by another user
