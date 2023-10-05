package com.dashboard.signup.service;

import com.dashboard.signup.persistance.entity.Customer;
import com.dashboard.signup.utils.userAccessEnum;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.concurrent.ExecutorService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;
    private final MongoCollection<Document> sequenceCollection;

    @Value("${db.host}")
    private String connectionString;
    @Value("${db.name}")
    private String dbName;
    @Value("${db.collection.signup}")
    private String signupCollectionName;
    @Value("${db.collection.signupSequence}")
    private String signupSequenceCollectionName;
    @Value("${loginUserName}")
    private String loginUserName;
    @Value("${firstName}")
    private String firstName;
    @Value("${lastName}")
    private String lastName;
    @Value("${password}")
    private String password;
    @Value("${email}")
    private String email;
    @Value("${threadCount}")
    private int threadCount;
    @Autowired
    private ExecutorService executorService;

    @Autowired
    public CustomerService(@Value("${db.host}") String connectionString, @Value("${db.name}") String dbName, @Value("${db.collection.signup}") String signupCollectionName, @Value("${db.collection.signupSequence}") String signupSequenceCollectionName) {
        this.mongoClient = MongoClients.create(connectionString);
        this.database = this.mongoClient.getDatabase(dbName);
        this.collection = this.database.getCollection(signupCollectionName);
        this.sequenceCollection = this.database.getCollection(signupSequenceCollectionName);
    }

    public void createUser(Customer signup){
        try {
            Long id = getNextSequence("signup_sequences");
            signup.setId(id);
            signup.setUserAccess(userAccessEnum.PERMITTED.name());
            Document document = new Document("id", signup.getId())
                    .append("firstName", signup.getFirstName())
                    .append("lastName", signup.getLastName())
                    .append("email", signup.getEmail())
                    .append("userName",signup.getUserName())
                    .append("password",signup.getPassword())
                    .append("userAccess",signup.getUserAccess());
            collection.insertOne(document);
        } catch (Exception e) {
            System.out.println("Error creating user");
        }
    }


    public void updateUser(Customer customer, userAccessEnum userAccess){
        try{
            Document filter = new Document("id", customer.getId());
            List<Bson> updates = new ArrayList<>();
            updates.add(Updates.set("userAccess", userAccess.name()));
            collection.updateOne(filter, Updates.combine(updates));
        } catch (Exception e) {
            System.out.println("Error updating user");
        }
    }

    public Customer findUserById(Long userId){
        try{
            Document filter = new Document("id", userId);
            Document userDocument = collection.find(filter).first();
            if(userDocument != null){
                return Customer.fromDocument(userDocument);
            }
        } catch (Exception e) {
            System.out.println("Error finding user");
        }
        return null;
    }

    public List<Customer> fetchAllUsers(){
        List<Customer> users = new ArrayList<>();
        try {
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                users.add(Customer.fromDocument(userDocument));
            }
        } catch (Exception e) {
            System.out.println("Error fetching users");
        }
        return users;
    }

    public Boolean isSignupRequestValid(Customer signup){
        System.out.println(signup.toString());
        if((signup == null) || (signup.getFirstName() == null ||
           signup.getLastName() == null ||
           signup.getEmail() == null ||
           signup.getUserName() == null ||
           signup.getPassword() == null)) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean isUserValidforRegistration(String userName){
        return findByuserName(userName) == null;
    }

    public Boolean isUserSigninValid(String userName, String password){
        System.out.println("Inside valid user");
        return ((userName.isEmpty()) && (password.isEmpty())) ? false : true;
    }

    public  HttpStatus authenticate (String userName, String password){
        System.out.println("authenticate");
        Customer user = findByuserName(userName);
        if(user != null){
            return (user.getPassword().equals(password) && user.getUserAccess().equals(userAccessEnum.PERMITTED.name())) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    private Long getNextSequence(String seqName) {
        Document sequence = sequenceCollection.findOneAndUpdate(
                new Document("_id", seqName),
                new Document("$inc", new Document("seq", 1L)),
                new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER)
        );

        return sequence.getLong("seq");
    }

    private Customer findByuserName(String userName){
        Customer user = null;
        try{
            Document filter = new Document("userName", userName);
            Document userDocument = collection.find(filter).first();
            if(userDocument != null){
                user = Customer.fromDocument(userDocument);
            }
        } catch (Exception e) {
            System.out.println("Error fetching user by name");
        }
        return user;
    }

    public void performConcurrentTask() {
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            executorService.submit(() -> {
                Customer customCustomer = createCustomCustomer(index);
                createUser(customCustomer);
                System.out.println(String.format("User Id %d is created", index ));
            });
        }
    }

    private Customer createCustomCustomer(int index) {
        Customer customCustomer = new Customer();
        customCustomer.setFirstName(String.format(firstName,index));
        customCustomer.setLastName(String.format(lastName,index));
        customCustomer.setEmail(String.format(email,index));
        customCustomer.setUserName(String.format(loginUserName,index));
        customCustomer.setPassword(String.format(password,index));
        customCustomer.setUserAccess(userAccessEnum.PERMITTED.name());
        return customCustomer;
    }

    public void close() {
        mongoClient.close();
    }
}
