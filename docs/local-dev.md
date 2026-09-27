# Environnement de dev local (PostgreSQL + Garage S3)

En profil `dev`, l'application peut tourner entièrement sur la machine locale :
la base PostgreSQL et le stockage S3 (Garage) sont lancés dans des conteneurs **Podman**.
Seul le bot Discord reste en ligne (bot et serveur Discord de test).

| Service    | Conteneur      | Image                                 | Adresse locale           |
|------------|----------------|---------------------------------------|--------------------------|
| PostgreSQL | `dds-postgres` | `docker.io/library/postgres:17`       | `localhost:5432`         |
| Garage S3  | `dds-garage`   | `docker.io/waazaafr/garage-s3-simple` | `http://localhost:3900`  |

Les ports sont publiés uniquement sur `127.0.0.1` : les services ne sont pas accessibles depuis le réseau.

> Les commandes utilisent `podman`. Elles fonctionnent aussi avec `docker`, il suffit de remplacer le mot.

---

## 1. PostgreSQL

### Création (une seule fois)

```bash
podman run -d --name dds-postgres \
  -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=discord_drive_sync_dev \
  -p 127.0.0.1:5432:5432 \
  -v dds-pgdata:/var/lib/postgresql/data \
  docker.io/library/postgres:17
```

- Version 17, comme le serveur.
- Les données sont conservées dans le volume `dds-pgdata` (elles survivent à l'arrêt et à la suppression du conteneur).
- Les tables sont créées par **Flyway** au premier démarrage de l'application (`src/main/resources/db/migration`).

### Vérifier

```bash
podman exec dds-postgres pg_isready -U postgres
podman exec dds-postgres psql -U postgres -c "\l discord_drive_sync_dev"
```

### Console SQL

```bash
podman exec -it dds-postgres psql -U postgres -d discord_drive_sync_dev
```

### Importer un dump du serveur (optionnel)

Les dumps de `pg_dump/` sont au format SQL. Il faut les importer dans une base **vide**
(ils contiennent aussi la table `flyway_schema_history`, et Flyway appliquera ensuite les migrations plus récentes) :

```bash
podman exec dds-postgres psql -U postgres -c "DROP DATABASE IF EXISTS discord_drive_sync_dev WITH (FORCE)"
podman exec dds-postgres psql -U postgres -c "CREATE DATABASE discord_drive_sync_dev"
podman exec -i dds-postgres psql -U postgres -d discord_drive_sync_dev < pg_dump/<fichier>-dump.sql
```

---

## 2. Garage S3

### Création (une seule fois)

```bash
podman run -d --name dds-garage \
  -e NODE_IP=127.0.0.1 \
  -e ADMIN_TOKEN="$(head -c 32 /dev/urandom | base64 | tr -d '/+=' | head -c 40)" \
  -p 127.0.0.1:3900:9000 -p 127.0.0.1:3903:3903 \
  -v dds-garage-config:/config -v dds-garage-meta:/meta -v dds-garage-data:/data \
  docker.io/waazaafr/garage-s3-simple
```

- L'API S3 écoute sur le port **9000** dans le conteneur. Elle est publiée sur **3900**, le même port que sur le serveur.
- `3903` = API d'administration de Garage.
- Ne **pas** mettre `WEBUI_ENABLED=true` : avec cette image, ce mode démarre aussi Tailscale.

### Initialisation (une seule fois)

Attendre que le nœud réponde, puis déclarer la capacité, créer le bucket et une clé d'accès :

```bash
G="podman exec dds-garage garage"

$G status                                   # le nœud doit apparaître dans HEALTHY NODES

NODE_ID=$($G node id -q | cut -d@ -f1)
$G layout assign -z dc1 -c 20G "$NODE_ID"   # 20G = capacité déclarée, rien n'est réservé sur le disque
$G layout apply --version 1

$G bucket create discord-drive-sync
$G key create dds-dev-key
$G bucket allow --read --write --owner discord-drive-sync --key dds-dev-key
```

### Accepter les clients S3 en mode « virtual-hosted » (une seule fois)

Beaucoup de clients S3 (S3 Browser dans VS Code, S3 Drive…) mettent le bucket dans le nom d'hôte
(`discord-drive-sync.localhost:3900`). Par défaut, Garage n'accepte ce mode que sur `*.s3.garage.localhost`,
et ces clients affichent alors un bucket vide. Pour accepter `*.localhost` :

```bash
podman exec dds-garage sed -i 's|^root_domain = ".s3.garage.localhost"|root_domain = ".localhost"|' /config/garage.toml
podman restart dds-garage
```

La modification est conservée dans le volume `dds-garage-config`. Le mode « path style » utilisé par l'application
(`forcePathStyle(true)` dans `S3Config`) continue de fonctionner.

### Récupérer la clé d'accès

```bash
podman exec dds-garage garage key info dds-dev-key --show-secret
```

→ `Key ID` et `Secret key` à reporter dans le `.env`.

### Commandes utiles

```bash
podman exec dds-garage garage bucket list
podman exec dds-garage garage bucket info discord-drive-sync   # taille et nombre d'objets
podman exec dds-garage garage key list
podman logs --since 10m dds-garage                             # requêtes et erreurs S3
```

---

## 3. Configuration de l'application (`.env`)

```dotenv
DEV_DB_URL=localhost:5432
DEV_DB_NAME=discord_drive_sync_dev
DEV_DB_USERNAME=postgres
DEV_DB_PASSWORD=postgres

DEV_S3_URL=http://localhost:3900
DEV_S3_KEY_ID=<Key ID de dds-dev-key>
DEV_S3_SECRET_KEY=<Secret key de dds-dev-key>
DEV_S3_REGION=garage
DEV_S3_BUCKET=discord-drive-sync
```

Puis lancer l'application avec le profil `dev` (configuration `DDS-dev` dans IntelliJ ou VS Code).

---

## 4. Au quotidien

Les conteneurs **ne redémarrent pas tout seuls** après un redémarrage du PC :

```bash
podman start dds-postgres dds-garage     # démarrer
podman stop dds-postgres dds-garage      # arrêter
podman ps -a                             # état des conteneurs
```

---

## 5. Clients graphiques

### PostgreSQL : extension VS Code « PostgreSQL » (`ms-ossdata.vscode-pgsql`)

| Champ         | Valeur                   |
|---------------|--------------------------|
| Server name   | `localhost`              |
| Port          | `5432`                   |
| User / mot de passe | `postgres` / `postgres` |
| Database      | `discord_drive_sync_dev` |

IntelliJ (onglet Database) fonctionne avec les mêmes paramètres.

### S3 : extension VS Code « S3 Browser » (`nntk.vscode-s3`), S3 Drive, Cyberduck…

| Champ      | Valeur                                         |
|------------|------------------------------------------------|
| Endpoint   | `http://localhost:3900`                        |
| Région     | `garage` (**obligatoire**, `us-east-1` est refusé) |
| Access key / Secret | clé `dds-dev-key` (voir ci-dessus)    |
| Path style | activé si l'option existe                      |

---

## 6. Dépannage

| Symptôme | Cause / solution |
|----------|------------------|
| `BindException: Adresse déjà utilisée` au démarrage de l'appli | Une instance tourne déjà sur `DEV_SERVER_PORT`. `ss -ltnp \| grep :<port>` pour la trouver, puis l'arrêter. |
| `Connection refused` vers la base ou le S3 | Conteneurs arrêtés : `podman start dds-postgres dds-garage`. |
| Client S3 : bucket vide | Voir « virtual-hosted » ci-dessus, ou activer le path style dans le client. |
| `AuthorizationHeaderMalformed … expected: …/garage/…` | La région du client doit être `garage`. |
| `Access key … is not allowed to create buckets` | Le client a pris l'envoi d'un fichier pour une création de bucket (même cause que le bucket vide). |

---

## 7. Tout supprimer

```bash
podman rm -f dds-postgres dds-garage
podman volume rm dds-pgdata dds-garage-config dds-garage-meta dds-garage-data   # ⚠️ efface les données
```
