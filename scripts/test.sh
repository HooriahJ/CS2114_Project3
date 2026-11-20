#!/bin/sh
# Run from any directory. Requires JDK 8-17 and the course CS2-Support jars.
set -eu
cd "$(dirname "$0")/.."
: "${CS2_SUPPORT:?Set CS2_SUPPORT to the directory containing student.jar}"
if [ ! -f "$CS2_SUPPORT/student.jar" ]; then
    echo "CS2_SUPPORT must contain student.jar" >&2
    exit 1
fi
if [ -n "${JAVA_HOME:-}" ]; then
    compiler="$JAVA_HOME/bin/javac"
    runtime="$JAVA_HOME/bin/java"
else
    compiler=javac
    runtime=java
fi
build_dir=$(mktemp -d "${TMPDIR:-/tmp}/ziplineria-tests.XXXXXX")
trap 'rm -rf "$build_dir"' EXIT HUP INT TERM
"$compiler" -Xlint:all -cp "$CS2_SUPPORT/*" -d "$build_dir" \
    src/ziplineGame/*.java src/test/*.java
"$runtime" -cp "$build_dir:$CS2_SUPPORT/*" org.junit.runner.JUnitCore \
    test.GameControllerTest test.PriorityControllerTest test.MainGameTest \
    test.MainTest test.PlayerTest test.PriorityQueueTest test.QueueTest \
    test.RiderTest
