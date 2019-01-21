# Hitchhiking
This is very basic hitchhiking sharing application.

- [google play](https://play.google.com/store/apps/details?id=yuh.withfrds.com.hitchhiking)
- [github repo](https://github.com/stimulate/Hitchhiking)


## technical stacks
- firestore / firebase
- target sdk 23
- google play service (map and location)

### about fire store
- [query of firestore](https://firebase.google.com/docs/firestore/query-data/queries?utm_source=studio)
- [basic firestore](https://firebase.google.com/docs/firestore/?utm_source=studio)
- [how to get fire store getup](https://cloud.google.com/firestore/docs/quickstart)

### advantages of firestore than real time db
- [rt db does not do a real query but retrieve data](https://firebase.google.com/docs/database/admin/retrieve-data)

### disadvantages of firestore
- [cannot query array](https://firebase.google.com/docs/firestore/solutions/arrays)
- [cannot query geo points/ nearby query](https://stackoverflow.com/questions/46630507/how-to-run-a-geo-nearby-query-with-firestore)
- [cannot query subcollections](https://stackoverflow.com/questions/46573014/firestore-query-subcollections)
- [the range query (< <= > >=) can be only used in one filed](https://firebase.google.com/docs/firestore/query-data/queries)


## Errors Tracking
- [java.lang.IllegalStateException: This Activity already has an action bar supplied by the window decor](https://stackoverflow.com/questions/26515058/this-activity-already-has-an-action-bar-supplied-by-the-window-decor)


# Document
## About OurStore

this is an encapsulation class for interacting with fire store.

there are some public functions for now
- getDb
- getUserId
- postAnOffer
- postAnRequest
- setUserProfile
- getUserProfileMap (not yet)
