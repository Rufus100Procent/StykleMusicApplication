// DOM references
const songForm = document.getElementById('songForm');
const addSongButton = document.getElementById('addSongButton');
const fileInput = document.getElementById('fileInput');
const songNameInput = document.getElementById('songName');
const artistNameInput = document.getElementById('artistName');
const releaseYearInput = document.getElementById('releaseYear');
const songList = document.getElementById('songList');
const prevButton = document.getElementById('prevButton');
const nextButton = document.getElementById('nextButton');
const audioPlayer = document.getElementById('audioPlayer');
const errorContainer = document.getElementById('errorContainer');
const uploadFormContainer = document.getElementById('uploadFormContainer');
const genreInput = document.getElementById('genre');
const showCustomPrompt = (labelText, defaultValue, callback) => {
    const customPromptContainer = document.getElementById('customPromptContainer');
    const customPromptContent = document.getElementById('customPromptContent');
    const customPromptLabel = document.getElementById('customPromptLabel');
    const customPromptInput = document.getElementById('customPromptInput');
    const customPromptSubmit = document.getElementById('customPromptSubmit');
    const customPromptCancel = document.getElementById('customPromptCancel');

    customPromptLabel.textContent = labelText;
    customPromptInput.value = defaultValue;
    customPromptContainer.style.display = 'flex';

    customPromptSubmit.onclick = () => {
        const value = customPromptInput.value;
        customPromptContainer.style.display = 'none';
        callback(value);
    };

    customPromptCancel.onclick = () => {
        customPromptContainer.style.display = 'none';
    };
};

const showUploadForm = () => {
    uploadFormContainer.classList.add('show');
};

const hideUploadForm = () => {
    uploadFormContainer.classList.remove('show');
};

// Perform search when the page loads
const searchSongs = () => {
    const searchQuery = document.getElementById('searchQuery').value.toLowerCase();
    const songRows = document.querySelectorAll('#songList tr');

    songRows.forEach(row => {
        const songName = row.children[1].textContent.toLowerCase();
        const artist = row.children[2].textContent.toLowerCase();
        const album = row.children[3].textContent.toLowerCase();
        const genre = row.children[4].textContent.toLowerCase();
        const year = row.children[5].textContent.toLowerCase();

        const matches = songName.includes(searchQuery) || artist.includes(searchQuery) ||
            album.includes(searchQuery) || year.includes(searchQuery);

        row.style.display = matches ? 'table-row' : 'none';
    });
};
document.getElementById('searchQuery').addEventListener('input', searchSongs);

document.getElementById('searchQuery').addEventListener('input', searchSongs);
// Function to toggle the display of the song form

const toggleForm = () => {
    songForm.style.display = songForm.style.display === 'none' ? 'block' : 'none';
    addSongButton.textContent = songForm.style.display === 'none' ? 'Add Song' : 'Cancel';
};


let songs = []; // Array to store the songs
let currentSongIndex = 0; // Index of the currently playing song
let recentlyPlayed = [];
let shuffleMode = false;

const shuffleSongs = () => {
    shuffleMode = !shuffleMode;
    const shuffleButton = document.getElementById('shuffleButton');
    if (shuffleMode) {
        shuffleButton.classList.add('active');
    } else {
        shuffleButton.classList.remove('active');
    }
};


// Function to play the previous song
const playPreviousSong = () => {
    if (shuffleMode) {
        const randomIndex = getRandomIndex();
        playSongByIndex(randomIndex);
    } else {
        currentSongIndex = (currentSongIndex - 1 + songs.length) % songs.length;
        playSongByIndex(currentSongIndex);
    }
};
const getRandomIndex = () => {
    // Generate a random index different from the current one
    let randomIndex = currentSongIndex;
    while (randomIndex === currentSongIndex) {
        randomIndex = Math.floor(Math.random() * songs.length);
    }
    return randomIndex;
};
// Function to play the next song
const playNextSong = () => {
    if (shuffleMode) {
        const randomIndex = Math.floor(Math.random() * songs.length);
        playSongByIndex(randomIndex);
    } else {
        currentSongIndex = (currentSongIndex + 1) % songs.length;
        playSongByIndex(currentSongIndex);
    }
};

// Function to play a song by index
const playSongByIndex = (index) => {
    currentSongIndex = index;
    const song = songs[index];
    recentlyPlayed.unshift(song);
    if (recentlyPlayed.length > 3) {
        recentlyPlayed.pop();
    }

    fetch(`/${song.id}/file`)
        .then((response) => {
            if (!response.ok) {
                throw new Error('Failed to retrieve the song file');
            }
            return response.blob();
        })
        .then((blob) => {
            const fileUrl = URL.createObjectURL(blob);
            audioPlayer.src = fileUrl;
            audioPlayer.play();
            updateButtonStates();
            highlightCurrentSong();
        })
        .catch((error) => {
            console.error('Error:', error);
        });
};

// Function to update the states of the previous and next buttons
const updateButtonStates = () => {
    // TODO: Update button states
};

// Function to highlight the currently playing song
const highlightCurrentSong = () => {
    const rows = document.querySelectorAll('#songList tr');
    rows.forEach((row) => row.classList.remove('playing'));
    const currentRow = document.getElementById(`songRow${currentSongIndex}`);
    currentRow.classList.add('playing');
};

// Function to display specific error messages
const displayErrorMessages = (errors) => {
    errorContainer.innerHTML = Object.entries(errors)
        .map(([key, value]) => `<p class="errorMessage">${value}</p>`)
        .join('');

    // Clear error messages after 3 seconds
    setTimeout(() => {
        errorContainer.innerHTML = '';
    }, 3000);
};
// Function to display a success message
const displaySuccessMessage = (message) => {
    errorContainer.innerHTML = `<p class="successMessage">${message}</p>`;

    // Clear success message after 3 seconds
    setTimeout(() => {
        errorContainer.innerHTML = '';
    }, 3000);
};
// Function to validate form data
const validateFormData = (file, songName, artistName, genre, releaseYear) => {
    const fileName = file.name;
    const fileExtension = fileName.split('.').pop().toLowerCase();

    if (fileExtension !== 'mp3') {
        displayErrorMessages({ mp3File: 'Invalid file format. Only MP3 files are allowed.' });
        return false;
    }

    if (songName.trim() === '' || artistName.trim() === '' || genre.trim() === '') {
        displayErrorMessages({ songName: 'Song name, artist name, and genre cannot be empty.' });
        return false;
    }

    if (genre.trim() === '') {
        displayErrorMessages({ genre: 'Genre cannot be empty.' });
        return false;
    }

    // Only validate release year if it's not empty
    if (releaseYear.trim() !== '' && !/^\d{4}$/.test(releaseYear)) {
        displayErrorMessages({ songName: 'Invalid release year. Please enter a four-digit number.' });
        return false;
    }


    return true;
};

// Submit the form data and handle the response
document.getElementById('uploadForm').addEventListener('submit', (e) => {
    e.preventDefault();

    const file = fileInput.files[0];
    const songName = songNameInput.value;
    const artistName = artistNameInput.value;
    const genre = genreInput.value; // Get the genre value
    const releaseYear = releaseYearInput.value;

    if (!validateFormData(file, songName, artistName,genre ,releaseYear)) {
        return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('songName', songName);
    formData.append('artistName', artistName);
    formData.append('album', document.getElementById('album').value || '');
    formData.append('genre', genre); // Append the genre data to the form data
    formData.append('releaseYear', releaseYear);


    fetch('/upload', {
        method: 'POST',
        body: formData
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Failed to upload the song');
            }
            return response.json();
        })
        .then((song) => {
            songs.push(song);

            const row = document.createElement('tr');
            row.id = `songRow${songs.length - 1}`;
            row.innerHTML = `
        <td>
          <button class="playButton" onclick="playSongByIndex(${songs.length - 1})"><i class="fa-solid fa-play"></i></button>
          <button class="deleteButton" onclick="deleteSong(${song.id}, ${songs.length - 1})"><i class="fa-solid fa-circle-xmark fa-lg"></i></button>
          <button class="editButton" onclick="editSong(${song.id}, ${songs.length - 1})"><i class="fa-solid fa-pen-to-square fa-lg"></i></button>
        </td>
        <td>${song.songName}</td>
        <td>${song.artistName}</td>
        <td>${song.album || ''}</td>
        <td>${song.genre || ''}</td>
        <td>${song.releaseYear}</td>
      `;

            songList.appendChild(row);
            updateButtonStates();
            displaySuccessMessage(`Successfully uploaded ${song.songName}`);
        })
        .catch((error) => {
            console.error('Error:', error);

            if (error.message === 'Failed to upload the song') {
                displayErrorMessages(error.response);
            } else {
                displayErrorMessages({ duplicate: 'Failed to upload the song.' });
            }
        });
});

// Function to delete a song
// Function to delete a song
const deleteSong = (songId, rowIndex) => {
    // Check if the song being deleted is the currently playing song
    const isCurrentSong = currentSongIndex === rowIndex;

    // Show the custom prompt for confirmation
    showCustomPrompt("Are you sure you want to delete this song?", "Yes", (value) => {
        if (value.toLowerCase() === "yes") {
            fetch(`/${songId}`, {
                method: "DELETE"
            })
                .then((response) => {
                    if (response.ok) {
                        // If the deleted song was the currently playing song, stop the playback and clear the source
                        if (isCurrentSong) {
                            audioPlayer.pause();
                            audioPlayer.src = '';
                        }

                        // Remove the song from the songs array
                        songs.splice(rowIndex, 1);

                        // Remove the song row from the table
                        const row = document.getElementById(`songRow${rowIndex}`);
                        row.parentNode.removeChild(row);

                        updateButtonStates();
                        highlightCurrentSong();
                        displaySuccessMessage("Song successfully deleted");

                        // Reset the current song index if necessary
                        if (currentSongIndex >= rowIndex && currentSongIndex > 0) {
                            currentSongIndex--;
                        }
                    } else {
                        throw new Error("Failed to delete the song");
                    }
                })

        }
    });
};


// Function to edit a song
const editSong = (songId, rowIndex) => {
    const song = songs[rowIndex];

    // Define the labels and default values for the custom prompt
    const labelSongName = 'Enter a new song name:';
    const labelArtistName = 'Enter a new artist name:';
    const labelAlbum = 'Enter a new album:';
    const labelGenre = 'Enter a new Genre:';
    const labelReleaseYear = 'Enter a new release year:';

    // Set the default values based on the current song information
    const defaultValueSongName = song.songName;
    const defaultValueArtistName = song.artistName;
    const defaultValueAlbum = song.album || '';
    const defaultValueGenre = song.genre || '';
    const defaultValueReleaseYear = song.releaseYear.toString();

    // Show the custom prompt for each input field and handle the values
    showCustomPrompt(labelSongName, defaultValueSongName, (newSongName) => {
        showCustomPrompt(labelArtistName, defaultValueArtistName, (newArtistName) => {
            showCustomPrompt(labelAlbum, defaultValueAlbum, (newAlbum) => {
                showCustomPrompt(labelAlbum, defaultValueGenre, (newGenre) => {
                    showCustomPrompt(labelReleaseYear, defaultValueReleaseYear, (newReleaseYear) => {
                        // Perform the song edit using the entered values
                        const formData = new FormData();
                        formData.append('songName', newSongName);
                        formData.append('artistName', newArtistName);
                        formData.append('album', newAlbum);
                        formData.append('genre', newGenre);
                        formData.append('releaseYear', newReleaseYear);

                        fetch(`/${songId}/edit`, {
                            method: 'PUT',
                            body: formData
                        })
                            .then((response) => {
                                if (response.ok) {
                                    // Update the song object in the songs array
                                    songs[rowIndex].songName = newSongName;
                                    songs[rowIndex].artistName = newArtistName;
                                    songs[rowIndex].album = newAlbum;
                                    songs[rowIndex].genre = newGenre;
                                    songs[rowIndex].releaseYear = newReleaseYear;

                                    // Update the song row in the table
                                    const row = document.getElementById(`songRow${rowIndex}`);
                                    row.innerHTML = `
                  <td>
                    <button class="playButton" onclick="playSongByIndex(${rowIndex})"><i class="fa-solid fa-play"></i></button>
                    <button class="deleteButton" onclick="deleteSong(${songId}, ${rowIndex})"><i class="fa-solid fa-circle-xmark fa-lg"></i></button>
                    <button class="editButton" onclick="editSong(${songId}, ${rowIndex})"><i class="fa-solid fa-pen-to-square fa-lg"></i></button>
                  </td>
                  <td>${newSongName}</td>
                  <td>${newArtistName}</td>
                  <td>${newAlbum || ''}</td>
                  <td>${newGenre || ''}</td>
                  <td>${newReleaseYear}</td>
                `;

                                    highlightCurrentSong();
                                    displaySuccessMessage('Successfully updated');
                                } else {
                                    throw new Error('Failed to edit the song');
                                }
                            })
                            .catch((error) => {
                                console.error('Error:', error);
                                displayErrorMessages({ editError: 'Failed to edit the song.' });
                            });
                    });
                });
            });
        });
    });
};

const showRecentlyPlayed = () => {
    // Clear the songList table
    songList.innerHTML = '';

    // Iterate over the recentlyPlayed array and create rows for the top 3 songs
    recentlyPlayed.slice(0, 3).forEach((song, index) => {
        const row = document.createElement('tr');
        row.id = `recentlyPlayedRow${index}`;
        row.innerHTML = `
      <td>${index + 1}</td>
      <td>${song.songName}</td>
      <td>${song.artistName}</td>
      <td>${song.album || ''}</td>
      <td>${song.genre || ''}</td>
      <td>${song.releaseYear}</td>
    `;

        songList.appendChild(row);
    });
};
// Load the initial song list
fetch('/all')
    .then((response) => {
        if (!response.ok) {
            throw new Error('Failed to retrieve the song list');
        }
        return response.json();
    })
    .then((data) => {
        songs = data;

        // Render the song list
        songs.forEach((song, index) => {
            const row = document.createElement('tr');
            row.id = `songRow${index}`;
            row.innerHTML = `
        <td>
          <button class="playButton" onclick="playSongByIndex(${index})"><i class="fa-solid fa-play"></i></button>
          <button class="deleteButton" onclick="deleteSong(${song.id}, ${index})"><i class="fa-solid fa-circle-xmark fa-lg"></i></button>
          <button class="editButton" onclick="editSong(${song.id}, ${index})"><i class="fa-solid fa-pen-to-square fa-lg"></i></button>
        </td>
        <td>${song.songName}</td>
        <td>${song.artistName}</td>
        <td>${song.album || ''}</td>
        <td>${song.genre}</td>
        <td>${song.releaseYear}</td>
      `;

            songList.appendChild(row);
        });

        updateButtonStates();
        highlightCurrentSong();
    })
    .catch((error) => {
        console.error('Error:', error);
        displayErrorMessages({ listError: 'Failed to retrieve the song list.' });
    });

// Function to automatically play the next song when the current song ends
audioPlayer.addEventListener('ended', () => {
    if (currentSongIndex === songs.length - 1) {
        playSongByIndex(0);
    } else {
        playNextSong();
    }
});