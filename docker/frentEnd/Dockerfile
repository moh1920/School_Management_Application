# ---------- Build stage ----------
FROM node:18 AS build-stage
WORKDIR /app

# Copier seulement les fichiers de dépendances d'abord (évite de copier node_modules de l'hôte)
COPY package*.json ./

# Installer de façon reproductible si tu as package-lock.json, sinon npm install
RUN npm ci --silent || npm install --silent

# Copier le reste du projet (node_modules est ignoré par .dockerignore)
COPY . .

# Build Angular en production (usage moderne)
RUN npm run build -- --configuration=production

# ---------- Serve stage ----------
FROM nginx:alpine

# Nettoyer le dossier web par défaut (optionnel)
RUN rm -rf /usr/share/nginx/html/*

# Copier le build produit
COPY --from=build-stage /app/dist/application-student /usr/share/nginx/html

# Copier la config nginx (assure-toi que nginx.conf est dans le contexte de build)
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
