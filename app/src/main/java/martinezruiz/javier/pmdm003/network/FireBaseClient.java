package martinezruiz.javier.pmdm003.network;

import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseClient {

    private FireBaseClient(){}

    public static FirebaseFirestore getFireStore(){
        return FirebaseFirestore.getInstance();
    }
}
