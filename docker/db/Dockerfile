FROM postgres

ENV POSTGRES_DB swe304
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres

EXPOSE 5432

HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD pg_isready -U postgres || exit 1