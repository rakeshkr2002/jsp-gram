# JSP Gram - Social Media Web Application

JSP Gram is a modern social media web application that offers users the ability to connect, interact, and share moments seamlessly. Built using **Spring Boot**, **Thymeleaf**, and cutting-edge technologies, JSP Gram delivers a secure and feature-rich platform for users.

## Features

### User Functionalities
- **Registration and OTP Verification**:
  - Secure user registration with **email-based OTP verification**.
  - Login system for authenticated user access.
- **Profile Management**:
  - Users can upload and manage profile pictures using **Cloudinary**.
  - Edit bio and personal details.
- **Social Features**:
  - Follow and unfollow users with real-time updates on follower and following counts.
  - View suggestions for new connections.
- **Posts**:
  - Create, edit, and delete posts with captions and images.
  - Like, dislike, and comment on posts.
  - View posts from followed users in the home feed.
- **Premium Membership**:
  - Integrated **Razorpay payment gateway** for subscribing to premium membership.

### Technical Highlights
- Secure **Cloudinary Integration** for cloud-based image uploads.
- Seamless **Razorpay Integration** for payment handling.
- **Form Validation and Error Handling**:
  - Frontend validation ensures user-friendly experiences.
  - Backend error handling provides robust and secure operations.

## Technologies Used
- **Backend**:
  - Spring Boot, Hibernate (JPA), and MySQL.
  - Razorpay API for payment processing.
  - JavaMailSender for email-based OTP verification.
- **Frontend**:
  - Thymeleaf, HTML, CSS, and JavaScript.
  - Responsive design for mobile and desktop compatibility.
- **Cloud Storage**:
  - Cloudinary for secure media management.


## API Endpoints
- **Authentication**:
  - `POST /register`: Register a new user.
  - `POST /verify-otp`: Verify OTP for registration.
  - `POST /login`: Login to the application.
  - `GET /logout`: Logout and end user session.
- **Profile Management**:
  - `GET /profile`: View user profile.
  - `POST /update-profile`: Edit profile details and upload a new profile picture.
- **Post Management**:
  - `POST /add-post`: Add a new post.
  - `POST /edit/{id}`: Edit an existing post.
  - `GET /delete/{id}`: Delete a post by ID.
- **Interactions**:
  - `GET /like/{id}`: Like a post by ID.
  - `GET /comment/{id}`: Add a comment to a post.

## Folder Structure
```plaintext
src/
├── controller/          # Handles HTTP requests and routes
├── dto/                 # Data models (User, Posts, Comments)
├── helper/              # Utility classes (Cloudinary, EmailSender)
├── repository/          # JPA repositories for database operations
├── service/             # Business logic layer
├── resources/           
│   ├── static/          # CSS, images, and JavaScript
│   ├── templates/       # Thymeleaf HTML templates
│   └── application.properties # Configuration file
└── JspGramApplication.java # Main application file
```

## License
This project is licensed under the [MIT License](./LICENSE).

