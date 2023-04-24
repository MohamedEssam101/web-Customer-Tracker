package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.CurrencyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.entity.Customer;

// always  apply to DAO implementaions
//must be added , so spring can do component scan 
//and find this @Repository and handle exception translations for us
@Repository
public class CustomerDAOImpl implements CustomerDAO {

	//need to inject the sessionFactory
	// this will go to xml file and look for the auto wired (sessionFactory) bean id and link it
	@Autowired
	private SessionFactory sessionFactory;
	//
	@Override
	public List<Customer> getCustomers() {
		
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//create a query .. sort by last name
		Query<Customer> theQuery = 
				currentSession.createQuery("from Customer order by lastName",
											Customer.class);
		
		//get the result set , or get the list of customers from the query
		List<Customer> customers = theQuery.getResultList();
		
		//return the list of customers 
		return customers;
	}
	
	
	@Override
	public void saveCustomer(Customer theCustomer) {
		
		//get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//save  or  update the customer ... finally to database
		currentSession.saveOrUpdate(theCustomer);
		
	}


	@Override
	public Customer getCustomer(int theId) {

		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// fetch /read from database using primary key
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}


	@Override
	public void deleteCustomer(int theId) {
		
		
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//delete object with  primary key
		Query theQuery = currentSession.createQuery("delete from Customer"
				+ " where id=:customerId");
		
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
	}


	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
			
        	Query theQuery = null;

		//
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // search for firstName or lastName ... case insensitive
            theQuery =currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // theSearchName is empty ... so just get all customers
            theQuery =currentSession.createQuery("from Customer", Customer.class);            
        }
        
        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();
                
        // return the results        
        return customers;
        
	}

}
