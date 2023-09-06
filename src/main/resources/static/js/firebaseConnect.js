import { initializeApp } from "https://www.gstatic.com/firebasejs/10.3.1/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.3.1/firebase-analytics.js";
import { getStorage, ref, uploadBytes, getDownloadURL } from 'https://www.gstatic.com/firebasejs/10.3.1/firebase-storage.js';

const firebaseConfig = {
    apiKey: "AIzaSyDTZzbBI45ApKflddvOnfxc07BJBRbw8cg",
    authDomain: "goservice-squad9.firebaseapp.com",
    projectId: "goservice-squad9",
    storageBucket: "goservice-squad9.appspot.com",
    messagingSenderId: "17680711199",
    appId: "1:17680711199:web:a219983130702b5ad21f2a",
    measurementId: "G-BJ7LWL1JY1"
};
  const app = initializeApp(firebaseConfig);
  const storage = getStorage(app);

const btnHandleImage = document.getElementById("btn-handle-image");
const fileInput = document.getElementById("inputImage");
const fotoUsuario = document.querySelector(".foto-usuario-form");
const urlFoto = document.getElementById("urlFoto");
async function uploadImage() {
  const file = fileInput.files[0];
  if (!file) {
    alert("Por favor, selecione uma imagem para fazer upload.");
    return;
  }

  const storageRef = ref(storage, file.name);

  const uploadTask = uploadBytes(storageRef, file);

  try {
    // Aguarde o upload concluir
    const snapshot = await uploadTask;

    // Obtenha a URL de download após o upload bem-sucedido
    const downloadURL = await getDownloadURL(snapshot.ref);

    alert("Imagem enviada com sucesso. ");
    fotoUsuario.src = downloadURL;
    urlFoto.value = downloadURL;
  } catch (error) {
    // Lida com erros durante o upload
    console.error(error);
  }
}

btnHandleImage.addEventListener("click", uploadImage);