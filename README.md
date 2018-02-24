[![Build Status](https://travis-ci.org/cannawen/dota2timer.svg?branch=master)](https://travis-ci.org/cannawen/dota2timer) [![codecov](https://codecov.io/gh/cannawen/dota2timer/branch/master/graph/badge.svg)](https://codecov.io/gh/cannawen/dota2timer) [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://opensource.guide/how-to-contribute/)

## dota2timer

Dota 2 coaching app on Android, to remind you of game timings (runes, etc)

## Configuration

See [configuration.yml](app/src/main/assets/configuration.yml)
```yaml
events:
  - name: "Runes"
    time_initial: 120
    time_repeat: 120
    time_advance_notice: 10
    enabled: true
  - name: "Pull"
    time_initial: 71
    time_expire: 600
    time_repeat: 30
    time_advance_notice: 10
    enabled: false
    ...
```
Each event is described:

| Key | Type | Required | Default | Description |
| --- | --- | --- | --- | --- |
| name | String | Yes | N/A | Text that will be spoken when notifying the user of an event |
| time_initial | Integer | No | `0` | When the first event should be triggered (in seconds) |
| time_expire | Integer | No | `NO_EXPIRY` | When to stop notifying user about this event (in seconds) |
| time_repeat | Integer | No | `0` | How often this event should be triggered (in seconds) |
| time_advance_notice | Integer | No | `0` | How much advance notice to give the user (in seconds) |
| enabled | Boolean | No | `false` | Is this event enabled by default, or does it need to be manually enabled by the user? |
