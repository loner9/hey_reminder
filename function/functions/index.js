//import firebase module functions
const functions = require('firebase-functions');

//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);