#!/bin/sh -ex

gradle clean publishToMavenLocal publish closeAndReleaseRepository