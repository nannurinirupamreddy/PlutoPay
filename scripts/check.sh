#!/usr/bin/env bash
# Compiles PlutoPay and runs the transfer checks. Exits non-zero if anything fails.
set -euo pipefail

cd "$(dirname "$0")/.."
CLASSPATH="res/json-simple-1.1.1.jar"
OUT="$(mktemp -d)"
trap 'rm -rf "$OUT"' EXIT

command -v javac >/dev/null || { echo "javac not found: install JDK 17+" >&2; exit 1; }

echo "Compiling $(ls src/*.java | wc -l | tr -d ' ') source files..."
javac -d "$OUT" -cp "$CLASSPATH" src/*.java test/*.java

echo "Running transfer checks..."
# User reads latestAccountID.txt from the working directory, so run from a scratch copy.
cp latestAccountID.txt "$OUT/"
(cd "$OUT" && java -cp ".:$OLDPWD/$CLASSPATH" TransferChecks)
