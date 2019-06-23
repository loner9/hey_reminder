'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotification = functions.database.ref('/Notifications/{receiverUserId}/{notificationId}')
    .onWrite((data, context) => {
        const receiverUserId = context.params.receiverUserId;
        const notificationId = context.params.notificationId;


        console.log('Kami memiliki notifikasi untuk dikirim ke :', receiverUserId);


        if (!data.after.val()) {
            console.log('Sebuah notifikasi telah terhapus :', notificationId);
            return null;
        }

        const senderUserId = admin.database().ref(`/Notifications/${receiverUserId}/${notificationId}`).once('value');

        return senderUserId.then(fromUserResult => {
            const fromSenderUserId = fromUserResult.val().from;
            console.log('Kamu memiliki notifikasi dari : ', fromSenderUserId);
            const userQuery = admin.database().ref(`/Users/${fromSenderUserId}/name`).once('value');
            return userQuery.then(userResult => {
                const senderUserName = userResult.val();

                const deviceToken = admin.database().ref(`/Users/${receiverUserId}/deviceToken`).once('value');

                return deviceToken.then(result => {
                    const tokenId = result.val();

                    const payload =
                    {
                        notification:
                        {
                            from: fromSenderUserId,
                            title: "Permintaan Kontak Baru",
                            body: `${senderUserName} ingin tersambung denganmu`,
                            icon: "default"
                        }
                    };

                    return admin.messaging().sendToDevice(tokenId, payload)
                        .then(response => {
                            console.log('Ini adalah fitur notifikasi.');
                        });
                });
            });
        });
    });
