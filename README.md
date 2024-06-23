# Android SMS Transaction Viewer

This Android app, built with Kotlin, helps users to view and analyze SMS transaction messages from specific banks. It uses a bottom navigation view to switch between two main fragments: Home and Chart. Upon first launch, the app requests permissions to read and receive SMS messages. Once granted, it loads recent transactions from predefined banks in the Home fragment.

## Features

- **Permissions Handling**: Requests SMS read and receive permissions at startup.
- **Home Fragment**: Displays recent transactions with filter options.
- **Chart Fragment**: Shows a line chart of transactions that can be filtered and zoomed.
- **Dynamic Bank Configuration**: Planned feature to allow users to add and manage banks and their SMS identifiers via a settings page.

## Installation

To run this project, you will need Android Studio and an Android device or emulator supporting API Level 23 (Marshmallow) or higher.

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/Kidmek/EPhone.git

2. **Open the Project in Android Studio**:

   - Start Android Studio.
   - Open the project by selecting `Open an Existing Project` and navigating to the cloned repository.

3. **Run the Application**:

   - Connect your Android device or use the AVD Manager to start an emulator.
   - Run the application through Android Studio by pressing `Shift + F10` or clicking the Run button.

## App Overview

### Home Fragment

- **Functionality**: Displays a list of recent transactions retrieved via SMS from predefined banks such as BOA (Bank of Abyssinia) and CBE (Commercial Bank of Ethiopia).
- **Filters**: Users can filter transactions by All, Credited, and Debited.
- **Transaction Details**: Each transaction entry shows:
  - Transaction type icon (credit or debit)
  - Bank name
  - Date and time of the transaction
  - Transaction amount
  - Remaining account balance

### Chart Fragment

- **Functionality**: Visualizes transaction amount on a line chart (x-axis: days ago , y-axis: balance) .
- **Filters**: Includes a dropdown to filter data by bank.
- **Interactivity**:
  - Supports vertical zoom to adjust value scale and horizontal zoom to adjust the time range.
  - Tap on data points to view transaction amounts and details.

## Future Enhancements

- **Dynamic Bank Addition**: Introduce a settings page to add and configure new banks and their SMS codes.
- **Time Filters**: Implement filtering options based on the transaction date and time.
- **Current Balance**: Implement cards for each banks to show last checked balance and checked time
- **Pagination**: Implement pagination and loading status for efficiency.


## Preview



https://github.com/Kidmek/EPhone/assets/61234322/93594d3a-623b-4065-9c44-bc927a4d0cfb

## Contributing

Contributions are welcome! Please feel free to submit pull requests or create issues for bugs and feature requests.



