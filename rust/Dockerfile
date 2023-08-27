FROM ubuntu:20.04

ENV TZ=Europe/Paris
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get -qq update

RUN apt-get install -y -q \
    build-essential \
    openssl \
    make \
    cmake \
    pkg-config \
    libssl-dev \
    libpq-dev \
    curl \
    pkg-config \
    openssl
    
RUN curl https://sh.rustup.rs -sSf | sh -s -- -y

ENV PATH="/root/.cargo/bin:${PATH}"

WORKDIR /usr/src/app

RUN USER=root cargo init

COPY Cargo.toml .
COPY ./src src

RUN cargo build --release

EXPOSE 7004

CMD ["./target/release/rust"]