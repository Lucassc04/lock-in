# Lock In Backend: A Workout Tracker Application

## Overview

This project was created to apply university learnings while exploring new concepts. Every architectural decision was intentionally made to solve a personal need: while excellent apps exist for tracking running progress (like Strava), I never found a truly effective gym tracking solution. This application aims to fill that gap, with plans to grow and add features that genuinely help track workout progress.
This project helped me learn a lot and add some real experience to creating something. Had to learn how to better write tests, implement other dependencies such as MapStruct and JWT Authentication, and overall learn how to write cleaner and readable code.
## Tech Stack

- **Language & Framework**: Java, Spring Boot, Spring Security
- **Database**: PostgreSQL (production), H2 (development)
- **Authentication**: JWT with Refresh and Access Tokens

## Getting Started

Clone the repository and start the application:

```bash
docker compose up --build
```

## API Documentation

### Authentication (`/api/v1/auth`)

All users must register and log in before using the application.

#### Register
**POST** `/register`

```json
{
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "password": "string"
}
```

#### Login
**POST** `/login`

```json
{
    "email": "string",
    "password": "string"
}
```

**Response**: Returns an access token (for API requests) and a refresh token (as a cookie for token renewal).

#### Refresh Token
**POST** `/refresh`

Renews your access token using the refresh token cookie. If the refresh token expires, you must log in again.

---

### Exercises (`/api/v1/exercise`)

Create custom exercises tailored to your workout needs. Default exercises will be added in future updates.

#### Available Muscle Groups

**Chest**: `UPPER_CHEST`, `LOWER_CHEST`, `MIDDLE_CHEST`

**Back**: `TRAPEZIUS_UPPER_BACK`, `RHOMBOIDS_UPPER_BACK`, `TERES_MAJOR_UPPER_BACK`, `LATS`

**Shoulders**: `FRONT_DELTS`, `LATERAL_DELTS`, `REAR_DELTS`

**Arms**:
- Biceps: `LONG_HEAD_BICEPS`, `SHORT_HEAD_BICEPS`, `BRACHIALIS`, `FOREARMS`
- Triceps: `LONG_HEAD_TRICEPS`, `SHORT_HEAD_TRICEPS`, `MEDIAL_HEAD_TRICEPS`

**Core**: `ABS`

**Lower Body**:
- Glutes: `MAXIMUS_GLUTES`, `MEDIUS_GLUTES`, `MINIMUS_GLUTES`
- Legs: `QUADS`, `HAMSTRING`, `ADDUCTORS`, `ABDUCTORS`, `CALVES`

#### Endpoints

**POST** `/` - Create Exercise

```json
{
    "name": "Bench Press",
    "primaryMuscles": ["MIDDLE_CHEST"],
    "secondaryMuscles": ["FRONT_DELTS"],
    "description": "With Barbell"
}
```

**GET** `/{id}` - Get exercise by ID (your exercises only)

**GET** `/` - Get all available exercises

**PUT** `/{id}` - Update exercise (requires ExerciseRequest body)

**DELETE** `/{id}` - Delete exercise

---

### Workout Plans (`/api/v1/workoutPlan`)

Create structured workout plans using your custom exercises.

#### WorkoutPlanRequest Format

```json
{
    "name": "Mini Chest Workout",
    "series": [
        {
            "exerciseName": "Bench Press",
            "series": 4
        },
        {
            "exerciseName": "Incline Dumbbell Press",
            "series": 3
        }
    ]
}
```

#### Endpoints

**POST** `/` - Create workout plan (requires WorkoutPlanRequest)

**GET** `/{id}` - Get workout plan by ID (creator only)

**PUT** `/{id}` - Update workout plan (creator only, requires WorkoutPlanRequest)

**DELETE** `/{id}` - Delete workout plan (creator only)

**PUT** `/subscribe/{id}` - Subscribe to another user's workout plan

---

### Workouts (`/api/v1/workout`)

Track your actual workout sessions.

#### Start a Workout
**POST** `/start?workoutPlanId={id}` (optional parameter)

Starts an ongoing workout session. Optionally follow a workout plan you created or subscribed to.

#### Cancel a Workout
**POST** `/cancel`

Cancels the current ongoing workout.

#### Finish a Workout
**PATCH** `/finish`

Completes your workout session.

#### executedWorkoutRequest Format

```json
{
    "workingSeries": [
        {
            "exerciseName": "Bench Press",
            "weight": 20.0,
            "repetitions": 6,
            "series": 2
        }
    ],
    "warmupSeries": []
}
```

#### Log a Past Workout
**POST** `/`

Manually add a completed workout.

```json
{
    "executedWorkoutRequest": {
        "workingSeries": [...],
        "warmupSeries": [],
    },
    "startTime": "2024-01-20T10:00:00",
    "finishTime": "2024-01-20T11:30:00"
    "notes": "string",
}
```

---


## Contributing

This is a personal learning project, but suggestions and feedback are welcome!
