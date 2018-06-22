# Hitchhiking
This is very basic hitchhiking sharing application.


## technical stacks
- firestore / firebase
- target sdk 23
- google play service (map and location)

### about fire store
- [query of firestore](https://firebase.google.com/docs/firestore/query-data/queries?utm_source=studio)
- [basic firestore](https://firebase.google.com/docs/firestore/?utm_source=studio)
- [how to get fire store getup](https://cloud.google.com/firestore/docs/quickstart)

### advantages of firestore than real time db
- [rt db does not have a really query but a retrieving data](https://firebase.google.com/docs/database/admin/retrieve-data)

### disadvantages of firestore
- [cannot query array](https://firebase.google.com/docs/firestore/solutions/arrays)
- [cannot query geo points/ nearby query](https://stackoverflow.com/questions/46630507/how-to-run-a-geo-nearby-query-with-firestore)
- [cannot query subcollections](https://stackoverflow.com/questions/46573014/firestore-query-subcollections)


## Errors Tracking
- [java.lang.IllegalStateException: This Activity already has an action bar supplied by the window decor](https://stackoverflow.com/questions/26515058/this-activity-already-has-an-action-bar-supplied-by-the-window-decor)


# Document
## About OurStore

this is an encapsulation class for interacting with fire store.

there are 4 public functions for now
- postAnOffer
- postAnRequest
- setUserProfile (not yet)
- getUserProfileMap (not yet)
