# Setup and Implementation Guide

## Overview

This chatbot was refactored from an incomplete Dialogflow implementation to a fully functional school search application using:
- **Gemini AI** (Google's free AI API) for natural language conversations
- **Google Places API** (free tier with generous limits) for school location data
- **Geocoding API** to convert location names to coordinates

## What Was Changed

### 1. Removed Dependencies
- ❌ Dialogflow (was incomplete and requires paid service)
- ❌ Missing credential files
- ❌ Incomplete session management code

### 2. Added New Architecture

#### API Layer (`app/src/main/java/com/example/testingproject/api/`)
- **Models.kt**: Data classes for Gemini and Places API responses
- **ApiService.kt**: Retrofit interfaces for API calls
- **RetrofitClient.kt**: HTTP client configuration

#### Service Layer (`app/src/main/java/com/example/testingproject/service/`)
- **SchoolSearchService.kt**: Business logic for:
  - Detecting school search queries
  - Extracting locations from natural language
  - Geocoding locations
  - Searching nearby schools
  - Formatting results

#### UI Layer
- **activity_main.xml**: Simple chat interface with scrollable chat history
- **MainActivity.kt**: Handles user input and displays bot responses

### 3. Key Features Implemented

#### Smart Query Detection
The app automatically detects if a user is asking about schools:
```kotlin
// Examples that trigger school search:
"Find schools near Kathmandu"
"Schools in New York"
"Show me education centers around London"
```

#### Location Extraction
Automatically extracts the location from natural language:
```kotlin
"Find schools near Kathmandu" → "Kathmandu"
"Schools in New York City" → "New York City"
```

#### Geocoding
Converts location names to coordinates using Google Geocoding API:
```kotlin
"Kathmandu" → lat: 27.7172, lng: 85.3240
```

#### School Search
Searches for schools within 5km radius using Google Places API with results including:
- School name
- Address (vicinity)
- Rating (if available)
- Number of reviews

#### Fallback to AI Chat
For non-school queries, uses Gemini AI for general conversation.

## API Key Setup

### Getting Gemini API Key (100% Free)

1. Go to https://makersuite.google.com/app/apikey
2. Sign in with Google account
3. Click "Create API Key"
4. Copy the key
5. **Free tier limits**: 60 requests per minute (more than enough for personal use)

### Getting Google Places API Key (Free Tier)

1. Go to https://console.cloud.google.com/
2. Create a new project (or select existing)
3. Enable these APIs:
   - Places API
   - Geocoding API
4. Go to Credentials → Create Credentials → API Key
5. Copy the key
6. **Optional but recommended**: Restrict the key:
   - API restrictions: Select "Places API" and "Geocoding API"
   - Application restrictions: Select "Android apps" and add your app's package name and SHA-1

**Free tier**: $200 monthly credit = ~28,000 place searches/month

### Adding Keys to Project

1. Copy `local.properties.example` to `local.properties`:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edit `local.properties`:
   ```properties
   GEMINI_API_KEY=AIza...your_actual_key
   PLACES_API_KEY=AIza...your_actual_key
   ```

3. The build system automatically injects these as BuildConfig fields

## Building the Project

### Requirements
- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 11 or newer
- Android SDK with API 26+ (Android 8.0)
- Internet access to Google Maven repositories

### Build Steps

1. Open project in Android Studio
2. Sync Gradle (should happen automatically)
3. Connect Android device or start emulator
4. Click Run (or press Shift+F10)

### If Build Fails

**"SDK location not found"**:
- Add to `local.properties`: `sdk.dir=/path/to/your/android/sdk`
- On Mac: Usually `/Users/[username]/Library/Android/sdk`
- On Windows: Usually `C:\Users\[username]\AppData\Local\Android\Sdk`
- On Linux: Usually `/home/[username]/Android/Sdk`

**"API Keys Not Configured" in app**:
- Ensure `local.properties` has valid API keys
- Rebuild project: Build → Rebuild Project
- Clean build: Build → Clean Project, then rebuild

**Network issues downloading dependencies**:
- Check internet connection
- Try using a VPN if behind corporate firewall
- Disable any proxy settings that might interfere

## How It Works

### User Flow

1. **User types message**: "Find schools near Kathmandu"
2. **App detects intent**: Recognizes school search query
3. **Extract location**: "Kathmandu"
4. **Geocode**: Converts "Kathmandu" to coordinates (27.7172, 85.3240)
5. **Search places**: Queries Google Places API for schools within 5km
6. **Format results**: Displays top 5 schools with names, addresses, ratings
7. **Display**: Shows in chat interface

### Non-School Queries

1. **User types**: "What's the weather like?"
2. **App detects**: Not a school search query
3. **Send to Gemini**: Forwards to Gemini AI with context
4. **Get response**: Gemini provides helpful answer
5. **Display**: Shows in chat interface

## Code Structure

```
app/
├── src/main/
│   ├── AndroidManifest.xml          # Permissions (Internet, Location)
│   ├── java/com/example/testingproject/
│   │   ├── MainActivity.kt          # UI controller, handles user input
│   │   ├── api/
│   │   │   ├── Models.kt           # Data classes for API responses
│   │   │   ├── ApiService.kt       # Retrofit service interfaces
│   │   │   └── RetrofitClient.kt   # HTTP client setup
│   │   └── service/
│   │       └── SchoolSearchService.kt  # Core business logic
│   └── res/
│       └── layout/
│           └── activity_main.xml   # Chat UI layout
└── build.gradle.kts                # Dependencies configuration
```

## Testing

### Manual Testing Scenarios

1. **School Search - Major City**:
   - Input: "Find schools near Tokyo"
   - Expected: List of 5 schools in Tokyo with ratings

2. **School Search - Specific Area**:
   - Input: "Schools in Kathmandu, Nepal"
   - Expected: List of schools in Kathmandu

3. **General Chat**:
   - Input: "What can you help me with?"
   - Expected: Gemini AI explains its capabilities

4. **Invalid Location**:
   - Input: "Find schools near XYZ123ABC"
   - Expected: Error message about location not found

5. **Empty Input**:
   - Input: [empty]
   - Expected: Toast message asking for input

## Troubleshooting

### "I couldn't find the location"
- Check spelling of location name
- Try more specific name: "Kathmandu, Nepal" instead of just "Kathmandu"
- Try different location format

### "Error searching for schools"
- Check internet connection
- Verify PLACES_API_KEY is correct
- Check if Places API is enabled in Google Cloud Console
- Check if you've exceeded free tier limits (unlikely)

### "Error communicating with AI"
- Check internet connection
- Verify GEMINI_API_KEY is correct
- Check if you've exceeded rate limits (60/min)

### No results for school search
- Location might not have schools in 5km radius
- Try different location
- Check Google Maps to verify schools exist in that area

## Security Notes

1. **API Keys in local.properties**:
   - This file is gitignored
   - Never commit API keys to version control

2. **API Key Restrictions** (Recommended):
   - Restrict Places API key to your app's package name
   - Restrict Gemini API key to specific referers
   - Monitor usage in respective consoles

3. **Free Tier Monitoring**:
   - Set up billing alerts in Google Cloud Console
   - Monitor Gemini usage at https://makersuite.google.com/

## Future Enhancements

Potential improvements:
- [ ] Add map view to show school locations
- [ ] Filter schools by rating threshold
- [ ] Add distance information for each school
- [ ] Save favorite schools
- [ ] Add more search types (hospitals, restaurants, etc.)
- [ ] Voice input support
- [ ] Multi-language support
- [ ] Dark mode
- [ ] Export search results

## License

MIT License - Free for personal and commercial use.
