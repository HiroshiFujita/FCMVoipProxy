package jp.co.exec.voip.fcm;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * @author fujita
 * @see https://firebase.google.com/docs/admin/setup?hl=ja#add_the_sdk
 * @see https://github.com/firebase/firebase-admin-java
 */
@SpringBootApplication
public class FcmVoipProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FcmVoipProxyApplication.class, args);
	}

	public static void initializeToken() throws IOException {
		//サービス アカウント用の秘密鍵ファイルを生成するには:
		//Firebase コンソールで、[設定] > [サービス アカウント] を開きます。
		//[新しい秘密鍵の生成] をクリックし、[キーを生成] をクリックして確定します。
		//キーを含む JSON ファイルを安全に保管します。
		//【注意】秘密鍵の情報は機密扱いとし、公開レポジトリには保存しないでください。
		//TODO 環境変数　GOOGLE_APPLICATION_CREDENTIALS　から取得する
		FileInputStream serviceAccount = new FileInputStream(
				"c:/fcm/pocketchart-pushtest-firebase-adminsdk-knmo0-2e70e845ec.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://pocketchart-pushtest.firebaseio.com")
				.build();

		FirebaseApp.initializeApp(options);

		// As an admin, the app has access to read and write all data, regardless of Security Rules
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restricted_access/secret_document");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Object document = dataSnapshot.getValue();
				System.out.println(document);
			}

			@Override
			public void onCancelled(DatabaseError error) {
			}
		});
	}

}
