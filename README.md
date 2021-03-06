# meet-n-music
VIA University College Android Development course project. 



## About the app
Meet 'n Music is an android app that is used to publish and share musical events: festivals, concerts, parties, etc. Users can search for events and notify to the event manager that they will assist. Each event shows its own information: description, images, weather, COVID restrictions, etc. 

Our motivation for this project was to create an app where users can still be conected between them trough music as we think it is one of the things that most unites young people.

## Figma prototype
[You can see app prototype here](https://www.figma.com/proto/aGaNYe0UEvxEouh3t2du4n/AndroidPrototype?node-id=19%3A77&scaling=scale-down&page-id=0%3A1&starting-point-node-id=12%3A2)

## Video demonstration
[You can see the video demonstration here](https://www.youtube.com/watch?v=1iB0S4_wRZI)

## MoSCoW requirements
Our application will have some features with different crucial roles, so in order to have them structured we made a MoSCoW requirements.
- Meet n' Music is based on CRUD operations, so it **must have** :
    - A general home feed where the user can see scheduled events of its interest. ✅
    - Detailed description for each event (Where, when, which music, etc). ✅
    - COVID-19 prevention measures and restrictions. ✅
    - A search bar where the user can find events either by name or genre. ✅
    - All user settings and user profile features. ✅
    - Log in and sign up options. ✅
    - Creation event button for letting the user organize one. ✅
    - Images inside event descriptions. ✅
    - 'I will attend' button, in order to register the user to the event. ✅
    - Editing and checking the already created event. ✅

- Once we the 'must' requirements are done, our application will hard improve with some conditions that **should be** :
    - Feed organization based on location. ❌
    - Correct use of an API for the temperature and weather conditions of an event. ❌
    - Use of an other API for creating a map where the user can see its location and the events near him. ✅

- For a high quality android app we **could apply** :
    - Q&A section below the event description. ❌
    - Notifications and alerts in the phone. ❌

- We thought about this ideas, but we **won't have time** to implement them :
    - A place where you can see the history and activity of your friends. ❌
    - Changing the Q&A section for a real time chat. ❌


## APIs
For using Google Maps SDK, you need to create ```local.properties``` file and add:
```
MAPS_API_KEY={key here}
```

For connecting with Firebase Services, follow Android Studio instructions and the project will be set.
