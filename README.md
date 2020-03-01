# datastore
DataStore project for Fworks

Working example : http://navira.xyz:2202/datastore/
(repo for above implementation project : https://github.com/vimal-raj/datastoreweb)




Object Creation
===============

xyz.navira.fworks.DataStore

// default name and path(system temp) will be created if not provided

DataStore dS = new DataStore();
DataStore dS = new DataStore(String name);
DataStore dS = new DataStore(String name, String path);

DataStore Methods
-----------------

dS.createDataFile(name);
dS.createDataFile(name, ttl); // ttl -> file expiry in seconds
dS.deleteDataFile(name);

@returns
DataFile Object (instance of File Object)

DataFile Methods
-----------------

// inherits all methods from File class, additionaly has below methods

Getters and Setters for name, ttl

DataFile.getContent() // Returns JSONObject (json simple lib)
DataFile.putContent(JSONObject obj) // return nothing, accepts JSONObject
DataFile.getContentAsString() // Returns JSON string
