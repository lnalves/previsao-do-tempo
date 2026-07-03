# Weather Forecast

Desktop app that tracks current weather and forecasts for user-saved locations via the Open-Meteo API, persisting everything for later consultation.

## Language

**Location**:
A user-saved place with a name and geographic coordinates that weather data is fetched for.
_Avoid_: City, cidade, localizacao

**Weather Reading**:
An observed weather condition (temperature, feels-like, precipitation) captured at the moment the user refreshes data. A fact about the past — never changes once recorded.
_Avoid_: Hourly data, dados horarios, current weather

**Daily Forecast**:
The predicted max/min temperature and precipitation probability for a specific date at a Location. A prediction — superseded by newer fetches for the same date.
_Avoid_: Daily data, dados diarios

**Reading History**:
The chronological record of Weather Readings for a Location, each enriched with the Daily Forecast that existed for that date.
_Avoid_: Histórico de previsões, forecast history
