# Test cases

Manual checks for FivePages. Run them on a real phone or an emulator with a fresh install unless the case says otherwise.

**How to fill this in:** put **Pass** or **Fail** in the Result column, the tester's name, the date, and a short note if it failed. Do not mark a case Pass unless you actually ran it.

| | |
|---|---|
| App version / commit | `fcc59ef` (main) |
| Device and Android version | Emulator and an Android phone |
| Tester | Franzie Frielle Mangalindan, Sire Ermond Manalo, Abigail Ann Sarmiento, Christian Allen Soriano |
| Date | October 22, 2026 |

The results below come from the team's test sheet, the Google Doc "FivePages Test Cases" (Part 1).

Automated unit tests are separate. Run `./gradlew testDebugUnitTest` (34 tests, all passing, see the README).

---

## 1. Onboarding

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| ON-01 | Fresh install, open the app | Welcome screen shows first | Pass | |
| ON-02 | Tap the main button on Welcome | How it works screen opens | Pass | |
| ON-03 | Continue to the reminders screen | Reminder permission screen opens | Pass | |
| ON-04 | Finish onboarding, close the app, open it again | App opens on My books, not on Welcome | Pass | |

## 2. My books

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| LB-01 | Open My books with no books | Empty state and an add button show | Pass | |
| LB-02 | Add three books, return to My books | All three show with their progress | Pass | |
| LB-03 | Type part of a title in search | List narrows to matching books | Pass | |
| LB-04 | Change the sort option | Order of books changes | Pass | |
| LB-05 | Tap each bottom bar tab | Library, Progress and Settings each open, and the selected tab is highlighted | Pass | |

## 3. Add a book

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| AB-01 | Leave the title empty | Add button does not save | Pass | |
| AB-02 | Enter a title that already exists (different capitals or extra spaces) | Red message: "You already have a book with this title" | Pass | |
| AB-03 | Enter pages and a finish date | Daily goal shows and matches pages divided by days left | Pass | |
| AB-04 | Add a valid book | "Book added" dialog shows, then the book is in My books | Pass | |
| AB-05 | Tap back during entry | Returns to My books, nothing saved | Pass | |

## 4. Book details and reading session

| ID | Steps | Expected | Result | Notes |
|---|---|---|--------|---|
| BD-01 | Tap a book | Details show percent, pages left, days left and today's goal | Pass   | |
| BD-02 | Tap start session | Timer screen opens for this book | Pass   | |
| BD-03 | Pause and resume the timer | Time stops, then continues | Pass   | |
| BD-04 | Leave the timer and choose to discard | Confirmation shows. Confirming discards, cancelling stays | Pass   | |
| BD-05 | Finish the timer, enter pages, save | Nice one! screen shows the pages | Pass   | |
| BD-06 | Tap undo on the Nice one! screen | Session is removed and the progress goes back | Pass   | Screen returns to the Log Reading Session page. Users are given another option to log the correct number of pages. If the back button is clicked, it discards the overall session. |
| BD-07 | Log enough pages to finish the book | Book shows 100% | Pass   | Suggestion: add the option to type in the number of pages to log for convenience. Big numbers are harder to input if only plus is allowed. |

## 5. Book options

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| BO-01 | Options > Edit title, enter a new unique title, save | "Title updated" dialog, **Done** returns to My books, new title shows | Pass | |
| BO-02 | Edit title to another book's title | Red message and save is blocked | Pass | "You already have a book with this title" appears. |
| BO-03 | Options > Edit goal, change pages and date, save | "Goal updated" dialog, **Done** returns to My books, goal is new | Pass | Changes are properly reflected throughout the book data. |
| BO-04 | Options > Delete book | Confirmation dialog shows | Pass | |
| BO-05 | Confirm delete | "Book deleted" dialog, book is gone, its sessions are gone | Pass | |
| BO-06 | Cancel delete | Book still exists | Pass | Book remains after clicking "Keep Book". |
| BO-07 | Options > Session history with no sessions | Empty message, no crash | Pass | |
| BO-08 | Log two sessions, open Session history | Both listed, newest first, with date, pages and time | Pass | |
| BO-09 | A session shorter than one minute | Time shows as 1 min, a session with no timer shows a dash | Pass | |

## 6. Progress and streak

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| PR-01 | Log a session today, open Progress | Streak shows 1 | Pass | Reflects the correct date in the Progress screen as well. |
| PR-02 | Check the week chart | Pages appear on today's bar, week starts on Monday | Pass | |
| PR-03 | Check sessions this month | Count matches the sessions logged this month | Pass | |
| PR-04 | Tap Preview on Progress | Missed yesterday screen shows | Pass | |
| PR-05 | Leave the Preview screen without choosing | No freeze used, no data changed | Pass | |
| PR-06 | Read yesterday, skip nothing, open the app | No Missed yesterday screen shows | Pass | |
| PR-07 | Have a streak, skip yesterday (change the phone date or seed data), open the app | Missed yesterday screen shows with the streak that can be saved | Pass | Observation: the Missed yesterday overlay appears after opening the Progress screen. |
| PR-08 | On Missed yesterday, use the freeze | Grace day used screen shows, streak is kept, freezes left is 0 | Pass | Day streak is retained. |
| PR-09 | After using the freeze, read today | Streak continues, the frozen day did not add a number | Pass | |
| PR-10 | Try a second freeze in the same month | Not offered | Pass | "Use Streak Freeze" is greyed out. |
| PR-11 | Choose Fresh start | Goes to My books and the streak begins again | Pass | A streak begins at 1 only after a page is logged. |

## 7. Settings and reminders

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| ST-01 | Open Settings | Reminder list shows | Pass | |
| ST-02 | Switch a reminder off and on | Switch changes and the state is kept | Pass | |
| ST-03 | Add a reminder with a time | It appears in the list | Pass | |
| ST-04 | Edit a reminder time | New time shows | Pass | |
| ST-05 | Delete a reminder | Confirmation, then it is removed | Pass | |

## 8. Data and stability

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| DT-01 | Add books and sessions, force close, reopen | Everything is still there | Pass | |
| DT-02 | Rotate the phone on a main screen | Screen redraws and no data is lost | Pass | The screen follows when rotated, it scrolls just fine. |
| DT-03 | Tap a button twice quickly (Add, Save, Delete) | Only one action happens | Pass | Buttons work immediately when clicked, so no action is repeated. |
| DT-04 | Use the system back button on every screen | It goes to the previous screen and never leaves the app by mistake | Pass | |
| DT-05 | Use the app with the phone in airplane mode | Everything still works | Pass | |

## 9. Look and feel

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| UI-01 | Compare each screen with its Figma frame | Layout, colours and fonts match | Pass | Overall design is consistent with the prototype. A minor difference is how some element sizes appear. |
| UI-02 | Set the phone font size to Large | Text is still readable and nothing is cut off | Pass | At maximum text size a few words are cut off, but most text stays intact. Cut off: the "Discard" button in dialogs, "Send notification at this time" in Reminder window, and the "0-day" in the streak freeze screen. |
| UI-03 | Check every icon-only button | It has a content description and a big enough tap area | Pass | Buttons are working smoothly. |

---

## Summary

| Section | Cases |   Pass | Fail | Not run |
|---|---:|-------:|---:|--------:|
| 1 Onboarding | 4 |      4 | 0 |       0 |
| 2 My books | 5 |      5 | 0 |       0 |
| 3 Add a book | 5 |      5 | 0 |       0 |
| 4 Book details and session | 7 |      7 | 0 |       0 |
| 5 Book options | 9 |      9 | 0 |       0 |
| 6 Progress and streak | 11 |     11 | 0 |       0 |
| 7 Settings and reminders | 5 |      5 | 0 |       0 |
| 8 Data and stability | 5 |      5 | 0 |       0 |
| 9 Look and feel | 3 |      3 | 0 |       0 |
| **Total** | **54** | **54** | **0** |   **0** |

