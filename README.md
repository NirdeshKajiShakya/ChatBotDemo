# School Search AI Chatbot

An Android chatbot application that helps users find schools near any location using free AI and mapping services.

## Features

- 🤖 **AI-Powered Chat**: Uses Google's Gemini AI (free tier) for natural conversation
- 🏫 **School Search**: Find schools near any location worldwide
- 📍 **Location-Based**: Search schools in and around specific cities/areas
- ⭐ **Ratings & Reviews**: View school ratings and review counts
- 💬 **Natural Language**: Ask questions in plain English

## Example Queries

- "Find schools near Kathmandu"
- "Show me schools in New York"
- "What schools are around London?"
- "List schools close to Tokyo"

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Android device/emulator with API level 26+
- Google Gemini API key (free)
- Google Places API key (free tier)

### Setting Up API Keys

Both services offer free tiers:

#### 1. Get Gemini API Key (Free)
1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy your API key

#### 2. Get Google Places API Key (Free Tier)
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable "Places API" and "Geocoding API"
4. Go to "Credentials" → "Create Credentials" → "API Key"
5. Copy your API key
6. (Optional) Restrict the key to Android apps and specific APIs

**Note**: Google Places API has a free tier with monthly credits. Monitor your usage in the console.

### Installation

1. Clone this repository:
```bash
git clone https://github.com/NirdeshKajiShakya/ChatBotDemo.git
cd ChatBotDemo
```

2. Copy `local.properties.example` to `local.properties`:
```bash
cp local.properties.example local.properties
```

3. Edit `local.properties` and add your API keys:
```properties
GEMINI_API_KEY=your_gemini_api_key_here
PLACES_API_KEY=your_google_places_api_key_here
```

4. Open the project in Android Studio

5. Sync Gradle files

6. Run the app on your device/emulator

## Architecture

- **MainActivity.kt**: Main UI controller
- **SchoolSearchService.kt**: Handles AI chat and school search logic
- **API Package**:
  - `Models.kt`: Data models for API responses
  - `ApiService.kt`: Retrofit service interfaces
  - `RetrofitClient.kt`: HTTP client configuration

## Tech Stack

- **Language**: Kotlin
- **UI**: Android Views (XML layouts)
- **Networking**: Retrofit + OkHttp
- **Async**: Kotlin Coroutines
- **AI**: Google Gemini API
- **Maps/Places**: Google Places API + Geocoding API

## Free Tier Limitations

### Gemini API (Google AI)
- Free tier: 60 requests per minute
- Sufficient for personal/development use

### Google Places API
- $200 monthly credit (covers ~28,000 search requests)
- Free tier is generous for moderate usage
- Monitor usage in Google Cloud Console

## Project Structure

```
app/src/main/java/com/example/testingproject/
├── MainActivity.kt              # Main activity
├── api/
│   ├── ApiService.kt           # Retrofit interfaces
│   ├── Models.kt               # API response models
│   └── RetrofitClient.kt       # HTTP client setup
└── service/
    └── SchoolSearchService.kt  # Core business logic
```

## Troubleshooting

### "API Keys Not Configured" Error
- Ensure `local.properties` exists in the project root
- Verify API keys are correct and active
- Rebuild the project (Build → Rebuild Project)

### No Schools Found
- Check your internet connection
- Verify the location name is spelled correctly
- Try a more specific location (e.g., "Kathmandu, Nepal")

### API Errors
- Check if you've enabled the required APIs in Google Cloud Console
- Verify API key restrictions aren't blocking requests
- Check if you've exceeded free tier limits

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Acknowledgments

- Google Gemini AI for free AI capabilities
- Google Places API for location services
- Android community for great resources

## Support

For issues and questions, please create an issue in the GitHub repository.
