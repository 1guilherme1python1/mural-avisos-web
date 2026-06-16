#!/usr/bin/env bash
set -euo pipefail

FRONTEND_DOMAIN="sigepx.mpac.mp.br"
BACKEND_DOMAIN="sigepauthx.mpac.mp.br"
ENV_FILE=".env.prod"
COMPOSE_FILE="docker-compose.prod.yml"

cd "$(dirname "$0")"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker nao encontrado. Instale o Docker antes de subir a producao."
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "Docker Compose nao encontrado. Instale o plugin 'docker compose'."
  exit 1
fi

random_secret() {
  if command -v openssl >/dev/null 2>&1; then
    openssl rand -base64 32 | tr -d '\n'
  else
    date +%s%N | sha256sum | awk '{print $1}'
  fi
}

if [ ! -f "$ENV_FILE" ]; then
  cat > "$ENV_FILE" <<EOF
FRONTEND_DOMAIN=$FRONTEND_DOMAIN
BACKEND_DOMAIN=$BACKEND_DOMAIN
MYSQL_DATABASE=mural_comunitario
MYSQL_USER=mural_user
MYSQL_PASSWORD=$(random_secret)
MYSQL_ROOT_PASSWORD=$(random_secret)
EOF
  chmod 600 "$ENV_FILE"
  echo "Arquivo $ENV_FILE criado com senhas aleatorias."
fi

echo "Subindo producao para https://$FRONTEND_DOMAIN ..."
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" up -d --build --remove-orphans --force-recreate

echo
echo "Pronto. Verifique os containers com:"
echo "  docker compose --env-file $ENV_FILE -f $COMPOSE_FILE ps"
echo
echo "Acesse:"
echo "  https://$FRONTEND_DOMAIN"
echo
echo "API:"
echo "  https://$BACKEND_DOMAIN/api/comunicados"
