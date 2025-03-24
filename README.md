<div align="center">
  <img src="app/src/main/res/drawable/logo2.png" alt="TrusToken Logo" width="400" height="200">

# TrusToken Mobile Application

Secure Digital Wallet Management at Your Fingertips
</div>

## 📱 Screenshots

<div align="center">
  <img src="/api/placeholder/250/500" alt="Login Screen" width="250">
  <img src="/api/placeholder/250/500" alt="Home Screen" width="250">
  <img src="/api/placeholder/250/500" alt="Payment Screen" width="250">
</div>

## 📝 Project Overview
TrusToken is a comprehensive mobile application designed for secure digital wallet management, user authentication, and seamless financial transactions. Built with Kotlin and Jetpack Compose, the app provides a robust and user-friendly experience for managing digital assets and payments.

## 🔐 Native Cryptographic Functions

### Native Function Overview
TrusToken leverages several critical native cryptographic functions to enhance security:

| Function | Purpose | Usage in Application |
|----------|---------|----------------------|
| libint(int) | Integer manipulation | Low-level computational operations |
| login(tokenPin) | User Authentication | Secure USB token-based login |
| signData() | Transaction Signing | Cryptographically sign transactions |
| verify(string, plainText) | Signature Verification | Validate transaction authenticity |
| encrypt() | Data Encryption | Secure sensitive information |
| decrypt(string) | Data Decryption | Retrieve original sensitive data |
| logout() | Session Termination | Securely end user session |

### Security Implementation Details

#### Authentication Flow
In the ActivationViewModel, the login() native function is used during PIN verification:
- Validates user credentials against USB token
- Provides a secure, hardware-based authentication mechanism
- Prevents unauthorized access with cryptographic verification

#### Transaction Security
In the HomeViewModel, native functions play crucial roles:
- signData(): Generates cryptographic signatures for transactions
- verify(): Validates transaction signatures before processing
- logout(): Logout Session form TrusToken after verification
- Ensures transaction integrity and non-repudiation

#### Encryption Mechanisms
- encrypt() and decrypt() protect sensitive user and transaction data
- Prevents unauthorized data access
- Provides an additional layer of security beyond standard encryption

## 🏗 Architecture
The application follows a clean, modular architecture with clear separation of concerns:
- Presentation Layer: Jetpack Compose UI components
- Domain Layer: Business logic and use cases
- Data Layer: Service implementations and data management
- Core Layer: Shared utilities, navigation, and dependency injection

## 🔑 Authentication Features
### User Authentication Modules
- Sign Up Screen:
- New user registration
- Secure account creation
- Login Screen:
- User authentication
- Credential validation
- Forget Password:
- Password reset functionality
- Change password interface

### Authentication Flow
- Implements secure sign-in methods
- Generates and manages user wallet addresses
- Handles authentication states
- Provides error handling for authentication processes

## 💰 Payment and Wallet Functionality
### Wallet Management
- Wallet Creation: Secure wallet generation
- Transaction Handling:
- Transaction listing
- Transaction details view
- Transaction parsing and management

### Payment Screens
- Payment Screen:
- Initiate and confirm payments
- Transaction confirmation
- Transaction List Screen:
- View transaction history

### Security Considerations
- Native functions provide hardware-level security
- USB token acts as a physical authentication factor
- Cryptographic operations executed in secure native environment

## 🔒 Advanced Security Features
- Hardware-based authentication
- Cryptographic transaction signing
- Secure data encryption/decryption
- Tamper-resistant transaction verification


## 📱 User Interface
### UI Components
- Custom themed screens
- Responsive design
- Material Design principles
- Custom fonts (Poppins, Roboto)
- Adaptive UI components

### Screens and Navigation
- Home Screen
- User Profile
- Payment Screens
- Authentication Screens
- Transaction Screens

## 🧩 Technical Specifications
### Language and Frameworks
- Primary Language: Kotlin
- UI Framework: Jetpack Compose
- Architecture: Clean Architecture, MVVM + MVI,
- Dependency Injection: Koin
- Database: Firestore, Firebase Realtime Database, Firebase Storage

### Native Components
- C++ cryptographic libraries
- PKCS#11 support
- Native method integrations

## 📦 Project Structure

saurav1375-trustathon/
├── app/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/example/trustoken_starter/
│ │ │ │ ├── auth/             # Authentication modules
│ │ │ │ ├── core/             # Core utilities
│ │ │ │ ├── di/               # Dependency Injection
│ │ │ │ ├── payment/          # Payment-related modules
│ │ │ │ ├── ui/               # UI Theme and Components
│ │ │ │ └── ...
│ │ │ └── res/                # Resources
│ │ └── ...
└── ...


## 🚀 Key Features
1. Secure User Authentication
2. Digital Wallet Management
3. Transaction Processing
4. PIN Verification
5. Responsive UI Design
6. Comprehensive Error Handling

## 🛠 Setup and Installation
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Build and run the project

### Prerequisites
- Android Studio
- Kotlin SDK
- Gradle
- Android SDK

## 📌 Dependencies
- Jetpack Compose
- Kotlin Coroutines
- Native C++ Libraries
- Koin Dependency Injection

## 🔒 Security Considerations
- Implements secure authentication mechanisms
- Uses native cryptographic libraries
- Comprehensive error and network error handling
- Secure wallet address generation

## 🚀 Future Roadmap and Expansion Plans

### 1. Campus Cryptocurrency Ecosystem
#### Vision: Decentralized Campus Currency Platform
- *Generalized Campus Currency Framework*
  - Each institution can create and manage its own digital currency
  - Customizable token economics for specific campus needs
  - Easy integration with existing campus infrastructure

#### Key Features
- Scalable token creation mechanism
- Institution-specific currency management
- Transparent and secure transaction tracking

### 2. IIT Bhilai Marketplace Integration
#### Comprehensive Campus Commerce Solution
- *Shop User Type*
  - Special user category for campus vendors and shops
  - Simplified payment acceptance mechanism
  - Integrated point-of-sale (POS) system

#### Payment Innovations
- USB Token-based instant payments
- Plug-and-play payment system for merchants
- Real-time transaction verification

### 3. Enhanced Authentication Methods
#### Biometric Integration
- *Advanced Security Layers*
  - Biometric signature for transactions
  - Multi-factor authentication
  - Secure data signing using biometric credentials

#### Proposed Biometric Features
- Fingerprint transaction authorization
- Facial recognition for high-value transactions
- Seamless, secure user verification

### 4. Technical Enhancements
- Implement native biometric signing functions
- Extend current native cryptographic library
- Create abstraction layers for biometric integration

### 5. Long-term Vision
- Create a replicable, secure digital transaction ecosystem
- Empower educational institutions with custom financial technologies
- Provide students with modern, secure financial tools

## 🌐 Potential Impact
- Simplified campus financial interactions
- Reduced cash handling
- Enhanced financial transparency
- Technological innovation in educational financial systems


## 🚧 Disclaimer
This is a sample application and should not be used for production without proper security audits and enhancements.