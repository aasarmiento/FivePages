# Test cases

Manual checks for FivePages. Run them on a real phone or an emulator with a fresh install unless the case says otherwise.

**How to fill this in:** put **Pass** or **Fail** in the Result column, the tester's name, the date, and a short note if it failed. Do not mark a case Pass unless you actually ran it.

| | |
|---|---|
| App version / commit | |
| Device and Android version | |
| Tester | |
| Date | |

Automated unit tests are separate. Run `./gradlew testDebugUnitTest` (33 tests, all passing, see the README).

---

## 1. Onboarding

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| ON-01 | Fresh install, open the app | Welcome screen shows first | | |
| ON-02 | Tap the main button on Welcome | How it works screen opens | | |
| ON-03 | Continue to the reminders screen | Reminder permission screen opens | | |
| ON-04 | Finish onboarding, close the app, open it again | App opens on My books, not on Welcome | | |

## 2. My books

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| LB-01 | Open My books with no books | Empty state and an add button show | | |
| LB-02 | Add three books, return to My books | All three show with their progress | | |
| LB-03 | Type part of a title in search | List narrows to matching books | | |
| LB-04 | Change the sort option | Order of books changes | | |
| LB-05 | Tap each bottom bar tab | Library, Progress and Settings each open, and the selected tab is highlighted | | |

## 3. Add a book

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| AB-01 | Leave the title empty | Add button does not save | | |
| AB-02 | Enter a title that already exists (different capitals or extra spaces) | Red message: "You already have a book with this title" | | |
| AB-03 | Enter pages and a finish date | Daily goal shows and matches pages divided by days left | | |
| AB-04 | Add a valid book | "Book added" dialog shows, then the book is in My books | | |
| AB-05 | Tap back during entry | Returns to My books, nothing saved | | |

## 4. Book details and reading session

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| BD-01 | Tap a book | Details show percent, pages left, days left and today's goal | | |
| BD-02 | Tap start session | Timer screen opens for this book | | |
| BD-03 | Pause and resume the timer | Time stops, then continues | | |
| BD-04 | Leave the timer and choose to discard | Confirmation shows. Confirming discards, cancelling stays | | |
| BD-05 | Finish the timer, enter pages, save | Nice one! screen shows the pages | | |
| BD-06 | Tap undo on the Nice one! screen | Session is removed and the progress goes back | | |
| BD-07 | Log enough pages to finish the book | Book shows 100% | | |

## 5. Book options

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| BO-01 | Options > Edit title, enter a new unique title, save | "Title updated" dialog, **Done** returns to My books, new title shows | | |
| BO-02 | Edit title to another book's title | Red message and save is blocked | | |
| BO-03 | Options > Edit goal, change pages and date, save | "Goal updated" dialog, **Done** returns to My books, goal is new | | |
| BO-04 | Options > Delete book | Confirmation dialog shows | | |
| BO-05 | Confirm delete | "Book deleted" dialog, book is gone, its sessions are gone | | |
| BO-06 | Cancel delete | Book still exists | | |
| BO-07 | Options > Session history with no sessions | Empty message, no crash | | |
| BO-08 | Log two sessions, open Session history | Both listed, newest first, with date, pages and time | | |
| BO-09 | A session shorter than one minute | Time shows as 1 min, a session with no timer shows a dash | | |

## 6. Progress and streak

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| PR-01 | Log a session today, open Progress | Streak shows 1 | | |
| PR-02 | Check the week chart | Pages appear on today's bar, week starts on Monday | | |
| PR-03 | Check sessions this month | Count matches the sessions logged this month | | |
| PR-04 | Tap Preview on Progress | Missed yesterday screen shows | | |
| PR-05 | Leave the Preview screen without choosing | No freeze used, no data changed | | |
| PR-06 | Read yesterday, skip nothing, open the app | No Missed yesterday screen shows | | |
| PR-07 | Have a streak, skip yesterday (change the phone date or seed data), open the app | Missed yesterday screen shows with the streak that can be saved | | |
| PR-08 | On Missed yesterday, use the freeze | Grace day used screen shows, streak is kept, freezes left is 0 | | |
| PR-09 | After using the freeze, read today | Streak continues, the frozen day did not add a number | | |
| PR-10 | Try a second freeze in the same month | Not offered | | |
| PR-11 | Choose Fresh start | Goes to My books and the streak begins again | | |

## 7. Settings and reminders

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| ST-01 | Open Settings | Reminder list shows | | |
| ST-02 | Switch a reminder off and on | Switch changes and the state is kept | | |
| ST-03 | Add a reminder with a time | It appears in the list | | |
| ST-04 | Edit a reminder time | New time shows | | |
| ST-05 | Delete a reminder | Confirmation, then it is removed | | |

## 8. Data and stability

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| DT-01 | Add books and sessions, force close, reopen | Everything is still there | | |
| DT-02 | Rotate the phone on a main screen | Screen redraws and no data is lost | | |
| DT-03 | Tap a button twice quickly (Add, Save, Delete) | Only one action happens | | |
| DT-04 | Use the system back button on every screen | It goes to the previous screen and never leaves the app by mistake | | |
| DT-05 | Use the app with the phone in airplane mode | Everything still works | | |

## 9. Look and feel

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| UI-01 | Compare each screen with its Figma frame | Layout, colours and fonts match | | |
| UI-02 | Set the phone font size to Large | Text is still readable and nothing is cut off | | |
| UI-03 | Check every icon-only button | It has a content description and a big enough tap area | | |

---

## Summary

| Section | Cases | Pass | Fail | Not run |
|---|---:|---:|---:|---:|
| 1 Onboarding | 4 | | | |
| 2 My books | 5 | | | |
| 3 Add a book | 5 | | | |
| 4 Book details and session | 7 | | | |
| 5 Book options | 9 | | | |
| 6 Progress and streak | 11 | | | |
| 7 Settings and reminders | 5 | | | |
| 8 Data and stability | 5 | | | |
| 9 Look and feel | 3 | | | |
| **Total** | **54** | | | |
