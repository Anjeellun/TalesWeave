# Android Application Project - "Belajar Pengembangan Aplikasi Android Intermediate" Submission

## Overview

This Android application is a submission for the "Belajar Pengembangan Aplikasi Android Intermediate" course by Dicoding as a part of the Bangkit Academy cohort. The app builds upon the previous submission, incorporating new features such as Maps integration, Paging List with Paging 3, and unit testing in the ViewModel.

## Features

### Existing Features

The application maintains the functionality of the previous submission, including Favorite User with Database and Theme Settings.

### New Features

#### Maps Integration

- A new page displays a map showing the locations of stories using markers or custom icons.
- Data for stories with latitude and longitude information is retrieved using the following parameter: `https://story-api.dicoding.dev/v1/stories?location=1`.

#### Paging List

- Implements Paging 3 to display a list of stories efficiently.
- Enables seamless scrolling through large sets of data.

#### Unit Testing

- Implements unit tests for ViewModel functions that retrieve Paging data.
- Includes scenarios to ensure proper data loading, non-null results, correct data count, and accurate retrieval of the first data item.
- Covers scenarios for empty data, ensuring the returned data count is zero.

## Additional Notes

- This project serves as evidence of successful completion of the "Belajar Pengembangan Aplikasi Android Intermediate", demonstrates advanced Android app development skills by incorporating new features such as Maps integration, Paging List with Paging 3, and unit testing, while maintaining the excellence of the existing features.
