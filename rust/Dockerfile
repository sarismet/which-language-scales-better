FROM rust:1.72 as build

RUN USER=root cargo new --bin iso_scale
WORKDIR /iso_scale

COPY ./Cargo.lock ./Cargo.lock
COPY ./Cargo.toml ./Cargo.toml

RUN cargo build --release
RUN rm src/*.rs

COPY ./src ./src

RUN rm ./target/release/deps/*
RUN cargo build --release

FROM rust:1.72

COPY --from=build /iso_scale/target/release/rust .

EXPOSE 7004

CMD ["./rust"]

